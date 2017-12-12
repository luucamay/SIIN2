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
import com.google.android.gms.maps.model.Polyline;
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
                mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);

                LatLng centroBolivia = new LatLng(-17.141273, -64.397211);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(centroBolivia, 5));
            }

        });
        LoaderManager loaderManager = getLoaderManager();
        loaderManager.initLoader(TRAMO_LOADER_ID, null, this);

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
            for (int i = 0; i < tramos.size(); i++) {
                Tramo tramo = tramos.get(i);
                PolylineOptions polylineOptions = new PolylineOptions();
                polylineOptions.addAll(tramo.coordenadas)
                        .width(20)
                        .color(Color.parseColor("#" + tramo.color))
                        .clickable(true);
                mMap.addPolyline(polylineOptions);
            }
            // Add a listener for polyline clicks that changes the clicked polyline's color.
            mMap.setOnPolylineClickListener(new GoogleMap.OnPolylineClickListener() {
                @Override
                public void onPolylineClick(Polyline polyline) {
                    // Flip the values of the red, green and blue components of the polyline's color.
                    polyline.setColor(polyline.getColor() ^ 0x00ffffff);
                    //acá implementar el método para mostrar la vista personalizada
                    //de la pollilinea obtener sus corrdenadas y poner el marcador en la primera coordenada
                    LatLng posicion = polyline.getPoints().get(0);
                    Marker markerDePolilinea = mMap.addMarker(new MarkerOptions().position(posicion).alpha(0));

                    //creando el infowindow
                    //primero las especificaciones del marker
                    InfoWindow.MarkerSpecification markerSpec =
                            new InfoWindow.MarkerSpecification(1,1);
                    //ahora el infowindow de tipo form fragment
                    formWindow = new InfoWindow(markerDePolilinea, markerSpec, new FormFragment());
                    infoWindowManager.toggle(formWindow, true);
                }
            });

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
            for (Tramo t : tramos) {
                Log.d("My array list content: ", t.getId());
            }
            updateUi(tramos);
        } else return;
    }

    @Override
    public void onLoaderReset(Loader<List<Tramo>> loader) {
    }


}
