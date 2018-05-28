package com.cs442.svaccaro.pong;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.ArrayList;

public class ResultLoader extends AsyncTaskLoader<ArrayList<Result>> {

    Context context;
    String keyword;

    public ResultLoader(Context context, String keyword) {
        super(context);
        this.context = context;
        this.keyword = keyword;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public ArrayList<Result> loadInBackground() {

        // Perform a HTTP request for game data and process the response.
        ArrayList<Result> games = Queryutils.SearchGames(context, keyword);
        return games;
    }

}
