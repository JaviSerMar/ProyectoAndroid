package com.example.serrano.proyectoandroid;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Body;

/**
 * Interfaz que define los métodos de la API para realizar solicitudes HTTP.
 */
public interface ApiService {

    /**
     * Realiza una solicitud POST para enviar los datos del sensor al servidor.
     *
     * @param sensorData Objeto que contiene los datos del sensor a enviar en el cuerpo de la solicitud.
     * @return Una llamada de Retrofit que envía los datos al servidor y no espera respuesta del cuerpo.
     */
    @POST("/")
    Call<Void> postSensorData(@Body SensorData sensorData);
}
