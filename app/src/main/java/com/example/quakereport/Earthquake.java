package com.example.quakereport;

public class Earthquake {
    private double mmagnitude;
    private String mLocation;
    private long mtimeInmilliseconds;
    private String mURL;

    public Earthquake(double magnitude,String Location,long timeInmilliseconds,String url){
        mmagnitude=magnitude;
        mLocation=Location;
        mtimeInmilliseconds=timeInmilliseconds;
        mURL=url;
    }

    public double getMmagnitude() {
        return mmagnitude;
    }

    public String getMcity() {
        return mLocation;
    }

    public long getMtimeInmilliseconds() {
        return mtimeInmilliseconds;
    }

    public String getmURL() {
        return mURL;
    }
}
