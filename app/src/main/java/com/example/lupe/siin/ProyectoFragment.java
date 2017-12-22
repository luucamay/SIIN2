package com.example.lupe.siin;


import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProyectoFragment
        extends Fragment implements LoaderManager.LoaderCallbacks<List<Proyecto>> {

    private static final String LOG_TAG = ProyectoFragment.class.getName();

    private String request_url =
            "http://marte.abc.gob.bo/webService/proyectos.php/lista_proyectos";

    private static final String PROYECTO_REQUEST_URL =
            "http://marte.abc.gob.bo/webService/proyectos.php/lista_proyectos";

    private static final String BUSCAR_PROYECTO_REQUEST_URL =
            "http://marte.abc.gob.bo/webService/proyectos.php/search_proyecto/";

    private int PROYECTO_LOADER_ID = 1;

    private ProyectoAdapter mAdapter;

    private TextView mEmptyStateTextView;

    private ImageButton mImageButton;

    public ProyectoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_proyecto, container, false);
        // Find a reference to the {@link ListView} in the layout
        ListView proyectoListView = (ListView) rootView.findViewById(R.id.list);

        mEmptyStateTextView = (TextView) rootView.findViewById(R.id.empty_view);
        proyectoListView.setEmptyView(mEmptyStateTextView);

        // Create a new adapter that takes an empty list of proyectos as input
        mAdapter = new ProyectoAdapter(getContext(), new ArrayList<Proyecto>());

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        proyectoListView.setAdapter(mAdapter);

        // Set an item click listener on the ListView, which sends an intent to a web browser
        // to open a website with more information about the selected proyecto.
        proyectoListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                /*
                // Find the current proyecto that was clicked on
                Proyecto currentProyecto = mAdapter.getItem(position);

                // Convert the String URL into a URI object (to pass into the Intent constructor)
                Uri proyectoUri = Uri.parse(currentProyecto.getUrl());

                // Create a new intent to view the proyecto URI
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, proyectoUri);

                // Send the intent to launch a new activity
                startActivity(websiteIntent);
                */
            }
        });

        mImageButton = (ImageButton) rootView.findViewById(R.id.imageButtonSearch);

        mImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editText = (EditText) getView().findViewById(R.id.textoBuscado);
                String textoBuscado = editText.getText().toString();
                LoaderManager loaderManager = getLoaderManager();
                request_url = BUSCAR_PROYECTO_REQUEST_URL + "" + textoBuscado;
                PROYECTO_LOADER_ID++;
                loaderManager.initLoader(PROYECTO_LOADER_ID, null, ProyectoFragment.this);
            }
        });

        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager)
                getContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {
            // Get a reference to the LoaderManager, in order to interact with loaders.
            android.support.v4.app.LoaderManager loaderManager = getLoaderManager();

            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            loaderManager.initLoader(PROYECTO_LOADER_ID, null, this);
        } else {
            // Otherwise, display error
            // First, hide loading indicator so error message will be visible
            View loadingIndicator = rootView.findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);

            // Update empty state with no connection error message
            mEmptyStateTextView.setText(R.string.no_internet_connection);
        }

        return rootView;
    }


    public void buscarProyecto(View view) {
        EditText editText = (EditText) getView().findViewById(R.id.textoBuscado);
        String textoBuscado = editText.getText().toString();
        LoaderManager loaderManager = getLoaderManager();
        request_url = BUSCAR_PROYECTO_REQUEST_URL + "" + textoBuscado;
        PROYECTO_LOADER_ID++;
        loaderManager.initLoader(PROYECTO_LOADER_ID, null, this);
    }

    @Override
    public Loader<List<Proyecto>> onCreateLoader(int id, Bundle args) {
        return new ProyectoLoader(getContext(), request_url);
    }

    @Override
    public void onLoadFinished(Loader<List<Proyecto>> loader, List<Proyecto> proyectos) {
        // Hide loading indicator because the data has been loaded
        View loadingIndicator = getView().findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);

        // Set empty state text to display "No proyectos found."
        mEmptyStateTextView.setText(R.string.no_proyectos);

        // Clear the adapter of previous proyecto data
        mAdapter.clear();

        // If there is a valid list of {@link Proyecto}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (proyectos != null && !proyectos.isEmpty()) {
            mAdapter.addAll(proyectos);
        }
        request_url = PROYECTO_REQUEST_URL;
    }

    @Override
    public void onLoaderReset(Loader<List<Proyecto>> loader) {
        // Loader reset, so we can clear out our existing data.
        mAdapter.clear();
        request_url = PROYECTO_REQUEST_URL;
    }
}
