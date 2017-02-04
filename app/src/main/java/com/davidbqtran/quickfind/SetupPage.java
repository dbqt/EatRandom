package com.davidbqtran.quickfind;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.Manifest.permission.*;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import org.json.JSONArray;
import org.json.JSONObject;
import java.net.URL;
import java.security.Permission;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;


public class SetupPage extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private final String AppId = "k6QdreYeqRXHOTHKFkMacQ";
    private final String AppSecret = "27SK1Oovt8uCq5s8zWjeg3kqVcHk1wKNADZma1h82cw9zyARgjf6GmJQ6zcWwqME";
    private final String AuthUrl = "https://api.yelp.com/oauth2/token";

    private String AccessToken = "";

    private GoogleApiClient mGoogleApiClient;

    private Location mLastLocation;

    private ArrayList<ToggleButton> allButtons;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_page);

        // Create an instance of GoogleAPIClient.
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        allButtons = new ArrayList<ToggleButton>();
        allButtons.add((ToggleButton) findViewById(R.id.BreakFastButton));
        allButtons.add((ToggleButton) findViewById(R.id.BarPubButton));
        allButtons.add((ToggleButton) findViewById(R.id.JapaneseButton));
        allButtons.add((ToggleButton) findViewById(R.id.ChineseButton));
        allButtons.add((ToggleButton) findViewById(R.id.VietButton));
        allButtons.add((ToggleButton) findViewById(R.id.ThaiButton));
        allButtons.add((ToggleButton) findViewById(R.id.BurgerButton));
        allButtons.add((ToggleButton) findViewById(R.id.PizzaButton));
        allButtons.add((ToggleButton) findViewById(R.id.ItalianButton));
        allButtons.add((ToggleButton) findViewById(R.id.CafeTeaButton));
        allButtons.add((ToggleButton) findViewById(R.id.MiddleEastButton));
        allButtons.add((ToggleButton) findViewById(R.id.MediterraneanButton));
        allButtons.add((ToggleButton) findViewById(R.id.FastFood));
        allButtons.add((ToggleButton) findViewById(R.id.IndianButton));
    }

    public void FindClick(View v){

        if(mLastLocation == null && ((EditText) findViewById(R.id.editTextLocation)).getText().toString().isEmpty()) {
            Toast.makeText(this, "Location is not set!",
                    Toast.LENGTH_SHORT).show();

            return;
        }

        AuthenticatorYelp ay = new AuthenticatorYelp();
        ay.execute();
    }

    public void GetLocationClick(View v) {
        GetLocation();
    }

    public void ToggleAll(View v) {
        boolean allOn = true;
        for(ToggleButton tg : allButtons) {
            allOn = allOn && tg.isChecked();
        }

        if(allOn) {
            for(ToggleButton tg : allButtons) {
                tg.setChecked(false);
            }
        } else {
            for(ToggleButton tg : allButtons) {
                tg.setChecked(true);
            }
        }
    }

    private void AuthenticateYelp(){
        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType,
                "client_id="+AppId+
                "&client_secret="+AppSecret+
                        "&grant_type=client_credentials");
        Request request = new Request.Builder()
                .url(AuthUrl)
                .post(body)
                .addHeader("content-type", "application/x-www-form-urlencoded")
                .addHeader("cache-control", "no-cache")
                .build();

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
        }
    }

    private void RequestYelp(){
        OkHttpClient client = new OkHttpClient();


        String terms = "term=";

        if(((ToggleButton) findViewById(R.id.BreakFastButton)).isChecked()) {
            terms += "+breakfast";
        }
        if(((ToggleButton) findViewById(R.id.MiddleEastButton)).isChecked()) {
            terms += "+mideastern";
        }
        if(((ToggleButton) findViewById(R.id.PizzaButton)).isChecked()) {
            terms += "+pizza";
        }
        if(((ToggleButton) findViewById(R.id.VietButton)).isChecked()) {
            terms += "+vietnamese";
        }
        if(((ToggleButton) findViewById(R.id.ItalianButton)).isChecked()) {
            terms += "+italian";
        }
        if(((ToggleButton) findViewById(R.id.ChineseButton)).isChecked()) {
            terms += "+chinese";
        }
        if(((ToggleButton) findViewById(R.id.CafeTeaButton)).isChecked()) {
            terms += "+cafe+tea";
        }
        if(((ToggleButton) findViewById(R.id.ThaiButton)).isChecked()) {
            terms += "+thai";
        }
        if(((ToggleButton) findViewById(R.id.MediterraneanButton)).isChecked()) {
            terms += "+mediterranean";
        }
        if(((ToggleButton) findViewById(R.id.BurgerButton)).isChecked()) {
            terms += "+burger+sandwich";
        }
        if(((ToggleButton) findViewById(R.id.IndianButton)).isChecked()) {
            terms += "+indian";
        }
        if(((ToggleButton) findViewById(R.id.ChineseButton)).isChecked()) {
            terms += "+chinese";
        }
        if(((ToggleButton) findViewById(R.id.JapaneseButton)).isChecked()) {
            terms += "+japanese";
        }
        if(((ToggleButton) findViewById(R.id.FastFood)).isChecked()) {
            terms += "+fastfood";
        }

        String location = "";
        EditText locationText = (EditText)findViewById(R.id.editTextLocation);
        if(!locationText.getText().toString().isEmpty()) {
            location = "&location="+locationText.getText().toString();
        }
        if(mLastLocation != null) {
            location = "&latitude="+String.valueOf(mLastLocation.getLatitude()) + "&longitude="+String.valueOf(mLastLocation.getLongitude());
        }

        String price = "&price=";
        boolean anyPrice = true;
        if(((ToggleButton)findViewById(R.id.priceToggle1)).isChecked()) {
            price += "1";
            anyPrice = false;
        }
        if(((ToggleButton)findViewById(R.id.priceToggle2)).isChecked()) {
            if(!anyPrice){
                price += ",";
            }
            price += "2";
            anyPrice = false;
        }
        if(((ToggleButton)findViewById(R.id.priceToggle3)).isChecked()) {
            if(!anyPrice){
                price += ",";
            }
            price += "3";
            anyPrice = false;
        }
        if(((ToggleButton)findViewById(R.id.priceToggle4)).isChecked()) {
            if(!anyPrice){
                price += ",";
            }
            price += "4";
            anyPrice = false;
        }

        if(anyPrice) {
            price += "1,2,3,4";
        }

        // TODO: radius
        String radius = "";//"&radius=10000";

        String finalUrl = "https://api.yelp.com/v3/businesses/search?"+terms+"&open_now=true"+location+radius+price;
        Log.i("yelp", "GET: " + finalUrl);
        Request request = new Request.Builder()
                .url(finalUrl)
                .get()
                .addHeader("authorization", "Bearer "+AccessToken)
                .addHeader("cache-control", "no-cache")
                .build();

        try {
            Response response = client.newCall(request).execute();


            String json = response.body().string();
            Log.i("Yelp", "Yelp GET Response: " + json);
            try {

                JSONObject obj = new JSONObject(json);
                JSONArray arr = obj.getJSONArray("businesses");
                Random random = new Random();
                JSONObject elem = arr.getJSONObject(random.nextInt(arr.length()));
                String name = elem.getString("name");
                JSONObject coordinates = elem.getJSONObject("coordinates");
                String lon = coordinates.getString("longitude");
                String lat = coordinates.getString("latitude");
                Log.i("Yelp", "Place: "+name+" lon="+lon+" lat"+lat );

            } catch (Throwable t) {
                Log.i("Yelp", "Could not parse malformed JSON: \"" + json + "\"");
            }
        } catch(Exception e) {
            Log.i("Yelp", "Error GET " + e.toString());
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    private void GetLocation(){
        // check perms & request
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            if (this.checkSelfPermission(ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    this.checkSelfPermission(ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION}, 1);
            }
        }
        // if ok perms
        if(this.checkSelfPermission(ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                this.checkSelfPermission(ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED  ) {
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                    mGoogleApiClient);
            if (mLastLocation != null) {
                EditText locationText = (EditText)findViewById(R.id.editTextLocation);
                locationText.setHint("Location Detected!");
                locationText.setText("");
                Button locationButton = (Button)findViewById(R.id.LocationButton);
                locationButton.setText("Location Detected!");
            }
        } else {
            Log.i("Location", "no permission");
        }
    }

    private class AuthenticatorYelp extends AsyncTask<URL, Integer, Long> {
        protected Long doInBackground(URL... urls) {
            AuthenticateYelp();
            RequestYelp();
            return Long.parseLong("0");
        }

        protected void onProgressUpdate(Integer... progress) {
        }

        protected void onPostExecute(Long result) {
        }
    }
}
