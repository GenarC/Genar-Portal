package com.genar.portal.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.genar.portal.R;
import com.genar.portal.model.App;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ApplicationAdapter extends RecyclerView.Adapter<ApplicationAdapter.ApplicationViewHolder>{

    private ArrayList<App> appList;
    private Context context;

    public ApplicationAdapter(ArrayList<App> appList, Context context) {
        this.appList = appList;
        this.context = context;
    }

    @Override
    public ApplicationAdapter.ApplicationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_application,parent,false);
        return new ApplicationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ApplicationViewHolder holder, int position) {
        final App app = appList.get(position);
        animate(holder.cvView,position);

        if(position == 0){
            ViewGroup.MarginLayoutParams layoutParams =
                    (ViewGroup.MarginLayoutParams) holder.cvView.getLayoutParams();
            layoutParams.topMargin = 32;
            holder.cvView.setLayoutParams(layoutParams);
        }

        holder.tvAppName.setText(app.getName());
        holder.tvFirmName.setText(app.getFirmName());
        holder.tvVersion.setText(app.getVersion());

        Picasso.with(context)
                .load(app.getPicUrl())
                .placeholder(R.drawable.defaulticon)
                .networkPolicy(NetworkPolicy.OFFLINE)
                .into(holder.imgvAppImage, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {
                        Picasso.with(context)
                                .load(app.getPicUrl())
                                .placeholder(R.drawable.defaulticon)
                                .into(holder.imgvAppImage);
                    }
                });

        holder.btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent viewIntent =new Intent("android.intent.action.VIEW",Uri.parse(app.getAndDownloadUrl()));
                    context.startActivity(viewIntent);
                }catch(Exception e) {
                    Toast.makeText(context.getApplicationContext(),"Unable to Connect Try Again...",
                            Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        });
    }

    private void animate(View view, final int pos) {
        view.animate().cancel();
        view.setTranslationY(100);
        view.setAlpha(0);
        view.animate().alpha(1.0f).translationY(0).setDuration(500).setStartDelay(pos * 100);
    }

    @Override
    public int getItemCount() {
        return appList.size();
    }

    static class ApplicationViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.appListItem_card) CardView cvView;
        @BindView(R.id.applist_appname) TextView tvAppName;
        @BindView(R.id.appist_firmname) TextView tvFirmName;
        @BindView(R.id.applist_version) TextView tvVersion;
        @BindView(R.id.applist_appimage) ImageView imgvAppImage;
        @BindView(R.id.applist_btndownload) Button btnDownload;

        ApplicationViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this,itemView);
        }
    }

}
