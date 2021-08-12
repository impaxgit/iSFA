package com.example.pauldavies.isfa;

import android.content.Context;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class GetLocaation extends AsyncTask<String,Void ,String>
{
    Context context;
    private HttpURLConnection conn;
    private URL url;
    AsyncTaskDelegate delegate;

    public GetLocaation(AsyncTaskDelegate delegate)
    {
        this.delegate   =   delegate;
    }

    @Override
    protected String doInBackground(String... params)
    {
        try
        {
            url = new URL(Commons.URL_ROOT + Commons.URL_BINS);

            try
            {

                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(Commons.CON_TIMEOUT);
                conn.setConnectTimeout(Commons.CON_TIMEOUT);
                conn.setRequestMethod(Commons.CON_METHOD);
                conn.setDoInput(true);
                conn.setDoOutput(true);

                try
                {

                    int response_code = conn.getResponseCode();

                    // Check if successful connection made
                    if (response_code == HttpURLConnection.HTTP_OK) {

                        // Read data sent from server
                        InputStream input = conn.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                        StringBuilder result = new StringBuilder();
                        String line;

                        while ((line = reader.readLine()) != null) {
                            result.append(line);
                        }


                        return (result.toString());

                    } else {

                        return ("unsuccessful");
                    }

                } catch (IOException e) {
                    return e.getMessage();
                }

            } catch (IOException e1) {


                return e1.getMessage();
            }

        } catch (MalformedURLException e) {


            return e.getMessage();
        } finally
        {
            conn.disconnect();
        }


    }

    @Override
    protected void onPreExecute()
    {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        delegate.getResult(result);
    }

}
