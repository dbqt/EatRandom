package com.davidbqtran.quickfind;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_splash);
        anime();
    }


    private void anime(){
        findViewById(R.id.textView2).setAlpha(0);
        findViewById(R.id.button2).setAlpha(0);
        findViewById(R.id.textView2).animate().alpha(1).setDuration(4000).setStartDelay(1000);
        findViewById(R.id.button2).animate().alpha(1).setDuration(1500).setStartDelay(6000);
    }

    public void LoadNext(View v){
        Intent i = new Intent(this, SetupPage.class);
        startActivity(i);
    }
}
