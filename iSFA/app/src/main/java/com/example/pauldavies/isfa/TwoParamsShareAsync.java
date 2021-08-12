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

public class TwoParamsShareAsync extends AsyncTask<String,Void,String>
{
    AsyncTaskDelegate delegate;
    URL url;
    String result;
    StringBuilder stringBuilder;
    BufferedReader bufferedReader;
    BufferedWriter bufferedWriter;
    HttpURLConnection httpURLConnection;
    String uri;
    String customer;

    public TwoParamsShareAsync(String uri,AsyncTaskDelegate delegate)
    {

        this.delegate = delegate;
        this.uri = uri;
    }

    @Override
    protected String doInBackground(String... strings)
    {
        stringBuilder   =   new StringBuilder();
        try
        {
            url =   new URL(Commons.URL_ROOT+uri);
            httpURLConnection   =   (HttpURLConnection)url.openConnection();

            httpURLConnection.setRequestMethod(Commons.CON_METHOD);
            httpURLConnection.setDoInput(Commons.YES);
            httpURLConnection.setDoOutput(Commons.YES);
            httpURLConnection.setReadTimeout(Commons.CON_TIMEOUT);
            httpURLConnection.setConnectTimeout(Commons.CON_TIMEOUT);

            String params   =   new Uri.Builder().appendQueryParameter("salesperson_id", customer).build().getEncodedQuery();

            bufferedWriter  =   new BufferedWriter(new OutputStreamWriter(httpURLConnection.getOutputStream()));
            bufferedWriter.write(params);
            bufferedWriter.flush();
            bufferedWriter.close();

            httpURLConnection.connect();

            bufferedReader  =   new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));

            if(bufferedReader != null)
            {
                while((result   =   bufferedReader.readLine())  !=  null)
                {
                    stringBuilder.append(result+"\n");
                }
            }
            else
            {
                stringBuilder.append(Commons.NO_RESULT);
            }

            bufferedReader.close();

        }
        catch (IOException e)
        {
            stringBuilder.append(Commons.ERROR);
        }

        httpURLConnection.disconnect();

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
