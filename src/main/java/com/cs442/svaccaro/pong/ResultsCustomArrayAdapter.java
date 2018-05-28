package com.cs442.svaccaro.pong;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class ResultsCustomArrayAdapter extends ArrayAdapter<Result> {

    Bitmap image;

    public ResultsCustomArrayAdapter(Context context, List objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View listitemview = convertView;
        if (listitemview == null) {
            listitemview = LayoutInflater.from(getContext()).inflate(R.layout.results_custom_array_adapter, parent, false);
        }

        Result temp = getItem(position);

        //Setting the image
        ImageView im = (ImageView) listitemview.findViewById(R.id.ResultsCustomArrayAdapter_ImageView);
        String url = temp.getImageurl();
        Picasso.with(getContext()).load(url).centerCrop().resize(500, 500).placeholder(R.drawable.error_image).into(im);

        //Setting the name
        TextView name = (TextView) listitemview.findViewById(R.id.ResultsCustomArrayAdapter_Name_TextView);
        name.setText(temp.getName());

        //Setting the rating
        TextView rating = (TextView) listitemview.findViewById(R.id.ResultsCustomArrayAdapter_Rating_TextView);
        rating.setText(temp.getPegi());

        //Setting the release date
        TextView release = (TextView) listitemview.findViewById(R.id.ResultsCustomArrayAdapter_ReleaseDate_TextView);
        release.setText(temp.getReleasedate());

        //Setting the console type
        TextView console = (TextView) listitemview.findViewById(R.id.ResultsCustomArrayAdapter_Console_TextView);
        console.setText(temp.getConsole());

        return listitemview;
    }
}
