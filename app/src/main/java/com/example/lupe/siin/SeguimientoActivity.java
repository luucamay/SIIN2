package com.example.lupe.siin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

public class SeguimientoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seguimiento);
        WebView myWebView = (WebView) findViewById(R.id.webview);
        myWebView.loadUrl("http://siin.abc.gob.bo/rest_ejecucion/API/resumen/seguimiento.php");
    }
}
