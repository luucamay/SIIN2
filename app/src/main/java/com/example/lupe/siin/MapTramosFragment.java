package com.example.lupe.siin;

import android.app.LoaderManager;
import android.content.Loader;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.appolica.interactiveinfowindow.InfoWindow;
import com.appolica.interactiveinfowindow.InfoWindowManager;
import com.appolica.interactiveinfowindow.fragment.MapInfoWindowFragment;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class MapTramosFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<List<Tramo>>,
        InfoWindowManager.WindowShowListener,
        GoogleMap.OnMarkerClickListener {

    private final String LOG_TAG = MapTramosFragment.class.getName();
    private static final String USGS_REQUEST_URL =
            "http://abc.phuyu.me/geoserver/wfs?srsName=EPSG%3A4326&typename=geonode%3Atja01&outputFormat=json&version=1.0.0&service=WFS&request=GetFeature";
    private static final int TRAMO_LOADER_ID = 1;
    private TextView mEmptyStateTextView;
    private GoogleMap mMap;
    private InfoWindow formWindow;
    private InfoWindowManager infoWindowManager;

    public MapTramosFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final MapInfoWindowFragment mapInfoWindowFragment =
                (MapInfoWindowFragment) getFragmentManager().findFragmentById(R.id.infoWindowMap);

        //infoWindowManager.setWindowShowListener(MapFragmentActivity.this);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_map_tramos, container, false);
    }

    @Override
    public Loader<List<Tramo>> onCreateLoader(int i, Bundle bundle) {
        return new TramoLoader(getActivity(), USGS_REQUEST_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<Tramo>> loader, List<Tramo> tramos) {
        // Hide loading indicator because the data has been loaded
        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);

        // Set empty state text to display "No tramos found."
        mEmptyStateTextView.setText(R.string.no_tramos);


        // If there is a valid list of {@link Tramo}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (tramos != null && !tramos.isEmpty()) {
            for (Tramo t : tramos){
                //aqui mapear cada tramoa
            }

        }
    }

    private View findViewById(int loading_indicator) {
        return getView();
    }

    @Override
    public void onLoaderReset(Loader<List<Tramo>> loader) {

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
    public boolean onMarkerClick(Marker marker) {
        return false;
    }
}
