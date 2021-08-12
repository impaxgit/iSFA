package com.example.pauldavies.isfa;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

public class TripManagement extends AppCompatActivity
{

    Context context;
    CommonClass commonClass;
    int mode_control=1;
    DB db;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_management);

        this.setTitle("iSFA::Trip Management");

        context =   this;
        db  =   new DB(context);
        commonClass =   new CommonClass();

        findViewById(R.id.routes_request_stock).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                final AlertDialog progressbar = commonClass.createProgressBar(context);

                new ExternalConnectionTest(new AsyncTaskDelegate()
                {
                    @Override
                    public void getResult(String result)
                    {
                        if(result.contains(Commons.CON_SUCCESS))
                        {
                            //Get customers
                            new GetCustomersList(new AsyncTaskDelegate()
                            {
                                @Override
                                public void getResult(String result)
                                {
                                    try
                                    {
                                        JSONObject jsonObject   =   new JSONObject(result);
                                        JSONArray jsonArray =   jsonObject.getJSONArray(Commons.RESULT);
                                        for(int i=0; i<jsonArray.length(); i++)
                                        {
                                            JSONObject jsonObject1  =   jsonArray.getJSONObject(i);
                                            Customer    customers   =   new Customer(jsonObject1.getString("customer_name"), jsonObject1.getString("contact_person_mobile"), jsonObject1.getString("customer_code"));
                                            customers.customerDetails.add(new String[]{jsonObject1.getString("outlet_type"), jsonObject1.getString("contact_person"), jsonObject1.getString("mobile"), jsonObject1.getString("business_owner"), jsonObject1.getString("route")});

                                            /*Delete the current customer record before inserting a new one.
                                             *This assumes that the record coming form the external source is most updated.*
                                             */
                                            db.deleteCustomer(jsonObject1.getString("customer_code"));


                                            db.createCustomer(jsonObject1.getString("customer_code")
                                                    , jsonObject1.getString("customer_name")
                                                    , jsonObject1.getString("description")
                                                    , jsonObject1.getString("outlet_type")
                                                    , jsonObject1.getString("route")
                                                    , jsonObject1.getString("geocoordnates")
                                                    , jsonObject1.getString("date_created")
                                                    , jsonObject1.getString("customerType")
                                                    , Integer.valueOf(jsonObject1.getString("taxable"))
                                                    , jsonObject1.getString("email")
                                                    , jsonObject1.getString("mobile")
                                                    , jsonObject1.getString("contact_person")
                                                    , jsonObject1.getString("email")
                                                    , jsonObject1.getString("contact_person_mobile")
                                                    , Float.valueOf(jsonObject1.getString("credit_limit"))
                                                    , Float.valueOf(jsonObject1.getString("credit_balance"))
                                                    , Integer.valueOf(jsonObject1.getString("status"))
                                                    , Integer.valueOf(jsonObject1.getString("sync")));
                                        }
                                    }
                                    catch (JSONException e)
                                    {
                                        commonClass.createToaster(context, "Could not parse customer data", Commons.TOASTER_LONG, R.drawable.warning);
                                    }
                                }
                            }).execute();
                        }
                        else
                        {
                            commonClass.createToaster(context, "Could not connect externally.", Commons.TOASTER_LONG, R.drawable.sad);
                        }

                        commonClass.destroyDialog(progressbar);
                    }
                }).execute();
            }
        });
