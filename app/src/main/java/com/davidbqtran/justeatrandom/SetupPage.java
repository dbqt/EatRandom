package com.davidbqtran.justeatrandom;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import org.json.JSONArray;
import org.json.JSONObject;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;


public class SetupPage extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private final String AppId = "k6QdreYeqRXHOTHKFkMacQ";
    private final String AppSecret = "27SK1Oovt8uCq5s8zWjeg3kqVcHk1wKNADZma1h82cw9zyARgjf6GmJQ6zcWwqME";
    private final String AuthUrl = "https://api.yelp.com/oauth2/token";

    private String AccessToken = "";

    private GoogleApiClient mGoogleApiClient;

    private Location mLastLocation;

    private ArrayList<ToggleButton> allButtons;

    public static Activity myActivity;

    private String lon, lat, name, olocation, phone, jsonAddress, img, json;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_page);
        myActivity = this;
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
        ((ScrollView)findViewById(R.id.activity_setup_page)).smoothScrollTo(0,0);
        findViewById(R.id.parentLayout).requestFocus();
        ToggleView();

        SeekBar sk=(SeekBar) findViewById(R.id.seekBar);
        ((TextView)findViewById(R.id.distanceText)).setText("Max distance: "+(sk.getProgress()+1)+"km");
        sk.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // TODO Auto-generated method stub
                ((TextView)findViewById(R.id.distanceText)).setText("Max travel distance: "+(progress+1)+"km");


            }
        });


    }

    public void FindClick(View v){

        if(mLastLocation == null && ((EditText) findViewById(R.id.editTextLocation)).getText().toString().isEmpty()) {
            Toast.makeText(this, "Location is not set!",
                    Toast.LENGTH_SHORT).show();

            return;
        }

        ((Button)findViewById(R.id.SendButton)).setEnabled(false);
        AuthenticatorYelp ay = new AuthenticatorYelp();
        ay.execute();
    }

    private boolean openedView = true;
    public void ToggleFoodView(View v){
        ToggleView();
    }

    private void ToggleView(){
        openedView = !openedView;
        if(openedView){
            findViewById(R.id.button).setVisibility(View.VISIBLE);
            findViewById(R.id.list1).setPivotY(0);
            findViewById(R.id.list2).setPivotY(0);
            findViewById(R.id.list1).animate().scaleY(1).setDuration(400); //.setVisibility(View.VISIBLE);
            findViewById(R.id.list2).animate().scaleY(1).setDuration(400);
            findViewById(R.id.list1).setVisibility(View.VISIBLE);
            findViewById(R.id.list2).setVisibility(View.VISIBLE);
            ((TextView) findViewById(R.id.typeToggle)).setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_keyboard_arrow_up_black, 0);
            //((TextView) findViewById(R.id.typeToggle)).setCompoundDrawables(null, null, ResourcesCompat.getDrawable(getResources(), R.drawable.ic_keyboard_arrow_up_black , null), null);
        } else {
            findViewById(R.id.button).setVisibility(View.GONE);
            findViewById(R.id.list1).setPivotY(0);
            findViewById(R.id.list2).setPivotY(0);
            findViewById(R.id.list1).animate().scaleY(0).setDuration(400);
            findViewById(R.id.list2).animate().scaleY(0).setDuration(400);
            findViewById(R.id.list1).setVisibility(View.GONE);
            findViewById(R.id.list2).setVisibility(View.GONE);
            ((TextView) findViewById(R.id.typeToggle)).setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_keyboard_arrow_down_black, 0);
            //((TextView) findViewById(R.id.typeToggle)).setCompoundDrawables(null, null, ResourcesCompat.getDrawable(getResources(), R.drawable.ic_keyboard_arrow_down_black , null), null);
        }
        //findViewById(R.id.linearBro).requestLayout();
        findViewById(R.id.parentLayout).requestLayout();
    }


    public void ShowResult() {
        Intent i = new Intent(this, ResultPage.class);
        i.putExtra("json", this.json);
        startActivity(i);
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

    private Long RequestYelp(){
        OkHttpClient client = new OkHttpClient();


        String terms = "categories=";

        boolean first = true;

        if(((ToggleButton) findViewById(R.id.BreakFastButton)).isChecked()) {
            if(!first) terms += ",";
            terms += "breakfast_brunch";
            first = false;
        }
        if(((ToggleButton) findViewById(R.id.MiddleEastButton)).isChecked()) {
            if(!first) terms += ",";
            terms += "mideastern";
            first = false;
        }
        if(((ToggleButton) findViewById(R.id.PizzaButton)).isChecked()) {
            if(!first) terms += ",";
            terms += "pizza";
            first = false;
        }
        if(((ToggleButton) findViewById(R.id.VietButton)).isChecked()) {
            if(!first) terms += ",";
            terms += "vietnamese";
            first = false;
        }
        if(((ToggleButton) findViewById(R.id.ItalianButton)).isChecked()) {
            if(!first) terms += ",";
            terms += "italian";
            first = false;
        }
        if(((ToggleButton) findViewById(R.id.ChineseButton)).isChecked()) {
            if(!first) terms += ",";
            terms += "chinese";
            first = false;
        }
        if(((ToggleButton) findViewById(R.id.CafeTeaButton)).isChecked()) {
            if(!first) terms += ",";
            terms += "cafes,coffee";
            first = false;
        }
        if(((ToggleButton) findViewById(R.id.ThaiButton)).isChecked()) {
            if(!first) terms += ",";
            terms += "thai";
            first = false;
        }
        if(((ToggleButton) findViewById(R.id.MediterraneanButton)).isChecked()) {
            if(!first) terms += ",";
            terms += "mediterranean";
            first = false;
        }
        if(((ToggleButton) findViewById(R.id.BurgerButton)).isChecked()) {
            if(!first) terms += ",";
            terms += "burgers,sandwiches";
            first = false;
        }
        if(((ToggleButton) findViewById(R.id.IndianButton)).isChecked()) {
            if(!first) terms += ",";
            terms += "indpak";
            first = false;
        }
        if(((ToggleButton) findViewById(R.id.JapaneseButton)).isChecked()) {
            if(!first) terms += ",";
            terms += "japanese";
            first = false;
        }
        if(((ToggleButton) findViewById(R.id.FastFood)).isChecked()) {
            if(!first) terms += ",";
            terms += "food_court,hotdogs";
            first = false;
        }

        String location = "";
        EditText locationText = (EditText)findViewById(R.id.editTextLocation);
        if(!locationText.getText().toString().isEmpty()) {
            location = "&location="+locationText.getText().toString().replace(" ", "+");
            olocation = locationText.getText().toString().replace(" ", "+");
        }
        if(mLastLocation != null) {
            location = "&latitude="+String.valueOf(mLastLocation.getLatitude()) + "&longitude="+String.valueOf(mLastLocation.getLongitude());
            olocation = String.valueOf(mLastLocation.getLatitude()) + ","+String.valueOf(mLastLocation.getLongitude());
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
        int radiusValue = (((SeekBar)findViewById(R.id.seekBar)).getProgress()+1) * 1000;
        String radius = "&radius="+(radiusValue);

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
            this.json = json;
            Log.i("Yelp", "Yelp GET Response: " + json);
            try {

                JSONObject obj = new JSONObject(json);
                int nb = obj.getInt("total");
                if(nb == 0) {
                    return Long.parseLong("-1");
                }
                JSONArray arr = obj.getJSONArray("businesses");
                Random random = new Random();
                JSONObject elem = arr.getJSONObject(random.nextInt(arr.length()));
                name = elem.getString("name");
                phone = elem.getString("display_phone");
                JSONObject jsonLocation = elem.getJSONObject("location");
                jsonAddress = jsonLocation.getJSONArray("display_address").toString();
                JSONObject coordinates = elem.getJSONObject("coordinates");
                lon = coordinates.getString("longitude");
                lat = coordinates.getString("latitude");
                img = elem.getString("image_url");
                Log.i("Yelp", "Place: "+name+" lon="+lon+" lat"+lat );

            } catch (Throwable t) {
                Log.i("Yelp", "Could not parse malformed JSON: \"" + json + "\"");
                JSONObject ob = new JSONObject(json);
                return Long.parseLong("-1");
            }
        } catch(Exception e) {
            Log.i("Yelp", "Error GET " + e.toString());

        }

        return Long.parseLong("0");
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        GetLocation();
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

        boolean perm = true;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            perm = this.checkSelfPermission(ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                    this.checkSelfPermission(ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        }
        // if ok perms
        if(perm) {
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                    mGoogleApiClient);
            if (mLastLocation != null) {
                EditText locationText = (EditText)findViewById(R.id.editTextLocation);
                locationText.setHint("Your Location Detected!");
                locationText.setText("");
                Button locationButton = (Button)findViewById(R.id.LocationButton);
                locationButton.setText("Your Location Detected!");
            }
        } else {
            //((Button)findViewById(R.id.LocationButton)).setEnabled(false);
            Log.i("Location", "no permission");
        }
    }

    private class AuthenticatorYelp extends AsyncTask<URL, Integer, Long> {
        protected Long doInBackground(URL... urls) {
            AuthenticateYelp();
            Long r = RequestYelp();
            return r;
        }

        protected void onProgressUpdate(Integer... progress) {
        }

        protected void onPostExecute(Long result) {
            ((Button)findViewById(R.id.SendButton)).setEnabled(true);
            if(result == -1) {
                Toast.makeText(myActivity, "Unable to find food, please check the settings again",
                        Toast.LENGTH_LONG).show();
            }
            else {
                ((SetupPage)myActivity).ShowResult();
            }
        }
    }
}
