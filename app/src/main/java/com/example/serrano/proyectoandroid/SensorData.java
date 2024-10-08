package com.example.serrano.proyectoandroid;

public class SensorData {
    private String type; // Identificador del sensor
    private float value; // Valor de temperatura
    private String timestamp; // Marca de tiempo de la lectura

    // Constructor
    public SensorData(String type, float value, String timestamp) {
        this.type = type;
        this.value = value;
        this.timestamp = timestamp;
    }

    // Getters
    public String gettype() {
        return type;
    }



    public float getvalue() {
        return value;
    }

    public String getTimestamp() {
        return timestamp;
    }

    // Setters
    public void settype(String type) {
        this.type = type;
    }

    public void setMeedida(float value) {
        this.value = value;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}

