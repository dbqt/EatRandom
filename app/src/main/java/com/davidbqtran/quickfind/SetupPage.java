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
import android.widget.EditText;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import org.json.JSONObject;
import java.net.URL;
import java.security.Permission;

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
    }

    public void FindClick(View v){
        AuthenticatorYelp ay = new AuthenticatorYelp();
        ay.execute();
    }

    public void GetLocationClick(View v) {
        GetLocation();
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


        String terms = "";
        // TODO: terms

        String location = "";
        EditText locationText = (EditText)findViewById(R.id.editTextLocation);
        if(!locationText.getText().toString().isEmpty()) {
            location = "&location="+locationText.getText().toString();
        }
        if(mLastLocation != null) {
            location = "&latitude="+String.valueOf(mLastLocation.getLatitude()) + "&longitude="+String.valueOf(mLastLocation.getLongitude());
        }
        Request request = new Request.Builder()
                .url("https://api.yelp.com/v3/businesses/search?"+terms+location)
                .get()
                .addHeader("authorization", "Bearer "+AccessToken)
                .addHeader("cache-control", "no-cache")
                .addHeader("postman-token", "a0f30f52-c3e6-7263-7f17-0145489a0ab2")
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
                locationText.setText("latitude="+String.valueOf(mLastLocation.getLatitude()) + "&longitude="+String.valueOf(mLastLocation.getLongitude()));
            }
        } else {
            Log.i("Location", "no permission");
        }
    }

    private class AuthenticatorYelp extends AsyncTask<URL, Integer, Long> {
        protected Long doInBackground(URL... urls) {
            AuthenticateYelp();
            return Long.parseLong("0");
        }

        protected void onProgressUpdate(Integer... progress) {
        }

        protected void onPostExecute(Long result) {
        }
    }

}