//End trip
        findViewById(R.id.trip_end).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                final Cursor cursor    =   db.getDailyChecks();
                String mom="";
                if(cursor.getCount()>0)
                {
                   LayoutInflater layoutInflater    =   (LayoutInflater)context.getSystemService(LAYOUT_INFLATER_SERVICE);
                   final View view    =   layoutInflater.inflate(R.layout.end_trip, null, false);
                   final AlertDialog dialog   =   commonClass.createCustomDialog(context, view);

                   cursor.moveToFirst();

                   mom = cursor.getString(cursor.getColumnIndex(Commons.DAILY_MODE_OF_TRANSPORT));

                   ((TextView)view.findViewById(R.id.end_trip_movement_mode)).setText(cursor.getString(cursor.getColumnIndex(Commons.DAILY_MODE_OF_TRANSPORT)));
                   ((TextView)view.findViewById(R.id.end_trip_starttime)).setText(cursor.getString(cursor.getColumnIndex(Commons.DAILY_TIME_START)));
                   ((TextView)view.findViewById(R.id.end_trip_odostart)).setText(cursor.getString(cursor.getColumnIndex(Commons.DAILY_ODOMETER_READING_START)));
                   ((TextView)view.findViewById(R.id.end_trip_comment_ini)).setText(cursor.getString(cursor.getColumnIndex(Commons.DAILY_COMMENT)));
                   ((TextView)view.findViewById(R.id.end_trip_reg_number)).setText(cursor.getString(cursor.getColumnIndex(Commons.DAILY_REGISTRATION_NUMBER)));

                   if(cursor.getString(cursor.getColumnIndex(Commons.DAILY_MODE_OF_TRANSPORT)).equals("Foot"))
                   {
                       view.findViewById(R.id.end_trip_odo_end).setEnabled(false);
                   }
                   else
                   {
                       view.findViewById(R.id.end_trip_odo_end).setEnabled(true);
                   }

                    final String momm   =   mom;
                    view.findViewById(R.id.end_btn_Save).setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            if(!((EditText)view.findViewById(R.id.end_trip_odo_end)).getText().toString().trim().isEmpty())
                            {
                                LayoutInflater layoutInflater1  =   (LayoutInflater)context.getSystemService(LAYOUT_INFLATER_SERVICE);
                                View view1  =   layoutInflater1.inflate(R.layout.dialog_normal, null, false);
                                final AlertDialog dialog1 =   commonClass.createCustomDialog(context, view1);


                                ((ImageView)view1.findViewById(R.id.dialog_normal_icon)).setImageResource(R.drawable.warning);
                                ((TextView)view1.findViewById(R.id.dialog_normal_title)).setText("Trip Close Alert");
                                ((TextView)view1.findViewById(R.id.dialog_normal_message)).setText("You are about to close this trip.\nPlease note that this operation cannot be reversed.\nDo you wish to continue?");
                                ((Button)view1.findViewById(R.id.button3)).setText("Yes");
                                ((Button)view1.findViewById(R.id.button4)).setText("No");
                                (view1.findViewById(R.id.button3)).setOnClickListener(new View.OnClickListener()
                                {
                                    @Override
                                    public void onClick(View v)
                                    {

                                            if(momm.equals("Foot"))
                                            {
                                                if(db.closeDailyCheck(commonClass.getCurrentTime(), 0, ((EditText)view.findViewById(R.id.end_trip_comment_lst)).getText().toString().trim()))
                                                {
                                                    commonClass.createToaster(context, "Trip closed successfully.", Toast.LENGTH_LONG, R.drawable.smile);
                                                    commonClass.destroyDialog(dialog);
                                                }
                                                else
                                                {
                                                    commonClass.createToaster(context, "Failed to close trip.\nPlease try again else contact admin.", Toast.LENGTH_LONG, R.drawable.sad);
                                                }
                                            }
                                            else
                                            {
                                                if(db.closeDailyCheck(commonClass.getCurrentTime(), Float.valueOf(((EditText)view.findViewById(R.id.end_trip_odo_end)).getText().toString().trim()), ((EditText)view.findViewById(R.id.end_trip_comment_lst)).getText().toString().trim()))
                                                {
                                                    commonClass.createToaster(context, "Trip closed successfully.", Toast.LENGTH_LONG, R.drawable.smile);
                                                    commonClass.destroyDialog(dialog);
                                                }
                                                else
                                                {
                                                    commonClass.createToaster(context, "Failed to close trip.\nPlease try again else contact admin.", Toast.LENGTH_LONG, R.drawable.sad);
                                                }
                                            }

                                        commonClass.destroyDialog(dialog1);
                                    }
                                });

                                view1.findViewById(R.id.button4).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        commonClass.destroyDialog(dialog1);
                                    }
                                });
                            }
                            else
                            {
                                ((EditText)view.findViewById(R.id.end_trip_odo_end)).setError("Odometer reading?");
                                ((EditText)view.findViewById(R.id.end_trip_odo_end)).requestFocus();
                            }

                        }

                    });


                    view.findViewById(R.id.end_btn_Cancel).setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            commonClass.destroyDialog(dialog);
                        }
                    });

               }
               else
               {
                   commonClass.createToaster(context, "Sorry, no trip records were found.", Toast.LENGTH_LONG, R.drawable.sad);
               }
            }
        });

        findViewById(R.id.trip_routes).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(context, JourneyPlan.class);
                startActivity(intent);
            }
        });

        //Start Trip
        findViewById(R.id.trip_start).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                LayoutInflater layoutInflater   =  (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
                final View view   =   layoutInflater.inflate(R.layout.start_trip, null, false);
                final AlertDialog dialog  =   commonClass.createCustomDialog(context, view);

                view.findViewById(R.id.start_trip_btn_save).setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {

                        if(mode_control==0)
                        {
                            if(db.createDailyRouteCheck("Foot", "", 0, 0, ((EditText)view.findViewById(R.id.startTrip__txt_coments)).getText().toString().trim(), commonClass.getCurrentDate(), commonClass.getCurrentTime(), "", 0, ""))
                            {
                                commonClass.createToaster(context, "Trip has been started.", Toast.LENGTH_LONG, R.drawable.smile);
                                commonClass.destroyDialog(dialog);
                            }
                            else
                            {
                                commonClass.createToaster(context, "Could not start trip, please try again.\nIf this persists contact admin.", Toast.LENGTH_LONG, R.drawable.sad);
                            }
                        }
                        else
                        {
                            if(!((EditText)view.findViewById(R.id.startTrip_details_txt_Odometerstart)).getText().toString().trim().isEmpty())
                            {
                               if(!((EditText)view.findViewById(R.id.startTrip_details_txt_RegNo)).getText().toString().trim().isEmpty())
                               {
                                   if(db.createDailyRouteCheck("Van", ((EditText)view.findViewById(R.id.startTrip_details_txt_RegNo)).getText().toString().trim(), Float.valueOf(((EditText)view.findViewById(R.id.startTrip_details_txt_Odometerstart)).getText().toString().trim()), 0, ((EditText)view.findViewById(R.id.startTrip__txt_coments)).getText().toString().trim(), commonClass.getCurrentDate(), commonClass.getCurrentTime(), "", 0, ""))
                                   {
                                       commonClass.createToaster(context, "Trip has been started.", Toast.LENGTH_LONG, R.drawable.smile);
                                       commonClass.destroyDialog(dialog);
                                   }
                                   else
                                   {
                                       commonClass.createToaster(context, "Could not start trip, please try again.\nIf this persists contact admin.", Toast.LENGTH_LONG, R.drawable.sad);
                                   }
                               }
                               else
                               {
                                   ((EditText)view.findViewById(R.id.startTrip_details_txt_RegNo)).setError("Van Reg. no?");
                                   (view.findViewById(R.id.startTrip_details_txt_RegNo)).requestFocus();
                               }
                            }
                            else
                            {
                                ((EditText)view.findViewById(R.id.startTrip_details_txt_Odometerstart)).setError("Odometer reading?");
                                (view.findViewById(R.id.startTrip_details_txt_Odometerstart)).requestFocus();
                            }
                        }
                    }
                });

                view.findViewById(R.id.start_trip_close).setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        commonClass.destroyDialog(dialog);
                    }
                });

                ((Spinner)view.findViewById(R.id.startTrip_sp_modeofMovement)).setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View v, int position, long id)
                    {
                        if(parent.getItemAtPosition(position).toString().equals("Foot"))
                        {
                            mode_control=0;
                            view.findViewById(R.id.startTrip_details_txt_Odometerstart).setEnabled(false);
                            view.findViewById(R.id.startTrip_details_txt_RegNo).setEnabled(false);
                        }
                        else
                        {
                            mode_control=1;
                            view.findViewById(R.id.startTrip_details_txt_Odometerstart).setEnabled(true);
                            view.findViewById(R.id.startTrip_details_txt_RegNo).setEnabled(true);
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }
        });
    }
}
