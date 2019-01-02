package com.nef.corgi.apppowercorpore.Activitys;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.nef.corgi.apppowercorpore.DTO.RutinaDTO;
import com.nef.corgi.apppowercorpore.GetRutinas;
import com.nef.corgi.apppowercorpore.R;
import com.nef.corgi.apppowercorpore.mensaje.ReadCSV;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

    public class Table_workout_web extends AppCompatActivity //implements CompoundButton.OnCheckedChangeListener
    {
        String user;
    public Table_workout_web(String username){
        user=username;
    }
        private static final String DL = ";";
        private List<RutinaDTO> rutinalistcsv = new ArrayList<>();
        private TableLayout tableworkout =null;
        ToggleButton btnToggle;

        //TODO tabla dinamica para los ejercicios tag para el editor
        //TODO darle estilos a la tabla
//TODO revisar lo de la toolbar
        @Override
        public void onCreate (Bundle savedInstanteState){
            super.onCreate(savedInstanteState);
            android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar_workout);
            setSupportActionBar(toolbar);


            btnToggle = (ToggleButton) findViewById(R.id.workout_BtnToggle);
            // btnToggle.setOnCheckedChangeListener(this);

            setContentView(R.layout.layout_show_workouts);
            tableworkout = findViewById(R.id.tableworkout);
            tableworkout.setShrinkAllColumns(true);
            tableworkout.setShrinkAllColumns(true);
            InputStream inputStream = getResources().openRawResource(R.raw.rutina_csv);

            RutinaDTO rutina = new RutinaDTO();
            GetRutinas gr = new GetRutinas(user);
            gr.execute((Runnable) rutina);
            if(rutina != null){
            RutinaDTO.Ejercicio ejercicios = new RutinaDTO.Ejercicio();
            ejercicios=rutina.getListaEjercicios().get(1);
            final TableRow row_header = new TableRow(this);
            row_header.setId(R.id.id_row_header);
            TextView headerview = new TextView(this);
            String headertext = "Ejercicio";
            headerview.setText(headertext);
            headerview.setTextColor(getColor(R.color.colorAccent));
            headerview.setTextSize(22);
            row_header.addView(headerview);//se añade la cabecera
            String header_series="";
            for (int i=0;i<ejercicios.getSerie().size();i++){
                header_series =" "+ header_series+"Serie "+(i+1)+"  ";
            }
            TextView seriesView = new TextView(this);
            seriesView.setText(header_series);
            seriesView.setTextColor(getColor(R.color.colorAccent));
            seriesView.setTextSize(22);
            row_header.setBackgroundColor(getColor(R.color.colorPrimary));

            row_header.addView(seriesView);//se añade la cabecera
            tableworkout.addView(row_header);
            String rep="   ";

            int j=0;
            for(int i=0;i<rutina.getListaEjercicios().size();i++){
                final TableRow row_body = new TableRow(this);
                rep="";
                TextView nameexercise = new TextView(this);
                //Nombre del ejercicio
                ejercicios=rutina.getListaEjercicios().get(i);
                nameexercise.setText(ejercicios.getNombreEjercicio());
                nameexercise.setTextColor(getColor(R.color.colorPrimaryDark));
                nameexercise.setTextSize(19);
                row_body.addView(nameexercise);//se añade la lista de los ejercicios
                TextView repeticiones = new TextView(this);
                int tam=j+ejercicios.getSerie().size();//se q es una chapuza, pero funciona
                for(j =j;j<tam;j++){
                    rep="   "+rep+ejercicios.getRepeticiones().get(j)+"            ";
                }//aqui hay q investigar un tag para para meter el editor
                repeticiones.setText(rep);
                repeticiones.setTextSize(19);
                repeticiones.setTextColor(getColor(R.color.textrep));
                row_body.addView(repeticiones);//se añade la lista de las repeticiones
                if(i%2==0){
                    row_body.setBackgroundColor(getColor(R.color.rowbodyimpar));
                }else{
                    row_body.setBackgroundColor(getColor(R.color.rowbodypar));
                }
                tableworkout.addView(row_body);
                row_body.setId(R.id.id_row_body);
            }
            }else{
                final TableRow row_header = new TableRow(this);
                row_header.setId(R.id.id_row_header);
                TextView headerview = new TextView(this);
                String headertext = "Rutina no encontrada";
                headerview.setText(headertext);
                headerview.setTextColor(getColor(R.color.colorAccent));
                headerview.setTextSize(22);
                row_header.addView(headerview);
                tableworkout.addView(row_header);
            }

            ///////////////////////peticion web a las rutinas/////////////////////
            class GetRutinas extends AsyncTask<String,Void,RutinaDTO> {
                private static final String HTTP_STATUS_OKCODE = "200";
                private static final String HTTP_STATUS_ERRORLOCALCODE = "4";
                private static final String HTTP_STATUS_ERRORSERVERCODE = "5";
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
                                csv = csv + line;
                            }
                            connection.disconnect();
                            List<RutinaDTO.Ejercicio> ejerciciolistcsv = new ArrayList<>();
                            String items[] = csv.split(DL);
                            BufferedReader reader = null;

                            String n_ejercicios = "";//array con el nombre de los ejercicios
                            ArrayList<Integer> series = new ArrayList<Integer>();//array de series
                            ArrayList<String> repeticiones = new ArrayList<String>();//array de repeticiones
                            RutinaDTO.Ejercicio ejercicio = new RutinaDTO.Ejercicio();
                            try {
                                //Lectura
                                n_ejercicios = items[0];//el nombre del ejercicio siempre es la primera casilla
                                while ((line = reader.readLine()) != null)
                                    for (int i = 1; i < items.length; i++) {
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
                            } catch (IOException e) {

                            }
                            try {
                                reader.close();
                            } catch (IOException ioe) {
                            }
                        }else{
                            rutina = null;
                        }
                        }catch(IOException e){

                        }





                    return rutina;
                }
            }
//TODO seria interesante en un futuro cambiar solo el tooglebutton con un cronometro ademas y un boton de pause
            //https://www.youtube.com/watch?v=GCrgW6ROYu0 referencia
        }
    /*
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        long t_rutina=0;
        if (btnToggle.isChecked())
            if (btnToggle.getTextOn().equals(getString(R.string.workout_toggle_on))){
            t_rutina=System.currentTimeMillis();}
            else{
            t_rutina=t_rutina-System.currentTimeMillis();
            }
    }*/
    }


