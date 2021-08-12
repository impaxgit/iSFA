package com.example.pauldavies.isfa;

import android.net.Uri;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class GetUserPermisions extends AsyncTask<String, Void, String>
{
    AsyncTaskDelegate delegate;

    String result;
    StringBuilder stringBuilder;
    BufferedReader bufferedReader;
    BufferedWriter bufferedWriter;
    URL url;
    HttpURLConnection httpURLConnection;

    public GetUserPermisions(AsyncTaskDelegate delegate)
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
            url =   new URL(Commons.URL_ROOT+Commons.URL_PERMISIONS);
            httpURLConnection   =   (HttpURLConnection)url.openConnection();
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setConnectTimeout(Commons.CON_TIMEOUT);
            httpURLConnection.setReadTimeout(Commons.CON_TIMEOUT);
            httpURLConnection.setRequestMethod(Commons.CON_METHOD);

            String params   =   new Uri.Builder()
                                .appendQueryParameter("username", strings[0])
                                .build().getEncodedQuery();

            bufferedWriter  =   new BufferedWriter(new OutputStreamWriter(httpURLConnection.getOutputStream()));
            bufferedWriter.write(params);
            bufferedWriter.flush();
            bufferedWriter.close();

            httpURLConnection.connect();

            if(httpURLConnection.getResponseCode()  ==  HttpURLConnection.HTTP_OK)
            {
                bufferedReader  =   new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                if(bufferedReader   != null)
                {
                    while((result   =   bufferedReader.readLine()) != null)
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
