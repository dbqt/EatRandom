package com.davidbqtran.justeatrandom;

import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.davidbqtran.justeatrandom.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ResultPage extends AppCompatActivity {

    HashSet<Integer> checkedPlace;

    String json = "";

    private final String googleMapKey = "AIzaSyBFhlkw7idNfIZ2Y22XWoKH4GY8yiE4o8o";

    private AdView mAdView;

    private Intent mapIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_page);

        Bundle b = getIntent().getExtras();
        if(b != null){
            json = b.getString("json");

            checkedPlace = new HashSet();
            UpdateFields();



            //prepareListData();

        } else {
            Toast.makeText(this, "Failed to get result, please try again",
                    Toast.LENGTH_SHORT).show();

            findViewById(R.id.View).setEnabled(false);
        }

        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    public void GetNextRandom(View v) {
        UpdateFields();
    }

    private void UpdateFields() {
        try {

            JSONObject obj = new JSONObject(this.json);
            int nb = obj.getInt("total");
            if (checkedPlace.size() >= nb) {
                //all possibility checked
                //Toast.makeText(this, "I showed you everything, please choose one...",Toast.LENGTH_LONG).show();

                checkedPlace.clear();
            }

            JSONArray arr = obj.getJSONArray("businesses");
            Random random = new Random();
            int index = random.nextInt(arr.length());

            // save random number to avoid getting it again
            while(checkedPlace.contains(index)){
                index = random.nextInt(arr.length());
            }
            checkedPlace.add(index);

            JSONObject elem = arr.getJSONObject(index);
            String name = elem.getString("name");
            String phone = elem.getString("display_phone");
            JSONObject jsonLocation = elem.getJSONObject("location");
            String jsonAddress = jsonLocation.getJSONArray("display_address").toString();
            JSONObject coordinates = elem.getJSONObject("coordinates");
            String lon = coordinates.getString("longitude");
            String lat = coordinates.getString("latitude");
            String img = elem.getString("image_url");
            Log.i("Yelp", "Place: "+name+" lon="+lon+" lat"+lat );

            ((TextView) findViewById(R.id.NAme)).setText("Go here: "+name);
            TextView tv = ((TextView) findViewById(R.id.addressText));
            String address = "";
            try {

                JSONArray o = new JSONArray(jsonAddress);
                for(int i = 0; i < o.length(); i++) {
                    address += o.getString(i);
                    address += "\n";
                }

                address += "\n";
                address += phone;
                tv.setText(address);

                Picasso.with(this).load(img).into((ImageView)findViewById(R.id.imageResto));

                findViewById(R.id.View).setEnabled(CanOpenExternalMap(name, lat, lon));
            } catch (Exception e) {
                Log.i("google", "address broken");
            }

        } catch (Exception e) {
            Log.i("eoore", e.getMessage());
        } catch (Throwable t) {
            Log.i("Yelp", "Could not parse malformed JSON: \"" + json + "\"");
        }

        findViewById(R.id.Next).setEnabled(false);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                findViewById(R.id.Next).setEnabled(true);
            }
        }, 500);
    }

    public void ViewOnExternal(View v) {
        startActivity(mapIntent);
    }

    public void Back(View v){
        Intent i = new Intent(this, SetupPage.class);
        startActivity(i);
    }

    private boolean CanOpenExternalMap(String name, String lat, String lon){
        try {
            String query = URLEncoder.encode(name, "utf-8");

            Uri gmmIntentUri = Uri.parse("geo:" + lat + "," + lon +"?q="+query);
            mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
        } catch (Exception e){
            Log.i("error", "google map error");
        }
        if (mapIntent.resolveActivity(getPackageManager()) != null) {
            return true;
        }
        return false;
    }
}
