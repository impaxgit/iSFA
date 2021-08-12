package com.example.pauldavies.isfa;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

public class AssetTrackingAdapter extends RecyclerView.Adapter<AssetTrackingAdapter.ViewHolder>
{
    ArrayList<Assets> assets;
    Context context;
    CommonClass commonClass;
    DB db;
    SharedPrefs sharedPrefs;

    private static int currentPosition = -1;
    String selectedCondition,selectedItem;
    int mode_control=1;

    public AssetTrackingAdapter(ArrayList<Assets> assets, Context context)
    {
        this.assets = assets;
        this.context = context;
    }

    @SuppressLint("ClickableViewAccessibility")
    @NonNull
    @Override
    public AssetTrackingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
        commonClass=new CommonClass();
        sharedPrefs =   new SharedPrefs(context);
        db=new DB(context);

        final View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.asset_tracking, viewGroup, false);

        Spinner onCondition = v.findViewById(R.id.asset_spcondition);
        onCondition.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                selectedCondition = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });


        final Spinner onSite = v.findViewById(R.id.asset_sponsite);
        onSite.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                selectedItem = parent.getItemAtPosition(position).toString();

                if(parent.getItemAtPosition(position).toString().equals("YES"))
                {
                    mode_control=0;
                    v.findViewById(R.id.asset_spreason).setEnabled(false);
                    v.findViewById(R.id.asset_reason).setEnabled(false);

                    v.findViewById(R.id.asset_txtcondition).setEnabled(true);
                    v.findViewById(R.id.asset_spcondition).setEnabled(true);
                }
                else
                {
                    mode_control=1;
                    v.findViewById(R.id.asset_txtcondition).setEnabled(false);
                    v.findViewById(R.id.asset_spcondition).setEnabled(false);

                    v.findViewById(R.id.asset_spreason).setEnabled(true);
                    v.findViewById(R.id.asset_reason).setEnabled(true);
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //
        //Date Picker

        final EditText edittext=v.findViewById(R.id.asset_nextservicedate);
        final DatePickerDialog[] datePickerDialog = new DatePickerDialog[1];

        edittext.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                switch (event.getAction())
                {
                    case MotionEvent.ACTION_DOWN:

                        final Calendar cldr = Calendar.getInstance();
                        int day = cldr.get(Calendar.DAY_OF_MONTH);
                        int month = cldr.get(Calendar.MONTH);
                        int year = cldr.get(Calendar.YEAR);

                        // date picker dialog
                        datePickerDialog[0] = new DatePickerDialog(context,
                                new DatePickerDialog.OnDateSetListener()
                                {
                                    @Override
                                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
                                    {
                                        edittext.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                                    }
                                }, year, month, day);
                                    datePickerDialog[0].show();

                        break;
                }

                return false;
            }
        });

        v.findViewById(R.id.asset_btnSave).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v2) {
                String assetno = ((TextView) v.findViewById(R.id.asset_txtNo)).getText().toString().trim();
                String assetname = ((TextView) v.findViewById(R.id.asset_txtName)).getText().toString().trim();
                String reason = ((EditText) v.findViewById(R.id.asset_spreason)).getText().toString().trim();
                String comments = ((EditText) v.findViewById(R.id.asset_comments)).getText().toString().trim();
                String nextServicedate = ((EditText) v.findViewById(R.id.asset_nextservicedate)).getText().toString().trim();

                if (!nextServicedate.isEmpty()) {
                    if (db.trackAsset(assetno, assetname, selectedItem, selectedCondition, reason, commonClass.getCurrentDate(), commonClass.getCurrentTime(), edittext.getText().toString(), comments) != -1)
                    {
                        Snackbar.make(v2,"Update successfully",Snackbar.LENGTH_SHORT).show();

                    }
                    else
                        {
                        commonClass.createToaster(context, "Could not save the record.\nTry again!", Toast.LENGTH_LONG, R.drawable.sad);
                      }
                }
                else
                    {
                    ((EditText) v.findViewById(R.id.asset_nextservicedate)).setError("Next Service Date?");
                    (v.findViewById(R.id.asset_nextservicedate)).requestFocus();
                    }
            }

        });
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AssetTrackingAdapter.ViewHolder viewHolder, final int position)
    {
        Assets asset = assets.get(position);

        viewHolder.assetName.setText(asset.getAssetName());
        viewHolder.lastServiceDate.setText(asset.getLastServiceDate());


        final boolean isExpanded = position==currentPosition;
        final Animation slideDown= AnimationUtils.loadAnimation(context,R.anim.anim);
        viewHolder.relativeLayout.setVisibility(isExpanded?View.VISIBLE:View.GONE);
        viewHolder.itemView.setActivated(isExpanded);
        viewHolder.relativeLayout.startAnimation(slideDown);

        viewHolder.assetName.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                currentPosition = isExpanded ? -1:position;
                notifyDataSetChanged();
            }
        });

    }

    @Override
    public int getItemCount()
    {
        return assets.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder
    {
        //  TextView onSite,onCondition,reason;
        Spinner  onCondition,onSite;
        EditText nextServiceDate, reason;
        TextView lastServiceDate,comments,assetName;
        Button track;
        RelativeLayout relativeLayout;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);

            relativeLayout=itemView.findViewById(R.id.asset_relativelayout);
            assetName =itemView.findViewById(R.id.asset_txtName);
            onSite = itemView.findViewById(R.id.asset_sponsite);
            onCondition = itemView.findViewById(R.id.asset_spcondition);
            reason = itemView.findViewById(R.id.asset_spreason);
            lastServiceDate =itemView.findViewById(R.id.asset_lastservicingdate);
            nextServiceDate=itemView.findViewById(R.id.asset_nextservicedate);
            comments= itemView.findViewById(R.id.asset_comments);
            track   = itemView.findViewById(R.id.asset_btnSave);
        }
    }
}
