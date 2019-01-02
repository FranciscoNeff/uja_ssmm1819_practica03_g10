package com.nef.corgi.apppowercorpore.Activitys;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.nef.corgi.apppowercorpore.Authetication;
import com.nef.corgi.apppowercorpore.DTO.Preferences;
import com.nef.corgi.apppowercorpore.DTO.UserDTO;
import com.nef.corgi.apppowercorpore.R;
import com.nef.corgi.apppowercorpore.Registros;
import com.nef.corgi.apppowercorpore.StatusNetkwork;

import java.io.BufferedReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;

import java.net.URL;
import java.text.ParseException;

import java.text.SimpleDateFormat;
import java.util.Date;

import es.dmoral.toasty.Toasty;


//TOAST DE COLORES Y CON ICONOS//https://www.codingdemos.com/android-toast-message-tutorial/
//el email sera el identificador unico de nuestra aplicacion(futuro)
//en Translations Edit, contraseña esta Untranslate debido a la ñ (si se ve que el uso de la ñ se hace casi obligatorio, se cambiara la fuente)
public class MainActivity extends AppCompatActivity implements Authetication.OnFragmentInteractionListener {

    private static final String SERVICE_DEFAULT_WEB = "http://";
    private static final String DOMAIN = "labtelema.ujaen.es";//el dominio es estatico
    private static final String RESOURCE = "/ssmm/autentica.php";
    private static final String QUERY_USER = "?user=";
    private static final String QUERY_PASS = "&pass=";
    private static final int PORT = 80;
    SimpleDateFormat FORMATO = new SimpleDateFormat("y-M-d-H-m-s");
    public static final String PARAM_USER_NAME="USER";
    public static final String PARAM_USER_EMAIL="email";
    public static final String PARAM_USER_EXPIRED="expires";
    public Context context;
    private StatusNetkwork networkStateReceiver;
    ConnectTask task = new ConnectTask();
    Registros pref = new Registros();
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("INICIO", "Bienvenido a PowerCorpore");
        FragmentManager fm = getSupportFragmentManager();
        Fragment frag_inicio = fm.findFragmentById(R.id.main_container);
        //Binvenida y muestra el nombre de usuario,en vez del de la app//si se comenta el menu lateral es como se ve mejo
        networkStateReceiver= new StatusNetkwork(getApplicationContext());
        networkStateReceiver.onReceive(getApplicationContext(), getIntent());
            //TODO forzar desconectado
//Seria interesante forzarlo a que lo haga una vez siempre y cuando este desconectado
//el primer conectado lo lanza dos veces
        if (frag_inicio == null) {
            FragmentTransaction ft = fm.beginTransaction();
            Authetication fragment = Authetication.newInstance("", "");
            ft.add(R.id.main_container, fragment, "login");
            ft.commit();
        } else
            Toast.makeText(this, getString(R.string.mainactivity_fragmentepresent), Toast.LENGTH_SHORT).show();
        SharedPreferences sf = getPreferences(MODE_PRIVATE);
        String expires = sf.getString("EXPIRES", "");
        String nombre = sf.getString("USER", "");
        if (nombre.length() > 0 && expires.length() > 0) {

            //comprobar el expires para q la sesion sea valida
            try {
                FORMATO = new SimpleDateFormat("y-M-d-H-m-s");
                if (FORMATO.parse(expires).getTime() > System.currentTimeMillis()) {
                    //expires>=tactual sesion valida
                    Intent intent = new Intent(this, ServiceActivity.class);
                    intent.putExtra(ServiceActivity.PARAM_USER_NAME, nombre);
                    intent.putExtra(ServiceActivity.PARAM_USER_EXPIRED, expires);//Maldito format
                    Toasty.success(this,getString(R.string.UserRegistred),Toast.LENGTH_SHORT).show();//quitar esta linea para la practica 3 no es visualemte atractividad
                    startActivity(intent);
                }
           } catch (ParseException e_date) {
                e_date.printStackTrace();
           }

        }

    }

    @Override
    public void onResume() {
        super.onResume();
        registerReceiver(networkStateReceiver, new IntentFilter(android.net.ConnectivityManager.CONNECTIVITY_ACTION));
    }
    @Override
    public void onPause() {
        unregisterReceiver(networkStateReceiver);
        super.onPause();
    }

    @Override
    public void onFragmentInteraction(UserDTO user) {
        Autentica userLOG = new Autentica();
        userLOG.execute(user);

    }


    public class Autentica extends AsyncTask<UserDTO, Void, UserDTO> { //recibo un usario sin sesion ,iteracion intermedia,devuelvo un usuario con SSID y EXPIRES
        private static final String HTTP_STATUS_OKCODE = "200";
        private static final String HTTP_STATUS_ERRORLOCALCODE = "4";
        private static final String HTTP_STATUS_ERRORSERVERCODE = "5";
        private static final String BAD_LOGGING="ERROR";

        @Override
        protected UserDTO doInBackground(UserDTO... userDTOS) {
            UserDTO data;
            UserDTO result = new UserDTO();
            if (userDTOS != null) {
                data = userDTOS[0];
                data.setDominio(DOMAIN);//se dejan activos por si en un futuro estos actuan a traves de un dominio distinto
                data.setPuerto(PORT);//pero tanto el dominio como el puerto sera fijos
                String service = SERVICE_DEFAULT_WEB + data.getDominio() + ":" + data.getPuerto() + RESOURCE + QUERY_USER + data.getUser_name() + QUERY_PASS + data.getPass();
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
                            if (line.startsWith(BAD_LOGGING)){
                                result=null;
                            }
                            else{
                            if (line.startsWith("SESSION-ID=")) {//compara que la cadena empiece de esta manera
                                String parts[] = line.split("&");//para trocear una cadena se usa split, devuelve un array por cada trozo
                                if (parts.length == 2) {
                                    if (parts[1].startsWith(("EXPIRES="))) {
                                        result = processSesion(data, parts[0], parts[1]);

                                    }
                                }
                            }

                        }
                        }
                        br.close();
                    } else if (code.startsWith(HTTP_STATUS_ERRORLOCALCODE)) {//errores 4XX
                        //revisar lo de start
                        Toast.makeText(getApplicationContext(), getText(R.string.http_code_error_4XX), Toast.LENGTH_LONG).show();
                        result = null;
                    } else if (code.startsWith(HTTP_STATUS_ERRORSERVERCODE)) {//errores 5XX
                        Toast.makeText(getApplicationContext(), getText(R.string.http_code_error_5XX), Toast.LENGTH_LONG).show();
                       result = null;
                    }
                    connection.disconnect();//cierra la conexion
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    result = null;//cambiar por result
                } catch (IOException ioex) {
                    ioex.printStackTrace();
                    result=null;
                } finally {
                    return result;
                }

            } else {
                return result;
            }
        }

        @Override
        protected void onPostExecute(UserDTO user) {
            super.onPostExecute(user);

            if (user != null) {
                Toast.makeText(getApplicationContext(), getString(R.string.correct_log_process )+ user.getUser_name(), Toast.LENGTH_LONG).show();
                SharedPreferences sp = getPreferences(MODE_PRIVATE);//crea un fichero con el nombre user.getUser_name()
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("USER", user.getUser_name());
                editor.putString("EMAIL", user.getEmail_user());
                editor.putString("SID", user.getSid());
                FORMATO = new SimpleDateFormat("y-M-d-H-m-s");
                Date temp = (user.getExpires());
                editor.putString("EXPIRES", FORMATO.format(temp));
                editor.commit();

                Intent intent = new Intent(getApplicationContext(), ServiceActivity.class);
                intent.putExtra(ServiceActivity.PARAM_USER_NAME, user.getUser_name());
                intent.putExtra(ServiceActivity.PARAM_USER_SID, user.getSid());
                intent.putExtra(ServiceActivity.PARAM_USER_EXPIRED, FORMATO.format(user.getExpires()));
                startActivity(intent);

            } else {
                SharedPreferences sp = getPreferences(MODE_PRIVATE);//crea un fichero con el nombre user.getUser_name()
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("USER","");
                editor.putString("EMAIL", "");
                editor.putString("SID", "");
                editor.putString("EXPIRES", "");
                editor.commit();
                Toasty.error(getApplicationContext(),getString(R.string.Bad_logging),Toast.LENGTH_SHORT).show();

            }
    }

    /**
     * @param input   userDTO
     * @param session SESSION ID=XX
     * @param expires EXPIRES=XX
     * @return userDTO
     */

    protected UserDTO processSesion(UserDTO input, String session, String expires) {
        FORMATO = new SimpleDateFormat("y-M-d-H-m-s");
        session = session.substring(session.indexOf("=") + 1, session.length());//copia la cadeda desde que encuentre el igual
        expires = expires.substring(expires.indexOf("=") + 1, expires.length());//como la copia desde que encuentra el igual le suma uno para coger la cadena
        input.setSid(session);
        try {
            input.setExpires(FORMATO.parse(expires));//se le introduce un date
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return input;
    }
}


    private String downloadURL(String user, String pass) throws IOException {
        InputStream is = null;
        String result = "";

        HttpURLConnection conn = null;
        try {
            String contentAsString = "";
            String tempString = "";
            String url = SERVICE_DEFAULT_WEB + DOMAIN + RESOURCE + QUERY_PASS + user + QUERY_PASS + pass;
            URL service_url = new URL(url);
            conn = (HttpURLConnection) service_url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            // Starts the query
            conn.connect();
            final int response = conn.getResponseCode();
            final int contentLength = conn.getHeaderFieldInt("Content-length", 1000);
            String mimeType = conn.getHeaderField("Content-Type");
            String encoding = mimeType.substring(mimeType.indexOf(";"));
            Log.d(SERVICE_DEFAULT_WEB, "The response is: " + response);
            is = conn.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            while ((tempString = br.readLine()) != null) {
                contentAsString = contentAsString + tempString;
                task.onProgressUpdate(contentAsString.length());
            }

            return contentAsString;
        } catch (MalformedURLException mex) {
            result = "URL mal formateada: " + mex.getMessage();
            System.out.println(result);
        } catch (IOException e) {
            result = "Excepción: " + e.getMessage();
            System.out.println(result);
        } finally {
            if (is != null) {
                is.close();
                conn.disconnect();
            }
        }
        return result;
    }

class ConnectTask extends AsyncTask<UserDTO, Integer, String> {
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        TextView banner = findViewById(R.id.main_degree);
        banner.setText(R.string.main_connecting);
    }
    @Override
    protected String doInBackground(UserDTO... userDTOS) {
        //Hacer un check db

        try {
            return downloadURL( userDTOS[0].getUser_name(), userDTOS[0].getPass());
        } catch (IOException ioex) {
            ioex.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        Toast.makeText(getApplicationContext(), getString(R.string.main_progress) + " " + String.valueOf(values[0]), Toast.LENGTH_LONG).show();

    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
        TextView banner = findViewById(R.id.main_degree);
        banner.setText(R.string.main_connected);
    }
}
}
