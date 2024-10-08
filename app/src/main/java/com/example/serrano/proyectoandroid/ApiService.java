package com.example.serrano.proyectoandroid;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Body;

public interface ApiService {
    @POST("/")
    Call<Void> postSensorData(@Body SensorData sensorData);
}
