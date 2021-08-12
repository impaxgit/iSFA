package com.example.pauldavies.isfa;

import android.content.Context;
import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Login extends AppCompatActivity
{

    CommonClass commonClass;
    Context context;
    SharedPrefs sharedPrefs;
    DB  db;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);

        context =   this;
        commonClass =   new CommonClass();
        sharedPrefs =   new SharedPrefs(context);
        db  =   new DB(context);

        //Validate the pass and text for data
        findViewById(R.id.login_btn_login).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(!((EditText)findViewById(R.id.login_txt_username)).getText().toString().trim().isEmpty())
                {
                    if(!((EditText)findViewById(R.id.login_txt_password)).getText().toString().trim().isEmpty())
                    {
                        //Check if there is internet connection before making external request
                        if(commonClass.checkNetwork(context).equals(Commons.NO_NETWORK))
                        {
                            commonClass.createToaster(context, "Sorry, no internet connection.\nConnect then try again.", Commons.TOASTER_LONG, R.drawable.sad);
                        }
                        else
                        {
                            //Check internet connection then make request if it exists.
                            final AlertDialog progressbar =commonClass.createProgressBar(context);

                            new ExternalConnectionTest(new AsyncTaskDelegate()
                            {
                                @Override
                                public void getResult(String result)
                                {
                                    switch (result)
                                    {
                                        case Commons.CON_BAD_GATEWAY:
                                            commonClass.destroyDialog(progressbar);
                                            commonClass.createToaster(context, "Bad gateway", Commons.TOASTER_LONG, R.drawable.warning);
                                        break;
                                        case Commons.CON_BAD_REQUEST:
                                            commonClass.destroyDialog(progressbar);
                                            commonClass.createToaster(context, "Bad request", Commons.TOASTER_LONG, R.drawable.warning);
                                        break;
                                        case Commons.CON_CLIENT_TIMEOUT:
                                            commonClass.destroyDialog(progressbar);
                                            commonClass.createToaster(context, "Request timed out", Commons.TOASTER_LONG, R.drawable.warning);
                                        break;
                                        case Commons.CON_FORBIDDEN:
                                            commonClass.destroyDialog(progressbar);
                                            commonClass.createToaster(context, "Access forbidden.", Commons.TOASTER_LONG, R.drawable.warning);
                                        break;
                                        case Commons.CON_INTERNAL_ERROR:
                                            commonClass.createToaster(context, "Request error.", Commons.TOASTER_LONG, R.drawable.warning);
                                            commonClass.destroyDialog(progressbar);
                                        break;
                                        case Commons.CON_REQ_NOT_FOUND:
                                            commonClass.createToaster(context, "Request could not be found.", Commons.TOASTER_LONG, R.drawable.warning);
                                            commonClass.destroyDialog(progressbar);
                                        break;
                                        case Commons.CON_REQ_UNACCEPTABLE:
                                            commonClass.createToaster(context, "Request not acceptable.", Commons.TOASTER_LONG, R.drawable.warning);
                                            commonClass.destroyDialog(progressbar);
                                        break;
                                        case Commons.ERROR:
                                            commonClass.createToaster(context, "Unknown error occurred.", Commons.TOASTER_LONG, R.drawable.warning);
                                            commonClass.destroyDialog(progressbar);
                                        break;
                                        case Commons.NO_RESULT:
                                           commonClass.createToaster(context, "No record was found matching your login credentials.", Commons.TOASTER_LONG, R.drawable.warning);
                                        break;
                                        default:
                                            if(result.contains(Commons.RESULT))
                                            {
                                               if(result.contains(Commons.CON_SUCCESS)) //Connection was successful, can proceed with request to login in
                                               {
                                                   //The connection is okay, can proceed with login procedure
                                                   new LoginAsyncTask
                                                       (
                                                           ((EditText) findViewById(R.id.login_txt_username)).getText().toString().trim(),
                                                           ((EditText) findViewById(R.id.login_txt_password)).getText().toString().trim(),
                                                           new AsyncTaskDelegate()
                                                           {
                                                               @Override
                                                               public void getResult(String result)
                                                               {
                                                                   switch (result)
                                                                   {
                                                                       case Commons.ERROR: //URL error
                                                                           commonClass.createToaster(context, "Unspecified error prevented you from loggin in. Please try again later.", Commons.TOASTER_LONG, R.drawable.sad);
                                                                           break;
                                                                       case Commons.NO_RESULT: //Null was returned
                                                                           commonClass.createToaster(context, "Sorry, we could not well identify.", Commons.TOASTER_LONG, R.drawable.sad);
                                                                           break;
                                                                       default:
                                                                           if(result.contains(Commons.RESULT))
                                                                           {
                                                                               if(result.contains(Commons.USERNAME_NO_VAL))
                                                                               {
                                                                                   commonClass.createToaster(context, "Seems you did not provide a username.", Commons.TOASTER_LONG, R.drawable.sad);
                                                                               }
                                                                               else if(result.contains(Commons.PASSWORD_NO_VAL))
                                                                               {
                                                                                   commonClass.createToaster(context, "You possibly did not provide password.", Commons.TOASTER_LONG, R.drawable.sad);
                                                                               }
                                                                               else if(result.contains(Commons.ERROR_FETCHING_DATA))
                                                                               {
                                                                                   commonClass.createToaster(context, "There was an error logging in. Try again and if this persists contact the admin.", Commons.TOASTER_LONG, R.drawable.sad);
                                                                               }
                                                                               else if(result.contains(Commons.NO_RECORD_MATCH))
                                                                               {
                                                                                   commonClass.createToaster(context, "We could not get any active record matching the credentials you provided.", Commons.TOASTER_LONG, R.drawable.sad);
                                                                               }
                                                                               else if(result.contains(Commons.NOT_EXACT))
                                                                               {
                                                                                   commonClass.createToaster(context, "Sorry, you have more than one active accounts.\nYou cannot log in.\nPlease inform the system admin.", Commons.TOASTER_LONG, R.drawable.warning);
                                                                               }
                                                                               else if(result.contains(Commons.SESSION_EXCEED))
                                                                               {
                                                                                   commonClass.createToaster(context, "Sorry, you have more than one active accounts.\nYou cannot log in.\nPlease inform the system admin.", Commons.TOASTER_LONG, R.drawable.warning);
                                                                               }
                                                                               else if(result.contains(Commons.RESULT_SUCCESS)) //Log in was successful, get to the next interface.
                                                                               {
                                                                                   sharedPrefs.putItem("logged_in", "YES");
                                                                                   sharedPrefs.putItem("username", ((EditText) findViewById(R.id.login_txt_username)).getText().toString().trim());
                                                                                   commonClass.createToaster(context, "Welcome, "+((EditText) findViewById(R.id.login_txt_username)).getText().toString().trim().toUpperCase(), Commons.TOASTER_SHORT, R.drawable.smile);

                                                                                   //Get the user permissions and keep them
                                                                                   new GetUserPermisions(new AsyncTaskDelegate()
                                                                                   {
                                                                                       @Override
                                                                                       public void getResult(String result)
                                                                                       {
                                                                                           if(result.contains(Commons.NO_PERMISIONS))
                                                                                           {
                                                                                               commonClass.createToaster(context, "No permissions have been defined for you. Contact the admin.", Commons.TOASTER_LONG, R.drawable.sad);
                                                                                           }
                                                                                           else if(result.contains(Commons.RESULT))
                                                                                           {
                                                                                               try
                                                                                               {
                                                                                                   JSONObject   jsonObject  =   new JSONObject(result);
                                                                                                   JSONArray    jsonArray   =   jsonObject.getJSONArray(Commons.RESULT);
                                                                                                   for(int i=0; i<jsonArray.length(); i++)
                                                                                                   {
                                                                                                       JSONObject jsonObject1   =   jsonArray.getJSONObject(i);
                                                                                                       db.deletePermissions();
                                                                                                       db.createPermisions(((EditText) findViewById(R.id.login_txt_username)).getText().toString().trim()
                                                                                                       , jsonObject1.getString("category")
                                                                                                       , jsonObject1.getInt("call_objective")
                                                                                                       , jsonObject1.getInt("stock_take")
                                                                                                       , jsonObject1.getInt("generate_order")
                                                                                                       , jsonObject1.getInt("invoice_print")
                                                                                                       , jsonObject1.getInt("payment_collection")
                                                                                                       , jsonObject1.getInt("machandising")
                                                                                                       , jsonObject1.getInt("order_delivery")
                                                                                                       , jsonObject1.getInt("asset_tracking")
                                                                                                       , jsonObject1.getInt("complete_call")
                                                                                                       , jsonObject1.getInt("is_active"));
                                                                                                   }
                                                                                               }
                                                                                               catch (JSONException e)
                                                                                               {
                                                                                                   commonClass.createToaster(context, "Could not parse json data", Commons.TOASTER_LONG, R.drawable.sad);
                                                                                               }
                                                                                           }
                                                                                           else
                                                                                           {
                                                                                               commonClass.createToaster(context, "Could not assign permission. Contact the system admin.", Commons.TOASTER_LONG, R.drawable.sad);
                                                                                           }
                                                                                       }
                                                                                   }).execute(((EditText) findViewById(R.id.login_txt_username)).getText().toString().trim());

                                                                                   Intent intent    =   new Intent(context, Home.class);
                                                                                   startActivity(intent);

                                                                                   finish();
                                                                               }
                                                                               else
                                                                               {
                                                                                   commonClass.createToaster(context, "Unknown error prevented you from login in.", Commons.TOASTER_LONG, R.drawable.warning);
                                                                               }
                                                                           }
                                                                           else
                                                                           {
                                                                               commonClass.createToaster(context, "Unknown error prevented you from login in.", Commons.TOASTER_LONG, R.drawable.warning);
                                                                           }

                                                                           break;
                                                                   }
                                                               }
                                                           }
                                                       ).execute();
                                               }
                                               else if(result.contains(Commons.CON_FAILED)) //Connection failed
                                               {
                                                   commonClass.createToaster(context, "Failed to establish external connection...Could not initiate request.", Commons.TOASTER_LONG, R.drawable.sad);
                                               }
                                               else //Error not known
                                               {
                                                   commonClass.createToaster(context, "Unknown error occurred while establishing external connection.", Commons.TOASTER_LONG, R.drawable.warning);
                                               }
                                            }
                                            else
                                            {
                                                commonClass.createToaster(context, "Unknown connection result.", Commons.TOASTER_LONG, R.drawable.warning);
                                            }

                                            commonClass.destroyDialog(progressbar);
                                        break;
                                    }
                                }
                            }).execute();
                        }

                    }
                    else
                    {
                        ((EditText)findViewById(R.id.login_txt_password)).setError(Commons.REQUIRED_FIELD);
                        (findViewById(R.id.login_txt_password)).requestFocus();
                    }
                }
                else
                {
                    ((EditText)findViewById(R.id.login_txt_username)).setError(Commons.REQUIRED_FIELD);
                    (findViewById(R.id.login_txt_username)).requestFocus();
                }
            }
        });

        findViewById(R.id.login_btn_changecredentials).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                LayoutInflater layoutInflater   =   (LayoutInflater)context.getSystemService(LAYOUT_INFLATER_SERVICE);
                final View credential_change  =   layoutInflater.inflate(R.layout.credentials_change_layout, null, false);
                final AlertDialog credentials_dialog  =   commonClass.createCustomDialog(context, credential_change);
                credentials_dialog.show();

                ((TextView)credential_change.findViewById(R.id.txt_login_username_change)).setText(sharedPrefs.getItem("username"));

                //Listen to user inputs and show the upper labels since the hints are no longer visible
                ((EditText)credential_change.findViewById(R.id.txt_login_current_password)).addTextChangedListener(new TextWatcher()
                {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after){ }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if(!((EditText)credential_change.findViewById(R.id.txt_login_current_password)).getText().toString().trim().isEmpty())
                        {
                            credential_change.findViewById(R.id.textView7).setVisibility(View.VISIBLE);
                        }
                        else
                        {
                            credential_change.findViewById(R.id.textView7).setVisibility(View.INVISIBLE); }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        }});

                ((EditText)credential_change.findViewById(R.id.txt_login_new_password)).addTextChangedListener(new TextWatcher()
                {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after){ }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if(!((EditText)credential_change.findViewById(R.id.txt_login_new_password)).getText().toString().trim().isEmpty()) {
                            credential_change.findViewById(R.id.textView8).setVisibility(View.VISIBLE); }
                        else {
                            credential_change.findViewById(R.id.textView8).setVisibility(View.INVISIBLE); } }

                    @Override
                    public void afterTextChanged(Editable s) {
                    }});

                ((EditText)credential_change.findViewById(R.id.txt_login_confirmation_password)).addTextChangedListener(new TextWatcher()
                {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after){ }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if(!((EditText)credential_change.findViewById(R.id.txt_login_confirmation_password)).getText().toString().trim().isEmpty()) {
                            credential_change.findViewById(R.id.textView9).setVisibility(View.VISIBLE); }
                        else {
                            credential_change.findViewById(R.id.textView9).setVisibility(View.INVISIBLE); } }

                    @Override
                    public void afterTextChanged(Editable s) {
                    }});

                //Close the dialog on pressing close
                credentials_dialog.findViewById(R.id.btn_login_creds_close).setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        commonClass.destroyDialog(credentials_dialog);
                    }
                });

                //Do validation for the fields to ensure they are not empty when submitting
                credential_change.findViewById(R.id.btn_login_change_creds).setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        if(!((EditText)credential_change.findViewById(R.id.txt_login_username_change)).getText().toString().trim().isEmpty())
                        {
                            if(!((EditText)credential_change.findViewById(R.id.txt_login_current_password)).getText().toString().trim().isEmpty())
                            {
                                if(!((EditText)credential_change.findViewById(R.id.txt_login_new_password)).getText().toString().trim().isEmpty())
                                {
                                    if(!((EditText)credential_change.findViewById(R.id.txt_login_confirmation_password)).getText().toString().isEmpty())
                                    {
                                        Toast.makeText(context, "Ready to go", Toast.LENGTH_LONG).show();
                                    }
                                    else
                                    {
                                        ((EditText)credential_change.findViewById(R.id.txt_login_confirmation_password)).setError("Confirmation password?");
                                        (credential_change.findViewById(R.id.txt_login_confirmation_password)).requestFocus();
                                    }
                                }
                                else
                                {
                                    ((EditText)credential_change.findViewById(R.id.txt_login_new_password)).setError("New password?");
                                    (credential_change.findViewById(R.id.txt_login_new_password)).requestFocus();
                                }
                            }
                            else
                            {
                                ((EditText)credential_change.findViewById(R.id.txt_login_current_password)).setError("Your current password?");
                                (credential_change.findViewById(R.id.txt_login_current_password)).requestFocus();
                            }
                        }
                        else
                        {
                            ((EditText)credential_change.findViewById(R.id.txt_login_username_change)).setError("Your username required here!");
                            (credential_change.findViewById(R.id.txt_login_username_change)).requestFocus();
                        }
                    }
                });

            }
        });
    }
}
