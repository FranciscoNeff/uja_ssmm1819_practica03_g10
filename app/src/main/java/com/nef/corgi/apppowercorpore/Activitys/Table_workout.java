package com.nef.corgi.apppowercorpore.Activitys;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import com.nef.corgi.apppowercorpore.DTO.RutinaDTO;
import com.nef.corgi.apppowercorpore.R;
import com.nef.corgi.apppowercorpore.mensaje.ReadCSV;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class Table_workout extends AppCompatActivity  {
    private List<RutinaDTO> rutinalistcsv = new ArrayList<>();
    private TableLayout tableworkout =null;
    //TODO tabla dinamica para los ejercicios tag para el editor
   //TODO darle estilos a la tabla

    @Override
    public void onCreate (Bundle savedInstanteState){
        super.onCreate(savedInstanteState);
        setContentView(R.layout.layout_show_workouts);
        tableworkout = findViewById(R.id.tableworkout) ;
        tableworkout.setShrinkAllColumns(true);
        tableworkout.setShrinkAllColumns(true);
        InputStream inputStream = getResources().openRawResource(R.raw.rutina_csv);
        ReadCSV readCSV=new ReadCSV();
        RutinaDTO rutina = new RutinaDTO();
        try {
            rutina=readCSV.readRutinacsv(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        RutinaDTO.Ejercicio ejercicios = new RutinaDTO.Ejercicio();
        ejercicios=rutina.getListaEjercicios().get(1);
        final TableRow row_header = new TableRow(this);
        row_header.setId(R.id.id_row_header);
        TextView headerview = new TextView(this);
        String headertext = "Nombre";
        headerview.setText(headertext);
        headerview.setTextColor(getColor(R.color.colorAccent));
        headerview.setTextSize(22);
        row_header.addView(headerview);//se añade la cabecera
String header_series="";
        for (int i=0;i<ejercicios.getSerie().size();i++){
                        header_series ="   "+ header_series+ejercicios.getSerie().get(i)+"       ";
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
            //hasta aqui funciona
            TextView repeticiones = new TextView(this);
            int tam=j+ejercicios.getSerie().size();//se q es una chapuza, pero funciona
            for(j =j;j<tam;j++){
                 rep="    "+rep+ejercicios.getRepeticiones().get(j)+"     ";
            }//aqui hay q investigar un tag para para meter el editor
            repeticiones.setText(rep);
            repeticiones.setTextSize(19);
            repeticiones.setTextColor(getColor(R.color.colorPrimary));
            row_body.addView(repeticiones);//se añade la lista de las repeticiones
            if(i%2==0){
            row_body.setBackgroundColor(getColor(R.color.colorendgradient));
            }else{
                row_body.setBackgroundColor(getColor(R.color.colorAccent));
            }
            tableworkout.addView(row_body);
            row_body.setId(R.id.id_row_body);
        }

    }
}
