package com.davidbqtran.quickfind;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import javax.net.ssl.HttpsURLConnection;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SetupPage extends AppCompatActivity {

    private final String AppId = "k6QdreYeqRXHOTHKFkMacQ";
    private final String AppSecret = "27SK1Oovt8uCq5s8zWjeg3kqVcHk1wKNADZma1h82cw9zyARgjf6GmJQ6zcWwqME";

    private final String AuthUrl = "https://api.yelp.com/oauth2/token";
    private String AccessToken = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_page);
    }

    public void FindClick(View v){
        AuthenticatorYelp ay = new AuthenticatorYelp();
        ay.execute();
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
