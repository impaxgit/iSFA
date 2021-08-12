package com.example.pauldavies.isfa;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ShareAsyncClass extends AsyncTask<String, Void, String>
{
    AsyncTaskDelegate delegate;
    URL url;
    String result;
    StringBuilder stringBuilder;
    BufferedReader bufferedReader;
    HttpURLConnection httpURLConnection;
    String uri;

    public ShareAsyncClass(String uri, AsyncTaskDelegate delegate)
    {
        super();

        this.delegate   =   delegate;
        this.uri    =   uri;
    }

    @Override
    protected String doInBackground(String... strings)
    {
        stringBuilder   =   new StringBuilder();
        try
        {
            url =   new URL(Commons.URL_ROOT+uri);
            httpURLConnection   =   (HttpURLConnection)url.openConnection();
            httpURLConnection.setDoInput(true);
            httpURLConnection.setRequestMethod(Commons.CON_METHOD);
            httpURLConnection.setReadTimeout(Commons.CON_TIMEOUT);
            httpURLConnection.setConnectTimeout(Commons.CON_TIMEOUT);
            httpURLConnection.connect();

            if(httpURLConnection.getResponseCode()  ==  HttpURLConnection.HTTP_OK)
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
                    stringBuilder.append("No data");
                }
            }
            else
            {
                stringBuilder.append("Could not connect externally");
            }
        }
        catch (IOException e)
        {
            stringBuilder.append("Could not connect");
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
