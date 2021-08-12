package com.example.pauldavies.isfa;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public  class GetExpenseType extends AsyncTask<String,Void,String>
{
    URL url;
    HttpURLConnection httpURLConnection;
    BufferedReader bufferedReader;
    StringBuilder stringBuilder;
    String result;
    AsyncTaskDelegate asyncTaskDelegate;

    public GetExpenseType(AsyncTaskDelegate asyncTaskDelegate)
    {
        this.asyncTaskDelegate=asyncTaskDelegate;
    }

    @Override
    protected String doInBackground(String... strings)
    {
        stringBuilder   =   new StringBuilder();
        try {
            url =   new URL(Commons.URL_ROOT+Commons.URL_GET_EXPENSE_TYPE);
            httpURLConnection   =   (HttpURLConnection)url.openConnection();
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setConnectTimeout(Commons.CON_TIMEOUT);
            httpURLConnection.setReadTimeout(Commons.CON_TIMEOUT);
            httpURLConnection.setRequestMethod(Commons.CON_METHOD);

            httpURLConnection.connect();

            if(httpURLConnection.getResponseCode()  ==  HttpURLConnection.HTTP_OK)
            {
                bufferedReader  =   new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                if(bufferedReader != null)
                {
                    while((result = bufferedReader.readLine()) != null)
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
                stringBuilder.append("Could not connect");
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
        asyncTaskDelegate.getResult(s);
    }
}
