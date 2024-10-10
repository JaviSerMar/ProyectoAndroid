
package com.example.serrano.proyectoandroid;


import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelUuid;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @file MainActivity.java
 * @brief Clase principal que maneja la búsqueda de dispositivos BLE y el envío de datos de sensores.
 */

public class MainActivity extends AppCompatActivity {

    /// Elementos de la interfaz de usuario para mostrar los valores de CO2 y temperatura.
    private TextView valorCo2TextView;
    private TextView valorTemperaturaTextView;


    /// UUID del dispositivo buscado.
    private String nuevoUuid;

    /// Tipo de sensor (CO2 o temperatura).
    private String tipo;

    /// Servicio API para enviar los datos al servidor.
    ApiService api;

    /// Constante que representa el sensor de CO2.
    private static final int SENSOR_CO2 = 11;

    /// Constante que representa el sensor de temperatura.
    private static final int SENSOR_TEMPERATURA = 12;


    // --------------------------------------------------------------
    // --------------------------------------------------------------
    private static final String ETIQUETA_LOG = ">>>>";

    private static final int CODIGO_PETICION_PERMISOS = 11223344;

    // --------------------------------------------------------------
    // --------------------------------------------------------------

    /// Escáner Bluetooth LE (Low Energy) para detectar dispositivos BLE.
    private BluetoothLeScanner elEscanner;

    /// Callback utilizado para manejar los resultados del escaneo BLE.
    private ScanCallback callbackDelEscaneo = null;

    // --------------------------------------------------------------
    // --------------------------------------------------------------


    /**
     * @brief Inicia la búsqueda de todos los dispositivos BLE cercanos.
     */
    private void buscarTodosLosDispositivosBTLE() {
        Log.d(ETIQUETA_LOG, " buscarTodosLosDispositivosBTL(): empieza ");

        Log.d(ETIQUETA_LOG, " buscarTodosLosDispositivosBTL(): instalamos scan callback ");


        // Configuración de los filtros y ajustes de escaneo
        List<ScanFilter> scanFilters = new ArrayList<>();
        ScanSettings scanSettings = new ScanSettings.Builder()
                .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY) // o SCAN_MODE_BALANCED
                .build();


        // Callback para manejar los resultados del escaneo
        this.callbackDelEscaneo = new ScanCallback() {
            @Override
            public void onScanResult(int callbackType, ScanResult resultado) {
                super.onScanResult(callbackType, resultado);
                Log.d(ETIQUETA_LOG, " buscarTodosLosDispositivosBTL(): onScanResult() ");

                mostrarInformacionDispositivoBTLE(resultado);
            }

            @Override
            public void onBatchScanResults(List<ScanResult> results) {
                super.onBatchScanResults(results);
                Log.d(ETIQUETA_LOG, " buscarTodosLosDispositivosBTL(): onBatchScanResults() ");

            }

            @Override
            public void onScanFailed(int errorCode) {
                super.onScanFailed(errorCode);
                Log.d(ETIQUETA_LOG, " buscarTodosLosDispositivosBTL(): onScanFailed() ");

            }
        };

        Log.d(ETIQUETA_LOG, " buscarTodosLosDispositivosBTL(): empezamos a escanear ");


