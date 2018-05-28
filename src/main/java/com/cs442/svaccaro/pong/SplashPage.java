package com.cs442.svaccaro.pong;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SplashPage extends AppCompatActivity
{

    AnimatorSet Lset;
    AnimatorSet Rset;
    AnimatorSet Aset;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_page);

        //animation
        Log.i("Splash","Startin Animation");
        //Get Dims
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getRealSize(size);
        float minX=-25;
        float minY=0;
        float maxX = size.x-1050;
        float maxY = size.y-500;
        Log.i("Splash","MaxX: "+maxX);
        Log.i("Splash","MaxY: "+maxY);

        //Objects to Animate
        TextView PLeft= (TextView)findViewById(R.id.PL);
        TextView PRight= (TextView)findViewById(R.id.PR);
        PLeft.setX(minX);
        PLeft.setY(maxY);

        PRight.setX(maxX);
        PRight.setY(minY);

        //Animator
        int adur=5000;
        ObjectAnimator LU = ObjectAnimator.ofFloat(PLeft,"y",minY);//from Max -> Min
        ObjectAnimator LD = ObjectAnimator.ofFloat(PLeft,"y",maxY);//from Min -> Max
        LU.setDuration(adur);
        LD.setDuration(adur);
        Lset=new AnimatorSet();
        Lset.playSequentially(LU,LD);

        ObjectAnimator RU = ObjectAnimator.ofFloat(PRight,"y",minY);//from Max -> Min
        ObjectAnimator RD = ObjectAnimator.ofFloat(PRight,"y",maxY);//from Min -> Max
        RU.setDuration(adur);
        RD.setDuration(adur);
        Rset=new AnimatorSet();
        Rset.playSequentially(RD,RU);

        Aset= new AnimatorSet();
        Aset.playTogether(Lset,Rset);

        Aset.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                Aset.start();
            }
        });
        Aset.start();

        //Enter Button
        Button enterApp = (Button)findViewById(R.id.Enter);
        enterApp.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                Intent OpenApp=new Intent(SplashPage.this,MainPage.class);
                startActivity(OpenApp);
            }
        });
    }
}
