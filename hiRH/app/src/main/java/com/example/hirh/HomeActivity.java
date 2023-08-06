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
import android.widget.ImageButton;

public class HomeActivity extends AppCompatActivity {

    ImageButton ti, in, va, as;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ti=findViewById(R.id.btnTicket);
        in=findViewById(R.id.btnCamara);
        va=findViewById(R.id.btnVacaciones);
        as=findViewById(R.id.btnAsistencia);

        ti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), TicketActivity.class);
                startActivity(intent);
            }
        });
        in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), EmpleadoActivity.class);
                startActivity(intent);
            }
        });

        as.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AsistenciaActivity.class);
                startActivity(intent);
            }
        });

        va.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), VacacionesActivity.class);
                startActivity(intent);
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

}//class