        // Inicia el escaneo con los filtros y ajustes configurados
        this.elEscanner.startScan(scanFilters, scanSettings, callbackDelEscaneo);

    } // ()

    // --------------------------------------------------------------
    // --------------------------------------------------------------


    /**
     * @brief Muestra la información de un dispositivo BLE detectado.
     *
     * @param resultado Resultado de la búsqueda de dispositivos BLE.
     */
    private void mostrarInformacionDispositivoBTLE(ScanResult resultado) {

        BluetoothDevice bluetoothDevice = resultado.getDevice();
        byte[] bytes = resultado.getScanRecord().getBytes();
        int rssi = resultado.getRssi();

        // Parseo de la trama iBeacon
        TramaIBeacon tib = new TramaIBeacon(bytes);

        int tipoSensor = Utilidades.bytesToInt(tib.getMinor());
        int valorSensor = Utilidades.bytesToInt(tib.getMajor());

        // Actualiza los valores mostrados en la interfaz de usuario
        actualizarValores(tipoSensor, valorSensor);


        // Logs de depuración para mostrar información del dispositivo
        Log.d(ETIQUETA_LOG, "Valores actualizados: tipoSensor=" + tipoSensor + ", valorSensor=" + valorSensor);
        Log.d(ETIQUETA_LOG, " ****************************************************");
        Log.d(ETIQUETA_LOG, " ****** DISPOSITIVO DETECTADO BTLE ****************** ");
        Log.d(ETIQUETA_LOG, " ****************************************************");
        Log.d(ETIQUETA_LOG, " nombre = " + bluetoothDevice.getName());
        Log.d(ETIQUETA_LOG, " toString = " + bluetoothDevice.toString());

        /*
        ParcelUuid[] puuids = bluetoothDevice.getUuids();
        if ( puuids.length >= 1 ) {
            //Log.d(ETIQUETA_LOG, " uuid = " + puuids[0].getUuid());
           // Log.d(ETIQUETA_LOG, " uuid = " + puuids[0].toString());
        }*/

        Log.d(ETIQUETA_LOG, " dirección = " + bluetoothDevice.getAddress());
        Log.d(ETIQUETA_LOG, " rssi = " + rssi);

        Log.d(ETIQUETA_LOG, " bytes = " + new String(bytes));
        Log.d(ETIQUETA_LOG, " bytes (" + bytes.length + ") = " + Utilidades.bytesToHexString(bytes));


        Log.d(ETIQUETA_LOG, " ----------------------------------------------------");
        Log.d(ETIQUETA_LOG, " prefijo  = " + Utilidades.bytesToHexString(tib.getPrefijo()));
        Log.d(ETIQUETA_LOG, "          advFlags = " + Utilidades.bytesToHexString(tib.getAdvFlags()));
        Log.d(ETIQUETA_LOG, "          advHeader = " + Utilidades.bytesToHexString(tib.getAdvHeader()));
        Log.d(ETIQUETA_LOG, "          companyID = " + Utilidades.bytesToHexString(tib.getCompanyID()));
        Log.d(ETIQUETA_LOG, "          iBeacon type = " + Integer.toHexString(tib.getiBeaconType()));
        Log.d(ETIQUETA_LOG, "          iBeacon length 0x = " + Integer.toHexString(tib.getiBeaconLength()) + " ( "
                + tib.getiBeaconLength() + " ) ");
        Log.d(ETIQUETA_LOG, " uuid  = " + Utilidades.bytesToHexString(tib.getUUID()));
        Log.d(ETIQUETA_LOG, " uuid  = " + Utilidades.bytesToString(tib.getUUID()));
        Log.d(ETIQUETA_LOG, " major  = " + Utilidades.bytesToHexString(tib.getMajor()) + "( "
                + Utilidades.bytesToInt(tib.getMajor()) + " ) ");
        Log.d(ETIQUETA_LOG, " minor  = " + Utilidades.bytesToHexString(tib.getMinor()) + "( "
                + Utilidades.bytesToInt(tib.getMinor()) + " ) ");
        Log.d(ETIQUETA_LOG, " txPower  = " + Integer.toHexString(tib.getTxPower()) + " ( " + tib.getTxPower() + " )");
        Log.d(ETIQUETA_LOG, " ****************************************************");

    } // ()

    // --------------------------------------------------------------
    // --------------------------------------------------------------


    /**
     * @brief Inicia la búsqueda de un dispositivo BLE específico por su UUID.
     *
     * @param dispositivoBuscado UUID del dispositivo a buscar.
     */
    private void buscarEsteDispositivoBTLE(final UUID dispositivoBuscado) {
        Log.d(ETIQUETA_LOG, " buscarEsteDispositivoBTLE(): empieza ");
        Log.d(ETIQUETA_LOG, "  buscarEsteDispositivoBTLE(): instalamos scan callback ");


        this.callbackDelEscaneo = new ScanCallback() {
            @Override
            public void onScanResult(int callbackType, ScanResult resultado) {
                super.onScanResult(callbackType, resultado);
                Log.d(ETIQUETA_LOG, "buscarEsteDispositivoBTLE(): onScanResult()");

                // Parseo de la trama iBeacon
                BluetoothDevice dispositivo = resultado.getDevice();
                byte[] bytes = resultado.getScanRecord().getBytes();
                TramaIBeacon tib = new TramaIBeacon(bytes);
                UUID uuid = Utilidades.stringToUUID(Utilidades.bytesToString(tib.getUUID()));


                // Verifica si el dispositivo encontrado es el buscado por UUID
                if (uuid.equals(dispositivoBuscado)) {
                    Log.d(ETIQUETA_LOG, "Dispositivo encontrado: " + Utilidades.uuidToString(uuid));
                    mostrarInformacionDispositivoBTLE(resultado);

                    byte[] minorBytes = tib.getMinor();
                    byte[] majorBytes = tib.getMajor();

                    // Obtiene los valores major y minor del beacon
                    int minorValue = Utilidades.bytesToInt(minorBytes);
                    int majorValue = Utilidades.bytesToInt(majorBytes);
                    final String minorText = "Minor: " + minorValue;
                    actualizarValores(cargarBeacon(majorValue,minorValue),minorValue);


                    ApiService apiService = ApiClient.getApiService();


                    // Define el tipo de sensor según los valores
                    if (cargarBeacon(majorValue, minorValue) == 1) {
                        tipo = "CO2";
                    } else if (cargarBeacon(majorValue, minorValue) == 2) {
                        tipo = "temperature";
                    }


                    // Envío de los datos del sensor al servidor usando Retrofit
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                    String timestamp = sdf.format(new Date());
                    SensorData sensorData = new SensorData(tipo, minorValue, timestamp);

                    Call<Void> callback= api.postSensorData(sensorData);
                    callback.enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if (response.isSuccessful()) {
                                // Manejar la lista de datos de sensores aquí
                                Log.d(ETIQUETA_LOG, "DATO ENVIADO");
                            } else {
                                // Manejar el error de la respuesta
                                Log.d(ETIQUETA_LOG, "SE HA CONECTADO A LA BASE DE DATOS, PERO HA HABIDO UN PROBLEMA INSERTANDO DATOS" + response);
                            }
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Log.d(ETIQUETA_LOG, "ERROR DE CONEXIÓN: " + t);

                        }

                    });

                } else {
                    Log.d(ETIQUETA_LOG, "Dispositivo no objetivo encontrado: " + Utilidades.uuidToString(uuid));
                }
            }

            @Override
            public void onBatchScanResults(List<ScanResult> results) {
                super.onBatchScanResults(results);
                Log.d(ETIQUETA_LOG, "  buscarEsteDispositivoBTLE(): onBatchScanResults() ");

            }

            @Override
            public void onScanFailed(int errorCode) {
                super.onScanFailed(errorCode);
                Log.d(ETIQUETA_LOG, "  buscarEsteDispositivoBTLE(): onScanFailed() ");

            }
        };

        // Inicia el escaneo con configuración de baja latencia
        ScanSettings scanSettings = new ScanSettings.Builder()
                .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
                .build();

        Log.d(ETIQUETA_LOG, "  buscarEsteDispositivoBTLE(): empezamos a escanear buscando: " + dispositivoBuscado);

        this.elEscanner.startScan(null, scanSettings, this.callbackDelEscaneo);
    } // ()

    // --------------------------------------------------------------
    // --------------------------------------------------------------


    /**
     * @brief Detiene la búsqueda de dispositivos BLE.
     */
    private void detenerBusquedaDispositivosBTLE() {

        if (this.callbackDelEscaneo == null) {
            return;
        }

        this.elEscanner.stopScan(this.callbackDelEscaneo);
        this.callbackDelEscaneo = null;

    } // ()

    // --------------------------------------------------------------
    // --------------------------------------------------------------


    /**
     * @brief Acción al pulsar el botón para buscar todos los dispositivos BLE cercanos.
     *
     * @param v Vista que representa el botón pulsado.
     */
    public void botonBuscarDispositivosBTLEPulsado(View v) {
        Log.d(ETIQUETA_LOG, " boton buscar dispositivos BTLE Pulsado");
        this.buscarTodosLosDispositivosBTLE();
    } // ()

    // --------------------------------------------------------------
    // --------------------------------------------------------------


    /**
     * @brief Acción al pulsar el botón para buscar un dispositivo BLE específico.
     *
     * @param v Vista que representa el botón pulsado.
     */
    public void botonBuscarNuestroDispositivoBTLEPulsado(View v) {
        Log.d(ETIQUETA_LOG, " boton nuestro dispositivo BTLE Pulsado");
        this.buscarEsteDispositivoBTLE(Utilidades.stringToUUID(nuevoUuid));
    }
    // ()

    // --------------------------------------------------------------
    // --------------------------------------------------------------


    /**
     * @brief Acción al pulsar el botón para detener la búsqueda de dispositivos BLE.
     *
     * @param v Vista que representa el botón pulsado.
     */
    public void botonDetenerBusquedaDispositivosBTLEPulsado(View v) {
        Log.d(ETIQUETA_LOG, " boton detener busqueda dispositivos BTLE Pulsado");
        this.detenerBusquedaDispositivosBTLE();
    } // ()

    // --------------------------------------------------------------
    // --------------------------------------------------------------


    /**
     * @brief Inicializa el Bluetooth en el dispositivo si está disponible y habilitado.
     */
    private void inicializarBlueTooth() {
        Log.d(ETIQUETA_LOG, "inicializarBlueTooth(): obtenemos adaptador BT");

        // Obtener el adaptador Bluetooth
        BluetoothAdapter bta = BluetoothAdapter.getDefaultAdapter();

        // Verificar si el dispositivo soporta Bluetooth
        if (bta == null) {
            Log.e(ETIQUETA_LOG, "inicializarBlueTooth(): Error: Este dispositivo no soporta Bluetooth");
            return; // Salir si no hay soporte de Bluetooth
        }

        // Verificar si el Bluetooth está habilitado, de lo contrario habilitarlo
        if (!bta.isEnabled()) {
            Log.d(ETIQUETA_LOG, "inicializarBlueTooth(): habilitando adaptador BT");
            bta.enable();
        }

        Log.d(ETIQUETA_LOG, "inicializarBlueTooth(): habilitado = " + bta.isEnabled());
        Log.d(ETIQUETA_LOG, "inicializarBlueTooth(): estado = " + bta.getState());

        Log.d(ETIQUETA_LOG, "inicializarBlueTooth(): obtenemos escáner BTLE");
        this.elEscanner = bta.getBluetoothLeScanner();

        // Verificar si se obtuvo el escáner correctamente
        if (this.elEscanner == null) {
            Log.d(ETIQUETA_LOG, "inicializarBlueTooth(): Socorro: NO hemos obtenido escáner BTLE!!!!");
            return; // Salir si no se pudo obtener el escáner
        }

        // Verificación de permisos de ubicación
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    MainActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    CODIGO_PETICION_PERMISOS
            );
        } else {
            Log.d(ETIQUETA_LOG, "inicializarBlueTooth(): parece que YA tengo permisos de ubicación necesarios!!!!");
        }

        // Verificación de permisos según la versión de Android
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            // Para Android 12 y versiones posteriores, pedimos permisos de "dispositivos cercanos"
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                        MainActivity.this,
                        new String[]{Manifest.permission.BLUETOOTH_SCAN, Manifest.permission.BLUETOOTH_CONNECT},
                        CODIGO_PETICION_PERMISOS
                );
            } else {
                Log.d(ETIQUETA_LOG, "inicializarBlueTooth(): parece que YA tengo los permisos necesarios en Android 12+!!!!");
            }
        } else {
            // Para Android 11 y versiones anteriores, pedimos los permisos de Bluetooth y localización antiguos
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_ADMIN) != PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                        MainActivity.this,
                        new String[]{Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN, Manifest.permission.ACCESS_FINE_LOCATION},
                        CODIGO_PETICION_PERMISOS
                );
            } else {
                Log.d(ETIQUETA_LOG, "inicializarBlueTooth(): parece que YA tengo los permisos necesarios!!!!");
            }
        }
    } // ()



    /**
     * @brief Actualiza los valores en la interfaz de usuario según el tipo de sensor.
     *
     * @param tipoSensor Identificador del tipo de sensor (1: CO2, 2: Temperatura).
     * @param valor Valor del sensor a mostrar en la interfaz de usuario.
     */
    private void actualizarValores(int tipoSensor, int valor) {
        switch (tipoSensor) {
            case 1: // CO2
                valorCo2TextView.setText(String.valueOf(valor));
                break;
            case 2: // Temperatura
                valorTemperaturaTextView.setText(String.valueOf(valor));
                break;
            default:
                Log.d(ETIQUETA_LOG, "Tipo de sensor desconocido: " + tipoSensor);
        }
    }


    // --------------------------------------------------------------
    // --------------------------------------------------------------


    /**
     * @brief Método llamado al crear la actividad. Inicializa la interfaz y el Bluetooth.
     *
     * @param savedInstanceState Estado guardado de la actividad, si aplica.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        valorCo2TextView = findViewById(R.id.valorco2);
        valorTemperaturaTextView = findViewById(R.id.valortemperatura);


        Log.d(ETIQUETA_LOG, " onCreate(): empieza ");

        nuevoUuid = "cholosimeonejefe";

        api = ApiClient.getClient().create(ApiService.class);

        inicializarBlueTooth();

        Log.d(ETIQUETA_LOG, " onCreate(): termina ");

    } // onCreate()

    // --------------------------------------------------------------
    // --------------------------------------------------------------


    /**
     * @brief Maneja la respuesta de la solicitud de permisos.
     *
     * @param requestCode Código de solicitud de permiso.
     * @param permissions Permisos solicitados.
     * @param grantResults Resultados de la solicitud de permisos.
     */
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case CODIGO_PETICION_PERMISOS:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Log.d(ETIQUETA_LOG, " onRequestPermissionResult(): permisos concedidos  !!!!");
                    // Permission is granted. Continue the action or workflow
                    // in your app.
                } else {

                    Log.d(ETIQUETA_LOG, " onRequestPermissionResult(): Socorro: permisos NO concedidos  !!!!");

                }
                return;
        }
        // Other 'case' lines to check for other
        // permissions this app might request.
    } // ()




    /**
     * @brief Interpreta los datos recibidos de un beacon BLE y los categoriza como sensor de ozono o temperatura.
     *
     * @param major Valor mayor del beacon que contiene información sobre el tipo de sensor.
     * @param minor Valor menor del beacon que representa el valor medido por el sensor.
     * @return 1 si el tipo de sensor es ozono, 2 si es temperatura, 0 si no es reconocido.
     */
    public int cargarBeacon(int major, int minor) {
        int tipoMedicion = major >> 8;
        int contador = major & 0xFF;

        switch (tipoMedicion) {
            case 11:
                Log.d("Beacon", "Hay dato de OZONO. Valor: " + minor + ", Tiempo: " + contador);
                return 1;
            case 12:
                Log.d("Beacon", "Hay dato de TEMPERATURA. Valor: " + minor + ", Tiempo: " + contador);
                return 2;
            default:
                Log.d("Beacon", "No se detecta el dato");
                break;
        }
        return 0;
    }

}


 // Mainactivity
// --------------------------------------------------------------
// --------------------------------------------------------------
// --------------------------------------------------------------
// --------------------------------------------------------------


