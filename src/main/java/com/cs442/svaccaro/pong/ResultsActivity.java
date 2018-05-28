package com.cs442.svaccaro.pong;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cs442.svaccaro.pong.comparators.NameComparator;
import com.cs442.svaccaro.pong.comparators.NewestFirstDateComparator;
import com.cs442.svaccaro.pong.comparators.NewestLastDateComparator;

import java.util.ArrayList;
import java.util.Collections;

public class ResultsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<Result>> {

    private ResultsCustomArrayAdapter mAdapter;
    private TextView mEmptyStateTextView;
    private ProgressBar pro;
    String input;
    ListView listView;
    //
    ArrayList<Result> listGames;
    Button filterButton;
    Button sortButton;
    ArrayList<Result> temp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.results_activity);

        //Getting the input from the previous activity
        Bundle b = getIntent().getExtras();
        input = b.getString("input");
        listGames = new ArrayList<>();
        listView = (ListView) findViewById(R.id.ResultsActivity_list_view);
        mAdapter = new ResultsCustomArrayAdapter(this,listGames);

        //Setting the adapter
        listView.setAdapter(mAdapter);
        filterButton = (Button) findViewById(R.id.bt_filter);
        sortButton = (Button) findViewById(R.id.bt_sort);

        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listGames.size() > 0) {
                    PopupMenu popup = new PopupMenu(ResultsActivity.this, view);
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            updateList(item.toString());
                            return false;
                        }
                    });

                    Menu menu = popup.getMenu();
                    int i = 0;
                    menu.add(Menu.NONE, (i + 1), (i + 1), "Rating > 50");
                    menu.add(Menu.NONE, (i + 1), (i + 1), "Only PC Games");
                    menu.add(Menu.NONE, (i + 1), (i + 1), "All PC Games");
                    menu.add(Menu.NONE, (i + 1), (i + 1), "All PS4 Games");
                    menu.add(Menu.NONE, (i + 1), (i + 1), "All XBOX Games");
                    menu.add(Menu.NONE, (i + 1), (i + 1), "Remove Filters");

                    popup.show();
                }
            }
        });

        sortButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listGames.size() > 0) {
                    PopupMenu popup = new PopupMenu(ResultsActivity.this, view);
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            updateList(item.toString());
                            return false;
                        }
                    });

                    Menu menu = popup.getMenu();
                    menu.add(Menu.NONE, 0, 0, "Sort By :");
                    menu.add(Menu.NONE, 0, 0, "Newest First");
                    menu.add(Menu.NONE, 0, 0, "Newest Last");
                    menu.add(Menu.NONE, 0, 0, "Alphabetically");

                    popup.show();
                }
            }
        });
        //OnItemClickListener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Result temp = mAdapter.getItem(i);
                Game toSave = new Game();
                toSave.setGamename(temp.getName());
                toSave.setDbID(temp.getDbID());
                DBHandler dbh = new DBHandler(ResultsActivity.this);
                dbh.addSave(toSave);
                Intent intent = new Intent(ResultsActivity.this, ResultsInfo.class);
                intent.putExtra("object", temp);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });

        mEmptyStateTextView = (TextView) findViewById(R.id.ResultsActivity_empty_view);
        listView.setEmptyView(mEmptyStateTextView);

        pro = (ProgressBar) findViewById(R.id.ResultsActivity_progress_bar);

        //Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager)
                this.getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {

            // Initialize the loader. Pass in the int ID constant and pass in null for
            // the bundle. Pass in 'this' activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            getLoaderManager().initLoader(0, null, this);
        } else {
            // Otherwise, display error

            // First, hide loading indicator so error message will be visible
            pro.setVisibility(View.GONE);

            // Update empty state with no connection error message
            mEmptyStateTextView.setText(R.string.no_internet);
        }

    }

    private void updateList(String item) {
        if (item.equals("Alphabetically")) {

            if(temp!=null && temp.size()>0){
                Collections.sort(temp, new NameComparator());
                mAdapter = new ResultsCustomArrayAdapter(ResultsActivity.this, temp);
                listView.setAdapter(mAdapter);
            }else if (null != listGames && listGames.size() > 0){
                Collections.sort(listGames, new NameComparator());
                mAdapter = new ResultsCustomArrayAdapter(ResultsActivity.this, listGames);
                listView.setAdapter(mAdapter);
            }
        } else if (item.equals("Newest First")) {

            if(temp!=null && temp.size()>0){
                Collections.sort(temp, new NewestFirstDateComparator());
                mAdapter = new ResultsCustomArrayAdapter(ResultsActivity.this, temp);
                listView.setAdapter(mAdapter);
            }else if (null != listGames && listGames.size() > 0){
                Collections.sort(listGames, new NewestFirstDateComparator());
                mAdapter = new ResultsCustomArrayAdapter(ResultsActivity.this, listGames);
                listView.setAdapter(mAdapter);
            }
        } else if (item.equals("Newest Last")) {
            if(temp!=null && temp.size()>0){
                Collections.sort(temp, new NewestLastDateComparator());
                mAdapter = new ResultsCustomArrayAdapter(ResultsActivity.this, temp);
                listView.setAdapter(mAdapter);
            }else if (null != listGames && listGames.size() > 0){
                Collections.sort(listGames, new NewestLastDateComparator());
                mAdapter = new ResultsCustomArrayAdapter(ResultsActivity.this, listGames);
                listView.setAdapter(mAdapter);
            }
        } else if (item.equals("Rating > 50")) {
            temp = new ArrayList<>();
            for (Result game : listGames) {
                if (game.getRating() < 49.0) {

                } else {
                    temp.add(game);
                }

            }
            mAdapter = new ResultsCustomArrayAdapter(ResultsActivity.this, temp);
            listView.setAdapter(mAdapter);
        } else if (item.equals("All PC Games")) {
            temp = new ArrayList<>();
            for (Result game : listGames) {
                if (null != game.getConsole() && game.getConsole().contains("PC")) {
                    temp.add(game);
                }
            }
            mAdapter = new ResultsCustomArrayAdapter(ResultsActivity.this, temp);
            listView.setAdapter(mAdapter);
        }else if (item.equals("Only PC Games")) {
            temp = new ArrayList<>();
            for (Result game : listGames) {
                if (null != game.getConsole() && game.getConsole().equals("PC")) {
                    temp.add(game);
                }
            }
            mAdapter = new ResultsCustomArrayAdapter(ResultsActivity.this, temp);
            listView.setAdapter(mAdapter);
        }else if (item.equals("All PS4 Games")) {
            temp = new ArrayList<>();
            for (Result game : listGames) {
                if (null != game.getConsole() && game.getConsole().contains("PS4")) {
                    temp.add(game);
                }
            }
            if (temp.size() == 0) {
                //couldNotLoad.setVisibility(View.VISIBLE);
            } else {
                //couldNotLoad.setVisibility(View.GONE);
            }
            mAdapter = new ResultsCustomArrayAdapter(ResultsActivity.this, temp);
            listView.setAdapter(mAdapter);
        }else if (item.equals("All XBOX Games")) {
            temp = new ArrayList<>();
            for (Result game : listGames) {
                if (null != game.getConsole() && game.getConsole().contains("XBOX")) {
                    temp.add(game);
                }
            }
            mAdapter = new ResultsCustomArrayAdapter(ResultsActivity.this, temp);
            listView.setAdapter(mAdapter);
        } else if (item.equals("Remove Filters")) {
            //couldNotLoad.setVisibility(View.GONE);
            mAdapter = new ResultsCustomArrayAdapter(ResultsActivity.this, listGames);
            listView.setAdapter(mAdapter);
        }

    }

    //----------------------------------------------- loader --------------------------------------------------
    @Override
    public Loader<ArrayList<Result>> onCreateLoader(int id, Bundle args) {
        Log.d("RESULTSACTIVITY", input + " --------------------------------------");
        return new ResultLoader(this, input);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Result>> loader, ArrayList<Result> data) {

        // Clear the adapter of previous data
        mAdapter.clear();

        // If there is a valid list of games, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (data != null && !data.isEmpty()) {
            listGames = data;
            mAdapter.addAll(listGames);
        }

        Log.d("Debug", "Data is empty!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        mEmptyStateTextView.setText("No Games Found.");
        pro.setVisibility(View.GONE);
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Result>> loader) {
        mAdapter.clear();
    }

}


