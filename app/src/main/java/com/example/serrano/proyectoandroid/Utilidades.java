package com.example.serrano.proyectoandroid;


import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.UUID;

/**
 * @class Utilidades
 * @brief Clase que proporciona utilidades para manejar conversiones de datos.
 *
 * Esta clase incluye métodos para convertir cadenas a bytes, UUID a cadenas,
 * y realizar conversiones entre diferentes representaciones de datos.
 */
public class Utilidades {

    // -------------------------------------------------------------------------------
    // -------------------------------------------------------------------------------


    /**
     * @brief Convierte una cadena en un arreglo de bytes.
     *
     * Este método convierte el texto proporcionado en un arreglo de bytes
     * utilizando el conjunto de caracteres predeterminado.
     *
     * @param texto La cadena de texto que se desea convertir.
     * @return Un arreglo de bytes que representa la cadena de texto.
     */
    public static byte[] stringToBytes ( String texto ) {
        return texto.getBytes();
        // byte[] b = string.getBytes(StandardCharsets.UTF_8); // Ja
    } // ()

    // -------------------------------------------------------------------------------
    // -------------------------------------------------------------------------------


    /**
     * @brief Convierte una cadena de UUID en un objeto UUID.
     *
     * Este método convierte una representación de cadena de un UUID en un objeto UUID.
     * Se espera que la cadena tenga una longitud de 16 caracteres.
     *
     * @param uuid La cadena que representa el UUID.
     * @return Un objeto UUID correspondiente a la cadena proporcionada.
     * @throws Error Si la cadena no tiene 16 caracteres.
     */
    public static UUID stringToUUID( String uuid ) {
        if ( uuid.length() != 16 ) {
            throw new Error( "stringUUID: string no tiene 16 caracteres ");
        }
        byte[] comoBytes = uuid.getBytes();

        String masSignificativo = uuid.substring(0, 8);
        String menosSignificativo = uuid.substring(8, 16);
        UUID res = new UUID( Utilidades.bytesToLong( masSignificativo.getBytes() ), Utilidades.bytesToLong( menosSignificativo.getBytes() ) );

        // Log.d( MainActivity.ETIQUETA_LOG, " \n\n***** stringToUUID *** " + uuid  + "=?=" + Utilidades.uuidToString( res ) );

        // UUID res = UUID.nameUUIDFromBytes( comoBytes ); no va como quiero

        return res;
    } // ()

    // -------------------------------------------------------------------------------
    // -------------------------------------------------------------------------------


    /**
     * @brief Convierte un objeto UUID en una cadena.
     *
     * Este método convierte un objeto UUID en una representación de cadena.
     *
     * @param uuid El objeto UUID que se desea convertir.
     * @return Una cadena que representa el UUID.
     */
    public static String uuidToString ( UUID uuid ) {
        return bytesToString( dosLongToBytes( uuid.getMostSignificantBits(), uuid.getLeastSignificantBits() ) );
    } // ()

    // -------------------------------------------------------------------------------
    // -------------------------------------------------------------------------------


    /**
     * @brief Convierte un objeto UUID en una cadena hexadecimal.
     *
     * Este método convierte un objeto UUID en una representación de cadena hexadecimal.
     *
     * @param uuid El objeto UUID que se desea convertir.
     * @return Una cadena hexadecimal que representa el UUID.
     */
    public static String uuidToHexString ( UUID uuid ) {
        return bytesToHexString( dosLongToBytes( uuid.getMostSignificantBits(), uuid.getLeastSignificantBits() ) );
    } // ()

    // -------------------------------------------------------------------------------
    // -------------------------------------------------------------------------------


