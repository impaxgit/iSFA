package com.example.pauldavies.isfa;


import android.os.AsyncTask;
import android.util.Log;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class GetProducts extends AsyncTask<String, Void, String>
{
    URL url;
    HttpURLConnection httpURLConnection;
    BufferedReader bufferedReader;
    String result;
    StringBuilder stringBuilder;
    AsyncTaskDelegate delegate;

    public GetProducts(AsyncTaskDelegate delegate)
    {
        super();

        this.delegate  =   delegate;
    }

    @Override
    protected String doInBackground(String... strings)
    {
        stringBuilder   =   new StringBuilder();
        try
        {
            url =   new URL(Commons.URL_ROOT+Commons.URL_GET_PRODUCT_MASTER);
            httpURLConnection   =   (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoInput(true);
            httpURLConnection.setConnectTimeout(10000);
            httpURLConnection.setReadTimeout(10000);
            httpURLConnection.setDoOutput(true);

            httpURLConnection.connect();

            if(httpURLConnection.getResponseCode()  ==  HttpURLConnection.HTTP_OK)
            {
                bufferedReader  =   new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                if(bufferedReader != null)
                {
                    while ((result = bufferedReader.readLine()) != null)
                    {
                        stringBuilder.append(result+"\n");
                    }
                }
                else
                {
                    stringBuilder.append("No data found");
                }
            }
            else
            {
                stringBuilder.append("Could not establish connection");
            }
        }
        catch (IOException e)
        {
            stringBuilder.append(e.getMessage());
            Log.d("Testing", e.getLocalizedMessage());
        }

        return stringBuilder.toString();
    }

    @Override
    protected void onPreExecute()
    {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(String s)
    {
        super.onPostExecute(s);
        delegate.getResult(s);
    }
}