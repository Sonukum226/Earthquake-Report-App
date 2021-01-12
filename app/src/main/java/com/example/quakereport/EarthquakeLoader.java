package com.example.quakereport;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import java.util.List;

public class EarthquakeLoader extends AsyncTaskLoader<List<Earthquake>> {
        /*tag for log msg*/
    private static final String LOG_TAG=EarthquakeLoader.class.getName();
    private String mUrl;
    public EarthquakeLoader( Context context,String url) {
        super(context);
        mUrl=url;
    }
    private static final String Tag_msg=AsyncTaskLoader.class.getName();
    @Override
    protected void onStartLoading() {
        Log.i(Tag_msg,"onStartLoading is called");
        forceLoad();// required step to actually trigger the loadInBackground() method to execute.

    }

    @Override
    public List<Earthquake> loadInBackground() {
        Log.i(Tag_msg,"loadInBackground is called");
        if (mUrl==null) {
            return null;
        }
        // Perform the network request, parse the response, and extract a list of books.Li
        List<Earthquake> earthquakes=QueryUtils.fetchEarthquakeData(mUrl);


        return earthquakes;

    }
}
