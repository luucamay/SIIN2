package com.example.lupe.siin;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

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

        LatLng cochabamba = new LatLng(-17.396013, -66.163534);
        LatLng uno = new LatLng(-15.65, -69.12);
        LatLng dos = new LatLng(-16.58, -68.185);
        LatLng tres = new LatLng(-14.5614, -64.3853);
        LatLng cuatro = new LatLng(-17.11, -67.68);
        LatLng cinco = new LatLng(-17.54, -67.39);
        LatLng seis = new LatLng(-17.54, -67.39);
        LatLng siete = new LatLng(-14.09296, -64.3234);
        LatLng ocho = new LatLng(-15.09296, -67.01561);
        LatLng nueve = new LatLng(-14.35294, -64.52453);
        LatLng diez = new LatLng(-15.5308, -63.1123);
        LatLng once = new LatLng(-17, -63.1123);
        LatLng doce = new LatLng(-17, -63);

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(cochabamba,5));

        mMap.addMarker(new MarkerOptions().position(uno)
                .title("Proyecto:"));
        mMap.addMarker(new MarkerOptions().position(dos)
                .title("Proyecto:"));
        mMap.addMarker(new MarkerOptions().position(tres)
                .title("Proyecto:"));
        mMap.addMarker(new MarkerOptions().position(cuatro)
                .title("Proyecto:"));
        mMap.addMarker(new MarkerOptions().position(cinco)
                .title("Proyecto:"));
        mMap.addMarker(new MarkerOptions().position(seis)
                .title("Proyecto:"));
        mMap.addMarker(new MarkerOptions().position(siete)
                .title("Proyecto:"));
        mMap.addMarker(new MarkerOptions().position(ocho)
                .title("Proyecto:"));
        mMap.addMarker(new MarkerOptions().position(nueve)
                .title("Proyecto:"));
        mMap.addMarker(new MarkerOptions().position(diez)
                .title("Proyecto:"));
        mMap.addMarker(new MarkerOptions().position(once)
                .title("Proyecto:"));
        mMap.addMarker(new MarkerOptions().position(doce)
                .title("Proyecto:"));





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
    }

}
