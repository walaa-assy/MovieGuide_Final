package com.example.android.movieguide.app.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.movieguide.app.MovieInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 4/18/2016.
 */
public class MoviesDBHelper extends SQLiteOpenHelper {


    private static final int DATABASE_VERSION = 1;
    static final String DATABASE_NAME = "FAVORITEMOVIES.db";


    public MoviesDBHelper(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        // By default foreign_key=off - cascading deletes will not work
        db.execSQL("PRAGMA foreign_keys=ON");
    }

    public Long addFAVORITEMOVIES(MovieInfo favMovie) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(MoviesContract.FAVORITEMOVIES.COLUMN_MovieID, favMovie.getMovieId());
        values.put(MoviesContract.FAVORITEMOVIES.COLUMN_TITLE, favMovie.getTitle());
        values.put(MoviesContract.FAVORITEMOVIES.COLUMN_OVERVIEW, favMovie.getOverview());
        values.put(MoviesContract.FAVORITEMOVIES.COLUMN_POSTERPATH, favMovie.getPosterPath());
        values.put(MoviesContract.FAVORITEMOVIES.COLUMN_BACKDROPPATH, favMovie.getBackdropPath());
        values.put(MoviesContract.FAVORITEMOVIES.COLUMN_RELEASEDATE, favMovie.getReleaseDate());
        values.put(MoviesContract.FAVORITEMOVIES.COLUMN_VOTEAVERAGE, favMovie.getVoteAverage());
        values.put(MoviesContract.FAVORITEMOVIES.COLUMN_POPULARITY, favMovie.getPopularity());
        values.put(MoviesContract.FAVORITEMOVIES.COLUMN_VOTECOUNT, favMovie.getVoteCount());
        values.put(MoviesContract.FAVORITEMOVIES.COLUMN_IS_FAVORITE, "true");

