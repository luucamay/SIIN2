package com.example.lupe.siin;

import android.app.LoaderManager;
import android.content.Loader;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
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

import java.util.List;

public class MapFragmentActivity
        extends FragmentActivity
        implements LoaderManager.LoaderCallbacks<List<Tramo>>,
        InfoWindowManager.WindowShowListener,
        GoogleMap.OnMarkerClickListener {

    public static final String LOG_TAG = MapFragmentActivity.class.getSimpleName();
    private static final String TRAMO_REQUEST_URL =
            "http://abc.phuyu.me/geoserver/wfs?srsName=EPSG%3A4326&typename=geonode%3Atja01&outputFormat=json&version=1.0.0&service=WFS&request=GetFeature";
    private static final int TRAMO_LOADER_ID = 1;

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

        LoaderManager loaderManager = getLoaderManager();

        // Initialize the loader. Pass in the int ID constant defined above and pass in null for
        // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
        // because this activity implements the LoaderCallbacks interface).
        loaderManager.initLoader(TRAMO_LOADER_ID, null, this);

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
    //    Log.d("debug", "onWindowShowStarted: " + infoWindow);
    }

    @Override
    public void onWindowShown(@NonNull InfoWindow infoWindow) {
    //    Log.d("debug", "onWindowShown: " + infoWindow);
    }

    @Override
    public void onWindowHideStarted(@NonNull InfoWindow infoWindow) {
    //    Log.d("debug", "onWindowHideStarted: " + infoWindow);
    }

    @Override
    public void onWindowHidden(@NonNull InfoWindow infoWindow) {
    //    Log.d("debug", "onWindowHidden: " + infoWindow);
    }
    private void updateUi(List<Tramo> tramos) {

        try {
            //construir una linea
            for (int i=0; i< tramos.size();i++){
            Tramo tramo = tramos.get(i);
            PolylineOptions polylineOptions = new PolylineOptions();
            polylineOptions.addAll(tramo.coordenadas)
                    .width(25)
                    .color(Color.parseColor("#"+tramo.color))
                    .clickable(true);
            mMap.addPolyline(polylineOptions);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public Loader<List<Tramo>> onCreateLoader(int i, Bundle bundle) {
        return new TramoLoader(this, TRAMO_REQUEST_URL);

    }

    @Override
    public void onLoadFinished(Loader<List<Tramo>> loader, List<Tramo> tramos) {

        // If there is a valid list of {@link Tramo}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (tramos != null && !tramos.isEmpty()) {
            for (Tramo t : tramos){
                Log.d("My array list content: ", t.getId());
            }
            updateUi(tramos);
        } else return;
    }

    @Override
    public void onLoaderReset(Loader<List<Tramo>> loader) {

    }

}
