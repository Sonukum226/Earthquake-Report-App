package com.example.quakereport;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeoutException;

public class EarthquakeAdapter extends ArrayAdapter<Earthquake> {

    //This is our own custom constructor(It doesnt mirroe a superclass constructor)
    //the context is used to inflate the layout file,and the list the data we want to
    //populate in the lists

    //@param context the current context.used to inflate the layout file
    //@param earthquakes A list of EarthquakesReport(Placeholder) objects to dislay in a list
    public EarthquakeAdapter(Context context, List<Earthquake> earthquakes) {
        // Here, we initialize the ArrayAdapter's internal storage for the context and the list.
        // the second argument is used when the ArrayAdapter is populating a single TextView.
        // Because this is a custom adapter for TextViews.the adapter is not
        // going to use this second argument, so it can be any value. Here, we used 0.
        super(context, 0, earthquakes);

    }
    private static final String LOCATION_SEP="of";
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.earthquake_list_item, parent, false);
        }
        //Get the {@link Earthquake} object located at this position in the list
        Earthquake currentearthquake = getItem(position);

        //Getting the magnitude


        //Find the textview in earthequale_list.xml layout @magnitude

        TextView magnitude = (TextView) listItemView.findViewById(R.id.magnitude);
        String  Magnitude=formatMag(currentearthquake.getMmagnitude());
        magnitude.setText(Magnitude);

        //setting up the background colour of magnitude
        GradientDrawable magnitudecircle=(GradientDrawable) magnitude.getBackground();
        int magnitudecolor=getMagnitudeColor(currentearthquake.getMmagnitude());
        magnitudecircle.setColor(magnitudecolor);

        //getting the original location
        String originalLocation=currentearthquake.getMcity();

        String primarylocation;
        String locationoffset;

        if(originalLocation.contains(LOCATION_SEP)){
            String[] parts=originalLocation.split(LOCATION_SEP);
            locationoffset=parts[0]+LOCATION_SEP;
            primarylocation=parts[1];
        }else {
            locationoffset=getContext().getString(R.string.near_the);
            primarylocation=originalLocation;
        }

        //find the textview in=earthquake_list.xml  @Str 1
        TextView str1=(TextView)listItemView.findViewById(R.id.str1);
        str1.setText(locationoffset);  //

        //find the textview in earthqukae_list.xml @Str2
        TextView str2=(TextView)listItemView.findViewById(R.id.str2);
        str2.setText(primarylocation);//

        //Date ka object hai
        Date dateobject= new Date(currentearthquake.getMtimeInmilliseconds());
        //Find the text view with id for date
        TextView dateview=(TextView)listItemView.findViewById(R.id.Date);
        //formated date
        String formatteddate=dateFormat(dateobject);
        dateview.setText(formatteddate);//Set into the date view

        TextView timeview=(TextView)listItemView.findViewById(R.id.time);
        //formatted time
        String formaatedtime=formatime(dateobject);
        //set into the time
        timeview.setText(formaatedtime);

        return listItemView;

    }

    private int getMagnitudeColor(double mmagnitude) {
        int magnitudecolorID;
        int magfloor=(int) Math.floor(mmagnitude);
        switch (magfloor){
            case 0:
            case 1:
                magnitudecolorID=R.color.magnitude1;
                break;
            case 2:
                magnitudecolorID=R.color.magnitude2;
                break;
            case 3:
                magnitudecolorID=R.color.magnitude3;
                break;
            case 4:
                magnitudecolorID=R.color.magnitude4;
                break;
            case 5:
                magnitudecolorID=R.color.magnitude5;
                break;
            case 6:
                magnitudecolorID=R.color.magnitude6;
                break;
            case 7:
                magnitudecolorID=R.color.magnitude7;
                break;
            case 8:
                magnitudecolorID=R.color.magnitude8;
                break;
            case 9:
                magnitudecolorID=R.color.magnitude9;
                break;
            default:
                magnitudecolorID=R.color.magnitude10plus;
                break;
        }
        return ContextCompat.getColor(getContext(),magnitudecolorID);
//         to convert the color resource ID into an actual integer color value, and
//         return the result as the return value of the getMagnitudeColor() helper method.
    }

    private String formatMag(double mmagnitude) {
        DecimalFormat magformat=new DecimalFormat("0.0");
        return magformat.format(mmagnitude);
    }

    private String formatime(Date dateobject) {
        SimpleDateFormat formattime=new SimpleDateFormat("h:mm a");
        return formattime.format(dateobject);
    }

    //This method will return the formatd date ex:-2020-12-25
    private String dateFormat(Date dateobject) {
        SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(dateobject);
    }

}

