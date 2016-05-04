package com.example.android.movieguide.app;

/**
 * Created by Administrator on 4/21/2016.
 */

//class for trailers
public class ExtraBase {

    String movieNumber;
    String tagID;

    String key;
    String name;
    String site;
    String size;
    String type;

     final String BASE_TRAILER_URL = "https://www.youtube.com/watch?v=";

    public ExtraBase()
    {}

    public  ExtraBase(String movieNumber, String tagID,String key, String name, String site, String size, String type ){
        this.movieNumber = movieNumber;
        this.tagID = tagID;
        this.key = key;
        this.name = name;
        this.site = site;
        this.size = size;
        this.type = type;
    }

    public String getKey(){
        return  BASE_TRAILER_URL + key;
    }
}

