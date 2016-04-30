package com.example.android.movieguide.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends ActionBarActivity implements Callback{

    private static final String DETAILFRAGMENT_TAG = "DFTAG";
    boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (findViewById(R.id.container) != null) {

            mTwoPane = true;
        }
            else
            { mTwoPane = false; }

        MoviesFragment mFragment;
        if (savedInstanceState == null) {
           mFragment = new MoviesFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment, mFragment, "myFrag").commit();
        }else{
            mFragment = (MoviesFragment) getSupportFragmentManager().findFragmentByTag("myFrag");
        }
        mFragment.setListener(this);


        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.capperboard);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void respond(MovieInfo mov) {

        if (mTwoPane) {


            Bundle args= new Bundle();
            args.putParcelable("MovieInfo", mov);
            DetailFragment dFragment= new DetailFragment();
            dFragment.setArguments(args);
            getSupportFragmentManager().beginTransaction().replace(R.id.container,dFragment).commit();

        } else {
            Intent intent = new Intent(this, DetailActivity.class);
            intent.putExtra("MovieInfo", mov);
            startActivity(intent);
        }
    }
}


//DetailFragment dfragment = (DetailFragment) getSupportFragmentManager().findFragmentById(R.id.container);
//dfragment.getData(mov);
//        Intent i = this.getIntent();
//        mov = (MovieInfo) i.getParcelableExtra("com.example.android.movieguide.app.MovieInfo");
////startActivity(i);