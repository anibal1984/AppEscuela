package com.asuarez.appescuela;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONArray;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class menuBienvenida extends AppCompatActivity {

    private TextView txtRepresentante;
    Spinner spAlumno;
    Button btnConsultar;

    String recuperado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_bienvenida);

        btnConsultar = (Button) findViewById(R.id.btnConsultarAlumno);

        //Accion del boton consultar
        btnConsultar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String selec = spAlumno.getSelectedItem().toString();
                if (selec.equals("listarAlumno")) {
                    Intent i = new Intent(getApplicationContext(), menuProfesor.class);
                    startActivity(i);
                }
            }
        });

        //Forzar y cargar icono en el ActionBar
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_iconlogo);


        //Activar ir atras
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Recuperando cedula
        Bundle recupera = getIntent().getExtras();
        if (recupera != null) {

            recuperado = recupera.getString("ced");

        }

        txtRepresentante = (TextView) findViewById(R.id.txtRepresentanteLogueado);
        Thread tr2 = new Thread() {

            @Override
            public void run() {
                final String resultado = registroGET(recuperado, "obtenerRepresentante");
                final String resultado2 = registroGET(recuperado, "listarAlumno");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mostrarRepresentante(resultado);
                        cargarSpinner(arregloSpinner(resultado2));
                    }
                });
            }
        };

        tr2.start();

        spAlumno = (Spinner) findViewById(R.id.spAlumno);
/*        spAlumno.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                final int pos = i;
                Thread tr = new Thread(){

                    @Override
                    public void run() {
                        final String resultado4 = registroGET(recuperado, spAlumno.getItemAtPosition(pos)+"");
                    }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });*/


    }//TERMINA EL ONCREATE

    //METODO QUE SE CONECTA CON EL WEBSERVICE
    public String registroGET(String ced, String accion) {

        URL url = null;
        String linea = "";
        int respuesta = 0;
        StringBuilder resul = null;

        try {

            if (accion.equals("obtenerRepresentante")) {

                url = new URL("http://104.131.2.220/webServices/listarRepCed.php?cedula=" + ced);

            } else if (accion.equals("listarAlumno")) {

                url = new URL("http://104.131.2.220/webServices/listarAlumno.php?cedula=" + ced);

            }

            HttpURLConnection conection = (HttpURLConnection) url.openConnection();

            respuesta = conection.getResponseCode();

            resul = new StringBuilder();

            if (respuesta == HttpURLConnection.HTTP_OK) {


                InputStream in = new BufferedInputStream(conection.getInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                while ((linea = reader.readLine()) != null) {

                    resul.append(linea);

                }

            }

        } catch (Exception e) {
        }

        return resul.toString();

    }

    //METODO QUE PERMITE VER EL NOMBRE DEL PRESENTANTE LOGUEADO
    public void mostrarRepresentante(String response) {

        try {

            JSONArray json = new JSONArray(response);
            for (int i = 0; i < json.length(); i++) {

                txtRepresentante.setText("Bienvenido " + json.getJSONObject(i).getString("Nombres_Compeltos"));

            }

        } catch (Exception e) {
        }

    }

    //METODO QUE PERMITE LLENAR EL ARRAY PARA EL SPINNER
    public ArrayList<String> arregloSpinner(String response) {

        ArrayList<String> listado = new ArrayList<String>();
        try {

            JSONArray json = new JSONArray(response);
            String texto = "";
            for (int i = 0; i < json.length(); i++) {

                texto = json.getJSONObject(i).getString("Nombres_Completos");
                listado.add(texto);

            }

        } catch (Exception e) {
        }

        return listado;

    }

    //METODO QUE PERMITE CARGAR EL SPINNER
    public void cargarSpinner(ArrayList<String> datos) {

        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, datos);
        spAlumno.setAdapter(adaptador);

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