    /**
     * @brief Convierte un arreglo de bytes en una cadena.
     *
     * Este método convierte un arreglo de bytes en su representación de cadena.
     *
     * @param bytes El arreglo de bytes que se desea convertir.
     * @return Una cadena que representa los bytes, o una cadena vacía si el arreglo es nulo.
     */
    public static String bytesToString( byte[] bytes ) {
        if (bytes == null ) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append( (char) b );
        }
        return sb.toString();
    }

    // -------------------------------------------------------------------------------
    // -------------------------------------------------------------------------------


    /**
     * @brief Convierte dos longitudes en un arreglo de bytes.
     *
     * Este método toma dos longitudes y las convierte en un arreglo de bytes.
     *
     * @param masSignificativos La parte más significativa del UUID.
     * @param menosSignificativos La parte menos significativa del UUID.
     * @return Un arreglo de bytes que representa los dos longitudes.
     */
    public static byte[] dosLongToBytes( long masSignificativos, long menosSignificativos ) {
        ByteBuffer buffer = ByteBuffer.allocate( 2 * Long.BYTES );
        buffer.putLong( masSignificativos );
        buffer.putLong( menosSignificativos );
        return buffer.array();
    }

    // -------------------------------------------------------------------------------
    // -------------------------------------------------------------------------------


    /**
     * @brief Convierte un arreglo de bytes en un entero.
     *
     * Este método convierte un arreglo de bytes en un valor entero.
     *
     * @param bytes El arreglo de bytes que se desea convertir.
     * @return El valor entero correspondiente al arreglo de bytes.
     */
    public static int bytesToInt( byte[] bytes ) {
        return new BigInteger(bytes).intValue();
    }

    // -------------------------------------------------------------------------------
    // -------------------------------------------------------------------------------


    /**
     * @brief Convierte un arreglo de bytes en un long.
     *
     * Este método convierte un arreglo de bytes en un valor long.
     *
     * @param bytes El arreglo de bytes que se desea convertir.
     * @return El valor long correspondiente al arreglo de bytes.
     */
    public static long bytesToLong( byte[] bytes ) {
        return new BigInteger(bytes).longValue();
    }

    // -------------------------------------------------------------------------------
    // -------------------------------------------------------------------------------


    /**
     * @brief Convierte un arreglo de bytes en un entero, manejando errores.
     *
     * Este método convierte un arreglo de bytes en un valor entero,
     * y maneja posibles errores relacionados con la longitud del arreglo.
     *
     * @param bytes El arreglo de bytes que se desea convertir.
     * @return El valor entero correspondiente al arreglo de bytes, o 0 si el arreglo es nulo.
     * @throws Error Si el arreglo tiene más de 4 bytes.
     */
    public static int bytesToIntOK( byte[] bytes ) {
        if (bytes == null ) {
            return 0;
        }

        if ( bytes.length > 4 ) {
            throw new Error( "demasiados bytes para pasar a int ");
        }
        int res = 0;



        for( byte b : bytes ) {
           /*
           Log.d( MainActivity.ETIQUETA_LOG, "bytesToInt(): byte: hex=" + Integer.toHexString( b )
                   + " dec=" + b + " bin=" + Integer.toBinaryString( b ) +
                   " hex=" + Byte.toString( b )
           );
           */
            res =  (res << 8) // * 16
                    + (b & 0xFF); // para quedarse con 1 byte (2 cuartetos) de lo que haya en b
        } // for

        if ( (bytes[ 0 ] & 0x8) != 0 ) {
            // si tiene signo negativo (un 1 a la izquierda del primer byte
            res = -(~(byte)res)-1; // complemento a 2 (~) de res pero como byte, -1
        }
       /*
        Log.d( MainActivity.ETIQUETA_LOG, "bytesToInt(): res = " + res + " ~res=" + (res ^ 0xffff)
                + "~res=" + ~((byte) res)
        );
        */

        return res;
    } // ()

    // -------------------------------------------------------------------------------
    // -------------------------------------------------------------------------------


    /**
     * @brief Convierte un arreglo de bytes en una cadena hexadecimal.
     *
     * Este método convierte un arreglo de bytes en una representación de cadena hexadecimal.
     *
     * @param bytes El arreglo de bytes que se desea convertir.
     * @return Una cadena hexadecimal que representa el arreglo de bytes, o una cadena vacía si el arreglo es nulo.
     */
    public static String bytesToHexString( byte[] bytes ) {

        if (bytes == null ) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
            sb.append(':');
        }
        return sb.toString();
    } // ()
} // class Utilidades
// -----------------------------------------------------------------------------------
// -----------------------------------------------------------------------------------
// -----------------------------------------------------------------------------------
// -----------------------------------------------------------------------------------


