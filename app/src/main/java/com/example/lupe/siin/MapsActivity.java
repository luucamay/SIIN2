package com.example.lupe.siin;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

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
import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    /** Tag for the log messages */
    public static final String LOG_TAG = MapsActivity.class.getSimpleName();
    /** URL to query the dataset for information */
    private static final String TRAMO_REQUEST_URL =
            "http://abc.phuyu.me/geoserver/wfs?srsName=EPSG%3A4326&typename=geonode%3Atja01&outputFormat=json&version=1.0.0&service=WFS&request=GetFeature";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.getUiSettings().setZoomGesturesEnabled(true);

        LatLng centroBolivia = new LatLng(-17.141273, -64.397211);

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(centroBolivia,5));

        // Setting a custom info window adapter for the google map
        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            // Use default InfoWindow frame
            @Override
            public View getInfoWindow(Marker arg0) {
                return null;
            }

            // Defines the contents of the InfoWindow
            @Override
            public View getInfoContents(Marker arg0) {
                View v = null;
                try {

                    // Getting view from the layout file info_window_layout
                    v = getLayoutInflater().inflate(R.layout.custom_info_contents, null);

                    // Getting reference to the TextView to set latitude
                    TextView addressTxt = (TextView) v.findViewById(R.id.textTitle);
                    addressTxt.setText(arg0.getTitle());

                } catch (Exception ev) {
                    System.out.print(ev.getMessage());
                }

                return v;
            }
        });

        // Kick off an {@link AsyncTask} to perform the network request
        TramosAsyncTask task = new TramosAsyncTask();
        task.execute();

    }

    /**
     * Update the screen to display information from the given {@link EjecucionPresupuesto}.
     */
    private void updateUi(Tramo tramo) {
        LatLng cochabamba = new LatLng(-17.396013, -66.163534);

        mMap.addMarker(new MarkerOptions().position(cochabamba)
                .title("Proyecto: CONSERVACION VIAL TRAMO TJ01: Boyuibe - Villa Montes - Yacuiba - Pocitos; Villa Montes - Hito Br94 "));
    }

    /* Desde aqui estoy llamando el json data de un tramo y su shape*/
    /**
     * {@link AsyncTask} to perform the network request on a background thread, and then
     * update the UI with the first tramo in the response.
     */
    private class TramosAsyncTask extends AsyncTask<URL, Void, Tramo> {

        @Override
        protected Tramo doInBackground(URL... urls) {
            // Create URL object
            URL url = createUrl(TRAMO_REQUEST_URL);

            // Perform HTTP request to the URL and receive a JSON response back
            String jsonResponse = "";
            try {
                jsonResponse = makeHttpRequest(url);
            } catch (IOException e) {
                Log.e(LOG_TAG, "Problema al realizar la solicitud HTTP.", e);
            }

            // Extract relevant fields from the JSON response and create an {@link Event} object
            Tramo tramo = extractFeatureFromJson(jsonResponse);

            return tramo;
        }

        /**
         * Update the screen with the given tramo (which was the result of the
         * {@link MainActivity.SiinAsyncTask}).
         */
        @Override
        protected void onPostExecute(Tramo tramo) {
            if (tramo == null) {
                return;
            }

            try {
                if (mMap == null) {

                }
                //construir una linea
                PolylineOptions polylineOptions = new PolylineOptions();
                polylineOptions.addAll(tramo.coordenadas)
                        .width(25)
                        .color(Color.BLUE)
                        .clickable(true);
                mMap.addPolyline(polylineOptions);
                //for(LatLng latLng : result)
                  //  mMap.addMarker(new MarkerOptions().position(latLng).title("Revamp 15,click on the arrow below for directions"));

            } catch (Exception e) {
                e.printStackTrace();
            }
            //updateUi(tramo);

        }

        /**
         * Returns new URL object from the given string URL.
         */
        private URL createUrl(String stringUrl) {
            URL url = null;
            try {
                url = new URL(stringUrl);
            } catch (MalformedURLException exception) {
                Log.e(LOG_TAG, "Error al crear URL", exception);
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
         * Return an {@link Tramo} object by parsing out information
         * about the first tramo from the input tramoJSON string.
         */
        private Tramo extractFeatureFromJson(String tramoJSON) {
            // If the JSON string is empty or null, then return early.
            if (TextUtils.isEmpty(tramoJSON)) {
                return null;
            }

            try {
                JSONObject baseJsonResponse = new JSONObject(tramoJSON);
                JSONArray features = baseJsonResponse.getJSONArray("features");
                JSONObject shape = features.getJSONObject(1);
                String id = shape.getString("id");
                JSONObject geometry = shape.getJSONObject("geometry");
                JSONObject propiedades = shape.getJSONObject("properties");

                JSONArray coordenadasjson = geometry.getJSONArray("coordinates");
                JSONArray coordenadascero = coordenadasjson.getJSONArray(0);
                ArrayList<LatLng> latLngArrayList = new ArrayList<>();
                for (int i = 0; i < coordenadascero.length(); i++) {
                    JSONArray latitudlongitud = coordenadascero.getJSONArray(i);
                    //objeto de latitud y longitud
                    LatLng latLng = new LatLng(latitudlongitud.getDouble(1),latitudlongitud.getDouble(0));
                    //ponerlo en un array de latlng
                    latLngArrayList.add(latLng);
                }

                int objectid_1 = propiedades.getInt("OBJECTID_1");
                int objectid = propiedades.getInt("OBJECTID");
                float distancia =  (float) propiedades.getDouble("Distancia");;
                int objectid_2 = propiedades.getInt("OBJECTID_2");
                String poblacion = propiedades.getString("Pob");
                String tramo = propiedades.getString("Tramo");
                float shape_leng = (float) propiedades.getDouble("Shape_Leng");
                String color = propiedades.getString("color");
                int proyId = propiedades.getInt("proyId");
                int idSubproyecto = propiedades.getInt("idSubproyecto");

                // Create a new {@link Event} object
                return new Tramo(id,latLngArrayList,objectid_1,objectid,distancia,objectid_2,poblacion,tramo,shape_leng,
                        color,proyId,idSubproyecto);

            } catch (JSONException e) {
                Log.e(LOG_TAG, "Problema parseando los resultados JSON", e);
            }
            return null;
        }
    }
}
