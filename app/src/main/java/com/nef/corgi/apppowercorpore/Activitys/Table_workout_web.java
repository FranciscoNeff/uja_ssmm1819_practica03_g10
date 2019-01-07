package com.nef.corgi.apppowercorpore.Activitys;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.nef.corgi.apppowercorpore.R;

public class Table_workout_web extends AppCompatActivity {
   /* String username;
    public Table_workout_web(String username){
        this.username=username;
    }*/
    private  WebView webView;
    @Override
    protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_workout_web);
        webView = (WebView) findViewById(R.id.webview_workout);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("http://192.168.0.12:8085/serverssmm/getrutina/user1");
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
