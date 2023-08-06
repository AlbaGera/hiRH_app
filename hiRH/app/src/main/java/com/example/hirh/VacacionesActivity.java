package com.example.hirh;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class VacacionesActivity extends AppCompatActivity {

    EditText edtnumemp,edtfechaini, edtfechafin;
    Button btnsolicitud, btnversolicitud, btnfechai, btnfechaf;
    private int dia, mes, ano;
    RequestQueue requestQueue;
    private final static String CHANNEL_ID="NOTIFICACION";
    public final static int NOTIFICACION_ID=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vacaciones);

        edtnumemp=findViewById(R.id.edtNumEmp);
        edtfechafin=findViewById(R.id.edtFechaf);
        edtfechaini=findViewById(R.id.edtFechai);

        btnsolicitud=findViewById(R.id.btnSolicitud);
        btnversolicitud=findViewById(R.id.btnVerSolicitud);
        btnfechai=findViewById(R.id.btnfechaini);
        btnfechaf=findViewById(R.id.btnfechainf);

        btnsolicitud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                solicitarVacaciones("http://192.168.100.47/puenteproyecto/solicitar.php");
            }
        });

        btnfechaf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar c = Calendar.getInstance();
                dia=c.get(Calendar.DAY_OF_MONTH);
                mes=c.get(Calendar.MONTH);
                ano=c.get(Calendar.YEAR);

                DatePickerDialog datePickerDialog = new DatePickerDialog(VacacionesActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int monthofYear, int dayofMonth) {
                        edtfechafin.setText(year + "-" + (monthofYear+1) + "-" + dayofMonth);
                    }
                },dia,mes,ano);
                datePickerDialog.show();
            }
        });

        btnfechai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Calendar c = Calendar.getInstance();
                    dia=c.get(Calendar.DAY_OF_MONTH);
                    mes=c.get(Calendar.MONTH);
                    ano=c.get(Calendar.YEAR);

                    DatePickerDialog datePickerDialog = new DatePickerDialog(VacacionesActivity.this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker datePicker, int year, int monthofYear, int dayofMonth) {
                            edtfechaini.setText(year + "-" + (monthofYear+1) + "-" + dayofMonth);
                        }
                    },dia,mes,ano);
                    datePickerDialog.show();

            }
        });

        btnversolicitud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), DetalleVActivity.class);
                startActivity(intent);
            }
        });

    }//onCreate


    private void solicitarVacaciones(String URL){
        StringRequest stringRequest=new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(VacacionesActivity.this, "Solicitud enviada", Toast.LENGTH_SHORT).show();
                crearCanalNotificacion();
                crearNotificacion();
                limpiarCampos();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(VacacionesActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros=new HashMap<String, String>();
                parametros.put("id_empleado", edtnumemp.getText().toString());
                parametros.put("fecha_inicio", edtfechaini.getText().toString());
                parametros.put("fecha_fin", edtfechafin.getText().toString());

                return parametros;
            }
        };

        requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }//solicitarVacaciones

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

    public void limpiarCampos(){
        edtnumemp.setText("");
        edtfechafin.setText("");
        edtfechaini.setText("");
    }

}//class