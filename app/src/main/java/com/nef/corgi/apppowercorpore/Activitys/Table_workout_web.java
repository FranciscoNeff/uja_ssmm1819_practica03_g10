package com.nef.corgi.apppowercorpore.Activitys;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.nef.corgi.apppowercorpore.R;

public class Table_workout_web extends AppCompatActivity {
/*
 * SOlo estan disponibles los usuarios user1 y user2
 * se podrian recoger los datos modificados de la tabla y guardarlos de nuevo en otra tabla de nuesta BBDD
 * pero esto se realizaria a traves del servidor, ya que utilzamos un webview y por lo tanto esta parte no se
 * ha realizado, ya que esto seria mas de otro tipo de asignatura, como Ingieneria de Servicios
 *
 */
    private  WebView webView;
    private android.support.v7.widget.Toolbar toolbar;
    @Override
    protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar_workout);
        setSupportActionBar(toolbar);
        String username=getIntent().getStringExtra("username");
        setContentView(R.layout.show_workout_web);
        webView=(WebView) findViewById(R.id.webview_workout);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("http://192.168.0.12:8085/serverssmm/getrutina/"+username);
        //webView.loadUrl("http://google.com");
        WebSettings webSettings=webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
    }
@Override
    public void onBackPressed(){
        if (webView.canGoBack()){
            webView.goBack();
        }else{
            super.onBackPressed();
        }
}
    }
