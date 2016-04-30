package com.example.android.movieguide.app;

public class Reviews {

    String movieNumber;
    String TagID;
    String author;
    String content;
    String urlLink;


    public Reviews(String id, String reviewID ,String author, String content, String url) {
        this.movieNumber = id;
        this.TagID = reviewID;
        this.author = author;
        this.content = content;
        this.urlLink = url;
    }

}