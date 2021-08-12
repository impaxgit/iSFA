package com.example.pauldavies.isfa;


import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class GetCustomersList extends AsyncTask<String, Void, String>
{
    AsyncTaskDelegate delegate;

    URL url;
    HttpURLConnection httpURLConnection;
    BufferedReader bufferedReader;
    StringBuilder stringBuilder;
    String result;

    public GetCustomersList(AsyncTaskDelegate delegate)
    {
        super();

        this.delegate   =   delegate;
    }

    @Override
    protected String doInBackground(String... strings)
    {
        stringBuilder   =   new StringBuilder();
        try
        {
           url  =   new URL(Commons.URL_ROOT+Commons.URL_GET_CUSTOMERS);
           httpURLConnection    =  (HttpURLConnection) url.openConnection();
           httpURLConnection.setConnectTimeout(Commons.CON_TIMEOUT);
           httpURLConnection.setReadTimeout(Commons.CON_TIMEOUT);
           httpURLConnection.setRequestMethod(Commons.CON_METHOD);
           httpURLConnection.setDoInput(true);

           httpURLConnection.connect();

           if(httpURLConnection.getResponseCode()   ==  HttpURLConnection.HTTP_OK)
           {
               bufferedReader   =   new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
               if(bufferedReader != null)
               {
                   while((result = bufferedReader.readLine()) != null)
                   {
                       stringBuilder.append(result+"\n");
                   }
               }
               else
               {
                   stringBuilder.append("no customers assigned to you");
               }
           }
           else
           {
               stringBuilder.append("Could not open connection");
           }
        }
        catch (IOException e)
        {
            stringBuilder.append("Error pulling customers");
        }

        return stringBuilder.toString();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(String s)
    {
        super.onPostExecute(s);
        delegate.getResult(s);
    }
}
