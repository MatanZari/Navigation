package com.zari.matan.navigationdrawerexample.Network;

import android.os.AsyncTask;


import com.zari.matan.navigationdrawerexample.MainActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Matan on 6/3/2015
 */
public class MainHttpTask extends AsyncTask<Request, Void, Request> {

    MainActivity activity;



    public MainHttpTask(MainActivity activity) {
        this.activity = activity;

    }



    @Override
    protected Request doInBackground(Request... requests) {

        Request request = requests[0];
        URL url;
        HttpURLConnection connection = null;
        BufferedReader reader;
        StringBuilder builder;
        try {

        url = new URL(requests[0].urlStr);
        connection = (HttpURLConnection) url.openConnection();

            builder = new StringBuilder();

            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = reader.readLine())!= null){
                builder.append(line);
            }

            request.responseStr = builder.toString();
            reader.close();
            return request;
        }catch (IOException e){

            e.printStackTrace();
        }finally {
            assert connection != null;
            connection.disconnect();
        }



        return null;
    }


    @Override
    protected void onPostExecute(Request req) {
        super.onPostExecute(req);

        if (req != null){
            req.callback.handleResponse(req.responseStr);

        }
    }



}
