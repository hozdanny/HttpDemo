package project.android.hkust.httpdemo;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import cz.msebera.android.httpclient.Header;
import project.android.hkust.httpdemo.Model.Event;
import project.android.hkust.httpdemo.Model.User;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    final String TAG = "MainActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button conBtn = (Button) findViewById(R.id.con_btn);
        conBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.con_btn:
                if (isOnline()) {
                    AsyncHttpClient client = new AsyncHttpClient();
                    client.get("http://www.google.com", new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                            TextView tv = (TextView)findViewById(R.id.textView);
                            tv.setText(jsonTest());
                            //String s = new String(responseBody);
                            //tv.setText(s);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                            TextView tv = (TextView)findViewById(R.id.textView);
                            tv.setText("404");
                        }
                    });
                } else {
                    Toast.makeText(this, "You are not online", Toast.LENGTH_LONG).show();
                    TextView tv = (TextView) findViewById(R.id.textView);
                    tv.setText("You are not online");
                }
                break;
            default:
        }
    }

    public class AsynHttpTask extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {
            InputStream inputStream = null;
            HttpURLConnection urlConnection = null;
            String result = null;
            try {
                /* forming th java.net.URL object */
                URL url = new URL(params[0]);
                urlConnection = (HttpURLConnection) url.openConnection();

                 /* optional request header */
                urlConnection.setRequestProperty("Content-Type", "application/json");

                /* optional request header */
                urlConnection.setRequestProperty("Accept", "application/json");

                /* for Get request */
                urlConnection.setRequestMethod("GET");
                int statusCode = urlConnection.getResponseCode();

                /* 200 represents HTTP OK */
                if (statusCode == 200) {
                    inputStream = new BufferedInputStream(urlConnection.getInputStream());
                    String response = convertInputStreamToString(inputStream);
                    //parseResult(response);
                    result = response; // Successful
                } else {
                    result = null; //"Failed to fetch data!";
                }
            } catch (Exception e) {
                Log.d(TAG, e.getLocalizedMessage());
            }
            return result; //"Failed to fetch data!";
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            TextView tv = (TextView) findViewById(R.id.textView);
            tv.setText(s);
        }


    }


    private String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null) {
            result += line;
        }

            /* Close Stream */
        if (null != inputStream) {
            inputStream.close();
        }
        return result;
    }

//    private void parseResult(String result) {
//
//        placeInfoArrayList = new ArrayList<PlaceInfo>();
//
//        GsonBuilder gsonBuilder = new GsonBuilder();
//        Gson gson = gsonBuilder.create();
//
//        placeInfoArrayList = Arrays.asList(gson.fromJson(result, PlaceInfo[].class));
//
//    }

    private boolean isOnline() {

        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        } else {
            return false;
        }
    }

    private String jsonTest(){
        Event event = new Event();
        User user = new User();

        event.setId("001");

        user.setName("Sam");
        user.setGender("male");
        user.setEmail("729849400");
        user.setPhone("2132132");
        event.setEventHolder(user);

        event.setLocation("Hong Kong");

        event.setStatus("tbd");

        event.setTime("start  2015-1-1");

        ArrayList<String> dates = new ArrayList<String>();
        dates.add("2016-1-1");
        dates.add("2016-1-2");
        dates.add("2016-1-3");
        event.setTbdTime(dates);

        event.setDescription("1231837sdjhfiaslfblabflale");

        ArrayList<Event> events = new ArrayList<Event>();
        events.add(event);
        events.add(event);

        Gson gson = new Gson();
        String s = gson.toJson(events);
        return s;
    }
}
