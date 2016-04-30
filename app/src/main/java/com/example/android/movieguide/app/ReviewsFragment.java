package com.example.android.movieguide.app;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

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


/**
 * A placeholder fragment containing a simple view.
 */
public class ReviewsFragment extends Fragment {

    private static final String LOG_TAG = ReviewsFragment.class.getSimpleName();

    private DetailsAdapter rAdapter;
    private ArrayList<Reviews> rev = new ArrayList<>();
    private ListView rListView;
    MovieInfo movie;

    public ReviewsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_reviews, container, false);


        Intent i = getActivity().getIntent();
        movie = (MovieInfo) i.getParcelableExtra("MovieInfo");

        String number = movie.getMovieId();

        FetchReviewsTask reviewsTask = new FetchReviewsTask();
        reviewsTask.execute(number);
        rListView = (ListView) rootView.findViewById(R.id.reviews_listview);
        rAdapter = new DetailsAdapter(getActivity(), rev);
        rListView.setAdapter(rAdapter);

        return rootView;
    }

        public class FetchReviewsTask extends AsyncTask<String, Void, ArrayList<Reviews>> {

        private final String LOG_TAG = FetchReviewsTask.class.getSimpleName();


        private ArrayList<Reviews> getReviewsDataFromJson(String jsonStr)
                throws JSONException {
            // JSON Node names
            final String TAG_MOVIE_NUMBER = "id";
            final String TAG_RESULT = "results";
            final String TAG_REVIEW_ID = "id";
            final String TAG_REVIEW_AUTHOR = "author";
            final String TAG_REVIEW_CONTENT = "content";
            final String TAG_REVIEW_URL = "url";


            JSONObject reviewsDataJson = new JSONObject(jsonStr);
            //json object movie number know as movie id
            //JSONObject movieID= reviewsDataJson.getJSONObject(TAG_MOVIE_NUMBER);
            String movieNumber = reviewsDataJson.getString(TAG_MOVIE_NUMBER);


            //json array results
            JSONArray trailers = reviewsDataJson.getJSONArray(TAG_RESULT);


            // ArrayList<MovieInfo> ReviewsList = new ArrayList<MovieInfo>();

            ArrayList<Reviews> ReviewsList = new ArrayList<>();

            for (int i = 0; i < trailers.length(); i++) {

                // JSON Node names
                JSONObject c = trailers.getJSONObject(i);
                String reviewID = c.getString(TAG_REVIEW_ID);
                String author = c.getString(TAG_REVIEW_AUTHOR);
                String content = c.getString(TAG_REVIEW_CONTENT);
                String url = c.getString(TAG_REVIEW_URL);

//                    MovieInfo movieReview = new MovieInfo();
//                    movieReview.setMovieID(movieNumber);
//                    movieReview.setMovieReviewURL(url);

                Reviews movieReview = new Reviews(movieNumber, reviewID, author, content, url);

                ReviewsList.add(i, movieReview);

            }


            for (Reviews s : ReviewsList) {
                Log.v(LOG_TAG, "Reviews entry: " + s);

            }

            return ReviewsList;

        }

        @Override
        protected ArrayList<Reviews> doInBackground(String... params) {

            if (params.length == 0) {
                return null;
            }

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String moviesJsonStr = null;
//                String number = "209112";
//                String mode = "reviews";

            try {


                // Construct the URL for the moviedb query
                //http://api.themoviedb.org/3/movie/209112/reviews?api_key=00000000000000000000000


                final String MOVIES_BASE_URL = "http://api.themoviedb.org/3/movie";

                final String APPID_PARAM = "api_key";
                Uri builtUri = Uri.parse(MOVIES_BASE_URL).buildUpon()
                        .appendPath(params[0])
                        .appendPath("reviews")
                        .appendQueryParameter(APPID_PARAM, BuildConfig.TheMovieDB_API_KEY)
                        .build();

                URL url = new URL(builtUri.toString());
                Log.v(LOG_TAG, "http://api.themoviedb.org/3/movie/209112/videos?api_key=dee364a81187df2c66fa2851bb30b111");
                Log.v(LOG_TAG, "Built URI " + builtUri.toString());

                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null) {

                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                moviesJsonStr = buffer.toString();
                // Log.v(LOG_TAG, "Movies JSON String: " + moviesJsonStr);

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
                return getReviewsDataFromJson(moviesJsonStr);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }
            return null;
        }


        @Override
        protected void onPostExecute(ArrayList<Reviews> result) {
            if (result != null) {
                if (result != null) {
                    rAdapter.clear();
                    for (Reviews s : result) {
                        rAdapter.add(s);
                    }
                }
            }
        }


    }


}
