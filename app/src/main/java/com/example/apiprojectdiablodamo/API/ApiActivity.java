package com.example.apiprojectdiablodamo.API;

import android.os.Bundle;
import android.util.Base64;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.apiprojectdiablodamo.R;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApiActivity extends AppCompatActivity {

    private String jsonString; // Declarar la variable aquí
    private TextView jsonTextView; // Declarar el TextView aquí

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.api_layout);

        // Obtén una referencia al TextView
        jsonTextView = findViewById(R.id.jsonTextView);

        String clientId = "0cd1b84e2eb34dcf89f6731e1282f74e";
        String clientSecret = "6MKHGsFhv8dU9lE3jvL9z2aUhpGsmbwW";
        String credentials = clientId + ":" + clientSecret;
        String authHeaderValue = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);

        ApiInterface oAuthApiInterface = ApiService.getOAuthApiInterface();
        Call<AccessTokenResponse> callToken = oAuthApiInterface.obtenerTokenDeAcceso("client_credentials", authHeaderValue);

        callToken.enqueue(new Callback<AccessTokenResponse>() {
            @Override
            public void onResponse(Call<AccessTokenResponse> call, Response<AccessTokenResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String accessToken = "Bearer " + response.body().getAccess_token();
                    obtenerPersonaje(ApiService.getApiInterface(), accessToken);
                }
            }

            @Override
            public void onFailure(Call<AccessTokenResponse> call, Throwable t) {
                // Manejar el fallo en la llamada a la API
            }
        });
    }

    private void obtenerPersonaje(ApiInterface apiInterface, String accessToken) {
        Call<Personaje> callPersonaje = apiInterface.obtenerPersonaje(accessToken);

        // Dentro del método obtenerPersonaje
        callPersonaje.enqueue(new Callback<Personaje>() {
            @Override
            public void onResponse(Call<Personaje> call, Response<Personaje> response) {
                if (response.isSuccessful()) {
                    Personaje personaje = response.body();
                    jsonString = new Gson().toJson(response.body());
                    // Haz algo con los datos del personaje

                    // Verifica si jsonString no es nulo antes de formatearlo
                    if (jsonString != null) {
                        // Formatea el JSON para que sea legible
                        String formattedJson = formatJson(jsonString);

                        // Establece el JSON formateado en el TextView
                        jsonTextView.setText(formattedJson);
                    } else {
                        // Manejar el caso en que jsonString sea nulo
                    }
                } else {
                    // Manejar respuesta no exitosa
                }
            }

            @Override
            public void onFailure(Call<Personaje> call, Throwable t) {
                // Manejar el fallo en la llamada a la API
            }
        });
    }

    private String formatJson(String jsonString) {
        try {
            JSONObject json = new JSONObject(jsonString);
            return json.toString(4); // Indentación de 4 espacios
        } catch (JSONException e) {
            e.printStackTrace();
            return "Error al formatear el JSON";
        }
    }
}
