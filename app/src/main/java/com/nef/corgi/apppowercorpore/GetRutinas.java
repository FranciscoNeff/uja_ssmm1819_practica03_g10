package com.nef.corgi.apppowercorpore;

import android.os.AsyncTask;
import android.os.Bundle;

import com.nef.corgi.apppowercorpore.DTO.RutinaDTO;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class GetRutinas extends AsyncTask<String,Void,RutinaDTO>{
    private static final String DL = ";";
    String user;
    public GetRutinas(String username){
        user=username;
    }

            private static final String HTTP_STATUS_OKCODE = "200";
            private static final String HTTP_STATUS_ERRORLOCALCODE = "4";
            private static final String HTTP_STATUS_ERRORSERVERCODE = "5";
            private static final String BAD_LOGGING="ERROR";
             @Override
            protected RutinaDTO doInBackground(String... strings){
            String csv="";
            RutinaDTO rutina = new RutinaDTO();
            String service = "http://localhost:8085/serverssmm/getrutina/"+user;
            try {
                URL urlservice = new URL(service);
                HttpURLConnection connection = (HttpURLConnection) urlservice.openConnection();
                connection.setReadTimeout(10000);//milisegundos
                connection.setConnectTimeout(15000);//milisegundos
                connection.setRequestMethod("GET");
                connection.setDoInput(true);
                connection.connect();
                String code = String.valueOf(connection.getResponseCode());//recibe el codigo de respuesta de la peticion 1xx 2xx etc
                if (code.equalsIgnoreCase(HTTP_STATUS_OKCODE)) {
                    BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
                    String line;
                    while ((line = br.readLine()) != null) {
                        csv=csv+line;
                    }
                }
                connection.disconnect();
                List<RutinaDTO.Ejercicio> ejerciciolistcsv = new ArrayList<>();
                String items[]=csv.split(DL);
                BufferedReader reader=null;
                String line="";
                String n_ejercicios="";//array con el nombre de los ejercicios
                ArrayList<Integer> series = new ArrayList<Integer>();//array de series
                ArrayList<String> repeticiones = new ArrayList<String>();//array de repeticiones
                RutinaDTO.Ejercicio ejercicio = new RutinaDTO.Ejercicio();

                try{
                    //Lectura
                    n_ejercicios = items[0];//el nombre del ejercicio siempre es la primera casilla
                    while((line=reader.readLine())!=null)
                        for(int i=1; i<items.length;i++){
                            try {
                                series.add(Integer.parseInt(items[i]));//leo la serie en la que esta, es un numero
                            } catch (NumberFormatException numberex) {
                                numberex.printStackTrace();
                            }
                            i++;
                            repeticiones.add(items[i]);//esto es un string
                        }
                    ejercicio.setNombreEjercicio(n_ejercicios);//introduce ejercicio
                    ejercicio.setSerie(series);//introduce el array de series //revisar porq introduce las series y repeticiones de todos
                    ejercicio.setRepeticiones(repeticiones);//introduce el array de repeticiones
                    ejerciciolistcsv.add(ejercicio);
                    rutina.setListaEjercicios(ejerciciolistcsv);
                }catch (IOException e) {

                }try {
                    reader.close();
                }catch (IOException ioe){}
            }catch (IOException e){

                }
            return rutina;
            }
        }


