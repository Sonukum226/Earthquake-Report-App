package com.example.quakereport;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;

import android.annotation.SuppressLint;
import android.app.LoaderManager.LoaderCallbacks;
import androidx.loader.content.Loader;
import androidx.preference.PreferenceManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Earthquake>> {
   //adapter for the list of earthquakes
    private EarthquakeAdapter madapter;

    //Text view is displayed when the list is empty
    private TextView mEmptyStateTextView;

    private static final String USGS_REQUEST_URL="https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&orderby=time&minmag=6&limit=10";
    /**
     * Constant value for the earthquake loader ID. We can choose any integer.
     * This really only comes into play if you're using multiple loaders.
     */
    private static final String TAG_MSG=MainActivity.class.getName();
    private static final int EARTHQUAKE_LOADER_ID=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //find the reference in the layout
        ListView earthquakeview=(ListView)findViewById(R.id.list);
        //Create a new adpter of earthquake
        madapter=new EarthquakeAdapter(this,new ArrayList<Earthquake>());
        //Set the adapter on the listview
        earthquakeview.setAdapter(madapter);

        //Setting empty text view
        mEmptyStateTextView=(TextView) findViewById(R.id.emptyview);
        earthquakeview.setEmptyView(mEmptyStateTextView);


        //Start the AsyncTask to fetch the earthquake data
        EarthquakeAsyncTask task=new EarthquakeAsyncTask();
        task.execute(USGS_REQUEST_URL);

        // Set an item click listener on the ListView, which sends an intent to a web browser
        // to open a website with more information about the selected earthquake.
        earthquakeview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //We override the onItemClick() method, so that when a list item is clicked, we
                // find the corresponding Earthquake object from the adapter. (Note that we also
                // had to add the “final” modifier on the EarthquakeAdapter local variable,
                // so that we could access the adapter variable within the OnItemClickListener.)

                Earthquake currentEarthquake=madapter.getItem(position);

                Uri eathquakeUri=Uri.parse(currentEarthquake.getmURL());
                Intent websiteintent=new Intent(Intent.ACTION_VIEW,eathquakeUri);
                startActivity(websiteintent);
            }
        });

        ConnectivityManager cm=(ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=cm.getActiveNetworkInfo();

        if (networkInfo!=null && networkInfo.isConnected()) {

            LoaderManager loaderManager = getSupportLoaderManager();
            loaderManager.initLoader(EARTHQUAKE_LOADER_ID, null, this);
        }else {
            // Otherwise, display error
            // First, hide loading indicator so error message will be visible
            View loadingIndicator=findViewById(R.id.progress_bar);
            loadingIndicator.setVisibility(View.GONE);

            //update empty state with no connection error message
            mEmptyStateTextView.setText(R.string.no_internet_connection);
        }


    }
    // Here is the implementation of Loader
    @NonNull
    @Override
    public Loader<List<Earthquake>> onCreateLoader(int id, @Nullable Bundle args) {
        Log.i(TAG_MSG,"oncreate Method Called");
        return new EarthquakeLoader(this,USGS_REQUEST_URL);

    }

   // We need onLoadFinished(), where we'll do exactly what we did in onPostExecute(),
   // and use the earthquake data to update our UI - by updating the dataset in the
   // adapter.
    @Override
    public void onLoadFinished(@NonNull Loader<List<Earthquake>> loader, List<Earthquake> data) {

        Log.i(TAG_MSG,"onLoadFinishied is called");

        View loadingindicator=findViewById(R.id.progress_bar);
        loadingindicator.setVisibility(View.GONE);

        // Set empty state text to display "No earthquakes found."
        mEmptyStateTextView.setText(R.string.no_earthquakes);

        // Clear the adapter of previous earthquake data
        madapter.clear();
        if (data!=null && !data.isEmpty()){
         madapter.addAll(data); //this is starter
        }

    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<Earthquake>> loader) {
        // Loader reset, so we can clear out our existing data.
        madapter.clear();
        Log.i(TAG_MSG,"OnLoaderReset is called");
    }
    //AsynTask is here
    private class EarthquakeAsyncTask extends AsyncTask<String,Void, List<Earthquake>>{
        @Override
        protected List<Earthquake> doInBackground(String... urls) {
            if (urls.length<0 || urls[0]==null){
                return null;
            }
            List<Earthquake> result= QueryUtils.fetchEarthquakeData(urls[0]);
            return result;
        }

        @Override
        protected void onPostExecute(List<Earthquake> data) {
        //clear the adpater of previous earthquake data
            madapter.clear();
        //If there is a valid list of {@link Earthquake}s, then add them to the adapters
        //data set. This will trigger the Listview to update
        if (data !=null && !data.isEmpty())   {
            madapter.addAll(data);
          }
        }
    }
}

//fake results

//earthquake.add(new Earthquake("7.2","San francisco","Feb 2,2017"));
//        earthquake.add(new Earthquake("6.1","London","July 20,2015"));
//        earthquake.add(new Earthquake("3.8","Tokyo","Nov 10,2014"));
//        earthquake.add(new Earthquake("4.6","Mexico ciry","May 3,2016"));
//        earthquake.add(new Earthquake("5.5","Moscow","Jan 31,2015"));
//        earthquake.add(new Earthquake("6.7","Rio dejenario","Aug 19,2013"));
//        earthquake.add(new Earthquake("7.8","Paris","Oct 30,2011"));