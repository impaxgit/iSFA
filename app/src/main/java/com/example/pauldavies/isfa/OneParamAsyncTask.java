package com.example.pauldavies.isfa;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class OneParamAsyncTask extends AsyncTask<String, Void, String>
{
    String url;
    AsyncTaskDelegate asyncTaskDelegate;
    URL uri;
    HttpURLConnection httpURLConnection;
    BufferedReader bufferedReader;
    BufferedWriter bufferedWriter;
    StringBuilder stringBuilder;
    String result;

    public OneParamAsyncTask(String url, AsyncTaskDelegate asyncTaskDelegate)
    {
        super();

        this.url    =   url;
        this.asyncTaskDelegate  =   asyncTaskDelegate;
    }

    @Override
    protected String doInBackground(String... strings)
    {

        stringBuilder   =   new StringBuilder();
        try
        {
            uri =   new URL(Commons.URL_ROOT+url);
            httpURLConnection   =   (HttpURLConnection)uri.openConnection();
            httpURLConnection.setConnectTimeout(Commons.CON_TIMEOUT);
            httpURLConnection.setReadTimeout(Commons.CON_TIMEOUT);
            httpURLConnection.setRequestMethod(Commons.CON_METHOD);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);


            String params =  new Uri.Builder()
                             .appendQueryParameter("customer", strings[0])
                             .build()
                             .getEncodedQuery();


            bufferedWriter = new BufferedWriter(new OutputStreamWriter(httpURLConnection.getOutputStream()));
            bufferedWriter.write(params);
            bufferedWriter.flush();
            bufferedWriter.close();


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
                stringBuilder.append("No external connection");
            }
        }
        catch (IOException e)
        {
            stringBuilder.append("Error");
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
