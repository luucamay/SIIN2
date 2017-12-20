package com.example.lupe.siin;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class SplashScreenActivity extends AppCompatActivity {

    private ImageView logoImageView;
    private static int SPLASH_TIME_OUT = 1000;
    Animation animFadein;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);
        logoImageView = (ImageView) findViewById(R.id.logoImageView);

        animFadein = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.fade_in);
        logoImageView.startAnimation(animFadein);
        logoImageView = (ImageView) findViewById(R.id.background);
        logoImageView.startAnimation(animFadein);

        animFadein.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        new Handler().postDelayed(new Runnable() {


            @Override
            public void run() {

                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);

                finish();
            }
        }, SPLASH_TIME_OUT);
    }

}