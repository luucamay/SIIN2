package com.example.lupe.siin;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProyectoFragment extends Fragment {

    private static final String LOG_TAG = ProyectosActivity.class.getName();

    private String request_url =
            "http://marte.abc.gob.bo/webService/proyectos.php/lista_proyectos";

    private static final String PROYECTO_REQUEST_URL =
            "http://marte.abc.gob.bo/webService/proyectos.php/lista_proyectos";

    private static final String BUSCAR_PROYECTO_REQUEST_URL =
            "http://marte.abc.gob.bo/webService/proyectos.php/search_proyecto/";

    private int PROYECTO_LOADER_ID = 1;

    private ProyectoAdapter mAdapter;

    private TextView mEmptyStateTextView;

    public ProyectoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_proyecto, container, false);
    }

    public void buscarProyecto(View view) {
    }
}
