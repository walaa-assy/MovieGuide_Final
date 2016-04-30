package com.example.android.movieguide.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Administrator on 4/21/2016.
 */
public class DetailsAdapter extends ArrayAdapter<Reviews>{


    //ArrayList<Reviews> movieReviews;


    public DetailsAdapter(Context context, ArrayList<Reviews> users)
    {
        super(context, 0, users);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        Reviews mReview = getItem(position);
        if (convertView == null) {

            convertView = LayoutInflater.from(getContext()).inflate(R.layout.reviews_list, parent, false);

        }

        ((TextView) convertView.findViewById(R.id.author_textview)).setText(mReview.author);
       ((TextView) convertView.findViewById(R.id.content_textview)).setText(mReview.content);
//        ((TextView) convertView.findViewById(R.id.urlLink_textview)).setText(mReview.urlLink);

        return  convertView;
    }


}
