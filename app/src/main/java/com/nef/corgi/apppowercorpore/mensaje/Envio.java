package com.nef.corgi.apppowercorpore.mensaje;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.nef.corgi.apppowercorpore.DTO.MonitorDTO;
import com.nef.corgi.apppowercorpore.DTO.RutinaDTO;
import com.nef.corgi.apppowercorpore.DTO.UserDTO;
import com.nef.corgi.apppowercorpore.R;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class Envio extends AsyncTask<String, Integer, Boolean> {
    public Context context;
    UserDTO user = null;
    MonitorDTO monitor = null;
    private ProgressBar progressBar=null;
    SimpleDateFormat FORMATOFECHA = new SimpleDateFormat("DD/MM/AAAA");
    private static final String DL =";";
    private static final int FIN = 00001010 ;
    public int progreso=0;
    Calendar c= new GregorianCalendar();
    public Envio(Context c,UserDTO u, MonitorDTO mon) {
        context=c;
        user = u;
        monitor = mon;
    }
    public Envio(UserDTO u, MonitorDTO mon){
        user = u;
        monitor = mon;
    }
@Override
protected Boolean doInBackground(String... strings) {
    String csvuser=user.csvtoString();
    String csvmonitor=monitor.csvtoString();
        Boolean estado=null;
    List<RutinaDTO.Ejercicio> ejerciciolistcsv=new ArrayList<>();
    InputStream inputStream = context.getResources().openRawResource(R.raw.rutina_csv);
    String actualizacion;
    try {
        ReadCSV csvreader = new ReadCSV();
        RutinaDTO rutinacsv = csvreader.readRutinacsv(inputStream);
        RutinaDTO.Ejercicio ejercicio=null;
        ejerciciolistcsv=rutinacsv.getListaEjercicios();
        if(rutinacsv==null) { estado=false;//si la rutina esta vacia no actualizamos
        }
        else {

            progreso=rutinacsv.getListaEjercicios().size();
            actualizacion = csvuser+ DL + FORMATOFECHA.format(c.getTime()) + FIN +csvmonitor;//Union del mansaje de cabecera de user y monitor
            actualizacion=rutinacsv.csvtoStringRutina();
           //for (int i=0;i<rutinacsv.getListaEjercicios().size();i++) {//se xq no me deja meter un foreach
                for (RutinaDTO.Ejercicio ejercicioeach:ejerciciolistcsv) {
                    if(!isCancelled()) {
                        publishProgress((int)(ejerciciolistcsv.size()));
                    }
                    actualizacion=ejercicioeach.csvtoStringEjercicio();
                    progreso++;
                }
            }//mensaje de las rutinas
            //capturar las excepciones de actulizaciones
            //excepcion una cabeceras una de usuario una de monitor mas rutinalist.size()
            estado = true;

    }catch (IOException e)
    {
        actualizacion=null;
        e.printStackTrace();
        estado = false;
    }
        /*
         * Habria que añadir un enviar al servidor o bien al mail permitente del monitor
         * actualizacion = a formato mensaje en csv a mandar
         * se envia si el estado es true, se indica un fallo si estadao es false
         * */
        return estado;
        }

@Override
protected void onProgressUpdate(Integer... values){
        progressBar.setProgress(values[0]);
        progressBar.postInvalidate();
        super.onProgressUpdate(values);
        }
    protected void onPreExecute(){
        progressBar.setMax(progreso);//Hay q negociar el max
        progressBar.setProgress(0);
    }
        @Override
protected void onPostExecute(Boolean result) {
        super.onPostExecute(result);

        if (result) {//Revisar el toast ya que no los puede lanzar desde aqui
        Toast.makeText(context, "Actualizacion Correcta", Toast.LENGTH_LONG).show();
        }
        else{
        Toast.makeText(context, "Actualizacion Fallida", Toast.LENGTH_LONG).show();
        }

        }
}
//Ayuda
//http://www.sgoliver.net/blog/tareas-en-segundo-plano-en-android-i-thread-y-asynctask/
