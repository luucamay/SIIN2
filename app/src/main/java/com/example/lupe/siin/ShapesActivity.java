package com.example.lupe.siin;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;

public class ShapesActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shapes);
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
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(
                -63.63408837,
                -21.90347497
        ),5));

        // Instantiates a new Polygon object and adds points to define a rectangle
        PolygonOptions rectOptions = new PolygonOptions()
                .add(
                        new LatLng(
                                -63.63408837,
                                -21.90347497
                        ), new LatLng(
                                -63.63478683,
                                -21.90429754
                        ), new LatLng(
                                -63.63619636,
                                -21.90593581
                        ), new LatLng(
                                -63.63636364,
                                -21.90613435
                        ), new LatLng(
                                -63.6365256,
                                -21.90633726
                        ), new LatLng(
                                -63.63668211,
                                -21.90654439
                        ), new LatLng(
                                -63.63683306,
                                -21.90675561
                        ), new LatLng(
                                -63.63697836,
                                -21.90697077
                        ), new LatLng(
                                -63.63711789,
                                -21.9071897
                        ), new LatLng(
                                -63.63725155,
                                -21.90741227
                        ), new LatLng(
                                -63.63737927,
                                -21.9076383
                        ), new LatLng(
                                -63.63750093,
                                -21.90786764
                        ), new LatLng(
                                -63.63761646,
                                -21.90810014
                        ), new LatLng(
                                -63.63772578,
                                -21.90833562
                        ), new LatLng(
                                -63.63782881,
                                -21.90857392
                        ), new LatLng(
                                -63.63792548,
                                -21.90881487
                        ), new LatLng(
                                -63.63801571,
                                -21.9090583
                        ), new LatLng(
                                -63.63809945,
                                -21.90930404
                        ), new LatLng(
                                -63.64466263,
                                -21.92778696
                        ), new LatLng(
                                -63.64482396,
                                -21.92826167
                        ), new LatLng(
                                -63.64499136,
                                -21.92873428
                        ), new LatLng(
                                -63.64516478,
                                -21.92920471
                        ), new LatLng(
                                -63.64534421,
                                -21.92967288
                        ), new LatLng(
                                -63.64552962,
                                -21.93013871
                        ), new LatLng(
                                -63.64572097,
                                -21.93060214
                        ), new LatLng(
                                -63.64591823,
                                -21.93106307
                        ), new LatLng(
                                -63.64612137,
                                -21.93152145
                        ), new LatLng(
                                -63.64633037,
                                -21.93197719
                        ), new LatLng(
                                -63.64654517,
                                -21.93243022
                        ), new LatLng(
                                -63.65168354,
                                -21.94302644
                        ), new LatLng(
                                -63.65391692,
                                -21.94762114
                        ), new LatLng(
                                -63.65412735,
                                -21.94802664
                        ), new LatLng(
                                -63.65433252,
                                -21.94843482
                        ), new LatLng(
                                -63.65453239,
                                -21.94884562
                        ), new LatLng(
                                -63.65472693,
                                -21.94925897
                        ), new LatLng(
                                -63.6549161,
                                -21.94967481
                        ), new LatLng(
                                -63.65859853,
                                -21.95726555
                        ), new LatLng(
                                -63.65888031,
                                -21.95785848
                        ), new LatLng(
                                -63.65915718,
                                -21.95845372
                        ), new LatLng(
                                -63.65942913,
                                -21.95905122
                        ), new LatLng(
                                -63.65969615,
                                -21.95965094
                        ), new LatLng(
                                -63.65995821,
                                -21.96025285
                        ), new LatLng(
                                -63.66021529,
                                -21.9608569
                        ), new LatLng(
                                -63.67223136,
                                -21.98899483
                        ), new LatLng(
                                -63.67268712,
                                -21.99001439
                        ), new LatLng(
                                -63.67276605,
                                -21.99021789
                        ), new LatLng(
                                -63.67283886,
                                -21.99042367
                        ), new LatLng(
                                -63.67290549,
                                -21.99063152
                        ), new LatLng(
                                -63.67296587,
                                -21.99084128
                        ), new LatLng(
                                -63.67301994,
                                -21.99105275
                        ), new LatLng(
                                -63.67306767,
                                -21.99126575
                        ), new LatLng(
                                -63.673109,
                                -21.99148007
                        ), new LatLng(
                                -63.6731439,
                                -21.99169554
                        ), new LatLng(
                                -63.67317234,
                                -21.99191195
                        ), new LatLng(
                                -63.6731943,
                                -21.99212912
                        ), new LatLng(
                                -63.67331837,
                                -21.99398229
                        ), new LatLng(
                                -63.67350313,
                                -21.99484139
                        ), new LatLng(
                                -63.66912361,
                                -21.99541965
                        ), new LatLng(
                                -63.66686441,
                                -21.99638696
                        ), new LatLng(
                                -63.66683226,
                                -21.99641124
                        ), new LatLng(
                                -63.66680435,
                                -21.99644028
                        ), new LatLng(
                                -63.66678136,
                                -21.99647336
                        ), new LatLng(
                                -63.66676386,
                                -21.99650964
                        ), new LatLng(
                                -63.6667523,
                                -21.99654823
                        ), new LatLng(
                                -63.66674696,
                                -21.99658816
                        ), new LatLng(
                                -63.66674798,
                                -21.99662843
                        ), new LatLng(
                                -63.66675533,
                                -21.99666803
                        ), new LatLng(
                                -63.67182635,
                                -22.00889075
                        ), new LatLng(
                                -63.67390724,
                                -22.01391304
                        ), new LatLng(
                                -63.67413621,
                                -22.01446934
                        ), new LatLng(
                                -63.67694568,
                                -22.02132905
                        ), new LatLng(
                                -63.67744657,
                                -22.02266969
                        ), new LatLng(
                                -63.677451,
                                -22.02270052
                        ), new LatLng(
                                -63.6774506,
                                -22.02273167
                        ), new LatLng(
                                -63.67744538,
                                -22.02276238
                        ), new LatLng(
                                -63.67743547,
                                -22.02279191
                        ), new LatLng(
                                -63.6774211,
                                -22.02281955
                        ), new LatLng(
                                -63.67740263,
                                -22.02284463
                        ), new LatLng(
                                -63.67738049,
                                -22.02286655
                        ), new LatLng(
                                -63.67735523,
                                -22.02288478
                        ), new LatLng(
                                -63.676928,
                                -22.02308808
                        ), new LatLng(
                                -63.67690482,
                                -22.02310618
                        ), new LatLng(
                                -63.67688466,
                                -22.02312758
                        ), new LatLng(
                                -63.676868,
                                -22.0231518
                        ), new LatLng(
                                -63.6768552,
                                -22.02317827
                        ), new LatLng(
                                -63.67684658,
                                -22.02320638
                        ), new LatLng(
                                -63.67684233,
                                -22.02323547
                        ), new LatLng(
                                -63.67684255,
                                -22.02326487
                        ), new LatLng(
                                -63.67757357,
                                -22.02848099
                        ), new LatLng(
                                -63.67954342,
                                -22.04348021
                        ), new LatLng(
                                -63.67955095,
                                -22.04353666
                        ), new LatLng(
                                -63.67956435,
                                -22.04359201
                        ), new LatLng(
                                -63.67958348,
                                -22.04364565
                        ), new LatLng(
                                -63.67960812,
                                -22.043697
                        ), new LatLng(
                                -63.67963802,
                                -22.04374547
                        ), new LatLng(
                                -63.67967283,
                                -22.04379054
                        ), new LatLng(
                                -63.68023725,
                                -22.04454449
                        ), new LatLng(
                                -63.68163474,
                                -22.04630887
                        ), new LatLng(
                                -63.68167026,
                                -22.04635066
                        ), new LatLng(
                                -63.68170113,
                                -22.046396
                        ), new LatLng(
                                -63.68172698,
                                -22.04644437
                        ), new LatLng(
                                -63.68174753,
                                -22.04649522
                        ), new LatLng(
                                -63.68176254,
                                -22.04654797
                        ), new LatLng(
                                -63.68225766,
                                -22.04921868
                        ), new LatLng(
                                -63.68263287,
                                -22.05148735
                        ), new LatLng(
                                -63.68264741,
                                -22.05157843
                        ), new LatLng(
                                -63.6826684,
                                -22.05166825
                        ), new LatLng(
                                -63.68269573,
                                -22.05175634
                        ), new LatLng(
                                -63.68272925,
                                -22.05184227
                        ), new LatLng(
                                -63.68276881,
                                -22.05192559
                        ), new LatLng(
                                -63.68281419,
                                -22.05200588
                        ), new LatLng(
                                -63.68286518,
                                -22.05208275
                        ), new LatLng(
                                -63.6829215,
                                -22.05215579
                        ), new LatLng(
                                -63.68298288,
                                -22.05222463
                        ), new LatLng(
                                -63.683049,
                                -22.05228894
                        ), new LatLng(
                                -63.68311953,
                                -22.05234837
                        ), new LatLng(
                                -63.68344576,
                                -22.05264519
                        ), new LatLng(
                                -63.68406612,
                                -22.05319068
                        ));

// Get back the mutable Polygon
        Polygon polygon = mMap.addPolygon(rectOptions);
    }
}
