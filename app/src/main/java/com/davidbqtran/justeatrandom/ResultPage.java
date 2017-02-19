package com.davidbqtran.justeatrandom;

import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.davidbqtran.justeatrandom.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.vision.text.Text;
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

    private String yelpLink = "";

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

    public void BrowserYelpUrl(View v){
        if (!yelpLink.isEmpty()) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(yelpLink));
            startActivity(browserIntent);
        }
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

            Log.i("ONE", " aie : " + elem.toString());

            /*{"name":"Über Cafbar","location":{"state":"QC","city":"Montreal","display_address":["1101 Rue Fleury E","Montreal, QC H2C 2M9","Canada"],"address1":"1101 Rue Fleury E","zip_code":"H2C 2M9","address2":"","address3":"","country":"CA"},"rating":4.5,"phone":"+15143839009","coordinates":{"longitude":-73.659398,"latitude":45.557981},"url":"https:\/\/www.yelp.com\/biz\/%C3%BCber-cafbar-montr%C3%A9al-2?adjust_creative=k6QdreYeqRXHOTHKFkMacQ&utm_campaign=yelp_api_v3&utm_medium=api_v3_business_search&utm_source=k6QdreYeqRXHOTHKFkMacQ","distance":1781.355432438,"image_url":"https:\/\/s3-media4.fl.yelpcdn.com\/bphoto\/rao-3y-B1hajQ0-faV9X_g\/o.jpg","categories":[{"alias":"lounges","title":"Lounges"},{"alias":"coffee","title":"Coffee & Tea"},{"alias":"cocktailbars","title":"Cocktail Bars"}],"display_phone":"+1 514-383-9009","review_count":3,"id":"über-cafbar-montréal-2","price":"$$","is_closed":false}*/

            String name = elem.getString("name");
            String phone = elem.getString("display_phone");
            JSONObject jsonLocation = elem.getJSONObject("location");

            JSONObject coordinates = elem.getJSONObject("coordinates");
            String lon = coordinates.getString("longitude");
            String lat = coordinates.getString("latitude");

            yelpLink = elem.getString("url");

            // setup review count
            int reviewCount = elem.getInt("review_count");
            ((TextView)findViewById(R.id.reviewCount)).setText("Based on "+reviewCount+" reviews");

            // setup review stars
            int rating = ((Double)(elem.getDouble("rating") * 2)).intValue();
            switch (rating) {
                case 0:
                    ((ImageView)findViewById(R.id.yelpStars)).setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.stars_large_0, null));
                    break;
                case 1:
                    ((ImageView)findViewById(R.id.yelpStars)).setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.stars_large_0, null));
                    break;
                case 2:
                    ((ImageView)findViewById(R.id.yelpStars)).setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.stars_large_1, null));
                    break;
                case 3:
                    ((ImageView)findViewById(R.id.yelpStars)).setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.stars_large_1_half, null));
                    break;
                case 4:
                    ((ImageView)findViewById(R.id.yelpStars)).setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.stars_large_2, null));
                    break;
                case 5:
                    ((ImageView)findViewById(R.id.yelpStars)).setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.stars_large_2_half, null));
                    break;
                case 6:
                    ((ImageView)findViewById(R.id.yelpStars)).setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.stars_large_3, null));
                    break;
                case 7:
                    ((ImageView)findViewById(R.id.yelpStars)).setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.stars_large_3_half, null));
                    break;
                case 8:
                    ((ImageView)findViewById(R.id.yelpStars)).setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.stars_large_4, null));
                    break;
                case 9:
                    ((ImageView)findViewById(R.id.yelpStars)).setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.stars_large_4_half, null));
                    break;
                case 10:
                    ((ImageView)findViewById(R.id.yelpStars)).setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.stars_large_5, null));
                    break;
                default:
                    ((ImageView)findViewById(R.id.yelpStars)).setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.stars_large_0, null));
            }

            // setup preview img
            String img = elem.getString("image_url");
            ((ImageView)findViewById(R.id.imageResto)).setAlpha(img.isEmpty() ? 0f : 1f);

            Log.i("Yelp", "Place: "+name+" lon="+lon+" lat"+lat );

            ((TextView) findViewById(R.id.NAme)).setText(""+name);
            String jsonAddress = jsonLocation.getJSONArray("display_address").toString();
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
