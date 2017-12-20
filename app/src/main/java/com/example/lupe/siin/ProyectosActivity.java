package com.example.lupe.siin;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ProyectosActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<List<Proyecto>> {

    private static final String LOG_TAG = ProyectosActivity.class.getName();

    private String request_url =
            "http://marte.abc.gob.bo/webService/proyectos.php/lista_proyectos";

    private static final String PROYECTO_REQUEST_URL =
            "http://marte.abc.gob.bo/webService/proyectos.php/lista_proyectos";

    private static final String BUSCAR_PROYECTO_REQUEST_URL =
            "http://marte.abc.gob.bo/webService/proyectos.php/search_proyecto/";

    private int PROYECTO_LOADER_ID = 1;

    /**
     * Adapter for the list of earthquakes
     */
    private ProyectoAdapter mAdapter;

    /**
     * TextView that is displayed when the list is empty
     */
    private TextView mEmptyStateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proyectos);

        // Find a reference to the {@link ListView} in the layout
        ListView earthquakeListView = (ListView) findViewById(R.id.list);

        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        earthquakeListView.setEmptyView(mEmptyStateTextView);

        // Create a new adapter that takes an empty list of earthquakes as input
        mAdapter = new ProyectoAdapter(this, new ArrayList<Proyecto>());

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        earthquakeListView.setAdapter(mAdapter);

        // Set an item click listener on the ListView, which sends an intent to a web browser
        // to open a website with more information about the selected earthquake.
        earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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

        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {
            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getLoaderManager();

            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            loaderManager.initLoader(PROYECTO_LOADER_ID, null, this);
        } else {
            // Otherwise, display error
            // First, hide loading indicator so error message will be visible
            View loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);

            // Update empty state with no connection error message
            mEmptyStateTextView.setText(R.string.no_internet_connection);
        }
    }

    @Override
    public Loader<List<Proyecto>> onCreateLoader(int i, Bundle bundle) {
        // Create a new loader for the given URL
        Log.d("loader",request_url);
        return new ProyectoLoader(this, request_url);
    }

    @Override
    public void onLoadFinished(Loader<List<Proyecto>> loader, List<Proyecto> proyectos) {
        // Hide loading indicator because the data has been loaded
        View loadingIndicator = findViewById(R.id.loading_indicator);
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

    public void buscarProyecto(View view) {
        EditText editText = (EditText) findViewById(R.id.textoBuscado);
        String textoBuscado = editText.getText().toString();
        LoaderManager loaderManager = getLoaderManager();
        request_url = BUSCAR_PROYECTO_REQUEST_URL + "" + textoBuscado;
        PROYECTO_LOADER_ID ++;
        loaderManager.initLoader(PROYECTO_LOADER_ID, null, this);

    }
}

