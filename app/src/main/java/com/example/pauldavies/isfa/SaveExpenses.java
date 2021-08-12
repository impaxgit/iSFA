package com.example.pauldavies.isfa;

import android.content.Context;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class SaveExpenses extends AsyncTask<String,Void,String>
{
    AsyncTaskDelegate delegate;

    URL url;
    String result;
    Context context;
    HttpURLConnection httpURLConnection;
    BufferedReader bufferedReader;
    StringBuilder stringBuilder;

    public SaveExpenses(AsyncTaskDelegate delegate)
    {
        this.delegate = delegate;
    }

    @Override
    protected String doInBackground(String... strings)
    {
        stringBuilder   =   new StringBuilder();
        try
        {
            url =   new URL(Commons.URL_ROOT+Commons.URL_SAVEEXPENSES);
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
                    stringBuilder.append("Can not save the data");
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
        }

        return stringBuilder.toString();
    }

    @Override
    protected void onPreExecute()
    {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(String result)
    {
        super.onPostExecute(result);
        delegate.getResult(result);
    }
}
