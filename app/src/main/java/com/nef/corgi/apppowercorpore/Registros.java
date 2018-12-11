package com.nef.corgi.apppowercorpore;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.widget.TextView;

import com.nef.corgi.apppowercorpore.DTO.UserDTO;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;

public class Registros  {
    //esta clase aunque sea una actividad intenta hacer el apartado opcional, no funcionaba creando un fragment
    // no funciona bien esta clase, para la practica 3 se eliminara junto a su fragment_registe.xml
    public Context context;
public void Registros(Context c){
    this.context=c;
}
    SimpleDateFormat FORMATO = new SimpleDateFormat("y-M-d-H-m-s");
    public void AddRegistro(UserDTO reg){
        String cadena=reg.getUser_name()+";"+FORMATO.format(System.currentTimeMillis())+";"+"TipoRegistro"+".";
try {
    FileOutputStream fos =  context.openFileOutput("Registros.txt", context.MODE_APPEND);
    fos.write(cadena.getBytes());
    fos.close();
}catch (Exception ex){
    Log.e("Ficheros", "Error al escribir fichero a memoria interna");
}
    }

    public String ReadRegistro(){
        String  lectura="";
try{
    BufferedReader fin= new BufferedReader(new InputStreamReader(context.openFileInput("Registros.txt")));
    lectura=fin.readLine();
    fin.close();
}catch (Exception ex){
    Log.e("Ficheros", "Error al leer fichero a memoria interna");
}
        return lectura;
    }
  /*  protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_registros);
        final TextView helloTextView = (TextView) findViewById(R.id.listaRegitros);
        String registros = ReadRegistro();
        helloTextView.setText(registros);
    }*/
}


