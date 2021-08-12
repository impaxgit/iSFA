package com.example.pauldavies.isfa;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefs
{
    Context context;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public SharedPrefs(Context context)
    {
        this.context    =   context;
        sharedPreferences   =   context.getSharedPreferences("isfa", Context.MODE_PRIVATE);
        editor  =   sharedPreferences.edit();
    }

    public String getItem(String name)
    {
        if(sharedPreferences.getString(name, "") != null)
        {
            return sharedPreferences.getString(name, "");
        }
        else
        {
            return "";
        }
    }

    public void putItem(String name, String value)
    {
        editor.putString(name, value);
        editor.commit();
    }

    public void clearItem(String name)
    {
        editor.remove(name);
        editor.commit();
    }

    public void clearAll()
    {
        editor.clear();
        editor.commit();
    }

}
