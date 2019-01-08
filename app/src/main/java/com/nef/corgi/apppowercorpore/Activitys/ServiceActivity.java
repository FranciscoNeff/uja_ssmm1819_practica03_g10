package com.nef.corgi.apppowercorpore.Activitys;

import android.content.Intent;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.design.widget.NavigationView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.nef.corgi.apppowercorpore.DTO.MonitorDTO;
import com.nef.corgi.apppowercorpore.DTO.MonitorDTO.MalformedMonitorException;
import com.nef.corgi.apppowercorpore.DTO.UserDTO;
import com.nef.corgi.apppowercorpore.R;
import com.nef.corgi.apppowercorpore.mensaje.Envio;

import java.text.SimpleDateFormat;

public class ServiceActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    //Variables estaticas de control de usuario
    public static final String PARAM_USER_NAME="name";
    public static final String PARAM_USER_EMAIL="email";
    public static final String PARAM_USER_SID="sid";
    public static final String PARAM_USER_EXPIRED="expires";
    public static final UserDTO USERLOG = new UserDTO();
    SimpleDateFormat FORMATO = new SimpleDateFormat("y-M-d-H-m-s");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //botón flotante
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();//añadir accion(futuro)
            }
        });
        //Menu lateral
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        Intent intent = getIntent();
        String s_user = intent.getStringExtra(PARAM_USER_NAME);
        String s_sid = intent.getStringExtra(PARAM_USER_SID);
        String s_expires= intent.getStringExtra(PARAM_USER_EXPIRED);
        USERLOG.setUser_name(s_user);
        // AL menu lateral le pasamos el nombre del user
        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = (TextView) headerView.findViewById(R.id.show_user_name);
        navUsername.setTextColor(getColor(R.color.colorPrimaryDark));
        navUsername.setText(s_user);

        //Introduce el expires
        TextView navsubHeader= (TextView) headerView.findViewById(R.id.show_expires);
        navsubHeader.setTextColor(getColor(R.color.colorPrimaryDark));
        navsubHeader.setText(s_expires);
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        //llamada a la actualizacion de datos
        if (id == R.id.action_update) {//codigo para actualizar las rutinas de cliente a server
            MonitorDTO pmon;
            try {
               pmon = new MonitorDTO("NameMonitor", "MailMonitor");
            }catch (MalformedMonitorException e){
                pmon=null;
            }
Envio envio=new Envio(USERLOG,pmon);
envio.execute();
            }
            return true;
    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id ==  R.id.nav_user) {
//opcion para ver datos del usuario
        } else if (id == R.id.nav_work_out) { //opcion para elegir las rutinas
//metodo para mostrar rutinas desde la web
            Intent workout = new Intent(ServiceActivity.this,Table_workout_web.class);
            workout.putExtra("username",USERLOG.getUser_name());
          startActivity(workout);
          //metodo para mostrar rutinas estaticas
         //   Intent workout = new Intent(ServiceActivity.this,Table_workout.class);
          // startActivity(workout);

        }else if (id == R.id.nav_result) {
//opcion para ver los resultado las rutinas
        } else if (id == R.id.nav_share) {
//opcion para compartir los resultados por redes sociales

        } else if (id == R.id.nav_send) {
//opcion para recibir los datos almacenados al server
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
        }
}


