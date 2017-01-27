package com.asuarez.appescuela;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class menuProfesor extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_profesor);


        //Forzar y cargar icono en el ActionBar
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_iconlogo);


        //Activar ir atras
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



    }

    //SE MUESTRAN LAS OPCIONES DEL MENU
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menubar, menu);
        return true;
    }

    //ACCION DE CADA OPCION DEL MENU
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.cerrarSesion:
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
            case R.id.acerca:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
