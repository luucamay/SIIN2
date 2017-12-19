package com.example.lupe.siin;

/**
 * Created by lupe on 19/12/17.
 */


import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

/**
 * Loads a list of proyectos by using an AsyncTask to perform the
 * network request to the given URL.
 */
public class ProyectoLoader extends AsyncTaskLoader<List<Proyecto>> {

    /**
     * Tag for log messages
     */
    private static final String LOG_TAG = ProyectoLoader.class.getName();

    /**
     * Query URL
     */
    private String mUrl;

    /**
     * Constructs a new {@link ProyectoLoader}.
     *
     * @param context of the activity
     * @param url     to load data from
     */
    public ProyectoLoader(Context context, String url) {
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
    public List<Proyecto> loadInBackground() {
        if (mUrl == null) {
            return null;
        }
        // Perform the network request, parse the response, and extract a list of proyectos.
        List<Proyecto> proyectos = QueryUtils.fetchProyectoData(mUrl);
        return proyectos;
    }
}