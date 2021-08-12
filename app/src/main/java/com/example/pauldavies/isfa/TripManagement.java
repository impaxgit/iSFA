package com.example.pauldavies.isfa;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.database.Cursor;
import android.drm.DrmStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
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
                                                    , Integer.valueOf(jsonObject1.getString("sync"))
                                                    , jsonObject1.getString("business_owner")
                                                    , jsonObject1.getString("business_owner_number")
                                                    , jsonObject1.getString("business_owner_email")
                                                    , jsonObject1.getString("business_owner_address"));
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

        findViewById(R.id.trip_end).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                final Cursor cursor    =   db.getDailyChecks();
                String mom="";
                float odo_read=0;

                if(cursor.getCount()>0)
                {
                   LayoutInflater layoutInflater    =   (LayoutInflater)context.getSystemService(LAYOUT_INFLATER_SERVICE);
                   final View view    =   layoutInflater.inflate(R.layout.end_trip, null, false);
                   final AlertDialog dialog   =   commonClass.createCustomDialog(context, view);

                   cursor.moveToFirst();

                   mom = cursor.getString(cursor.getColumnIndex(Commons.DAILY_MODE_OF_TRANSPORT));
                   odo_read =   Float.valueOf(cursor.getString(cursor.getColumnIndex(Commons.DAILY_ODOMETER_READING_START)));

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
                    final float odo_final   =   odo_read;
                    view.findViewById(R.id.end_btn_Save).setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            if(momm.equals("Foot"))
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

                                        if(db.closeDailyCheck(commonClass.getCurrentTime(), 0, ((EditText)view.findViewById(R.id.end_trip_comment_lst)).getText().toString().trim()))
                                        {
                                            commonClass.createToaster(context, "Trip closed successfully.", Toast.LENGTH_LONG, R.drawable.smile);
                                            commonClass.destroyDialog(dialog);
                                        }
                                        else
                                        {
                                            commonClass.createToaster(context, "Failed to close trip.\nPlease try again else contact admin.", Toast.LENGTH_LONG, R.drawable.sad);
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
                                            if(Float.valueOf(((EditText)view.findViewById(R.id.end_trip_odo_end)).getText().toString().trim())>=odo_final)
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
                                            else
                                            {
                                                commonClass.createToaster(context, "The provided odometer reading must be equal to or greater than the reading when you started the trip.", Toast.LENGTH_LONG, R.drawable.warning);
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

        findViewById(R.id.routes_accept_reject_stock).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
               Intent intent =new Intent(context,AcceptReject_Stock.class);
               startActivity(intent);
            }
        });
        findViewById(R.id.trip_start).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Cursor cursor   =   db.getDailyChecks();
                if(cursor.getCount()>0)
                {
                    commonClass.createToaster(context, "Sorry,\nyou cannot start trip while you haven't closed the other one.", Toast.LENGTH_LONG, R.drawable.warning);
                }
                else
                {
                    LayoutInflater layoutInflater   =  (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
                    final View view   =   layoutInflater.inflate(R.layout.start_trip, null, false);
                    final AlertDialog dialog  =   commonClass.createCustomDialog(context, view);

                    view.findViewById(R.id.start_trip_btn_save).setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            //Get the vehicle controls values
                            CheckBox brakes     =    view.findViewById(R.id.vehicle_brakes);
                            CheckBox tyres      =    view.findViewById(R.id.vehicle_tyres_spare);
                            CheckBox spare      =    view.findViewById(R.id.vehicle_spare_tyre);
                            CheckBox water      =    view.findViewById(R.id.vehicle_water);
                            CheckBox dl         =    view.findViewById(R.id.vehicle_driving_licence);
                            CheckBox insurance  =    view.findViewById(R.id.vehicle_insurance);
                            CheckBox lights     =    view.findViewById(R.id.vehicle_lights);
                            CheckBox oil        =    view.findViewById(R.id.vehicle_oil);
                            CheckBox pressure   =    view.findViewById(R.id.vehicle_pressure);

                            int brake, tyre, spares, waters, dls, insurances, light, oils, pressures;

                            if(brakes.isChecked())
                            {
                                brake   =   1;
                            }
                            else
                            {
                                brake   =   0;
                            }

                            if(tyres.isChecked())
                            {
                                tyre    =   1;
                            }
                            else
                            {
                                tyre    =   0;
                            }

                            if(spare.isChecked())
                            {
                                spares  =   1;
                            }
                            else
                            {
                                spares  =   0;
                            }

                            if(water.isChecked())
                            {
                                waters  =   1;
                            }
                            else
                            {
                                waters  =   0;
                            }

                            if(dl.isChecked())
                            {
                                dls =   1;
                            }
                            else
                            {
                                dls =   0;
                            }

                            if(insurance.isChecked())
                            {
                                insurances  =   1;
                            }
                            else
                            {
                                insurances  =   0;
                            }

                            if(lights.isChecked())
                            {
                                light   =   1;
                            }
                            else
                            {
                                light   =   0;
                            }

                            if(oil.isChecked())
                            {
                                oils    =   1;
                            }
                            else
                            {
                                oils    =   0;
                            }

                            if(pressure.isChecked())
                            {
                                pressures   =   1;
                            }
                            else
                            {
                                pressures   =   0;
                            }
                            if(mode_control==0)
                            {


                                if(db.createDailyRouteCheck("Foot", "", 0, 0, ((EditText)view.findViewById(R.id.startTrip__txt_coments)).getText().toString().trim(), commonClass.getCurrentDate(), commonClass.getCurrentTime(), "", 0, "", brake, tyre, spares, waters, dls, insurances, light, oils, pressures))
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
                                        if(db.createDailyRouteCheck("Van", ((EditText)view.findViewById(R.id.startTrip_details_txt_RegNo)).getText().toString().trim(), Float.valueOf(((EditText)view.findViewById(R.id.startTrip_details_txt_Odometerstart)).getText().toString().trim()), 0, ((EditText)view.findViewById(R.id.startTrip__txt_coments)).getText().toString().trim(), commonClass.getCurrentDate(), commonClass.getCurrentTime(), "", 0, "", brake, tyre, spares, waters, dls, insurances, light, oils, pressures))
                                        {
                                            commonClass.destroyDialog(dialog);
                                            commonClass.createToaster(context, "Trip has been started.", Toast.LENGTH_LONG, R.drawable.smile);
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

                    ((Spinner)view.findViewById(R.id.startTrip_sp_modeofMovement)).setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
                    {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View v, int position, long id)
                        {
                            if(parent.getItemAtPosition(position).toString().equals("Foot"))
                            {
                                mode_control=0;
                                view.findViewById(R.id.startTrip_details_txt_Odometerstart).setEnabled(false);
                                view.findViewById(R.id.startTrip_details_txt_RegNo).setEnabled(false);
                                view.findViewById(R.id.vehicle_brakes).setEnabled(false);
                                view.findViewById(R.id.vehicle_tyres_spare).setEnabled(false);
                                view.findViewById(R.id.vehicle_spare_tyre).setEnabled(false);
                                view.findViewById(R.id.vehicle_water).setEnabled(false);
                                view.findViewById(R.id.vehicle_driving_licence).setEnabled(false);
                                view.findViewById(R.id.vehicle_insurance).setEnabled(false);
                                view.findViewById(R.id.vehicle_lights).setEnabled(false);
                                view.findViewById(R.id.vehicle_oil).setEnabled(false);
                                view.findViewById(R.id.vehicle_spare_tyre).setEnabled(false);
                                view.findViewById(R.id.vehicle_pressure).setEnabled(false);
                            }
                            else
                            {
                                mode_control=1;
                                view.findViewById(R.id.startTrip_details_txt_Odometerstart).setEnabled(true);
                                view.findViewById(R.id.startTrip_details_txt_RegNo).setEnabled(true);
                                view.findViewById(R.id.vehicle_brakes).setEnabled(true);
                                view.findViewById(R.id.vehicle_tyres_spare).setEnabled(true);
                                view.findViewById(R.id.vehicle_spare_tyre).setEnabled(true);
                                view.findViewById(R.id.vehicle_water).setEnabled(true);
                                view.findViewById(R.id.vehicle_driving_licence).setEnabled(true);
                                view.findViewById(R.id.vehicle_insurance).setEnabled(true);
                                view.findViewById(R.id.vehicle_lights).setEnabled(true);
                                view.findViewById(R.id.vehicle_oil).setEnabled(true);
                                view.findViewById(R.id.vehicle_spare_tyre).setEnabled(true);
                                view.findViewById(R.id.vehicle_pressure).setEnabled(true);
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }
            }
        });
    }
}
