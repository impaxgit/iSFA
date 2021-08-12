package com.example.pauldavies.isfa;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class AsyncGetCallObjectives extends AsyncTask<String, Void, String>
{
    AsyncTaskDelegate delegate;
    URL url;
    HttpURLConnection httpURLConnection;
    BufferedReader bufferedReader;
    StringBuilder stringBuilder;
    String result;

    public AsyncGetCallObjectives(AsyncTaskDelegate delegate)
    {
        this.delegate   =   delegate;
    }

    @Override
    protected String doInBackground(String... strings)
    {
        stringBuilder   =   new StringBuilder();
        try
        {
            url =    new URL(Commons.URL_ROOT+Commons.URL_GET_CALL_OBJECTIVES);
            httpURLConnection   =   (HttpURLConnection)url.openConnection();
            httpURLConnection.setDoOutput(false);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setRequestMethod(Commons.CON_METHOD);
            httpURLConnection.setReadTimeout(Commons.CON_TIMEOUT);
            httpURLConnection.setConnectTimeout(Commons.CON_TIMEOUT);

            httpURLConnection.connect();

            if(httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK)
            {
                bufferedReader  =   new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                if(bufferedReader != null)
                {
                    while((result   =   bufferedReader.readLine()) != null)
                    {
                        stringBuilder.append(result+"\n");
                    }
                }
                else
                {
                    stringBuilder.append("No result");
                }
            }
            else
            {
                stringBuilder.append("Could not connected");
            }
        }
        catch (IOException e)
        {
            stringBuilder.append("Error occurred");
        }
        return stringBuilder.toString();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        delegate.getResult(s);
    }
}
