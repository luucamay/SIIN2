package com.example.lupe.siin;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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


/**
 * A simple {@link Fragment} subclass.
 */
public class MapGeneralFragment extends Fragment implements
        InfoWindowManager.WindowShowListener,
        GoogleMap.OnMarkerClickListener,
        LoaderManager.LoaderCallbacks<List<Tramo>> {

    public static final String LOG_TAG = MapGeneralFragment.class.getSimpleName();
    private static final String TRAMO_REQUEST_URL =
            "http://192.168.4.231/geoserver/wfs?srsName=EPSG%3A4326&typename=geonode%3Atja01&outputFormat=json&version=1.0.0&service=WFS&request=GetFeature";
    private static final int TRAMO_LOADER_ID = 1;
    private GoogleMap mMap;
    private InfoWindow formWindow;
    private InfoWindowManager infoWindowManager;
    private Polyline mPolyline;


    public MapGeneralFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_map_general, container, false);

        //asegurate que obtengas al child fragment manager
        final MapInfoWindowFragment mapInfoWindowFragment =
                (MapInfoWindowFragment) getChildFragmentManager().findFragmentById(R.id.infoWindowMap);

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

        android.support.v4.app.LoaderManager loaderManager = getLoaderManager();
        loaderManager.initLoader(TRAMO_LOADER_ID, null, this);
        return rootView;
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

    }

    @Override
    public void onWindowShown(@NonNull InfoWindow infoWindow) {

    }

    @Override
    public void onWindowHideStarted(@NonNull InfoWindow infoWindow) {

    }

    @Override
    public void onWindowHidden(@NonNull InfoWindow infoWindow) {

    }

    @Override
    public Loader<List<Tramo>> onCreateLoader(int i, Bundle bundle) {
        return new TramoLoader(getContext(), TRAMO_REQUEST_URL);

    }

    @Override
    public void onLoadFinished(Loader<List<Tramo>> loader, List<Tramo> tramos) {
        // If there is a valid list of {@link Tramo}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (tramos != null && !tramos.isEmpty()) {
            updateUi(tramos);
        } else return;
    }

    private void updateUi(List<Tramo> tramos) {

        try {
            //construir una linea
            for (int i = 0; i < tramos.size(); i++) {
                Tramo tramo = tramos.get(i);
                PolylineOptions polylineOptions = new PolylineOptions();
                polylineOptions.addAll(tramo.getCoordenadas())
                        .width(20)
                        .color(Color.parseColor(tramo.getColor()))
                        .clickable(true);
                //aca añade la polilinea al mapa
                mPolyline = mMap.addPolyline(polylineOptions);
                //aca enlaza el objeto tramo con la polilinea
                mPolyline.setTag(tramo);
                Log.d("polilinea asignada",tramo.convierteACadena());
            }
            // Add a listener for polyline clicks that changes the clicked polyline's color.
            mMap.setOnPolylineClickListener(new GoogleMap.OnPolylineClickListener() {
                @Override
                public void onPolylineClick(Polyline polyline) {
                    // Flip the values of the red, green and blue components of the polyline's color.
                    polyline.setColor(polyline.getColor() ^ 0x00ffffff);

                    //acá implementar el método para mostrar la vista personalizada

                    // obtener el objeto Tramo de esta polilinea
                    Tramo mTramo = (Tramo) polyline.getTag();
                    Log.d("Tramo detalles:", mTramo.convierteACadena());
                    //de la pollilinea obtener sus corrdenadas y poner el marcador en la primera coordenada
                    LatLng posicion = polyline.getPoints().get(0);
                    Marker markerDePolilinea = mMap.addMarker(new MarkerOptions().position(posicion).alpha(0));
                    //creando el infowindow
                    //primero las especificaciones del marker
                    InfoWindow.MarkerSpecification markerSpec =
                            new InfoWindow.MarkerSpecification(1, 1);
                    //ahora el infowindow de tipo form fragment
                    FormFragment formFragment = FormFragment.newInstance(mTramo.getId(),
                            mTramo.getOBJECTID_1(),
                            mTramo.getOBJECTID(),
                            mTramo.getDistancia(),
                            mTramo.getOBJECTID_2(),
                            mTramo.getPoblacion(),
                            mTramo.getTramo(),
                            mTramo.getShape_Leng(),
                            mTramo.getProyId(),
                            mTramo.getIdSubproyecto()
                    );
                    formWindow = new InfoWindow(markerDePolilinea, markerSpec, formFragment);

                    infoWindowManager.toggle(formWindow, true);
                }
            });

        } catch (Exception e) {
            Log.d("updateUi", e.getMessage());
            e.printStackTrace();
        }


    }

    @Override
    public void onLoaderReset(Loader<List<Tramo>> loader) {

    }

}
