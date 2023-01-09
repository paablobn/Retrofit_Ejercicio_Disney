package com.example.ejercicio1ex.retrofit_ejercicio_disney.conexiones;

import com.example.ejercicio1ex.retrofit_ejercicio_disney.modelos.Personaje;
import com.example.ejercicio1ex.retrofit_ejercicio_disney.modelos.Respuesta;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiConexiones {

    // obtener paginna inicial
    @GET("/characters")
    Call<Respuesta> getPersonajes();

    // obtener siguiente pagina
    @GET("/characters?")
    Call<Respuesta> getPage(@Query("page") String page);

    // obtener 1 personaje
    @GET("/characters/{id}")
    Call<Personaje> getPersonaje(@Path("id") String id);
}
