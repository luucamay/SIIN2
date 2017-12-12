package com.example.lupe.siin;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.OnMapReadyCallback;


/**
 * A simple {@link Fragment} subclass.
 */
public class MapaGeneralFragment
        extends Fragment
        implements OnMapReadyCallback {

    public static final String LOG_TAG = MapaGeneralFragment.class.getSimpleName();
    private static final String TRAMO_REQUEST_URL =
            "http://abc.phuyu.me/geoserver/wfs?srsName=EPSG%3A4326&typename=geonode%3Atja01&outputFormat=json&version=1.0.0&service=WFS&request=GetFeature";
    private static final int TRAMO_LOADER_ID = 1;
    private GoogleMap mMap;
    public MapaGeneralFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        GoogleMapOptions options = new GoogleMapOptions();
        options.mapType(GoogleMap.MAP_TYPE_TERRAIN);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mapa_general, container, false);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {


        ///     LoaderManager loaderManager = getLoaderManager();
        //  loaderManager.initLoader(TRAMO_LOADER_ID, null, this);
    }


}
