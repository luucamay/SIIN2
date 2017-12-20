package com.example.lupe.siin;

import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
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
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * Created by lupe on 11/12/17.
 */

public final class QueryUtils {

    /**
     * Tag for the log messages
     */
    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

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

    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }


    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();

            //Acá empezamos a verificar si la conexion es HTTP o HTTPS
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

            //Desde acá es normal la petición
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
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
            Log.e(LOG_TAG, "Problem retrieving the tramo JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown.
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
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
     * Query the USGS dataset and return a list of {@link Tramo} objects.
     */
    public static List<Tramo> fetchTramoData(String requestUrl) {
        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        // Extract relevant fields from the JSON response and create a list of {@link Tramo}s
        List<Tramo> tramos = extractFeatureFromJson(jsonResponse);

        // Return the list of {@link Tramo}s
        return tramos;
    }

    /**
     * Return a list of {@link Tramo} objects that has been built up from
     * parsing the given JSON response.
     */
    private static List<Tramo> extractFeatureFromJson(String tramoJSON) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(tramoJSON)) {
            return null;
        }

        List<Tramo> tramos = new ArrayList<>();
        // Try to parse the JSON response string. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.

        try {
            // Extract the JSONArray associated with the key called "features",
            // which represents a list of features (or tramos).
            JSONObject baseJsonResponse = new JSONObject(tramoJSON);

            // Extract the JSONArray associated with the key called "features",
            // which represents a list of features (or tramos).
            JSONArray tramoArray = baseJsonResponse.getJSONArray("features");

            // For each tramo in the tramoArray, create an {@link Tramo} object
            for (int i = 0; i < tramoArray.length(); i++) {
                // Get a single tramo at position i within the list of tramos
                JSONObject currentTramo = tramoArray.getJSONObject(i);

                String id = currentTramo.getString("id");
                JSONObject geometry = currentTramo.getJSONObject("geometry");
                JSONObject propiedades = currentTramo.getJSONObject("properties");
                JSONArray coordenadasjson = geometry.getJSONArray("coordinates");
                JSONArray coordenadascero = coordenadasjson.getJSONArray(0);
                ArrayList<LatLng> latLngArrayList = new ArrayList<>();

                for (int j = 0; j < coordenadascero.length(); j++) {
                    JSONArray latitudlongitud = coordenadascero.getJSONArray(j);
                    //objeto de latitud y longitud
                    LatLng latLng = new LatLng(latitudlongitud.getDouble(1), latitudlongitud.getDouble(0));
                    //ponerlo en un array de latlng
                    latLngArrayList.add(latLng);
                }

                int objectid_1 = propiedades.getInt("OBJECTID_1");
                int objectid = propiedades.getInt("OBJECTID");
                float distancia = (float) propiedades.getDouble("Distancia");
                int objectid_2 = propiedades.getInt("OBJECTID_2");
                String poblacion = propiedades.getString("Pob");
                String tramoPropiedad = propiedades.getString("Tramo");
                float shape_leng = (float) propiedades.getDouble("Shape_Leng");
                String color = propiedades.getString("color");
                int proyId = propiedades.getInt("proyId");
                int idSubproyecto = propiedades.getInt("idSubproyecto");
                // Create a new {@link Tramo} object with the magnitude, location, time,
                // and url from the JSON response.
                Tramo tramoObjeto = new Tramo(id, latLngArrayList, objectid_1, objectid, distancia, objectid_2, poblacion, tramoPropiedad, shape_leng,
                        color, proyId, idSubproyecto);

                // Add the new {@link Tramo} to the list of tramos.
                tramos.add(tramoObjeto);
            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e(LOG_TAG, "Problem parsing the tramo JSON results", e);
        }
        // Return the list of tramos
        return tramos;
    }

    /**
     * Query the USGS dataset and return a list of {@link Proyecto} objects.
     */
    public static List<Proyecto> fetchProyectoData(String requestUrl) {
        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        // Extract relevant fields from the JSON response and create a list of {@link Tramo}s
        List<Proyecto> proyectos = extractProyectoFeatureFromJson(jsonResponse);

        // Return the list of {@link Proyecto}
        return proyectos;
    }

    /**
     * Return a list of {@link Tramo} objects that has been built up from
     * parsing the given JSON response.
     */
    private static List<Proyecto> extractProyectoFeatureFromJson(String proyectoJSON) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(proyectoJSON)) {
            return null;
        }

        List<Proyecto> proyectos = new ArrayList<>();
        // Try to parse the JSON response string. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.

        try {
            // Extract the JSONArray associated with the key called "features",
            // which represents a list of features (or tramos).
            JSONObject baseJsonResponse = new JSONObject(proyectoJSON);

            // Extract the JSONArray associated with the key called "features",
            // which represents a list of features (or tramos).
            JSONArray proyectoArray = baseJsonResponse.getJSONArray("data");

            // For each tramo in the tramoArray, create an {@link Tramo} object
            for (int i = 0; i < proyectoArray.length(); i++) {
                // Get a single tramo at position i within the list of tramos
                JSONObject currentProyecto = proyectoArray.getJSONObject(i);

                int id = currentProyecto.getInt("proyId");
                String sisin = currentProyecto.getString("proySISIN");
                String descrip = currentProyecto.getString("proyDescrip");
                /*float longitud = (float) currentProyecto.getDouble("proyLong");
                String estadoProyecto = currentProyecto.getString("estado_proyecto");
                String tipoProyecto = currentProyecto.getString("tipo_proyecto");*/
                float longitud = 12;
                String estadoProyecto = "en proceso";
                String tipoProyecto = "de tipo loco";
                // Create a new {@link Proyecto} object with the id, sisin, descrip,
                // and url from the JSON response.
                Proyecto proyectoObjeto = new Proyecto(id, sisin, descrip, longitud, estadoProyecto, tipoProyecto);

                // Add the new {@link Tramo} to the list of tramos.
                proyectos.add(proyectoObjeto);
            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e(LOG_TAG, "Problem parsing the tramo JSON results", e);
        }
        // Return the list of tramos
        return proyectos;
    }

}