        long ret = db.insert("FAVORITEMOVIES", null, values);
        db.close();
        return ret;
    }

    //  public List<MoviesContract.FAVORITEMOVIES> getFAVORITEMOVIES(String is_favorite){
    public List<MovieInfo> getFAVORITEMOVIES() {
        List<MovieInfo> ret = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from FAVORITEMOVIES", null);
        while (res.moveToNext()) {
            MovieInfo movieInfo = new MovieInfo();
            movieInfo.setMovieID(res.getString(res.getColumnIndex(MoviesContract.FAVORITEMOVIES.COLUMN_MovieID)));
            movieInfo.setMovieTitle(res.getString(res.getColumnIndex(MoviesContract.FAVORITEMOVIES.COLUMN_TITLE)));
            movieInfo.setMovieOverview(res.getString(res.getColumnIndex(MoviesContract.FAVORITEMOVIES.COLUMN_OVERVIEW)));
            movieInfo.setMoviePoster(res.getString(res.getColumnIndex(MoviesContract.FAVORITEMOVIES.COLUMN_POSTERPATH)));
            movieInfo.setMovieBackdrop(res.getString(res.getColumnIndex(MoviesContract.FAVORITEMOVIES.COLUMN_BACKDROPPATH)));
            movieInfo.setMovieReleaseDate(res.getString(res.getColumnIndex(MoviesContract.FAVORITEMOVIES.COLUMN_RELEASEDATE)));
            movieInfo.setMovieVoteAverage(Double.parseDouble(res.getString(res.getColumnIndex(MoviesContract.FAVORITEMOVIES.COLUMN_VOTEAVERAGE))));
            movieInfo.setMoviePopularity(Double.parseDouble(res.getString(res.getColumnIndex(MoviesContract.FAVORITEMOVIES.COLUMN_POPULARITY))));
            movieInfo.setMovieVoteCount(Double.parseDouble(res.getString(res.getColumnIndex(MoviesContract.FAVORITEMOVIES.COLUMN_VOTECOUNT))));
            ret.add(movieInfo);
        }
        return ret;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {


        // SQL statement to create the favoritemovies table
        final String FAVORITESMOVIES_TABLE_CREATE =
                "CREATE TABLE " +
                        MoviesContract.FAVORITEMOVIES.TABLE_NAME + " (" +
                        MoviesContract.FAVORITEMOVIES._ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                        MoviesContract.FAVORITEMOVIES.COLUMN_MovieID + " INTEGER, " +
                        MoviesContract.FAVORITEMOVIES.COLUMN_TITLE + " TEXT, " +
                        MoviesContract.FAVORITEMOVIES.COLUMN_OVERVIEW + " TEXT, " +
                        MoviesContract.FAVORITEMOVIES.COLUMN_POSTERPATH + " TEXT, " +
                        MoviesContract.FAVORITEMOVIES.COLUMN_BACKDROPPATH + " TEXT, " +
                        MoviesContract.FAVORITEMOVIES.COLUMN_RELEASEDATE + " TEXT, " +
                        MoviesContract.FAVORITEMOVIES.COLUMN_VOTEAVERAGE + " REAL, " +
                        MoviesContract.FAVORITEMOVIES.COLUMN_POPULARITY + " REAL, " +
                        MoviesContract.FAVORITEMOVIES.COLUMN_VOTECOUNT + " REAL, " +
                        MoviesContract.FAVORITEMOVIES.COLUMN_IS_FAVORITE + " TEXT" + ");";

        // SQL statement to create the reviews table
        final String REVIEWS_TABLE_CREATE =
                "CREATE TABLE " +
                        MoviesContract.REVIEWSENTRY.TABLE_NAME + " (" +
                        MoviesContract.REVIEWSENTRY._ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                        MoviesContract.REVIEWSENTRY.COLUMN_REVIEW_ID + " TEXT, " +
                        MoviesContract.REVIEWSENTRY.COLUMN_AUHTOR + " TEXT, " +
                        MoviesContract.REVIEWSENTRY.COLUMN_URL + " TEXT, " +
                        MoviesContract.REVIEWSENTRY.COLUMN_CONTENT + " TEXT, " +
                        MoviesContract.REVIEWSENTRY.COLUMN_FAVORITE_MOVIE_KEY + " INTEGER, " +
                        "foreign key(" + MoviesContract.REVIEWSENTRY.COLUMN_FAVORITE_MOVIE_KEY + ") references " +
                        MoviesContract.FAVORITEMOVIES.TABLE_NAME + "(" + MoviesContract.FAVORITEMOVIES._ID + ")" +
                        " ON DELETE CASCADE);";


        // SQL statement to create the trailers table
        final String TRAILERS_TABLE_CREATE =
                "CREATE TABLE " +
                        MoviesContract.TRAILERSENTRY.TABLE_NAME + " (" +
                        MoviesContract.TRAILERSENTRY._ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                        MoviesContract.TRAILERSENTRY.COLUMN_TRAILER_ID + " TEXT, " +
                        MoviesContract.TRAILERSENTRY.COLUMN_TRAILER_KEY + " TEXT, " +
                        MoviesContract.TRAILERSENTRY.COLUMN_NAME + " TEXT, " +
                        MoviesContract.TRAILERSENTRY.COLUMN_SITE + " TEXT, " +
                        MoviesContract.TRAILERSENTRY.COLUMN_SIZE + " REAL, " +
                        MoviesContract.TRAILERSENTRY.COLUMN_TYPE + " TEXT, " +
                        MoviesContract.TRAILERSENTRY.COLUMN_FAVORITE_MOVIE_KEY + " INTEGER, " +
                        "foreign key(" + MoviesContract.TRAILERSENTRY.COLUMN_FAVORITE_MOVIE_KEY + ") references " +
                        MoviesContract.FAVORITEMOVIES.TABLE_NAME + "(" + MoviesContract.FAVORITEMOVIES._ID + ")" +
                        " ON DELETE CASCADE);";

        db.execSQL(FAVORITESMOVIES_TABLE_CREATE);
        db.execSQL(REVIEWS_TABLE_CREATE);
        db.execSQL(TRAILERS_TABLE_CREATE);

    }


    /**
     * Called when the database needs to be upgraded. The implementation
     * should use this method to drop tables, add tables, or do anything else it
     * needs to upgrade to the new schema version.
     * <p>
     * <p>
     * The SQLite ALTER TABLE documentation can be found
     * <a href="http://sqlite.org/lang_altertable.html">here</a>. If you add new columns
     * you can use ALTER TABLE to insert them into a live table. If you rename or remove columns
     * you can use ALTER TABLE to rename the old table, then create the new table and then
     * populate the new table with the contents of the old table.
     * </p><p>
     * This method executes within a transaction.  If an exception is thrown, all changes
     * will automatically be rolled back.
     * </p>
     *
     * @param db         The database.
     * @param oldVersion The old database version.
     * @param newVersion The new database version.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
