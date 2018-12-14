package com.nef.corgi.apppowercorpore.DTO;

import java.sql.Date;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RutinaDTO {
    private String diaRutina;
    List<Ejercicio> listaEjercicios =new ArrayList<Ejercicio>();
    private  String tiempo; //cuando se guarde este valor ya estara guardado como la diferencia entre empezar y terminar
    private static final String DL =";";
    private static final int FIN = 00001010 ;
    public List<Ejercicio> getListaEjercicios() {
        return listaEjercicios;
    }

    public void setListaEjercicios(List<Ejercicio> listaEjercicios) {
        this.listaEjercicios = listaEjercicios;
    }
    public String getDiaRutina() {
        return diaRutina;
    }

    public void setDiaRutina(String diaRutina) {
        this.diaRutina = diaRutina;
    }

    public String getTiempo() {
        return tiempo;
    }

    public void setTiempo(String tiempo) {
        this.tiempo = tiempo;
    }

    /**
     * <datos> = <rutina><PUNTOYCOMA><repeticiones>
     *
     *
     */
    public RutinaDTO(String diaRutina,String tiempo,List<Ejercicio> listaEjercicios) throws MalformedRutinaException{
if(diaRutina.length()<0){
    new MalformedRutinaException(1);
}
        if(tiempo.length()<0){
            new MalformedRutinaException(1);
        }
        this.diaRutina = diaRutina;
    this.listaEjercicios = listaEjercicios;

        this.tiempo=tiempo;
    }

    public RutinaDTO(){}


    public String csvtoStringRutina() {
        SimpleDateFormat FORMATOFECHA = new SimpleDateFormat("DD/MM/AAAA");
        String csv=FORMATOFECHA.format(diaRutina)+DL+tiempo;
        return csv+FIN;
    }


    public class MalformedRutinaException extends Exception {
        private int type = 0;
        private static final int RUTINA_CORRECTA = 0;
        private static final int FALTAN_ELEMENTOS = 1;
        private static final int BAD_SERIES = 2;
        private static final int BAD_REP = 3;
        private static final int BAD_NAME_WORK = 4;
        private
        MalformedRutinaException(int type){
           this.type=type;
        }
        }
    public static class Ejercicio extends RutinaDTO {
        private  String nombreEjercicio;
        private  ArrayList<Integer> serie;
        private  ArrayList<String> repeticiones;

        public ArrayList<Integer> getSerie() {
            return serie;
        }

        public void setSerie(ArrayList<Integer> serie) {
            this.serie = serie;
        }

        public ArrayList<String> getRepeticiones() {
            return repeticiones;
        }

        public void setRepeticiones(ArrayList<String> repeticiones) {
            this.repeticiones = repeticiones;
        }

        public String getNombreEjercicio() {
            return nombreEjercicio;
        }

        public void setNombreEjercicio(String nombreEjercicio) {
            this.nombreEjercicio = nombreEjercicio;
        }
        public Ejercicio(){

        }


        public Ejercicio( List<Ejercicio> listaEjercicios, String nombreEjercicio, ArrayList<Integer> serie, ArrayList<String> repeticiones) throws MalformedRutinaException {
            super();
            if (serie.size()<0 ){// no deja ||serie=null
                throw new MalformedRutinaException(2);
            }
            if (repeticiones.size()<0 ){
                throw new MalformedRutinaException(3);
            }
            if(nombreEjercicio.length()<3){
                throw new MalformedRutinaException(3);
            }
            this.nombreEjercicio = nombreEjercicio;
            this.serie = serie;
            this.repeticiones = repeticiones;
        }

        public String csvtoStringEjercicio() {
            String csvejercicio= nombreEjercicio;
            csvejercicio= DL;
            for (int i=0;i<serie.size();i++){
                csvejercicio=csvejercicio +DL+ serie.get(i) +DL+ repeticiones.get(i);
             }
            return csvejercicio+FIN;
        }

    }

    }

