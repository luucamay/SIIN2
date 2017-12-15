package com.example.lupe.siin;

import android.app.Activity;
import android.content.Intent;
import android.net.http.SslError;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class SeguimientoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seguimiento);
        WebView myWebView = (WebView) findViewById(R.id.webviewSeguimiento);
        final Activity activity = this;

        Intent myIntent = getIntent(); // gets the previously created intent
        String mProyId = "";
        mProyId += myIntent.getIntExtra("tProyId",0);
        String mIdSubproyecto = "";
        mIdSubproyecto += myIntent.getIntExtra("tIdSubproyecto",0);

        myWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed(); // Ignore SSL certificate errors
            }
        });

        myWebView.loadUrl("http://siin.abc.gob.bo/rest_ejecucion/API/resumen/seguimiento.php?proyId="+mProyId
                +"&id_subproyecto="+mIdSubproyecto);
    }
}
