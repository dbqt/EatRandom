package com.davidbqtran.justeatrandom;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import com.google.android.gms.ads.MobileAds;

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_splash);
        MobileAds.initialize(getApplicationContext(), "ca-app-pub-5954594840677307~6923392474");
        anime();
    }


    private void anime(){
        findViewById(R.id.textView2).setAlpha(0);
        findViewById(R.id.button2).setAlpha(0);
        findViewById(R.id.textView2).animate().alpha(1).setDuration(2000).setStartDelay(1000);
        findViewById(R.id.button2).animate().alpha(1).setDuration(1500).setStartDelay(4000);
    }

    public void LoadNext(View v){
        Intent i = new Intent(this, SetupPage.class);
        startActivity(i);
    }
}
