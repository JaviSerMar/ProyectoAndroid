package com.example.serrano.proyectoandroid;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ServiceInfo;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import java.util.List;

public class BLEService extends Service {
    private static final String TAG = "BLEService";
    private static final String CHANNEL_ID = "BLEServiceChannel";

    private BluetoothAdapter bluetoothAdapter;
    private BluetoothManager bluetoothManager;
    private BluetoothLeScanner bluetoothLeScanner;

    @Override
    public void onCreate() {
        super.onCreate();

        // Inicializar el BluetoothManager y BluetoothAdapter
        bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        bluetoothAdapter = bluetoothManager.getAdapter();

        if (bluetoothAdapter != null && bluetoothAdapter.isEnabled()) {
            bluetoothLeScanner = bluetoothAdapter.getBluetoothLeScanner();
            // Iniciar la búsqueda de dispositivos BLE
            startBLEScan();
        } else {
            Log.e(TAG, "Bluetooth no está habilitado o no es compatible");
        }
    }

    private void startBLEScan() {
        Log.d(TAG, "Iniciando escaneo BLE en segundo plano...");

        // Iniciar el escaneo
        bluetoothLeScanner.startScan(scanCallback);
    }

    private final ScanCallback scanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            super.onScanResult(callbackType, result);
            BluetoothDevice device = result.getDevice();
            // Procesar los datos de los sensores (temperatura, CO2) aquí
            Log.d(TAG, "Dispositivo encontrado: " + device.getName());
            sendDataToServer(device, result.getRssi(), result.getScanRecord().getBytes());
        }

        @Override
        public void onBatchScanResults(List<ScanResult> results) {
            for (ScanResult result : results) {
                BluetoothDevice device = result.getDevice();
                Log.d(TAG, "Dispositivo encontrado en batch: " + device.getName());
                sendDataToServer(device, result.getRssi(), result.getScanRecord().getBytes());
            }
        }

        @Override
        public void onScanFailed(int errorCode) {
            super.onScanFailed(errorCode);
            Log.e(TAG, "Error en el escaneo: " + errorCode);
        }
    };

    private void sendDataToServer(BluetoothDevice device, int rssi, byte[] scanRecord) {
        Log.d(TAG, "Enviando datos al servidor del dispositivo: " + device.getName());
        // Implementación de la lógica para enviar los datos al servidor
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        createNotificationChannel();

        // Crear una notificación que será mostrada mientras el servicio está en primer plano
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                this,
                0,
                notificationIntent,
                PendingIntent.FLAG_IMMUTABLE // Cambia a FLAG_MUTABLE si necesitas mutabilidad
        );

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Escaneo de dispositivos BLE")
                .setContentText("Escaneando dispositivos y enviando datos al servidor")
                .setSmallIcon(android.R.drawable.ic_dialog_info) // Usa un ícono predeterminado
                .setContentIntent(pendingIntent)
                .build();

        // Mover el servicio al primer plano con la notificación
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            startForeground(1, notification, ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC); // Define el tipo de servicio
        } else {
            startForeground(1, notification); // Para versiones anteriores
        }

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (bluetoothLeScanner != null) {
            bluetoothLeScanner.stopScan(scanCallback);
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null; // No necesitamos enlazar el servicio a una actividad
    }

    // Crear el canal de notificaciones para Android 8.0 y superior
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Canal de Servicio BLE",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }
}
