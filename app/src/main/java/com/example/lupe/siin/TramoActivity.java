package com.example.lupe.siin;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lupe on 11/12/17.
 */

public class TramoActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<List<Tramo>>,
        SharedPreferences.OnSharedPreferenceChangeListener {

    private final String LOG_TAG = TramoActivity.class.getName();

    /**
     * URL for tramo data from the USGS dataset
     */
    private static final String USGS_REQUEST_URL =
            "http://abc.phuyu.me/geoserver/wfs?srsName=EPSG%3A4326&typename=geonode%3Atja01&outputFormat=json&version=1.0.0&service=WFS&request=GetFeature";

    /**
     * Constant value for the tramo loader ID. We can choose any integer.
     * This really only comes into play if you're using multiple loaders.
     */
    private static final int TRAMO_LOADER_ID = 1;

    /**
     * Adapter for the list of tramoss
     */
    private TramoAdapter mAdapter;

    /**
     * TextView that is displayed when the list is empty
     */
    private TextView mEmptyStateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tramo);

        // Find a reference to the {@link ListView} in the layout
        ListView tramoListView = (ListView) findViewById(R.id.list);

        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        tramoListView.setEmptyView(mEmptyStateTextView);

        // Create a new adapter that takes an empty list of tramos as input
        mAdapter = new TramoAdapter(this, new ArrayList<Tramo>());

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        tramoListView.setAdapter(mAdapter);

        // Set an item click listener on the ListView, which sends an intent to a web browser
        // to open a website with more information about the selected tramo.
        tramoListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // Find the current tramo that was clicked on
                Tramo currentTramo = mAdapter.getItem(position);
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
            loaderManager.initLoader(TRAMO_LOADER_ID, null, this);
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
    public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {

    }

    @Override
    public Loader<List<Tramo>> onCreateLoader(int i, Bundle bundle) {

        return new TramoLoader(this, USGS_REQUEST_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<Tramo>> loader, List<Tramo> tramos) {
        // Hide loading indicator because the data has been loaded
        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);

        // Set empty state text to display "No tramos found."
        mEmptyStateTextView.setText(R.string.no_tramos);

        // Clear the adapter of previous tramo data
        mAdapter.clear();

        // If there is a valid list of {@link Tramo}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (tramos != null && !tramos.isEmpty()) {
            for (Tramo t : tramos){
                Log.d("My array list content: ", t.getId());
            }
            mAdapter.addAll(tramos);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Tramo>> loader) {
        // Loader reset, so we can clear out our existing data.
        mAdapter.clear();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

}

