package com.example.ejercicio1ex.retrofit_ejercicio_disney;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.example.ejercicio1ex.retrofit_ejercicio_disney.adapters.PersonajesAdapter;
import com.example.ejercicio1ex.retrofit_ejercicio_disney.conexiones.ApiConexiones;
import com.example.ejercicio1ex.retrofit_ejercicio_disney.conexiones.RetrofitObject;
import com.example.ejercicio1ex.retrofit_ejercicio_disney.modelos.Personaje;
import com.example.ejercicio1ex.retrofit_ejercicio_disney.modelos.Respuesta;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private PersonajesAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private Respuesta respuesta;
    private List<Personaje> personajes;
    private Retrofit retrofitObject;
    private ApiConexiones conexiones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.contenedor);
        personajes = new ArrayList<>();
        adapter = new PersonajesAdapter(personajes, R.layout.personaje_view_holder, this);
        layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);

        retrofitObject = RetrofitObject.getConexion();
        conexiones = retrofitObject.create(ApiConexiones.class);

        cargaInicialPersonajes();

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                // si no puede hacer el scroll vertical haci abajo
                if (!recyclerView.canScrollVertically(1)) {
                    if (respuesta != null) {
                        String url = respuesta.getNextPage();
                        String page = url.split("=")[1];
                        cargarMasPaginas(page);
                    }
                }
            }
        });
    }

    private void cargarMasPaginas(String page) {
        Call<Respuesta> newPage = conexiones.getPage(page);
        newPage.enqueue(new Callback<Respuesta>() {
            @Override
            public void onResponse(Call<Respuesta> call, Response<Respuesta> response) {
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    int tamActual = personajes.size();
                    respuesta = response.body();
                    personajes.addAll(respuesta.getData());
                    adapter.notifyItemRangeInserted(tamActual, respuesta.getData().size());
                    Toast.makeText(MainActivity.this, "Cargada Pagina: " + page, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Respuesta> call, Throwable t) {

            }
        });
    }

    private void cargaInicialPersonajes() {
        Call<Respuesta> cargaInicial = conexiones.getPersonajes();
        cargaInicial.enqueue(new Callback<Respuesta>() {
            @Override
            public void onResponse(Call<Respuesta> call, Response<Respuesta> response) {
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    respuesta = response.body();
                    personajes.addAll(respuesta.getData());
                    adapter.notifyItemRangeInserted(0, respuesta.getData().size());

                }
            }

            @Override
            public void onFailure(Call<Respuesta> call, Throwable t) {

            }
        });
    }
}