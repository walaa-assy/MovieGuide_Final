package com.example.android.movieguide.app;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 4/7/2016.
 */
public class MovieInfo implements Parcelable {

    private String movieID;
    private String movieName;
    private String movieTitle;
    private String movieOverview;
    private String moviePoster;
    private String movieBackdrop;
    private String movieReleaseDate;
    private double movieVoteAverage;
    private double movieVoteCount;
    private double moviePopularity;
    private String movieLanguage;
    private boolean movieVideo;
    private String movieTrailerKey;
    private String movieReviewURL;

    final String BASE_IMAGE_URL = "http://image.tmdb.org/t/p/";

    final String POSTER_SIZE = "w342";
    final String BACKDROP_SIZE = "w780";
    final String DETAIL_POSTER_SIZE = "w185";
    final String BASE_TARILER_URL = "https://www.youtube.com/watch?v=";

    public MovieInfo(String id, String title, String overview, String poster, String backdrop_path, String releaseDate, double vote_avg, double popularity, String language, double votesCount) {

        this.movieID = id;
        this.movieTitle = title;
        this.movieOverview = overview;
        this.moviePoster = poster;
        this.movieBackdrop = backdrop_path;
        this.movieReleaseDate = releaseDate;
        this.movieVoteAverage = vote_avg;
        this.moviePopularity = popularity;
        this.movieLanguage = language;
        this.movieVoteCount = votesCount;
    }

    public MovieInfo() {

    }







    public void setMovieReviewURL(String url) {
        this.movieReviewURL = url;
    }

    // getters for all movie properties
    public String getMovieId() {
        return movieID;
    }


    public void setMovieID(String id) {
        this.movieID = id;
    }

    public void setMovieTitle(String title) {this.movieTitle = title;}

    public String getTitle() {
        return movieTitle;
    }

    public void setMovieOverview(String overview){this.movieOverview = overview;}

    public String getOverview() {
        return movieOverview;
    }

public void setMoviePoster(String poster) {this.moviePoster =  BASE_IMAGE_URL + POSTER_SIZE +poster ; }
    public String getPosterPath() {
        return BASE_IMAGE_URL + POSTER_SIZE + moviePoster;
    }

    public void setMovieBackdrop(String backdrop) {this.moviePoster =  BASE_IMAGE_URL + BACKDROP_SIZE + backdrop ; }
    public String getBackdropPath() {
        return BASE_IMAGE_URL + BACKDROP_SIZE + movieBackdrop;
    }

    public void setDetailPoster(String poster) {
       this.moviePoster = BASE_IMAGE_URL + DETAIL_POSTER_SIZE + poster;
    }

    public String getDetailPoster() {
        return BASE_IMAGE_URL + DETAIL_POSTER_SIZE + moviePoster;
    }


    public void  setMovieReleaseDate(String releaseDate){this.movieReleaseDate = releaseDate;}
    public String getReleaseDate() {
        return movieReleaseDate;
    }

    public void setMovieVoteAverage(double voteAverage){this.movieVoteAverage = voteAverage;}
    public double getVoteAverage() {
        return movieVoteAverage ;
    }

    public double getPopularity() {
        return moviePopularity;
    }
    public void setMoviePopularity(double popularity) {this.moviePopularity = popularity;}


    public  void setMovieVoteCount(double voteCount) {this.movieVoteCount = voteCount;}
    public double getVoteCount() {
        return movieVoteCount;
    }

    public void setTRailerUrl(String key) {this.movieTrailerKey = key;}
    public String getTrailerURL() {
        return BASE_TARILER_URL + movieTrailerKey;
    }

    /**
     * Describe the kinds of special objects contained in this Parcelable's
     * marshalled representation.
     *
     * @return a bitmask indicating the set of special object types marshalled
     * by the Parcelable.
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Flatten this object in to a Parcel.
     *
     * @param dest  The Parcel in which the object should be written.
     * @param flags Additional flags about how the object should be written.
     *              May be 0 or {@link #PARCELABLE_WRITE_RETURN_VALUE}.
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(movieID);
        dest.writeString(movieTitle);
        dest.writeString(movieOverview);
        dest.writeString(moviePoster);
        dest.writeString(movieBackdrop);
        dest.writeString(movieReleaseDate);
        dest.writeDouble(movieVoteAverage);
        dest.writeDouble(moviePopularity);
        dest.writeDouble(movieVoteCount);
        dest.writeString(movieLanguage);
        dest.writeString(String.valueOf(movieVideo));
        dest.writeString(movieTrailerKey);
        dest.writeString(movieReviewURL);
    }


    // Creator
    public static final Parcelable.Creator<MovieInfo> CREATOR = new Parcelable.Creator<MovieInfo>() {
        public MovieInfo createFromParcel(Parcel in) {
            return new MovieInfo(in);
        }

        public MovieInfo[] newArray(int size) {
            return new MovieInfo[size];
        }
    };

    // "De-parcel object
    private MovieInfo(Parcel in) {
        this.movieID = in.readString();
        this.movieTitle = in.readString();
        this.movieOverview = in.readString();
        this.moviePoster = in.readString();
        this.movieBackdrop = in.readString();
        this.movieReleaseDate = in.readString();
        this.movieVoteAverage = in.readDouble();
        this.moviePopularity = in.readDouble();
        this.movieVoteCount = in.readDouble();
        this.movieLanguage = in.readString();
        this.movieVideo = Boolean.parseBoolean(in.readString());
        this.movieTrailerKey = in.readString();
        this.movieReviewURL = in.readString();
    }


}
