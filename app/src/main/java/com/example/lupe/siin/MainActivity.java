package com.example.lupe.siin;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    /*
    CODIGO PARA PERMITIR QUE SE CONECTE A PESAR DE LOS CERTIFICADOS NO FIRMADOS
    */

    // always verify the host - dont check for certificate
    final static HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    };

    /**
     * Trust every server - dont check for any certificate
     */
    private static void trustAllHosts() {
        // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return new java.security.cert.X509Certificate[] {};
            }

            public void checkClientTrusted(X509Certificate[] chain,
                                           String authType) throws CertificateException {
            }

            public void checkServerTrusted(X509Certificate[] chain,
                                           String authType) throws CertificateException {
            }
        } };

        // Install the all-trusting trust manager
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection
                    .setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** Tag for the log messages */
    public static final String LOG_TAG = MainActivity.class.getSimpleName();
    /** URL to query the dataset for information */
    private static final String SIIN_REQUEST_URL =
            "http://siin.abc.gob.bo/rest_ejecucion/API/ejecucion/ejecucion.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //Este es el que controla el contenido de la pantalla
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Made by lupe

        // Kick off an {@link AsyncTask} to perform the network request
        SiinAsyncTask task = new SiinAsyncTask();
        task.execute();

        /* Este bloque de codigo hace que se llame al AsyncTask cada 10 minutos
        Timer timer = new Timer ();
        TimerTask hourlyTask = new TimerTask () {
            @Override
            public void run () {
                SiinAsyncTask task = new SiinAsyncTask();
                task.execute();
            }
        };
        timer.schedule (hourlyTask, 0l, 1000*60*60);
        */

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
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.resumen_general) {
            // Handle the camera action
        } else if (id == R.id.ejecucion_regional) {

        } else if (id == R.id.ejecucion_proyecto) {

        } else if (id == R.id.maps){
            Intent intent = new Intent(this, MapFragmentActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    // From here to ahead is lupe's code


    /**
     * Update the screen to display information from the given {@link EjecucionPresupuesto}.
     */
    private void updateUi(EjecucionPresupuesto ejecucionPresupuesto) {
        TextView mfecha = (TextView) findViewById(R.id.mfecha);
        TextView mprogramado_devengado = (TextView) findViewById(R.id.programado_devengado);
        TextView mreprogramado_devengado = (TextView) findViewById(R.id.reprogramado_devengado);
        TextView mpresupuesto_aprobado_devengado = (TextView) findViewById(R.id.presupuesto_aprobado_devengado);
        TextView mpresupuesto_vigente_devengado = (TextView) findViewById(R.id.presupuesto_vigente_devengado);
        TextView mprogramado_pagado = (TextView) findViewById(R.id.programado_pagado);
        TextView mreprogramado_pagado = (TextView) findViewById(R.id.reprogramado_pagado);
        TextView mpresupuesto_aprobado_pagado = (TextView) findViewById(R.id.presupuesto_aprobado_pagado);
        TextView mpresupuesto_vigente_pagado = (TextView) findViewById(R.id.presupuesto_vigente_pagado);

        mfecha.setText(ejecucionPresupuesto.fecha);
        mprogramado_devengado.setText(ejecucionPresupuesto.programado_devengado+" %");
        mreprogramado_devengado.setText(ejecucionPresupuesto.reprogramado_devengado+" %");
        mpresupuesto_aprobado_devengado.setText(ejecucionPresupuesto.presupuesto_aprobado_devengado+" %");
        mpresupuesto_vigente_devengado.setText(ejecucionPresupuesto.presupuesto_vigente_devengado+" %");
        mprogramado_pagado.setText(ejecucionPresupuesto.programado_pagado+" %");
        mreprogramado_pagado.setText(ejecucionPresupuesto.reprogramado_pagado+" %");
        mpresupuesto_aprobado_pagado.setText(ejecucionPresupuesto.presupuesto_aprobado_pagado+" %");
        mpresupuesto_vigente_pagado.setText(ejecucionPresupuesto.presupuesto_vigente_pagado+" %");

    }
    /**
     * {@link AsyncTask} to perform the network request on a background thread, and then
     * update the UI with the first earthquake in the response.
     */
    private class SiinAsyncTask extends AsyncTask<URL, Void, EjecucionPresupuesto> {

        @Override
        protected EjecucionPresupuesto doInBackground(URL... urls) {
            // Create URL object
            URL url = createUrl(SIIN_REQUEST_URL);

            // Perform HTTP request to the URL and receive a JSON response back
            String jsonResponse = "";
            try {
                jsonResponse = makeHttpRequest(url);
            } catch (IOException e) {
                Log.e(LOG_TAG, "Problem making the HTTP request.", e);
            }

            // Extract relevant fields from the JSON response and create an {@link Event} object
            EjecucionPresupuesto ejecucionPresupuesto = extractFeatureFromJson(jsonResponse);

            // Return the {@link Event} object as the result fo the {@link TsunamiAsyncTask}
            return ejecucionPresupuesto;
        }

        /**
         * Update the screen with the given earthquake (which was the result of the
         * {@link SiinAsyncTask}).
         */
        @Override
        protected void onPostExecute(EjecucionPresupuesto ejecucionPresupuesto) {
            if (ejecucionPresupuesto == null) {
                return;
            }

            updateUi(ejecucionPresupuesto);
        }

        /**
         * Returns new URL object from the given string URL.
         */
        private URL createUrl(String stringUrl) {
            URL url = null;
            try {
                url = new URL(stringUrl);
            } catch (MalformedURLException exception) {
                Log.e(LOG_TAG, "Error with creating URL", exception);
                return null;
            }
            return url;
        }

        /**
         * Make an HTTP request to the given URL and return a String as the response.
         */
        private String makeHttpRequest(URL url) throws IOException {
            String jsonResponse = "";

            // If the URL is null, then return early.
            if (url == null) {
                return jsonResponse;
            }

            HttpURLConnection urlConnection = null;
            InputStream inputStream = null;

            try {
                urlConnection = (HttpURLConnection) url.openConnection();

                boolean redirect = false;

                // normally, 3xx is redirect
                int status = urlConnection.getResponseCode();
                if (status != HttpURLConnection.HTTP_OK) {
                    if (status == HttpURLConnection.HTTP_MOVED_TEMP
                            || status == HttpURLConnection.HTTP_MOVED_PERM
                            || status == HttpURLConnection.HTTP_SEE_OTHER)
                        redirect = true;
                }

                if (redirect) {
                    // get redirect url from "location" header field
                    String newUrl = urlConnection.getHeaderField("Location");
                    // open the new connnection again
                    trustAllHosts();
                    HttpsURLConnection https = (HttpsURLConnection) new URL(newUrl).openConnection();
                    https.setHostnameVerifier(DO_NOT_VERIFY);
                    urlConnection = https;
                }
                urlConnection.setRequestMethod("GET");
                urlConnection.setReadTimeout(10000 /* milliseconds */);
                urlConnection.setConnectTimeout(15000 /* milliseconds */);
                urlConnection.connect();

                // If the request was successful (response code 200),
                // then read the input stream and parse the response.
                if (urlConnection.getResponseCode() == 200) {
                    inputStream = urlConnection.getInputStream();
                    jsonResponse = readFromStream(inputStream);
                } else {
                    Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
                }
            } catch (IOException e) {
                Log.e(LOG_TAG, "Problema al obtener los JSON de Ejecucion del presupuesto.", e);
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (inputStream != null) {
                    // function must handle java.io.IOException here
                    inputStream.close();
                }
            }
            return jsonResponse;
        }

        /**
         * Convert the {@link InputStream} into a String which contains the
         * whole JSON response from the server.
         */
        private String readFromStream(InputStream inputStream) throws IOException {
            StringBuilder output = new StringBuilder();
            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
                BufferedReader reader = new BufferedReader(inputStreamReader);
                String line = reader.readLine();
                while (line != null) {
                    output.append(line);
                    line = reader.readLine();
                }
            }
            return output.toString();
        }

        /**
         * Return an {@link EjecucionPresupuesto} object by parsing out information
         * about the first earthquake from the input earthquakeJSON string.
         */
        private EjecucionPresupuesto extractFeatureFromJson(String ejecucionPresupuestoJSON) {
            // If the JSON string is empty or null, then return early.
            if (TextUtils.isEmpty(ejecucionPresupuestoJSON)) {
                return null;
            }

            try {
                JSONObject baseJsonResponse = new JSONObject(ejecucionPresupuestoJSON);
                String fecha = baseJsonResponse.getString("fecha");
                String programado_devengado = baseJsonResponse.getString("programado_devengado");
                String reprogramado_devengado = baseJsonResponse.getString("reprogramado_devengado");
                String presupuesto_aprobado_devengado = baseJsonResponse.getString("presupuesto_aprobado_devengado");
                String presupuesto_vigente_devengado = baseJsonResponse.getString("presupuesto_vigente_devengado");
                String programado_pagado = baseJsonResponse.getString("programado_pagado");
                String reprogramado_pagado = baseJsonResponse.getString("reprogramado_pagado");
                String presupuesto_aprobado_pagado = baseJsonResponse.getString("presupuesto_aprobado_pagado");
                String presupuesto_vigente_pagado = baseJsonResponse.getString("presupuesto_vigente_pagado");

                // Create a new {@link Event} object
                return new EjecucionPresupuesto(fecha,
                        programado_devengado,
                        reprogramado_devengado,
                        presupuesto_aprobado_devengado,
                        presupuesto_vigente_devengado,
                        programado_pagado,
                        reprogramado_pagado,
                        presupuesto_aprobado_pagado,
                        presupuesto_vigente_pagado);

            } catch (JSONException e) {
                Log.e(LOG_TAG, "Problem parsing the earthquake JSON results", e);
            }
            return null;
        }
    }
}
