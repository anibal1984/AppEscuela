package com.asuarez.appescuela;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.PublicKey;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    Button btnIngresar;
    EditText txtCedula;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtCedula = (EditText) findViewById(R.id.txtCedula);
        btnIngresar = (Button) findViewById(R.id.btnIngresar);

        btnIngresar.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        Thread tr = new Thread(){

            @Override
            public void run() {

                final String resultado = enviarDatosGET(txtCedula.getText().toString());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        int r = obtDatosJSON(resultado);


                        if (r > 0){

                            Intent i = new Intent(getApplicationContext(), menuBienvenida.class);
                            i.putExtra("ced",txtCedula.getText().toString());
                            startActivity(i);

                        } else {

                            Toast.makeText(getApplicationContext(),"Número de Cédula Incorrecto",Toast.LENGTH_LONG).show();

                        }

                    }
                });

            }
        };

        tr.start();

    }

    public String enviarDatosGET(String cedula){

        URL url = null;
        String linea = "";
        int respuesta = 0;
        StringBuilder resul = null;

        try {
            url = new URL("http://104.131.2.220/webServices/valida.php?cedula="+cedula);
            HttpURLConnection conection = (HttpURLConnection)url.openConnection();
            respuesta = conection.getResponseCode();

            resul = new StringBuilder();

            if (respuesta == HttpURLConnection.HTTP_OK){

                InputStream in = new BufferedInputStream(conection.getInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                while ((linea=reader.readLine()) != null){

                    resul.append(linea);
                }
            }

        }catch (Exception e) {}

        return  resul.toString();

    }

    public int obtDatosJSON(String response){

        int res = 0;

        try {

            JSONArray json = new JSONArray(response);

            if (json.length() > 0){

                res = 1;

            }

        }catch (Exception e) {}

        return res;

    }

}
