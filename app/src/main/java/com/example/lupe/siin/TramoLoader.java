package com.example.lupe.siin; /**
 * Created by lupe on 11/12/17.
 */

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

/**
 * Loads a list of tramos by using an AsyncTask to perform the
 * network request to the given URL.
 */
public class TramoLoader extends AsyncTaskLoader<List<Tramo>> {

    /** Tag for log messages */
    private static final String LOG_TAG = TramoLoader.class.getName();

    /** Query URL */
    private String mUrl;

    /**
     * Constructs a new {@link TramoLoader}.
     *
     * @param context of the activity
     * @param url to load data from
     */
    public TramoLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    /**
     * This is on a background thread.
     */
    @Override
    public List<Tramo> loadInBackground() {
        if (mUrl == null) {
            return null;
        }

        // Perform the network request, parse the response, and extract a list of tramos.
        List<Tramo> tramos = QueryUtils.fetchTramoData(mUrl);
        return tramos;
    }
}