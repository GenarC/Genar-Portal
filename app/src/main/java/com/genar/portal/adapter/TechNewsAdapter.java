package com.genar.portal.adapter;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.support.customtabs.CustomTabsIntent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.genar.portal.R;
import com.genar.portal.model.TechNewsItem;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TechNewsAdapter extends RecyclerView.Adapter<TechNewsAdapter.NewsViewHolder>{
    private ArrayList<TechNewsItem> newsList;
    private Context context;

    public TechNewsAdapter(ArrayList<TechNewsItem> newsList, Context context) {
        this.newsList = newsList;
        this.context = context;
    }

    @Override
    public NewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_tech_news, parent, false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NewsViewHolder holder, int position) {
        final TechNewsItem t = newsList.get(position);
        holder.tvName.setText(t.getName());
        holder.tvName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                builder.setToolbarColor(Color.parseColor(t.getColor()));
                CustomTabsIntent customTabsIntent = builder.build();
                customTabsIntent.launchUrl(context, Uri.parse(t.getUrl()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    static class NewsViewHolder extends  RecyclerView.ViewHolder{
        @BindView(R.id.newslist_name)
        TextView tvName;

        NewsViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this,itemView);
        }
    }
}