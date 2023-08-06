package com.example.hirh;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DetalleVActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_vactivity);
        LinearLayout linearLayout = findViewById(R.id.linearLayout);

        String url = "http://192.168.100.47/puenteproyecto/obtener_datos_vacaciones.php";

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);
                                String id_vacaciones = jsonObject.getString("id_vacaciones");
                                String id_empleado = jsonObject.getString("id_empleado");
                                String fecha_inicio = jsonObject.getString("fecha_inicio");
                                String fecha_fin = jsonObject.getString("fecha_fin");
                                String estatus = jsonObject.getString("estatus");

                                // Crea un TextView para mostrar los datos
                                TextView textView = new TextView(DetalleVActivity.this);
                                textView.setText("Número de solicitud: " + id_vacaciones + "\n" +
                                        "Número de empleado: " + id_empleado + "\n" +
                                        "Fecha inicio: " + fecha_inicio + "\n" +
                                        "Área de trabajo: " + fecha_fin + "\n" +
                                        "Estatus: " + estatus + "\n" + "\n");
                                linearLayout.addView(textView);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);

    }//onCreate

    public void regresar(View view) {
        Intent intent = new Intent(this, VacacionesActivity.class);
        startActivity(intent);
    }//regresar
}//class