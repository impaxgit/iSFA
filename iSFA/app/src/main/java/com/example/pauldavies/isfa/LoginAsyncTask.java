package com.example.pauldavies.isfa;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.Buffer;

public class LoginAsyncTask extends AsyncTask<String, Void, String>
{
    AsyncTaskDelegate delegate;
    String username;
    String password;

    StringBuilder       stringBuilder;
    URL                 url;
    HttpURLConnection   httpURLConnection;
    BufferedWriter      bufferedWriter;
    BufferedReader      bufferedReader;
    String              result;

    public LoginAsyncTask(String username, String password, AsyncTaskDelegate delegate)
    {
        super();

        this.username   =   username;
        this.password   =   password;
        this.delegate   =   delegate;
    }

    @Override
    protected String doInBackground(String... strings)
    {
        stringBuilder   =   new StringBuilder();
        try
        {
            url                 =   new URL(Commons.URL_ROOT+Commons.URL_LOGIN);
            httpURLConnection   =   (HttpURLConnection)url.openConnection();

            httpURLConnection.setRequestMethod(Commons.CON_METHOD);
            httpURLConnection.setDoInput(Commons.YES);
            httpURLConnection.setDoOutput(Commons.YES);
            httpURLConnection.setReadTimeout(Commons.CON_TIMEOUT);
            httpURLConnection.setConnectTimeout(Commons.CON_TIMEOUT);

            String params   =   new Uri.Builder()
                                .appendQueryParameter("username", username)
                                .appendQueryParameter("password", password)
                                .build().getEncodedQuery();

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
