package com.nef.corgi.apppowercorpore.Activitys;

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

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

    public class Table_workout_web extends AppCompatActivity //implements CompoundButton.OnCheckedChangeListener
    {
        String user;
    public Table_workout_web(String username){
        user=username;
    }
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
            GetRutinas gr =new GetRutinas(user);
            RutinaDTO rutina = new RutinaDTO();
            gr.execute((Runnable) rutina);
               // rutina=gr.execute();
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


