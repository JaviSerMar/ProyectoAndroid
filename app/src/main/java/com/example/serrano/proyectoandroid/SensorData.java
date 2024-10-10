package com.example.serrano.proyectoandroid;


/**
 * Clase que representa los datos de un sensor, incluyendo su tipo, valor y marca de tiempo.
 */
public class SensorData {
    private String type; // < Identificador del sensor /
    private float value; // < Valor de la lectura del sensor (ejemplo: temperatura) /
    private String timestamp; // < Marca de tiempo de la lectura /


    /**
     * Constructor para crear una instancia de SensorData.
     *
     * @param type      Identificador del sensor.
     * @param value     Valor de la lectura del sensor.
     * @param timestamp Marca de tiempo de la lectura.
     */
    public SensorData(String type, float value, String timestamp) {
        this.type = type;
        this.value = value;
        this.timestamp = timestamp;
    }


    /**
     * Obtiene el tipo de sensor.
     *
     * @return El identificador del sensor.
     */
    public String gettype() {
        return type;
    }


    /**
     * Obtiene el valor de la lectura del sensor.
     *
     * @return El valor de la lectura.
     */
    public float getvalue() {
        return value;
    }


    /**
     * Obtiene la marca de tiempo de la lectura.
     *
     * @return La marca de tiempo de la lectura.
     */
    public String getTimestamp() {
        return timestamp;
    }


    /**
     * Establece el tipo de sensor.
     *
     * @param type El nuevo identificador del sensor.
     */
    public void settype(String type) {
        this.type = type;
    }


    /**
     * Establece el valor de la lectura del sensor.
     *
     * @param value El nuevo valor de la lectura.
     */
    public void setMeedida(float value) {
        this.value = value;
    }



    /**
     * Establece la marca de tiempo de la lectura.
     *
     * @param timestamp La nueva marca de tiempo de la lectura.
     */
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}

