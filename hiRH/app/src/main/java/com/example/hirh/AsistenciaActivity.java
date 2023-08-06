package com.example.hirh;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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

public class AsistenciaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asistencia);
        LinearLayout linearLayout = findViewById(R.id.linearLayout);

        String url = "http://192.168.100.47/puenteproyecto/obtener_datos_horas.php" ;

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);
                                String empleado_id = jsonObject.getString("empleado_id");
                                String date = jsonObject.getString("date");
                                String status = jsonObject.getString("status");


                                TextView textView = new TextView(AsistenciaActivity.this);
                                textView.setText(
                                        "Número de empleado: " + empleado_id + "\n" +
                                        "Fecha registrada: " + date + "\n" +
                                        "Estaus: " + status + "\n" + "\n");
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_principal,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int itemId = item.getItemId();
        if (itemId == R.id.itmHome) {
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
        } else if (itemId == R.id.itmTicket) {
            Intent intent = new Intent(this, TicketActivity.class);
            startActivity(intent);
        }else if (itemId == R.id.itmEmpleado) {
            Intent intent = new Intent(this, EmpleadoActivity.class);
            startActivity(intent);
        }else if (itemId == R.id.itmVacaciones) {
            Intent intent = new Intent(this, VacacionesActivity.class);
            startActivity(intent);
        }else if (itemId == R.id.itmPerfil) {
            Intent intent = new Intent(this, AsistenciaActivity.class);
            startActivity(intent);
        }else if (itemId == R.id.itmCerrar) {
            cerrarSesion();
        }
        return super.onOptionsItemSelected(item);
    }//onOptionsItemSelected

    public void cerrarSesion (){
        SharedPreferences preferences=getSharedPreferences("preferenciasLogin", Context.MODE_PRIVATE);
        preferences.edit().clear().commit();

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }//cerrarSesion

}//class