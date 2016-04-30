package com.example.android.movieguide.app.data;

import android.provider.BaseColumns;

/**
 * Created by Administrator on 4/17/2016.
 */
public class MoviesContract {

    public static final class FAVORITEMOVIES implements BaseColumns{

        public static final String TABLE_NAME = "FAVORITEMOVIES";

        public static final String COLUMN_MovieID = "Movie_id";
        public static final String COLUMN_TITLE  = "Title";
        public static final String COLUMN_OVERVIEW = "Overview";
        public static final String COLUMN_POSTERPATH =  "Poster";
        public static final String COLUMN_BACKDROPPATH = "Backdrop";
        public static final String COLUMN_RELEASEDATE = "ReleaseDate";
        public static final String COLUMN_VOTEAVERAGE  =  "Vote_Average";
        public static final String COLUMN_POPULARITY = "Popularity";
        public static final String  COLUMN_VOTECOUNT = "VoteCount";

        public static  final String COLUMN_IS_FAVORITE = "Is_Favorite";

    }

    public static final class TRAILERSENTRY implements BaseColumns{

        public static final String TABLE_NAME = "Trailers";

        public static final String COLUMN_FAVORITE_MOVIE_KEY = "Favorite_Movie_ID";

        public static final String COLUMN_TRAILER_ID = "Trailer_ID";
        public static final String COLUMN_TRAILER_KEY= "Trailer_key";
        public static final String COLUMN_NAME= "Name";
        public static final String COLUMN_SITE = "WebSite";
        public static final String COLUMN_SIZE = "Size";
        public static final String COLUMN_TYPE = "Trailer";

    }

    public static final class REVIEWSENTRY implements BaseColumns{

        public static final String TABLE_NAME = "Reviews";

        public static final String COLUMN_FAVORITE_MOVIE_KEY = "Favorite_Movie_ID";

        public static final String COLUMN_REVIEW_ID = "Review_ID";
        public static final String COLUMN_AUHTOR = "Author";
        public static final String COLUMN_URL = "Url";
        public static final String COLUMN_CONTENT = "Conetnt";

    }
}
