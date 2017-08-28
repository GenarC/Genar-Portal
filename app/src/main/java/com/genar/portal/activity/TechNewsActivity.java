package com.genar.portal.activity;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.customtabs.CustomTabsIntent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.genar.portal.R;
import com.genar.portal.adapter.TechNewsAdapter;
import com.genar.portal.model.TechNewsItem;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TechNewsActivity extends BaseActivity implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener{

    @BindView(R.id.technews_slider)
    SliderLayout slSlider;

    @BindView(R.id.technews_rcview)
    RecyclerView rvNewsView;

    private Uri targetUri;
    private String customTabColor;


    private ArrayList<TechNewsItem> techNews = new ArrayList<>();


    public void init(){
        techNews.add(new TechNewsItem("Wired","https://www.wired.com/","#F6F6F6"));
        techNews.add(new TechNewsItem("Webrazzi","https://en.webrazzi.com/","#f7d50a"));
        techNews.add(new TechNewsItem("The Verge","https://www.theverge.com/tech","#000000"));
        techNews.add(new TechNewsItem("TechCrunch","https://techcrunch.com/","#0A9E01"));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tech_news);

        ButterKnife.bind(this);

        init();

        setSlider();

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(TechNewsActivity.this);
        rvNewsView.setLayoutManager(layoutManager);
        TechNewsAdapter techAdapter = new TechNewsAdapter (techNews,TechNewsActivity.this);
        rvNewsView.setAdapter(techAdapter);

    }

    @Override
    public void onSliderClick(BaseSliderView slider) {
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        builder.setToolbarColor(Color.parseColor(customTabColor));
        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.launchUrl(this, targetUri);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if(position != 0){
            position--;
        }else{
            position = 3;
        }
        targetUri = Uri.parse(techNews.get(position).getUrl());
        customTabColor = techNews.get(position).getColor();
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public void setSlider(){
        HashMap<String,Integer> file_maps = new HashMap<>();
        file_maps.put("Wired",R.drawable.wired);
        file_maps.put("Webrazzi",R.drawable.webrazzi);
        file_maps.put("The Verge",R.drawable.verge);
        file_maps.put("Techcrunch", R.drawable.techcrunch);

        for(String name : file_maps.keySet()){
            TextSliderView textSliderView = new TextSliderView(this);
            // initialize a SliderLayout
            textSliderView
                    .description(name)
                    .image(file_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener(this);

            //add your extra information
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle()
                    .putString("extra",name);

            slSlider.addSlider(textSliderView);
        }
        slSlider.setPresetTransformer("Foreground2Background");
        slSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        slSlider.setCustomAnimation(new DescriptionAnimation());
        slSlider.setDuration(3000);
        slSlider.addOnPageChangeListener(this);
    }
}