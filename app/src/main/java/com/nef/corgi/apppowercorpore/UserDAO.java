package com.nef.corgi.apppowercorpore;

import android.content.SharedPreferences;

import com.nef.corgi.apppowercorpore.DTO.UserDTO;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


public class UserDAO {
    public static SimpleDateFormat FORMATO = new SimpleDateFormat("dd/MM/yyyy");
    public static void Credenciales (SharedPreferences sp, UserDTO userdto){

        //sp = getPreferences(MODE_PRIVATE);//crea un fichero de usuarios //TODO revisar el preferences
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("user",userdto.getUser_name());
        editor.putString("Email",userdto.getEmail_user());
        editor.putString("SID",userdto.getSid());
        editor.putString("EXPIRES",FORMATO.format(userdto.getExpires()));
        editor.putString("LAST_USER",userdto.getUser_name());
        editor.commit();
    }
//    public void UserDB_Test(){
//        List<UserDTO> listdb = new ArrayList<>();
//        listdb.add(new UserDTO("user1","12345"));
//        listdb.add(new UserDTO("user2","12345"));
//        listdb.add(new UserDTO("user3","12345"));
//    }//guardar para hacer un check


}
