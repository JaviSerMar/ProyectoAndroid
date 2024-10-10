package com.example.serrano.proyectoandroid;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Clase que gestiona la creación y configuración del cliente Retrofit para realizar
 * solicitudes a un servidor API.
 */

public class ApiClient {

    /**
     * Dirección base del servidor API.
     * Reemplaza con la URL de tu servidor.
     * @note La IP actual del servidor es: 192.168.132.131
     */
    private static final String BASE_URL = "http://192.168.132.131:13000/";

    /** Instancia de Retrofit. */
    private static Retrofit retrofit = null;



    /**
     * Obtiene la instancia del cliente Retrofit.
     * Si aún no ha sido inicializada, se crea una nueva instancia con la configuración adecuada.
     *
     * @return Instancia de Retrofit configurada con la URL base y GsonConverterFactory.
     */

    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }



    /**
     * Obtiene una instancia del servicio de la API.
     *
     * @return Instancia de la interfaz ApiService para realizar llamadas a la API.
     */
    public static ApiService getApiService() {
        return getClient().create(ApiService.class);
    }
}
