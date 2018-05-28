package com.cs442.svaccaro.pong;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ToggleButton;

import com.android.volley.VolleyError;
import com.igdb.api_android_java.callback.onSuccessCallback;
import com.igdb.api_android_java.model.APIWrapper;
import com.igdb.api_android_java.model.Parameters;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;


public class MainPage extends AppCompatActivity
{

    String MY_LOG="MainPage";
    String Recent [] = new String[10];
    ListView recentTop;
    ToggleButton TopRecent;
    TopGamesAdapter tga;
    ArrayList<Game> TopRated; //Stores TopRated For Search
    ArrayList<String>TopRatedGameNames;
    ArrayList<Game> SavedSearches; //Stores Saved Searches
    ArrayList<String> Genres ;
    String name;
    int id=-1;
    int gameID;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final DBHandler dbh = new DBHandler(this);
        //Which List to Display
        TopRecent = (ToggleButton) findViewById(R.id.Recent_Popular);
        Boolean initialDisplay = TopRecent.isChecked();
        if(initialDisplay==false)
        {
            displayTop();
        }
        else
        {
            displayRecent();
        }
        getGenres();
        //Test for Toggle Top/Recent
        TopRecent.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if (isChecked)
                {
                    // Recent Searches
                    Log.i(MY_LOG, "Display Recent");
                    displayRecent();
                }
                else
                {
                    // Popular Searches
                    Log.i(MY_LOG, "Display Top Rated");
                    displayTop();
                }
            }
        });

        //Launch Search
        Button ToSearch = (Button)findViewById(R.id.SearchLaunch);
        ToSearch.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                EditText toSearch = (EditText) findViewById(R.id.SearchBar);
                String keywords=toSearch.getText().toString();
                Intent LaunchSearch=new Intent(MainPage.this,ResultsActivity.class);
                LaunchSearch.putExtra("input",keywords);
                startActivity(LaunchSearch);
            }
        });

        ListView gameClicked= (ListView) findViewById(R.id.RecentPopList);
        gameClicked.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l)
            {
                Log.i(MY_LOG, "GAME IN LIST CLICKED");
                gameID=-1;
                String GameName="";
                Boolean displayCheck = TopRecent.isChecked();
                if(displayCheck==false)
                {
                    gameID=TopRated.get(position).getDbID();
                    GameName=TopRated.get(position).getGamename();
                }
                else
                {
                    gameID=SavedSearches.get(position).getDbID();
                    GameName=SavedSearches.get(position).getGamename();
                }

                //SEARCH FOR SPECIFIC GAME HERE
                if(gameID != -1)
                {
                    Log.i(MY_LOG, "Searching for Game: "+gameID+" "+GameName);
                    APIWrapper specificGameWrapper = new APIWrapper(MainPage.this, "5f1a0b504195e4cc15ff80a4547695ef");
                    String searchParam = "/games/"+gameID+"?fields=*";
                    Log.i(MY_LOG, "Query: "+searchParam);
                    Parameters gameToPull = new Parameters()
                            .addFilter(searchParam)
                            .addIds(String.valueOf(gameID));
                    specificGameWrapper.search(APIWrapper.Endpoint.GAMES, gameToPull, new onSuccessCallback()
                    {
                        @Override
                        public void onSuccess(JSONArray result)
                        {
                            try
                            {

                                Log.i(MY_LOG, "Getting Game as Json");
                                ArrayList<Result> toSend=Queryutils.extractFeatureFromJson(result);
                                Result r = toSend.get(0);

                                JSONObject jsonobject = result.getJSONObject(0);
                                Log.i(MY_LOG, "Json: " + jsonobject.toString());

                                String gameIDGot = jsonobject.getString("id");
                                Log.i(MY_LOG, "ID: " + gameIDGot);

                                String gameName = jsonobject.getString("name");
                                Log.i(MY_LOG, "Name: " + gameName);

                                JSONArray genreArray = jsonobject.getJSONArray("genres");
                                String genre=Genres.get(Integer.parseInt(genreArray.get(0).toString()));
                                for(int i = 1;i<genreArray.length();i++)
                                {
                                    genre=genre+";"+Genres.get(Integer.parseInt(genreArray.get(i).toString()));
                                }
                                Log.i(MY_LOG, "Genre: " + genre);

                                String releaseyr = jsonobject.getString("first_release_date");
                                long unixSeconds = 1372339860;
                                Date date = new Date(unixSeconds*1000L); // *1000 is to convert seconds to milliseconds
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z"); // the format of your date
                                sdf.setTimeZone(TimeZone.getTimeZone("GMT-4")); // give a timezone reference for formating (see comment at the bottom
                                String formattedDate = sdf.format(date);
                                Log.i(MY_LOG, "Release Year: " + formattedDate);

                                String esrbRate="-1";
                                if(jsonobject.has("esrb"))
                                {
                                    JSONObject ESRBObj=jsonobject.getJSONObject("esrb");
                                    esrbRate=ESRBObj.getString("rating");
                                }
                                Log.i(MY_LOG, "ESRB: " + esrbRate);
                                int esrbRating = Integer.parseInt(esrbRate);

                                Game pulled = new Game();
                                pulled.setDbID(Integer.parseInt(gameIDGot));
                                pulled.setGamename(gameName);
                                pulled.setGenre(genre);
                                pulled.setReleaseyr(formattedDate);
                                pulled.setEsrbRating(esrbRating);
                                dbh.addSave(pulled);
                                Log.i(MY_LOG, "Got Requested Game Data");
                                dbh.addSave(pulled);
                                Intent DisplayPulled = new Intent(MainPage.this, ResultsInfo.class);
                                DisplayPulled.putExtra("object",r);
                                startActivity(DisplayPulled);
                            }
                            catch (JSONException je)
                            {
                                Log.i(MY_LOG, "Error Reading Game Values");
                            }
                        }

                        @Override
                        public void onError(VolleyError error) {
                            Log.i(MY_LOG, "Could not Get Game Requested");
                        }
                    });

                }
             }
        });
    }


    //Display Recent Searches
    public void displayRecent()
    {
        Log.i(MY_LOG,"Setting Recent Adapter");
        DBHandler dbh= new DBHandler(this);
        //Added 2 lines
        SavedSearches = new ArrayList<>();
        SavedSearches = dbh.getSavedGames();
        ArrayList<String> recent= dbh.getSaves();
        if(recent.size()==0)
        {
            recent.add("No Recent Saves Available");
        }
        recentTop= (ListView) findViewById(R.id.RecentPopList);
        RecentGamesAdapter rga = new RecentGamesAdapter(MainPage.this, 0, recent);
        recentTop.setAdapter(rga);
    }

    //Display Top Rated Searches
    public void displayTop()
    {
        DBHandler dbh=new DBHandler(this);
        APIWrapper wrapper = new APIWrapper(this, "5f1a0b504195e4cc15ff80a4547695ef");
        Log.i(MY_LOG,"Getting Popular Rated");
        TopRated=new ArrayList<>();
        TopRatedGameNames=new ArrayList<>();
        Parameters params = new Parameters()
                .addFilter("/games/?fields=name,id,popularity&order=popularity:desc")
                .addFilter("[release_dates.platform][any]=49,48,6")
                .addFilter("[external.steam][exists]")
                .addLimit("10");

        wrapper.search(APIWrapper.Endpoint.GAMES, params, new onSuccessCallback(){
            @Override
            public void onSuccess(JSONArray result)
            {
                // JSONArray containing Top 10 Games
                for (int i = 0; i < result.length(); i++)
                {
                    Game g = new Game();
                    try
                    {
                        JSONObject jsonobject = result.getJSONObject(i);
                        Log.i(MY_LOG,"Json:"+jsonobject.toString());
                        String gameName= jsonobject.getString("name");
                        Log.i(MY_LOG,"Name:"+gameName);
                        String gameID = jsonobject.getString("id");
                        Log.i(MY_LOG,"ID:"+gameID);
                        String pop=jsonobject.getString("popularity");
                        Log.i(MY_LOG,"Pop:"+pop);
                        g.setDbID(Integer.parseInt(gameID));
                        g.setGamename(gameName);
                        TopRated.add(g);
                        TopRatedGameNames.add(gameName);
                    }
                    catch(JSONException je)
                    {
                        Log.i(MY_LOG,"Error Reading Json");
                    }
                    Log.i(MY_LOG,"Setting Top Rated Adapter");
                    recentTop= (ListView) findViewById(R.id.RecentPopList);
                    tga = new TopGamesAdapter(MainPage.this, 0, TopRatedGameNames);
                    recentTop.setAdapter(tga);
                }
            }

            @Override
            public void onError(VolleyError error)
            {
                Log.i(MY_LOG,"Could not Get Top Searches");
            }
        });
        dbh.addTopRated(TopRated); //Save to DB
    }

    public void getGenres()
    {
        name="";
        Genres = new ArrayList<>();
        for(int a=0;a<50;a++)
        {
            Genres.add("Genre Not Available");
        }
        APIWrapper wrapper = new APIWrapper(this, "5f1a0b504195e4cc15ff80a4547695ef");
        Log.i(MY_LOG,"Getting Genres");
            Parameters params = new Parameters()
                    .addLimit("50");
            wrapper.genres(params, new onSuccessCallback(){
                @Override
                public void onSuccess(JSONArray result)
                {
                    for(int i = 0; i<result.length();i++)
                    {
                        try
                        {
                            Log.i(MY_LOG, "Getting Genre");
                            JSONObject jsonobject = result.getJSONObject(i);
                            name = jsonobject.getString("name");
                            id=Integer.parseInt(jsonobject.getString("id"));
                            Log.i(MY_LOG, "Genre: " + name);
                        }
                        catch (JSONException je) {
                            Log.i(MY_LOG, "Error Reading Genre Json");
                        }
                        Genres.set(id,name);
                    }
                }
                @Override
                public void onError(VolleyError error)
                {
                    Log.i(MY_LOG,"Could not Get Genre");
                }
            });
    }
}
