package com.example.hirh;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class EmpleadoActivity extends AppCompatActivity {

    EditText numemple, nombre, correo, direccion, telefono, edad, puesto, estatus;
    Button agregar, tomarf;
    private ImageView foto;
    Bitmap bitmap;
    RequestQueue requestQueue;
    private final static String CHANNEL_ID="NOTIFICACION";
    public final static int NOTIFICACION_ID=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empleado);

        numemple=findViewById(R.id.edtNumEmpleado);
        nombre=findViewById(R.id.edtNombreEmp);
        correo=findViewById(R.id.edtCorreoEmp);
        direccion=findViewById(R.id.edtDireccionEmp);
        telefono=findViewById(R.id.edtTelEmp);
        edad=findViewById(R.id.edtEdadEmp);
        puesto=findViewById(R.id.edtPuestoEmp);
        estatus=findViewById(R.id.edtEstatusEmp);
        foto=(ImageView) findViewById(R.id.imgEmpleado);
        agregar=findViewById(R.id.btnAgregarEmp);
        tomarf=findViewById(R.id.btnImagen);

        tomarf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirCamara();
            }
        });

        agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ejecutarServicio("http://192.168.100.47/puenteproyecto/insertar_empleado.php");
            }
        });

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

    private void ejecutarServicio(String URL){
        StringRequest stringRequest=new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(EmpleadoActivity.this, "Empleado registrado", Toast.LENGTH_SHORT).show();
                crearCanalNotificacion();
                crearNotificacion();
                limpiarFormulario();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(EmpleadoActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                String imagen = getStringImagen(bitmap);

                Map<String, String> parametros=new HashMap<String, String>();

                parametros.put("idempleado", numemple.getText().toString());
                parametros.put("nombre", nombre.getText().toString());
                parametros.put("correo", correo.getText().toString());
                parametros.put("direccion", direccion.getText().toString());
                parametros.put("telefono", telefono.getText().toString());
                parametros.put("edad", edad.getText().toString());
                parametros.put("puesto", puesto.getText().toString());
                parametros.put("estatus", estatus.getText().toString());
                parametros.put("imagen", imagen);

                return parametros;
            }
        };

        requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }//ejecutarServicio

    private void abrirCamara(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(intent.resolveActivity(getPackageManager()) != null){
            startActivityForResult(intent,1);
        }
    }//abirCamara

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Toast.makeText(this, "Tomar foto", Toast.LENGTH_SHORT).show();
        if( requestCode == 1 && resultCode == RESULT_OK){
            Toast.makeText(this, "Foto capturada", Toast.LENGTH_SHORT).show();
            Bundle extras = data.getExtras();
            bitmap = (Bitmap) extras.get("data");
            foto.setImageBitmap(bitmap);
        }
    }//onActivityResult

    public String getStringImagen(Bitmap bmp){
        ByteArrayOutputStream baos= new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100,baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedimage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedimage;
    }//getStringImagen



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

    private void limpiarFormulario() {
        numemple.setText("");
        nombre.setText("");
        correo.setText("");
        direccion.setText("");
        telefono.setText("");
        edad.setText("");
        puesto.setText("");
        estatus.setText("");
    }//limpiarFormulario

}//class