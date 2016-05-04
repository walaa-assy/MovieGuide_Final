package com.example.android.movieguide.app;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.movieguide.app.data.MoviesDBHelper;
import com.squareup.picasso.Picasso;

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
import java.util.concurrent.ExecutionException;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailFragment extends Fragment {
    private static final String LOG_TAG = DetailFragment.class.getSimpleName();
    //  private int mActivatedPosition = GridView.INVALID_POSITION;
    private int mActivatedPosition = 0;
    MovieInfo m;

    public static final String MOVIE_BUNDLE = "MovieInfo";

    private CommonAdapter cAdapter;

    private static final String SHARE_HASHTAG = " #MovieguideApp";


    ArrayList<ExtraBase> TrailersList;

    boolean btnFavState = false;


    public DetailFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null && savedInstanceState.containsKey(MOVIE_BUNDLE)) {
            m = savedInstanceState.getParcelable(MOVIE_BUNDLE);
        }

    }


    public void insertFavoriteMovies(MovieInfo movie) {

        MoviesDBHelper helper = new MoviesDBHelper(getActivity());
        helper.getWritableDatabase();
        if (helper.addFAVORITEMOVIES(movie) != -1) {
            Toast.makeText(getActivity(), "successfully added", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.detailfragment, menu);
        MenuItem menuItem = menu.findItem(R.id.action_share);
        ShareActionProvider mShareActionProvider =
                (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);

        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(createShareTrailerIntent());
        } else {
            Log.d(LOG_TAG, "Share Action Provider is null?");
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        Bundle arguments = getArguments();
        if (arguments != null) {
            m = arguments.getParcelable(MOVIE_BUNDLE);
        } else {
            Intent i = getActivity().getIntent();
            m = (MovieInfo) i.getParcelableExtra(MOVIE_BUNDLE);
        }

        final String number = m.getMovieId();
        String title = m.getTitle();
        String posterStr = m.getPosterPath();
        String backdropStr = m.getBackdropPath();
        String detailPosterStr = m.getDetailPoster();
        String overview = m.getOverview();
        String rd = m.getReleaseDate();

        float vote = (float) m.getVoteAverage();
        double popularity = m.getPopularity();


        ImageView backDrop = (ImageView) rootView.findViewById(R.id.backdrop_image_view);
        Picasso.with(getActivity()).load(backdropStr).into(backDrop);

        ImageView posterImage = (ImageView) rootView.findViewById(R.id.poster_image_view);
        Picasso.with(getActivity()).load(detailPosterStr).into(posterImage);

        ((TextView) rootView.findViewById(R.id.title_text_view)).setText(title);
        ((TextView) rootView.findViewById(R.id.overview_text_view)).setText(overview);

        TextView rdText = (TextView) rootView.findViewById(R.id.releaseDate_text_view);
        rdText.setText(rd);

        RatingBar voteRatingBar = (RatingBar) rootView.findViewById(R.id.rating_bar);
        voteRatingBar.setStepSize((float) 0.5);
        float fVote = vote / 2;
        voteRatingBar.setRating(fVote);


        FetchTrailersTask trailerTask = new FetchTrailersTask();
        trailerTask.execute(number);
        try {
            TrailersList = trailerTask.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        ArrayList<ExtraBase> t = new ArrayList<>();
        ListView tListView = (ListView) rootView.findViewById(R.id.trailers_listview);
        cAdapter = new CommonAdapter(getActivity(), t);

        //Toast.makeText(getActivity(), uu, Toast.LENGTH_LONG).show();
        tListView.setAdapter(cAdapter);
        tListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                ExtraBase base = cAdapter.getItem(position);
                Uri uri = Uri.parse(base.getKey());
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                if (intent.resolveActivity(getActivity().getPackageManager()) != null)
                    startActivity(intent);
            }
        });

        final CheckBox favStar = (CheckBox) rootView.findViewById(R.id.fav_star);
        favStar.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
