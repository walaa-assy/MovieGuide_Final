package com.example.android.movieguide.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Administrator on 4/22/2016.
 */
//adapter for trailers

public class CommonAdapter  extends ArrayAdapter<ExtraBase>{


    public CommonAdapter(Context context, ArrayList<ExtraBase> users) {
        super(context,0 ,users);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ExtraBase base = getItem(position);


            if (convertView == null) {

                convertView = LayoutInflater.from(getContext()).inflate(R.layout.trailers_list, parent, false);

            }
            ImageView imageView = (ImageView) convertView.findViewById(R.id.trailer_logo);
            imageView.setAdjustViewBounds(true);
            imageView.setPadding(0,0,0,0);
            ((TextView) convertView.findViewById(R.id.trailer_name)).setText(base.name);


        return convertView;
    }
}
