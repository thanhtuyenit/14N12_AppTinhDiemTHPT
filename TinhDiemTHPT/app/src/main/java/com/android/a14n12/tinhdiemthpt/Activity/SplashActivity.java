package com.android.a14n12.tinhdiemthpt.Activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.a14n12.tinhdiemthpt.R;

public class SplashActivity extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 3000;
    private LinearLayout lnSplash;
//    private PREFS prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
//        prefs = PREFS.getInstance(this);
        lnSplash =  findViewById(R.id.ln_splash);

        Animation animSplash = AnimationUtils.loadAnimation(this,R.anim.anim_splash );
        lnSplash.startAnimation(animSplash);

        new Handler().postDelayed(new Runnable() {


            @Override
            public void run() {
                Intent i;
//                if (prefs.getStringParams(CONST.USER_ID) =="") {
//
//                    i = new Intent(SplashActivity.this, LoginActivity.class);
//                    Log.d("SplashActivity ", "run:  go to login");
//                } else {
                    i = new Intent(SplashActivity.this, MainActivity.class);
                    Log.d("SplashActivity ", "run: go to main " );
//                }

                startActivity(i);
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}
