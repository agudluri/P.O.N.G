package com.cs442.svaccaro.pong;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;

public class TopGamesAdapter extends ArrayAdapter<String>
{
    private ArrayList<String> top10;
    private Context Context;

    public TopGamesAdapter(Context context, int resource, ArrayList<String> top)
    {
        super(context, resource, top);
        this.Context = context;
        this.top10 = top;
    }

    //Custom View Set-Up
    public View getView(int position, View convertView, ViewGroup parent)
    {
        //Get Food
        String game = top10.get(position);

        //Custom View
        LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.top_ten_games_list_layout, null);

        TextView gameName = (TextView) view.findViewById(R.id.gameName);

        //set values
        gameName.setText(game);
        return view;
    }
}
