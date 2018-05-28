package com.cs442.svaccaro.pong;


import android.content.Context;
import android.util.Log;

import com.android.volley.VolleyError;
import com.igdb.api_android_java.callback.onSuccessCallback;
import com.igdb.api_android_java.model.APIWrapper;
import com.igdb.api_android_java.model.Parameters;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Queryutils {

    static ArrayList<Result> gameslist;
    /**
     * Create a private constructor because no one should ever create a QueryUtils object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private Queryutils() {
    }

    public static ArrayList<Result> SearchGames(final Context context, String keyword) {

        //Key is included
        APIWrapper wrapper = new APIWrapper(context, "be4df5276af9b9763300d5b82d7310ba");
        Parameters params = new Parameters();

        params.addSearch(keyword);
        params.addFields("*");
        params.addFilter("[external.steam][exists]");
        params.addFilter("[release_dates.platform][any]=49,48,6");
        params.addFilter("[release_dates.date][lt]=2017-11-30");
        params.addOrder("popularity:desc");
        //params.addLimit("5");

        wrapper.search(APIWrapper.Endpoint.GAMES, params, new onSuccessCallback() {
            @Override
            public void onSuccess(JSONArray jarray)
            {
                gameslist = extractFeatureFromJson(jarray);
            }

            @Override
            public void onError(VolleyError error)
            {
                Log.d("Error", "Didnt query at all --------------------- ");
            }
        });
        try
        {
            Thread.sleep(1500);
        }
        catch (InterruptedException e)
        {
            //handle
        }
        //  Log.d("GAMELIST SIZE", gameslist.size() + " ---------------------------");
        return gameslist;
    }

    public static ArrayList<Result> extractFeatureFromJson(JSONArray jarray) {
        String imageurl;
        String websiteurl = "";
        String pegi;
        String fullreleasedate;
        String console = "";
        String summary = "";
        double userrating = 0.0;
        double criticrating = 0.0;
        long steamid = 0;
        boolean flag = false;

        //empty ArrayList
        ArrayList<Result> games = new ArrayList<>();

        try
        {
            for (int i = 0; i < jarray.length(); i++)
            {
                JSONObject mainobject = jarray.optJSONObject(i);

                //Parsing the game's name
                String name = mainobject.optString("name");
                Log.d("Name: ", name + "  --------------------------------------------------");

                //Parsing the game's official URL
                if (mainobject.has("websites"))
                {
                    JSONArray websitesarray = mainobject.getJSONArray("websites");
                    for (int k = 0; k < websitesarray.length(); k++)
                    {
                        JSONObject websitesobject = websitesarray.optJSONObject(k);
                        int websitevalue = websitesobject.optInt("category");
                        if (websitevalue == 1)
                        {
                            flag = true;
                            websiteurl = websitesobject.optString("url");
                            Log.d("WEBSITE URL: ", websiteurl + " -----------------------------------");
                            break;
                        }
                    }

                }

                //Parsing the image URL
                if (mainobject.has("cover"))
                {
                    JSONObject imageobject = mainobject.optJSONObject("cover");
                    imageurl = imageobject.optString("url");
                    if (!imageurl.contains("http")) {
                        imageurl = "https:" + imageurl;
                    }
                } else
                {
                    imageurl = "https://developers.google.com/maps/documentation/streetview/images/error-image-generic.png";
                }
                Log.d("Image URL: ", imageurl + " -----------------------------------");

                //Parsing the game's PEGI (Pan Equropean Game Information) rating
                if (mainobject.has("pegi"))
                {
                    JSONObject pegiobject = mainobject.optJSONObject("pegi");
                    pegi = "PEGI " + getrating(pegiobject.optInt("rating"));
                } else {
                    pegi = "PEGI --";

                }
                Log.d("PEGI: ", pegi + "  --------------------------------------------------");


                //Parsing the game's release date
                if (mainobject.has("first_release_date")) {
                    Long releasedate = mainobject.optLong("first_release_date");
                    Date dateobj = new Date(releasedate);
                    SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy");
                    fullreleasedate = dateFormat.format(dateobj);
                } else {
                    fullreleasedate = "--";
                }
                Log.d("first_release_date: ", fullreleasedate + "  --------------------------------------------------");


                //Parsing the console type
                if (mainobject.has("release_dates")) {
                    JSONArray releasedatesarray = mainobject.getJSONArray("release_dates");
                    for (int j = 0; j < releasedatesarray.length(); j++) {
                        JSONObject releasedatesobject = releasedatesarray.getJSONObject(j);
                        int platformnumber = releasedatesobject.optInt("platform");
                        //   Log.d("PLATFORM NUMBER: ", releasedatesobject.optInt("platform") + "----------------------------");
                        if (platformnumber == 49 || platformnumber == 48 || platformnumber == 6) {
                            String platfrom = getConsole(platformnumber);
                            if (!console.contains(platfrom)) {
                                console = console + platfrom + ", ";
                            }
                        }
                    }
                }
                Log.d("console: ", console + "  --------------------------------------------------");


                //Parsing the games summary
                if (mainobject.has("summary")) {
                    summary = mainobject.optString("summary");
                }
                Log.d("summary: ", summary + "  --------------------------------------------------");

                //Parsing the game's rating
                if (mainobject.has("rating")) {
                    userrating = mainobject.optDouble("rating");
                }
                Log.d("userrating: ", userrating + "  --------------------------------------------------");


                //Parsing the game's rating
                if (mainobject.has("aggregated_rating")) {
                    criticrating = mainobject.optDouble("aggregated_rating");
                }

                //The steam id
                if (mainobject.has("external")) {
                    JSONObject steamidobject = mainobject.optJSONObject("external");
                    steamid = Long.parseLong(steamidobject.optString("steam"));
                    Log.d("STEAM iD: ", steamid + " ----------------------------------------");
                } else
                {
                    flag = false;
                }

                int gameIDGot = mainobject.getInt("id");

                //Pasing the game's genre
                if (flag && !console.isEmpty())
                {
                    games.add(new Result(name, fullreleasedate, websiteurl, pegi, imageurl, console.substring(0, console.length() - 2), summary, steamid, criticrating, userrating,gameIDGot));
                }
                else if(!console.isEmpty())
                {
                    games.add(new Result(name, fullreleasedate, websiteurl, pegi, imageurl, console.substring(0, console.length() - 2), summary, -1, criticrating, userrating,gameIDGot));
                }
                else
                {
                    games.add(new Result(name, fullreleasedate, websiteurl, pegi, imageurl, console.substring(0, console.length() - 2), summary, -1, criticrating, userrating,gameIDGot));
                }
                console = "";
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return games;
    }

    public static int getrating(int pegi) {
        Log.d("getRating ",  " -----------------------------------");
        int finalrating;
        switch (pegi) {
            case 1:
                finalrating = 3;
                break;
            case 2:
                finalrating = 7;
                break;
            case 3:
                finalrating = 12;
                break;
            case 4:
                finalrating = 16;
                break;
            case 5:
                finalrating = 18;
                break;
            default:
                finalrating = 0;

        }
        return finalrating;
    }

    public static String getConsole(int number) {
        String console = "lol";
        switch (number) {
            case 48:
                console = "PS4";
                break;
            case 49:
                console = "XBOX";
                break;
            case 6:
                console = "PC";
                break;
        }
        return console;
    }
}


