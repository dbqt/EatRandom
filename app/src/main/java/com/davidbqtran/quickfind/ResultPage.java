package com.davidbqtran.quickfind;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ResultPage extends AppCompatActivity {

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;

    String lat = "", lon = "", name = "", olocation = "", phone = "", jsonAddress = "";

    private final String googleMapKey = "AIzaSyBFhlkw7idNfIZ2Y22XWoKH4GY8yiE4o8o";

    private Intent mapIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_page);

        Bundle b = getIntent().getExtras();
        if(b != null){
            lon = b.getString("longitude");
            lat = b.getString("latitude");
            olocation = b.getString("olocation");
            name = b.getString("name");
            phone = b.getString("phone");
            jsonAddress = b.getString("jsonAddress");
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
            } catch (Exception e) {
                Log.i("google", "address broken");
            }


            findViewById(R.id.View).setEnabled(CanOpenExternalMap());
            //prepareListData();

        } else {
            Toast.makeText(this, "Failed to get result, please try again",
                    Toast.LENGTH_SHORT).show();

            findViewById(R.id.View).setEnabled(false);
        }
    }

    public void ViewOnExternal(View v) {
        startActivity(mapIntent);
    }

    public void Back(View v){
        Intent i = new Intent(this, SetupPage.class);
        startActivity(i);
    }

    private boolean CanOpenExternalMap(){
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

    private void GetDirecions(){
        OkHttpClient client = new OkHttpClient();

        String finalUrl = "https://maps.googleapis.com/maps/api/directions/json?origin="+olocation+"&destination="+lat+","+lon+"&key="+googleMapKey;
        Request request = new Request.Builder()
                .url(finalUrl)
                .get()
                .addHeader("content-type", "application/x-www-form-urlencoded")
                .addHeader("cache-control", "no-cache")
                .build();
/*
        try {
            Response response = client.newCall(request).execute();


            String json = response.body().string();
            //Log.i("Yelp", "Yelp Response: " + json);
            try {

                JSONObject obj = new JSONObject(json);
                AccessToken = obj.getString("access_token");
                Log.i("Yelp", "token: " + AccessToken);

            } catch (Throwable t) {
                Log.i("Yelp", "Could not parse malformed JSON: \"" + json + "\"");
            }
        } catch(Exception e) {
            Log.i("Yelp", "Error " + e.toString());
        }*/
    }

    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        // Adding child data
        listDataHeader.add("Top 250");
        listDataHeader.add("Now Showing");
        listDataHeader.add("Coming Soon..");

        // Adding child data
        List<String> top250 = new ArrayList<String>();
        top250.add("The Shawshank Redemption");
        top250.add("The Godfather");
        top250.add("The Godfather: Part II");
        top250.add("Pulp Fiction");
        top250.add("The Good, the Bad and the Ugly");
        top250.add("The Dark Knight");
        top250.add("12 Angry Men");

        List<String> nowShowing = new ArrayList<String>();
        nowShowing.add("The Conjuring");
        nowShowing.add("Despicable Me 2");
        nowShowing.add("Turbo");
        nowShowing.add("Grown Ups 2");
        nowShowing.add("Red 2");
        nowShowing.add("The Wolverine");

        List<String> comingSoon = new ArrayList<String>();
        comingSoon.add("2 Guns");
        comingSoon.add("The Smurfs 2");
        comingSoon.add("The Spectacular Now");
        comingSoon.add("The Canyons");
        comingSoon.add("Europa Report");

        listDataChild.put(listDataHeader.get(0), top250); // Header, Child data
        listDataChild.put(listDataHeader.get(1), nowShowing);
        listDataChild.put(listDataHeader.get(2), comingSoon);
    }
}
