package com.example.pauldavies.isfa;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class GetOrderHeaders extends AsyncTask<String ,Void,String >
{
    AsyncTaskDelegate delegate;

    URL url;
    String result;
    Context context;
    HttpURLConnection httpURLConnection;
    BufferedReader bufferedReader;
    StringBuilder stringBuilder;
    BufferedWriter bufferedWriter;

    public GetOrderHeaders(AsyncTaskDelegate delegate)
    {
        super();
        this.delegate = delegate;
    }

    @Override
    protected String doInBackground(String... strings)
    {
        stringBuilder   =   new StringBuilder();
        try
        {
            url  =   new URL(Commons.URL_ROOT+Commons.URL_GET_ORDERSHEADERS);
            httpURLConnection    =  (HttpURLConnection) url.openConnection();
            httpURLConnection.setConnectTimeout(Commons.CON_TIMEOUT);
            httpURLConnection.setReadTimeout(Commons.CON_TIMEOUT);
            httpURLConnection.setRequestMethod(Commons.CON_METHOD);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);

            String params   =   new Uri.Builder()
                                .appendQueryParameter("username", strings[0])
                                .build().getEncodedQuery();
            bufferedWriter  =   new BufferedWriter(new OutputStreamWriter(httpURLConnection.getOutputStream()));
            bufferedWriter.write(params);
            bufferedWriter.flush();
            bufferedWriter.close();

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
                    stringBuilder.append("no Orders for this customer today");
                }
            }
            else
            {
                stringBuilder.append("Could not open connection");
            }
        }
        catch (IOException e)
        {
            stringBuilder.append("Error pulling orders");
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
