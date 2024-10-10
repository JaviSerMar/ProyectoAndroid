package com.example.serrano.proyectoandroid;

import java.util.Arrays;


/**
 * Clase que representa una trama IBeacon, incluyendo los campos de datos necesarios
 * para identificar el dispositivo beacon y su información asociada.
 */
public class TramaIBeacon {
    private byte[] prefijo = null; // 9 bytes
    private byte[] uuid = null; // 16 bytes
    private byte[] major = null; // 2 bytes
    private byte[] minor = null; // 2 bytes
    private byte txPower = 0; // 1 byte

    private byte[] losBytes;

    private byte[] advFlags = null; // 3 bytes
    private byte[] advHeader = null; // 2 bytes
    private byte[] companyID = new byte[2]; // 2 bytes
    private byte iBeaconType = 0 ; // 1 byte
    private byte iBeaconLength = 0 ; // 1 byte

    private boolean noadvFlags;

    // -------------------------------------------------------------------------------
    // -------------------------------------------------------------------------------


    /**
     * Obtiene el prefijo de la trama.
     *
     * @return El prefijo de la trama.
     */
    public byte[] getPrefijo() {
        return prefijo;
    }

    // -------------------------------------------------------------------------------
    // -------------------------------------------------------------------------------


    /**
     * Obtiene el UUID del dispositivo.
     *
     * @return El UUID del dispositivo.
     */
    public byte[] getUUID() {
        return uuid;
    }

    // -------------------------------------------------------------------------------
    // -------------------------------------------------------------------------------


    /**
     * Obtiene el valor major del dispositivo.
     *
     * @return El valor major del dispositivo.
     */
    public byte[] getMajor() {
        return major;
    }

    // -------------------------------------------------------------------------------
    // -------------------------------------------------------------------------------


    /**
     * Obtiene el valor minor del dispositivo.
     *
     * @return El valor minor del dispositivo.
     */
    public byte[] getMinor() {
        return minor;
    }

    // -------------------------------------------------------------------------------
    // -------------------------------------------------------------------------------


    /**
     * Obtiene la potencia de transmisión.
     *
     * @return La potencia de transmisión del dispositivo.
     */
    public byte getTxPower() {
        return txPower;
    }

    // -------------------------------------------------------------------------------
    // -------------------------------------------------------------------------------


    /**
     * Obtiene los bytes de la trama.
     *
     * @return Los bytes de la trama completa.
     */
    public byte[] getLosBytes() {
        return losBytes;
    }

    // -------------------------------------------------------------------------------
    // -------------------------------------------------------------------------------


    /**
     * Obtiene las banderas de publicidad.
     *
     * @return Las banderas de publicidad.
     */
    public byte[] getAdvFlags() {
        return advFlags;
    }

    // -------------------------------------------------------------------------------
    // -------------------------------------------------------------------------------


    /**
     * Obtiene la cabecera de publicidad.
     *
     * @return La cabecera de publicidad.
     */
    public byte[] getAdvHeader() {
        return advHeader;
    }

    // -------------------------------------------------------------------------------
    // -------------------------------------------------------------------------------


    /**
     * Obtiene el ID de la compañía.
     *
     * @return El ID de la compañía.
     */
    public byte[] getCompanyID() {
        return companyID;
    }

    // -------------------------------------------------------------------------------
    // -------------------------------------------------------------------------------


    /**
     * Obtiene el tipo de IBeacon.
     *
     * @return El tipo de IBeacon.
     */
    public byte getiBeaconType() {
        return iBeaconType;
    }

    // -------------------------------------------------------------------------------
    // -------------------------------------------------------------------------------


    /**
     * Obtiene la longitud del IBeacon.
     *
     * @return La longitud del IBeacon.
     */
    public byte getiBeaconLength() {
        return iBeaconLength;
    }

    // -------------------------------------------------------------------------------
    // -------------------------------------------------------------------------------


    /**
     * Constructor que inicializa la trama IBeacon con los bytes dados.
     *
     * @param bytes Array de bytes que representan la trama IBeacon.
     */
    public TramaIBeacon(byte[] bytes ) {
        this.losBytes = bytes;

        // Verifica las banderas de publicidad
        if( losBytes[0] == 02 && losBytes[1] == 01 && losBytes[2] == 06){
            noadvFlags = false;
        }else{
            noadvFlags = true;
        }

        // Procesa los bytes de la trama según la presencia de banderas de publicidad
        if(noadvFlags){
        prefijo = Arrays.copyOfRange(losBytes, 0, 5+1 ); // 6 bytes
        uuid = Arrays.copyOfRange(losBytes, 6, 21+1 ); // 16 bytes
        major = Arrays.copyOfRange(losBytes, 22, 23+1 ); // 2 bytes
        minor = Arrays.copyOfRange(losBytes, 24, 25+1 ); // 2 bytes
        txPower = losBytes[ 26 ]; // 1 byte

        advHeader = Arrays.copyOfRange( prefijo, 0, 1+1 ); // 2 bytes
        companyID = Arrays.copyOfRange( prefijo, 2, 3+1 ); // 2 bytes
        iBeaconType = prefijo[ 4 ]; // 1 byte
        iBeaconLength = prefijo[ 5 ]; // 1 byte

        }else{
        prefijo = Arrays.copyOfRange(losBytes, 0, 8+1 ); // 9 bytes
        uuid = Arrays.copyOfRange(losBytes, 9, 24+1 ); // 16 bytes
        major = Arrays.copyOfRange(losBytes, 25, 26+1 ); // 2 bytes
        minor = Arrays.copyOfRange(losBytes, 27, 28+1 ); // 2 bytes
        txPower = losBytes[ 29 ]; // 1 byte

        advFlags = Arrays.copyOfRange( prefijo, 0, 2+1 ); // 3 bytes
        advHeader = Arrays.copyOfRange( prefijo, 3, 4+1 ); // 2 bytes
        companyID = Arrays.copyOfRange( prefijo, 5, 6+1 ); // 2 bytes
        iBeaconType = prefijo[ 7 ]; // 1 byte
        iBeaconLength = prefijo[ 8 ]; // 1 byte

        }
    }
}

// -----------------------------------------------------------------------------------
// -----------------------------------------------------------------------------------
// -----------------------------------------------------------------------------------
// -----------------------------------------------------------------------------------


