package com.example.lupe.siin;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;

import com.appolica.interactiveinfowindow.InfoWindow;
import com.appolica.interactiveinfowindow.InfoWindowManager;
import com.appolica.interactiveinfowindow.fragment.MapInfoWindowFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
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

public class MapFragmentActivity
        extends FragmentActivity
        implements InfoWindowManager.WindowShowListener,
        GoogleMap.OnMarkerClickListener {

    public static final String LOG_TAG = MapFragmentActivity.class.getSimpleName();
    private static final String TRAMO_REQUEST_URL =
            "http://abc.phuyu.me/geoserver/wfs?srsName=EPSG%3A4326&typename=geonode%3Atja01&outputFormat=json&version=1.0.0&service=WFS&request=GetFeature";

    private GoogleMap mMap;
    private InfoWindow formWindow;
    private InfoWindowManager infoWindowManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_fragment);

        final MapInfoWindowFragment mapInfoWindowFragment =
                (MapInfoWindowFragment) getSupportFragmentManager().findFragmentById(R.id.infoWindowMap);

        infoWindowManager = mapInfoWindowFragment.infoWindowManager();
        infoWindowManager.setHideOnFling(true);

        mapInfoWindowFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;
                mMap.getUiSettings().setZoomGesturesEnabled(true);

                LatLng centroBolivia = new LatLng(-17.141273, -64.397211);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(centroBolivia,5));
                // Kick off an {@link AsyncTask} to perform the network request
                MapFragmentActivity.TramosAsyncTask task = new MapFragmentActivity.TramosAsyncTask();
                task.execute();

                final Marker marker2 = mMap.addMarker(new MarkerOptions().position(new LatLng(-20.89275048,-63.37744245)));

                final int offsetX = (int) getResources().getDimension(R.dimen.marker_offset_x);
                final int offsetY = (int) getResources().getDimension(R.dimen.marker_offset_y);

                final InfoWindow.MarkerSpecification markerSpec =
                        new InfoWindow.MarkerSpecification(offsetX, offsetY);

                formWindow = new InfoWindow(marker2, markerSpec, new FormFragment());

                mMap.setOnMarkerClickListener(MapFragmentActivity.this);

                //añadiendo las rutas

            }

            //aca creando el TramosAsyncTask

        });

        infoWindowManager.setWindowShowListener(MapFragmentActivity.this);

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        InfoWindow infoWindow = formWindow;

        if (infoWindow != null) {
            infoWindowManager.toggle(infoWindow, true);
        }

        return true;
    }

    @Override
    public void onWindowShowStarted(@NonNull InfoWindow infoWindow) {
        Log.d("debug", "onWindowShowStarted: " + infoWindow);
    }

    @Override
    public void onWindowShown(@NonNull InfoWindow infoWindow) {
        Log.d("debug", "onWindowShown: " + infoWindow);
    }

    @Override
    public void onWindowHideStarted(@NonNull InfoWindow infoWindow) {
        Log.d("debug", "onWindowHideStarted: " + infoWindow);
    }

    @Override
    public void onWindowHidden(@NonNull InfoWindow infoWindow) {
        Log.d("debug", "onWindowHidden: " + infoWindow);
    }

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
                    LatLng latLng = new LatLng(latitudlongitud.getDouble(1), latitudlongitud.getDouble(0));
                    //ponerlo en un array de latlng
                    latLngArrayList.add(latLng);
                }

                int objectid_1 = propiedades.getInt("OBJECTID_1");
                int objectid = propiedades.getInt("OBJECTID");
                float distancia = (float) propiedades.getDouble("Distancia");
                ;
                int objectid_2 = propiedades.getInt("OBJECTID_2");
                String poblacion = propiedades.getString("Pob");
                String tramo = propiedades.getString("Tramo");
                float shape_leng = (float) propiedades.getDouble("Shape_Leng");
                String color = propiedades.getString("color");
                int proyId = propiedades.getInt("proyId");
                int idSubproyecto = propiedades.getInt("idSubproyecto");

                // Create a new {@link Event} object
                return new Tramo(id, latLngArrayList, objectid_1, objectid, distancia, objectid_2, poblacion, tramo, shape_leng,
                        color, proyId, idSubproyecto);

            } catch (JSONException e) {
                Log.e(LOG_TAG, "Problema parseando los resultados JSON", e);
            }
            return null;
        }
    }
}
