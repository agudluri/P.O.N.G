package com.cs442.svaccaro.pong;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;

public class ResultsInfo extends AppCompatActivity {

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.results_info);

        Bundle data = getIntent().getExtras();
        final Result result = (Result) data.getParcelable("object");

        //Image
        ImageView image = (ImageView) findViewById(R.id.ResultsInfo_ImageView);
        String url = result.getImageurl();
        Picasso.with(this).load(url).centerCrop().resize(500, 500).placeholder(R.drawable.error_image).into(image);

        //Name
        TextView name = (TextView) findViewById(R.id.ResultsInfo_Name_TextView);
        name.setText(result.getName());

        //Pegi
        TextView pegi = (TextView) findViewById(R.id.ResultsInfo_Rating_TextView);
        pegi.setText(result.getPegi());

        //Release Date
        TextView release = (TextView) findViewById(R.id.ResultsInfo_ReleaseDate_TextView);
        release.setText(result.getReleasedate());

        //Console
        TextView console = (TextView) findViewById(R.id.ResultsInfo_Console_TextView);
        console.setText(result.getConsole());

        //User Rating
        TextView userrating = (TextView) findViewById(R.id.ResultsInfo_UserRating_TextView);
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        userrating.setText(decimalFormat.format(result.getRating()));

        //Critics Rating
        TextView criticsrating = (TextView) findViewById(R.id.ResultsInfo_CriticRating_TextView);
        criticsrating.setText(decimalFormat.format(result.getCriticrating()));

        //Summary
        TextView summary = (TextView) findViewById(R.id.ResultsInfo_Summary_TextView);
        summary.setText(result.getSummary());

        //Official site button
        Button sitebutton = (Button) findViewById(R.id.ResultsInfo_Official_Site);
        sitebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(result.getWebsiteurl()));
                startActivity(intent);
            }
        });

        //Steam site
        Button steambutton = (Button) findViewById(R.id.ResultsInfo_Steam_Site);
        steambutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(result.getSteamid()==-1)
                {
                    Toast.makeText(ResultsInfo.this,"This game is unvailable on Steam at this time.",Toast.LENGTH_LONG).show();
                }
                else
                {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://store.steampowered.com/app/" + result.getSteamid()));
                    startActivity(intent);
                }
            }
        });
    }
}
