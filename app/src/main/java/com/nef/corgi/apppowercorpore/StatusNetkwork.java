package com.nef.corgi.apppowercorpore;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import es.dmoral.toasty.Toasty;

public class StatusNetkwork extends BroadcastReceiver {
    public Context cont;
public StatusNetkwork(Context c){
    cont=c;
}
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = manager.getActiveNetworkInfo();
        onNetworkChange(ni);
        cont=context;
    }
    private void onNetworkChange(NetworkInfo networkInfo) {
        if (networkInfo != null) {
            if (networkInfo.getState() == NetworkInfo.State.CONNECTED) {
                Log.d("MenuActivity", "CONNECTED");
                Toast.makeText(cont.getApplicationContext(),R.string.Connect ,Toast.LENGTH_SHORT).show();
            } else {
                Log.d("MenuActivity", "DISCONNECTED");
                Toast.makeText(cont.getApplicationContext(), R.string.Disconnected, Toast.LENGTH_SHORT).show();//no reconoce el getString en esta clase
            }
        }
    }

}
//Ayuda de //https://programacionymas.com/blog/detectar-cuando-app-se-conecta-o-desconecta-internet
