package csx060.uga.edu.uga_hack_4_app;

import android.util.Log;
import android.view.View;
import java.util.regex.Pattern;
import java.net.*;
import java.io.*;
import android.util.Log;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Pattern;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Pattern;

public class validateActivity extends AsyncTask<Void, Void, String.>{
    private Exception exception;
    private static final String API_URL = "https://certwebservices.ft.cashedge.com/sdk/Payments/Customers/";
    private static final String API_KEY = "prod-a59aa9a65739dcebd25d1d1c1621c703b22ac8c5e9bd99100cab75be443ccb1e7d6066256655505e476ab01a2385692abdd7845d40b4622bdfdccac3a52e70bf";
    public static boolean isValid(String email)
    {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (email == null)
            return false;
        return pat.matcher(email).matches();
    }

    protected void onPreExecute() {

    }

    protected void doInBackground(Void... urls) {
        String email = emailText.getText().toString();
        // Do some validation here
        if (isValid(email))
            System.out.print("Yes");
        else
            System.exit(0);

        try {
            URL url = new URL(API_URL);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod();
            try {
                //do something with http connection
                urlConnection.getResponseMessage();
            } finally {
                urlConnection.disconnect();
            }
        }
        catch(Exception e) {

        }
    }

    protected void onPostExecute(String response) {
        if(response == null) {
            response = "THERE WAS AN ERROR";
        }
       //progressBar.setVisibility(View.GONE);
        Log.i("INFO", response);
        //responseView.setText(response);
    }




}
