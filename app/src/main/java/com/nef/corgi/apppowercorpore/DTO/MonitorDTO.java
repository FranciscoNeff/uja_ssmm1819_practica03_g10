package com.nef.corgi.apppowercorpore.DTO;

public class MonitorDTO {
    String nameM;
    String emailM;
    private static final String DL =";";
    private static final int FIN = 00001010 ;
    public String getNameM() {
        return nameM;
    }

    public void setNameM(String nameM) {
        this.nameM = nameM;
    }

    public String getEmailM() {
        return emailM;
    }

    public void setEmailM(String emailM) {
        this.emailM = emailM;
    }

    public MonitorDTO(String nameM, String emailM) throws MalformedMonitorException{
       if(nameM.length()<4){
           throw new MalformedMonitorException(1);
       }
        if(emailM.length()<4){
            throw new MalformedMonitorException(1);
        }
        this.nameM = nameM;
        this.emailM = emailM;
    }
public MonitorDTO(){}
    public String csvtoString() {
        return  nameM +DL+ emailM +FIN ;
    }

    public class MalformedMonitorException extends Exception {
        private int type = 0;
        private static final int MONITOR_CORRECTO = 0;
        private static final int FALTAN_ELEMENTOS = 1;
        private
        MalformedMonitorException (int type){
            this.type=type;
        }
    }
}
