package com.nef.corgi.apppowercorpore.DTO;

import java.util.Date;

public class UserDTO {
    private String user_name;
    private String email_user;
    private String pass;
    private String sid;
    private Date expires;
    private String dominio;
    private int puerto;
    private static final String DL =";";
    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public Date getExpires() {
        return expires;
    }

    public void setExpires(Date expires) {
        this.expires = expires;
    }

    public String getDominio() {
        return dominio;
    }

    public void setDominio(String dominio) {
        this.dominio = dominio;
    }

    public int getPuerto() {
        return puerto;
    }

    public void setPuerto(int puerto) {
        this.puerto = puerto;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getEmail_user() {
        return email_user;
    }

    public void setEmail_user(String email_user) {
        this.email_user = email_user;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }
    public UserDTO (){}

    public UserDTO(String user_name, String pass,String email_user )

    throws MalformedUserException{

        this.user_name = user_name;
        if (user_name.length()<4){
            throw new MalformedUserException(2);
        }
        this.pass = pass;
        if (pass.length()<4){
            throw new MalformedUserException(3);
        }
        this.email_user = email_user;//cambiar por email_user; //dejamos este para comprobar mas rapido
        if (email_user.length()<9){
            throw new MalformedUserException(4);
        }
        // como tanto el dominio como el puerto permanecera invisibles al usuario estos se pasara con un valor predifinido al usuario
        //dominio ="labtelemaujaen.es";
        //puerto=80;
    }

    public UserDTO(String user_name, String email_user, String pass,String dominio, int puerto, String sid,Date expires) {
        this.user_name = user_name;
        this.email_user = email_user;
        this.pass = pass;
        this.dominio=dominio;//como el dominio va a ser transparente se deja predefinido
        this.puerto=puerto;
        this.expires=expires;
        this.sid=sid;
    }
//public UserDTO(String user_name,String pass){
//    this.pass = pass;
//    this.user_name = user_name;
//}


    public void setExpires(String expires) {
    }


    public String csvtoString() {
        return  user_name +DL+ email_user ;
    }

public class MalformedUserException extends Exception {
        private int type = 0;
        private static final int USER_CORRECTA = 0;
        private static final int FALTAN_ELEMENTOS = 1;
        private static final int BAD_NAME = 2;
        private static final int BAD_PASS = 3;
        private static final int BAD_EMAIL = 4;
        private
        MalformedUserException(int type){
            this.type=type;
        }
    }
}


