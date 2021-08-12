package com.example.pauldavies.isfa;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

public class AssetAdapter extends RecyclerView.Adapter<AssetAdapter.ViewHolder>
{
    ArrayList<Assets> assets;
    Context context;
    private static int currentPosition = -1;

    public AssetAdapter(ArrayList<Assets> assets, Context context)
    {
        this.assets = assets;
        this.context = context;
    }

    CommonClass commonClass;
    DB db;
    SharedPrefs sharedPrefs;
    @NonNull
    @Override
    public AssetAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
        commonClass=new CommonClass();
        sharedPrefs =   new SharedPrefs(context);
        db=new DB(context);

        final View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_assets, viewGroup, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AssetAdapter.ViewHolder viewHolder, final int position)
    {
        Assets asset = assets.get(position);

        viewHolder.assetName.setText(asset.getAssetName());
       // viewHolder.lastServiceDate.setText(asset.getLastServiceDate());
        viewHolder.onSite.setText(asset.isOnSite());
        viewHolder.onCondition.setText(asset.isCondition());
        viewHolder.reason.setText(asset.getReasons());
        viewHolder.nextServiceDate.setText(asset.getNextServiceDate());
        viewHolder.comments.setText(asset.getComments());


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

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView lastServiceDate,comments,assetName,nextServiceDate, reason,onCondition,onSite;
        RelativeLayout relativeLayout;
        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            assetName =itemView.findViewById(R.id.asset_txtName);
            onSite = itemView.findViewById(R.id.asset_txtonsite);
            onCondition = itemView.findViewById(R.id.asset_txtcondition);
            reason = itemView.findViewById(R.id.asset_reason);
          //  lastServiceDate =itemView.findViewById(R.id.asset_last_servicedate);
            nextServiceDate=itemView.findViewById(R.id.asset_nextservicedate);
            comments= itemView.findViewById(R.id.asset_comments);
            relativeLayout=itemView.findViewById(R.id.relative_assets);
        }
    }
}
