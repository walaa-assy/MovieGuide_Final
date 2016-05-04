package com.example.android.movieguide.app;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.example.android.movieguide.app.data.MoviesDBHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class MoviesFragment extends Fragment {


    //    private  ArrayList<MovieInfo> MovieList = new ArrayList<MovieInfo>();
//private  ArrayList<MovieInfo> MovieList;
    MovieAdapter movieAdapter;
    private MovieInfo movie;
    private GridView gridview;
    private String sorting;
    MovieAdapter favoriteAdapter;

//    ArrayList<MovieInfo> favorites;

    Callback comm;

    private static final String STATE_ACTIVATED_POSITION = "activated_position";
    private int mActivatedPosition = 0;//GridView.INVALID_POSITION;

    public MoviesFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.moviesfragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            updateMovies();
            return true;
        }


        return super.onOptionsItemSelected(item);
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);


        gridview = (GridView) rootView.findViewById(R.id.gridview);

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                if (sorting.equals("favorites")) {
                    movie = (MovieInfo) favoriteAdapter.getItem(position);

                } else {
                movie = (MovieInfo) movieAdapter.getItem(position);
                 }
                Toast.makeText(getActivity(), movie.getTitle(), Toast.LENGTH_SHORT).show();
                comm.respond(movie);
                mActivatedPosition = position;

            }
        });

        //if (isTablet(getActivity()))
            //gridview.performItemClick(gridview.getChildAt(0), 0,movieAdapter.getItemId(0));


        if (savedInstanceState != null
                && savedInstanceState.containsKey(STATE_ACTIVATED_POSITION)) {
            setActivatedPosition(savedInstanceState.getInt(STATE_ACTIVATED_POSITION));
        }


        return rootView;
    }

    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    public void setListener(Callback listener) {
        comm = listener;
    }




    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mActivatedPosition != GridView.INVALID_POSITION) {

            outState.putInt(STATE_ACTIVATED_POSITION, mActivatedPosition);
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void setActivateOnItemClick(boolean activateOnItemClick) {

        gridview.setChoiceMode(activateOnItemClick
                ? GridView.CHOICE_MODE_SINGLE
                : GridView.CHOICE_MODE_NONE);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void setActivatedPosition(int position) {
        if (position == GridView.INVALID_POSITION) {
            gridview.setItemChecked(mActivatedPosition, false);
        } else {
            gridview.setItemChecked(position, true);
        }

        mActivatedPosition = position;
    }



    private void updateMovies() {
//        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
//        String sorting = prefs.getString(getString(R.string.pref_sorting_key), getString(R.string.pref_sorting_most_popular));

        FetchMoviesTask moviesTask = new FetchMoviesTask();
        moviesTask.execute(sorting);

    }

//    @Override
//    public void onStart() {
//        super.onStart();
//         updateMovies();
//    }



    @Override
    public void onResume() {
        super.onResume();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        sorting = prefs.getString(getString(R.string.pref_sorting_key), getString(R.string.pref_sorting_most_popular));
        if (sorting.equals("favorites")) {

            ArrayList<MovieInfo> favorites = new ArrayList<>();
            MoviesDBHelper helper = new MoviesDBHelper(getActivity());
            helper.getReadableDatabase();
            favorites = (ArrayList<MovieInfo>) helper.getFAVORITEMOVIES();
             favoriteAdapter = new MovieAdapter(getActivity(), favorites);
            gridview.setAdapter(favoriteAdapter);

        } else
    updateMovies();


    }


    public class FetchMoviesTask extends AsyncTask<String, Void, ArrayList<MovieInfo>> {

        private final String LOG_TAG = FetchMoviesTask.class.getSimpleName();

        private ArrayList<MovieInfo> getMoviesDataFromJson(String jsonStr)
                throws JSONException {
            // JSON Node names
            final String TAG_RESULT = "results";
            final String TAG_POSTER_PATH = "poster_path";
            final String TAG_OVERVIEW = "overview";
            final String TAG_RELEASE_DATE = "release_date";
            final String TAG_GENRE_IDS = "genre_ids";
            final String TAG_ID = "id";
            final String TAG_ORIGINAL_TITLE = "original_title";
            final String TAG_ORIGINAL_LANGUAGE = "original_language";
            final String TAG_TITLE = "title";
            final String TAG_BACKDROP_PATH = "backdrop_path";
            final String TAG_POPULARITY = "popularity";
            final String TAG_VOTE_COUNT = "vote_count";
            final String TAG_VIDEO = "video";
            final String TAG_VOTE_AVERAGE = "vote_average";


            JSONObject moviesDataJson = new JSONObject(jsonStr);
            JSONArray movies = moviesDataJson.getJSONArray(TAG_RESULT);


            ArrayList<MovieInfo> MovieList = new ArrayList<MovieInfo>();


            for (int i = 0; i < movies.length(); i++) {


                JSONObject c = movies.getJSONObject(i);
                String id = c.getString(TAG_ID);
                String title = c.getString(TAG_TITLE);
                String overview = c.getString(TAG_OVERVIEW);
                String poster = c.getString(TAG_POSTER_PATH);
                // poster = "http://image.tmdb.org/t/p/w185/"+poster;
                String backDrop = c.getString(TAG_BACKDROP_PATH);
                // backDrop= "http://image.tmdb.org/t/p/w500/" + backDrop;
                String releaseDate = c.getString(TAG_RELEASE_DATE);
                double voteAverage = c.getDouble(TAG_VOTE_AVERAGE);
                double popularity = c.getDouble(TAG_POPULARITY);
                String language = c.getString(TAG_ORIGINAL_LANGUAGE);
                //boolean video = c.getBoolean(TAG_VIDEO);
                double voteCount = c.getDouble(TAG_VOTE_COUNT);

                MovieInfo movieDetails = new MovieInfo(id, title, overview, poster, backDrop, releaseDate, voteAverage, popularity, language, voteCount);
                MovieList.add(i, movieDetails);

            }



            return MovieList;

        }

        @Override
        protected ArrayList<MovieInfo> doInBackground(String... params) {

            if (params.length == 0) {
                return null;
            }

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String moviesJsonStr = null;

            try {



                final String MOVIES_BASE_URL = "http://api.themoviedb.org/3/movie";
                final String QUERY_PARAM = "top_rated";
                final String APPID_PARAM = "api_key";
                Uri builtUri = Uri.parse(MOVIES_BASE_URL).buildUpon()
                        .appendPath(params[0])
                        .appendQueryParameter(APPID_PARAM, BuildConfig.TheMovieDB_API_KEY)
                        .build();

                URL url = new URL(builtUri.toString());


                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {

                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null) {

                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {

                    return null;
                }
                moviesJsonStr = buffer.toString();


            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);

                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }

            try {
                return getMoviesDataFromJson(moviesJsonStr);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }
            return null;
        }


        @Override

        protected void onPostExecute(ArrayList<MovieInfo> result) {
            if (result != null) {

                movieAdapter = new MovieAdapter(getActivity(), result);
                gridview.setAdapter(movieAdapter);
              //  gridview.performItemClick(gridview.getChildAt(mActivatedPosition), 0,movieAdapter.getItemId(mActivatedPosition));
                if (mActivatedPosition != GridView.INVALID_POSITION) {
                    gridview.setSelection(mActivatedPosition);
                    gridview.smoothScrollToPosition(mActivatedPosition);
                }
                MovieInfo movie = result.get(mActivatedPosition);
                comm.respond(movie);

            }
        }
    }


}


