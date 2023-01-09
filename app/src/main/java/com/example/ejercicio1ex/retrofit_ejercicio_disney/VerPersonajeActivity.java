package com.example.ejercicio1ex.retrofit_ejercicio_disney;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ejercicio1ex.retrofit_ejercicio_disney.conexiones.ApiConexiones;
import com.example.ejercicio1ex.retrofit_ejercicio_disney.conexiones.RetrofitObject;
import com.example.ejercicio1ex.retrofit_ejercicio_disney.modelos.Personaje;
import com.squareup.picasso.Picasso;

import java.net.HttpURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class VerPersonajeActivity extends AppCompatActivity {

    private ImageView imgPersonaje;
    private TextView lbNombre;
    private TextView lbFilms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_personaje);

        imgPersonaje = findViewById(R.id.imgVerPersonaje);
        lbNombre = findViewById(R.id.lbNombreVerPersonaje);
        lbFilms = findViewById(R.id.lbFilmsVerPersonaje);

        if (getIntent().getExtras() != null && getIntent().getExtras().getString("ID") != null){
            cargarPersonaje(getIntent().getExtras().getString("ID"));
        }
    }

    private void cargarPersonaje(String id) {
        Retrofit retrofit = RetrofitObject.getConexion();
        ApiConexiones api = retrofit.create(ApiConexiones.class);
        Call<Personaje> getPersonaje = api.getPersonaje(id);
        getPersonaje.enqueue(new Callback<Personaje>() {
            @Override
            public void onResponse(Call<Personaje> call, Response<Personaje> response) {
                if (response.code()== HttpURLConnection.HTTP_OK){
                    lbNombre.setText(response.body().getName());
                    lbFilms.setText("");
                    for (String film:response.body().getFilms()) {
                        lbFilms.setText(lbFilms.getText()+"\n"+film);
                    }
                    Picasso.get()
                            .load(response.body().getImageUrl())
                            .into(imgPersonaje);
                }
            }

            @Override
            public void onFailure(Call<Personaje> call, Throwable t) {

            }
        });
    }
}