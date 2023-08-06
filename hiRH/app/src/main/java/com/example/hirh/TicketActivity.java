package com.example.hirh;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class TicketActivity extends AppCompatActivity {

    EditText edtnumticket, edtnumemp, edtnombre, edtarea, edttitulo, edtdescripcion;
    Button btnbuscar, btnagrgrar, btneditar,btneliminar,btnver;
    RequestQueue requestQueue;
    private final static String CHANNEL_ID="NOTIFICACION";
    public final static int NOTIFICACION_ID=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket);

        edtnumticket=findViewById(R.id.edtidticket);
        edtnumemp=findViewById(R.id.edtNumEmp);
        edtnombre=findViewById(R.id.edtNombre);
        edtarea=findViewById(R.id.edtArea);
        edttitulo=findViewById(R.id.edtTitulo);
        edtdescripcion=findViewById(R.id.edtDescripcion);


        btnagrgrar= findViewById(R.id.btnAgregar);
        btneditar= findViewById(R.id.btnEditar);
        btnbuscar= findViewById(R.id.btnBuscar);
        btneliminar= findViewById(R.id.btnEliminar);

        btnbuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buscarProducto("http://192.168.100.47/puenteproyecto/buscar.php?id_ticket="+edtnumticket.getText()+"");
            }
        });

        btnagrgrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ejecutarServicio("http://192.168.100.47/puenteproyecto/insertar.php");
            }
        });

        btneditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ejecutarServicio("http://192.168.100.47/puenteproyecto/editar.php");
            }
        });

        btneliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eliminarProducto("http://192.168.100.47/puenteproyecto/eliminar.php");
            }
        });


    }//conCreate

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

    private void ejecutarServicio(String URL){
        StringRequest stringRequest=new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(TicketActivity.this, "Ticket enviado", Toast.LENGTH_SHORT).show();
                crearCanalNotificacion();
                crearNotificacion();
                limpiarFormulario();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(TicketActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros=new HashMap<String, String>();
                parametros.put("id_ticket", edtnumticket.getText().toString());
                parametros.put("id_empleado", edtnumemp.getText().toString());
                parametros.put("nombre", edtnombre.getText().toString());
                parametros.put("area", edtarea.getText().toString());
                parametros.put("titulo", edttitulo.getText().toString());
                parametros.put("descripcion", edtdescripcion.getText().toString());

                return parametros;
            }
        };

        requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }//ejecutarServicio

    private void buscarProducto(String URL){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        edtnumemp.setText(jsonObject.getString("id_empleado"));
                        edtnombre.setText(jsonObject.getString("nombre"));
                        edtarea.setText(jsonObject.getString("area"));
                        edttitulo.setText(jsonObject.getString("titulo"));
                        edtdescripcion.setText(jsonObject.getString("descripcion"));
                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(TicketActivity.this, "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }//buscarProducto

    private void eliminarProducto(String URL){
        StringRequest stringRequest=new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(TicketActivity.this, "Ticket eliminado", Toast.LENGTH_SHORT).show();
                crearCanalNotificacion();
                crearNotificacion();
                limpiarFormulario();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(TicketActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros=new HashMap<String, String>();
                parametros.put("id_ticket", edtnumticket.getText().toString());

                return parametros;
            }
        };

        requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }//eliminarProducto

    private void limpiarFormulario() {
        edtnumticket.setText("");
        edtnumemp.setText("");
        edtnombre.setText("");
        edtarea.setText("");
        edttitulo.setText("");
        edtdescripcion.setText("");
    }//limpiarFormulario


    private void crearCanalNotificacion() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            CharSequence name = "Notificación";
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }//crearCanalNotificacion

    private void crearNotificacion(){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(),CHANNEL_ID);
        builder.setSmallIcon(R.drawable.notify);
        builder.setContentTitle("hi!RH");
        builder.setContentText("Operación exitosa");
        builder.setColor(Color.RED);
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        builder.setLights(Color.RED, 1000,1000);
        builder.setVibrate(new long[]{1000,1000,1000,1000,1000});
        builder.setDefaults(Notification.DEFAULT_SOUND);
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        notificationManagerCompat.notify(NOTIFICACION_ID, builder.build());
    }//crearNotificacion



}//class