if (favStar.isChecked()){ insertFavoriteMovies(m);}
            }

        }
        );


        final Button addFAV = (Button) rootView.findViewById(R.id.fav_button);
        if (btnFavState == true)

        {
            addFAV.setText("already a favorite");
            addFAV.setBackgroundResource(R.drawable.dontstop);
        }

        addFAV.setOnClickListener(new View.OnClickListener()

                                  {

                                      @Override
                                      public void onClick(View v) {

                                          MoviesDBHelper helper = new MoviesDBHelper(getActivity());
                                          helper.getReadableDatabase();
                                          if (helper.checkMovieExists(m)) {

                                              Toast.makeText(getActivity(), " Already A Favorite", Toast.LENGTH_SHORT).show();
                                              return;
                                          } else {
                                              insertFavoriteMovies(m);
                                              addFAV.setText("Added to favorites");
                                              addFAV.setBackgroundResource(R.drawable.dontstop);
                                          }
                                      }
                                  }
        );


        final Button btnReviews = (Button) rootView.findViewById(R.id.reviews_button);
        btnReviews.setOnClickListener(new View.OnClickListener()

                                      {
                                          @Override
                                          public void onClick(View v) {
                                              Intent i = new Intent(getActivity(), ReviewsActivity.class);
                                              i.putExtra(MOVIE_BUNDLE, m);
                                              startActivity(i);
                                          }
                                      }

        );

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(MOVIE_BUNDLE, m);
        super.onSaveInstanceState(outState);
    }

    private Intent createShareTrailerIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        String shareStr = m.getTitle();
        String tURL = TrailersList.get(0).getKey();
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareStr + " \n" + " TRAILER" + " \n" + tURL + " \n" + SHARE_HASHTAG);
        return shareIntent;
    }


    public class FetchTrailersTask extends AsyncTask<String, Void, ArrayList<ExtraBase>> {


        private final String LOG_TAG = FetchTrailersTask.class.getSimpleName();

        private ArrayList<ExtraBase> getTrailersDataFromJson(String jsonStr)
                throws JSONException {
            // JSON Node names
            final String TAG_MOVIE_NUMBER = "id";
            final String TAG_RESULT = "results";
            final String TAG_TRAILER_ID = "id";
            final String TAG_TRAILER_KEY = "key";
            final String TAG_TRAILER_NAME = "name";
            final String TAG_TRAILER_SITE = "site";
            final String TAG_TRAILER_SIZE = "size";
            final String TAG_TRAILER_TYPE = "type";


            JSONObject trailersDataJson = new JSONObject(jsonStr);
            //json object movie number know as movie id
            String movieID = trailersDataJson.getString(TAG_MOVIE_NUMBER);
            //  String movieNumber = movieID.getString(TAG_MOVIE_NUMBER);


            //json array results
            JSONArray trailers = trailersDataJson.getJSONArray(TAG_RESULT);


            ArrayList<ExtraBase> TrailersList = new ArrayList<>();

            //MovieList = new ArrayList<>();
            for (int i = 0; i < trailers.length(); i++) {

                // JSON Node names
                JSONObject c = trailers.getJSONObject(i);
                String trailerID = c.getString(TAG_TRAILER_ID);
                String key = c.getString(TAG_TRAILER_KEY);
                String name = c.getString(TAG_TRAILER_NAME);
                String site = c.getString(TAG_TRAILER_SITE);
                String size = c.getString(TAG_TRAILER_SIZE);
                String type = c.getString(TAG_TRAILER_TYPE);

//            MovieInfo movieTrailer = new MovieInfo();
//            movieTrailer.setMovieID(movieID);
//            movieTrailer.setMovieTrailerKey(key);
                m.setTRailerUrl(key);
                ExtraBase movieTrailer = new ExtraBase(movieID, trailerID, key, name, site, size, type);


                TrailersList.add(i, movieTrailer);

            }


            for (ExtraBase s : TrailersList) {
                Log.v(LOG_TAG, "Trailers entry: " + s);

            }

            return TrailersList;

        }

        @Override
        protected ArrayList<ExtraBase> doInBackground(String... params) {

            if (params.length == 0) {
                return null;
            }

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;


            String moviesJsonStr = null;


            try {


                final String MOVIES_BASE_URL = "http://api.themoviedb.org/3/movie";

                final String APPID_PARAM = "api_key";
                Uri builtUri = Uri.parse(MOVIES_BASE_URL).buildUpon()
                        .appendPath(params[0])
                        .appendPath("videos")
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
                return getTrailersDataFromJson(moviesJsonStr);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }
            return null;
        }


        @Override

        protected void onPostExecute(ArrayList<ExtraBase> result) {
            if (result != null) {
                if (result != null) {
                    cAdapter.clear();
                    for (ExtraBase s : result) {
                        cAdapter.add(s);
                    }
                }
            }
        }
    }
}