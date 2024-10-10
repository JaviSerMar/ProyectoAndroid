# README - Proyecto Android para Sensores de Contaminación

Este proyecto de Android tiene como objetivo recibir y mostrar datos de sensores de contaminación a través de Bluetooth Low Energy (BLE). La aplicación permite conectarse a dispositivos BLE, recibir datos de sensores de CO2 y temperatura, y mostrarlos en la interfaz de usuario.

## Estructura del Proyecto

La aplicación se compone de varias clases y métodos que gestionan la comunicación BLE y la interfaz de usuario. A continuación se describen los métodos y componentes clave:

### Clases y Métodos Principales

#### 1. `MainActivity`

La clase `MainActivity` es la actividad principal de la aplicación y contiene la lógica para gestionar la interfaz de usuario y la conexión BLE.

- **Métodos Importantes**:
  - **`onCreate(Bundle savedInstanceState)`**
    - Inicializa la interfaz de usuario, configura los elementos de texto para mostrar los valores de CO2 y temperatura, y llama al método `inicializarBlueTooth()`.
  
  - **`actualizarValores(int tipoSensor, int valor)`**
    - Actualiza los valores mostrados en la interfaz de usuario según el tipo de sensor (1 para CO2, 2 para temperatura).
  
  - **`onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)`**
    - Maneja la respuesta de la solicitud de permisos, verificando si se han concedido los permisos necesarios para la aplicación.

  - **`cargarBeacon(int major, int minor)`**
    - Interpreta los datos recibidos de un beacon BLE. Identifica el tipo de sensor (ozono o temperatura) y devuelve un código correspondiente.

#### 2. Variables de Instancia

- `valorCo2TextView` y `valorTemperaturaTextView`: Elementos de la interfaz de usuario que muestran los valores de los sensores.
- `api`: Instancia de la interfaz de API para realizar solicitudes a un servidor backend.
- `nuevoUuid`: UUID del beacon para conectarse.

### Requisitos

- **Hardware**: Un dispositivo Android con Bluetooth LE.
- **Permisos**: La aplicación requiere permisos para acceder a Bluetooth y ubicación.

### Instalación

1. **Configuración del Entorno**:
   - Abre Android Studio y clona el repositorio del proyecto.
   - Asegúrate de que todos los permisos necesarios están solicitados en el archivo `AndroidManifest.xml`.

2. **Carga del Código**:
   - Abre el archivo `MainActivity.java` y revisa que el código esté correctamente configurado para tus necesidades.

3. **Ejecuta la Aplicación**:
   - Conecta tu dispositivo Android a tu computadora.
   - Ejecuta la aplicación desde Android Studio.

### Uso

Una vez que la aplicación esté en funcionamiento, se conectará a dispositivos BLE y comenzará a recibir datos de los sensores. Los valores de CO2 y temperatura se mostrarán en la interfaz de usuario.

### Contribuciones

Si deseas contribuir a este proyecto, siéntete libre de realizar un fork y enviar un pull request.

### Licencia

Este proyecto está bajo la licencia MIT. Para más detalles, consulta el archivo LICENSE.
