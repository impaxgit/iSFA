package com.example.pauldavies.isfa;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.widget.Button;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class Fragment_JP extends Fragment
{
    View view;
    ExpandableListView expandableListView;
    ArrayList<Journey> journeys =new ArrayList<>();
    Context context;
    int lastExpandedPosition=-1;
    CommonClass commonClass;
    private ArrayList<Journey> plan;
    SharedPrefs sharedPrefs;

    public Fragment_JP()
    {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        context= this.getContext();
        commonClass =   new CommonClass();
        sharedPrefs =   new SharedPrefs(context);


        view=inflater.inflate(R.layout.fragment_fragment__j,container,false);

        //Get Routes
        expandableListView=view.findViewById(R.id.routes_elv_home);

        if(commonClass.checkNetwork(context).equals(Commons.NO_NETWORK))
        {
            commonClass.createToaster(context, "No internet connection.", Toast.LENGTH_LONG, R.drawable.warning);
        }
        else
        {
            new ExternalConnectionTest(new AsyncTaskDelegate()
            {
                @Override
                public void getResult(String result)
                {
                    if(result.contains(Commons.CON_SUCCESS))
                    {
                        new GetRoutes(journeys, getActivity(), new AsyncTaskDelegate()
                        {
                            JourneyPlanAdapter expandableListAdapter;

                            @Override
                            public void getResult(String result)
                            {
                                if (result != null)
                                {

                                    plan    =   new ArrayList<>();

                                    try {
                                        JSONObject jsonObject = new JSONObject(result);
                                        JSONArray jsonArray = jsonObject.optJSONArray("Journey Plan");

                                        for (int i = 0; i < jsonArray.length(); i++)
                                        {
                                            JSONObject object = jsonArray.getJSONObject(i);


                                            Journey journey = new Journey(object.getString("customer_code"), object.getString("customer_name"));

                                            journey.journeys.add(new String[]{object.getString("route"), object.getString("contact_person"), object.getString("contact_person_mobile"), object.getString("orders"), object.getString("outlet_type"), object.getString("decisionmaker")});

                                            plan.add(journey);

                                        }

                                        expandableListAdapter = new JourneyPlanAdapter(plan,context);
                                        expandableListView.setAdapter(expandableListAdapter);

                                        //On Group Click
                                        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener()
                                        {
                                            @Override
                                            public boolean onGroupClick(ExpandableListView parent, View v, final int groupPosition, long id)
                                            {
                                                LayoutInflater layoutInflater1  =   (LayoutInflater)context.getSystemService(LAYOUT_INFLATER_SERVICE);
                                                View view1  =   layoutInflater1.inflate(R.layout.dialog_normal, null, false);
                                                final AlertDialog dialog1 =   commonClass.createCustomDialog(context, view1);
                                                ((ImageView)view1.findViewById(R.id.dialog_normal_icon)).setImageResource(R.drawable.warning);
                                                ((TextView)view1.findViewById(R.id.dialog_normal_title)).setText("Outlet Checkin Alert");
                                                ((TextView)view1.findViewById(R.id.dialog_normal_message)).setText("You are about to check in to the Outlet .\nPlease note that this operation cannot be reversed.\nDo you wish to continue?");
                                                ((Button)view1.findViewById(R.id.button3)).setText("Yes");
                                                ((Button)view1.findViewById(R.id.button4)).setText("No");
                                                (view1.findViewById(R.id.button4)).setOnClickListener(new View.OnClickListener()
                                                {
                                                    @Override
                                                    public void onClick(View v)
                                                    {
                                                     dialog1.dismiss();
                                                    }
                                                });
                                                (view1.findViewById(R.id.button3)).setOnClickListener(new View.OnClickListener()
                                                {
                                                    @Override
                                                    public void onClick(View v)
                                                    {
                                                        Intent intent=new Intent(context,OutletActivity.class);
                                                        startActivity(intent);
                                                    }
                                                });
                                                return true;
                                            }
                                        });

                                    } catch (JSONException e)
                                    {
                                        commonClass.createToaster(context, "There was an error parsing json data.", Toast.LENGTH_LONG, R.drawable.sad);
                                    }
                                }
                            }
                        }).execute();

                        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener()
                        {

                            @Override
                            public void onGroupExpand(int groupPosition)
                            {
                                if (lastExpandedPosition != -1 && groupPosition != lastExpandedPosition)
                                {
                                    expandableListView.collapseGroup(lastExpandedPosition);
                                }
                                lastExpandedPosition = groupPosition;
                            }
                        });
                    }
                    else
                    {
                        commonClass.createToaster(context, "Could not establish external connection.", Toast.LENGTH_LONG, R.drawable.warning);
                    }
                }
            }).execute();

        }

        return view;
    }


}

