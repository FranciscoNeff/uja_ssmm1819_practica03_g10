package com.nef.corgi.apppowercorpore.mensaje;

import android.content.Context;


import com.nef.corgi.apppowercorpore.DTO.RutinaDTO;

import com.nef.corgi.apppowercorpore.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
/***
 * Formato en el que se guardan las rutinas
 * Fecha en la que se realiza DD/MM/AAAA
 * Array de los ejercicios realizados en ese dia
 * series de los ejercicios
 * repeticiones de los ejercicios
 *
 */

//ejemplo de excepciones en java de clase 4
public class ReadCSV {
    public Context context;//esto sirve para usarlo en varios metodos
    public ReadCSV(Context c){
        context = c;
    }
    public ReadCSV(){}
    private static final String DL =";";
    private static final int FIN = 00001010 ;
    /*
    En este codigo desfragmentamos un formato csv que seria el correspondiente
    solo al body de las rutinas
    ejercicios;series;repetciones CRLF
     */
    public RutinaDTO readRutinacsv (InputStream inputStream) throws IOException {
        List<RutinaDTO.Ejercicio> ejerciciolistcsv=new ArrayList<>();
        boolean exist=false;
       // InputStream rutinacsv = context.getResources().openRawResource(R.raw.rutina_csv);//TODO nunca llega a leer bien el recurso
        InputStream rutinacsv=inputStream;
        BufferedReader reader = null;
            reader = new BufferedReader(new InputStreamReader(rutinacsv, Charset.forName("UTF-8")));
        RutinaDTO rutina = new RutinaDTO();
        if(reader!=null) {
            exist = true;
            String line = "";
            String n_ejercicios;//array con el nombre de los ejercicios
            int[] series = new int[3];//array de series
            String[] repeticiones = {};//array de repeticiones
            while ((line = reader.readLine()) != null) {
                try {

                    RutinaDTO.Ejercicio ejercicio = new RutinaDTO.Ejercicio();
                    String[] items = line.split(DL);//La cadena se trocea con ;
                    String s_f_rutina="DIArutina";
                   String timerutina="TiempoRUTINA";
                    //int i = 2;
                    int i =0;
int j=0;
                        //Lectura
                        n_ejercicios = items[i];
                        i++;
                        do {
                            try{
                            series[j] = (Integer.parseInt(items[i]));//leo la serie en la que esta, es un numero
                        }catch (NumberFormatException numberex){
                            numberex.printStackTrace();
                    }
                            i++;
                            repeticiones[j] = items[i];//esto es un string
                            i++;
                            j++;
                        }
                        while (Integer.parseInt(items[i]) != FIN); //deberia trocear hasta terminar toda la serie y repeticiones
                        i++;
                        try{
                            ejercicio.setNombreEjercicio(n_ejercicios);//introduce ejercicio
                            ejercicio.setSerie(series);//introduce el array de series
                            ejercicio.setRepeticiones(repeticiones);//introduce el array de repeticiones
                            ejerciciolistcsv.add(ejercicio);
                            rutina = new RutinaDTO(s_f_rutina,timerutina,ejerciciolistcsv);
                       //introduce la lista de ejercicios en la rutina
                        //introduce la fecha que el dia realiza la rutina
                        //introcude el timestamp de la rutina
                        }catch (RutinaDTO.MalformedRutinaException e){
                            e.printStackTrace();
                            rutina=null;
                        }

                } catch (NullPointerException e) {//Tengo que poner el nullPointerException ya que con las demas excepciones da fallo
                    // ya que debido al metodo pide devolver un objeto
                    e.printStackTrace();
                   rutina=null;
                }

            }
        }else
        {rutina=null;
        System.out.print("Archivo no encontrado");}
        if(exist!=true){rutina=null;}
        reader.close();
        return rutina;
    }
}






