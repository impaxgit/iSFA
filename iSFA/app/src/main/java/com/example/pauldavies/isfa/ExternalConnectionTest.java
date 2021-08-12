package com.example.pauldavies.isfa;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ExternalConnectionTest extends AsyncTask<String, Void, String>
{
    AsyncTaskDelegate   asyncTaskDelegate;
    StringBuilder       stringBuilder;
    HttpURLConnection   httpURLConnection;
    URL                 url;
    BufferedReader      bufferedReader;
    String              result;

    public ExternalConnectionTest(AsyncTaskDelegate asyncTaskDelegate)
    {
        super();

        this.asyncTaskDelegate  =   asyncTaskDelegate;

    }

    @Override
    protected String doInBackground(String... strings)
    {

        stringBuilder   =   new StringBuilder();

        try
        {
            url                 =   new URL(Commons.URL_ROOT+Commons.URL_CON);
            httpURLConnection   =   (HttpURLConnection)url.openConnection();
            httpURLConnection.setConnectTimeout(Commons.CON_TIMEOUT);
            httpURLConnection.setReadTimeout(Commons.CON_TIMEOUT);
            httpURLConnection.setRequestMethod(Commons.CON_METHOD);
            httpURLConnection.setDoOutput(Commons.NO);
            httpURLConnection.setDoInput(Commons.YES);

            httpURLConnection.connect();

            switch (httpURLConnection.getResponseCode())
            {
                case HttpURLConnection.HTTP_BAD_GATEWAY:
                    stringBuilder.append(Commons.CON_BAD_GATEWAY);
                break;
                case HttpURLConnection.HTTP_BAD_REQUEST:
                    stringBuilder.append(Commons.CON_BAD_REQUEST);
                break;
                case HttpURLConnection.HTTP_CLIENT_TIMEOUT:
                    stringBuilder.append(Commons.CON_CLIENT_TIMEOUT);
                break;
                case HttpURLConnection.HTTP_FORBIDDEN:
                    stringBuilder.append(Commons.CON_FORBIDDEN);
                break;
                case HttpURLConnection.HTTP_INTERNAL_ERROR:
                    stringBuilder.append(Commons.CON_INTERNAL_ERROR);
                break;
                case HttpURLConnection.HTTP_NOT_FOUND:
                    stringBuilder.append(Commons.CON_REQ_NOT_FOUND);
                break;
                case HttpURLConnection.HTTP_NOT_ACCEPTABLE:
                    stringBuilder.append(Commons.CON_REQ_UNACCEPTABLE);
                break;
                case HttpURLConnection.HTTP_OK://Get the result of this connection
                    bufferedReader  =   new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                    if(bufferedReader   !=  null)
                    {
                        while ((result  =   bufferedReader.readLine()) != null)
                        {

                            stringBuilder.append(result+"\n");
                        }
                    }
                    else
                    {
                        stringBuilder.append(Commons.NO_RESULT);
                    }
                    stringBuilder.append(Commons.CON_SUCCESS);
                break;
                default:
                    stringBuilder.append(Commons.ERROR);
                break;
            }
        }
        catch (IOException io)
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
        asyncTaskDelegate.getResult(s);
    }
}
