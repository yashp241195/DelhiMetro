package com.yash.delhimetro;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.yash.delhimetro.ui.main.SectionsPagerAdapter;

import net.yanzm.mth.MaterialTabHost;

import java.util.ArrayList;

public class ExploreRoutes extends AppCompatActivity {



    ArrayList<String> stationList = new ArrayList<>();
    MaterialTabHost tabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_explore_routes);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        assert getSupportActionBar()!=null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        receiveData();

        tabHost = (MaterialTabHost) findViewById(android.R.id.tabhost);
        tabHost.setType(MaterialTabHost.Type.FullScreenWidth);


        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(
                this, getSupportFragmentManager(),stationList);

        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);


        for (int i = 0; i < sectionsPagerAdapter.getCount(); i++) {
            tabHost.addTab(sectionsPagerAdapter.getPageTitle(i));
        }


        viewPager.addOnPageChangeListener(tabHost);




        tabHost.setOnTabChangeListener(new MaterialTabHost.OnTabChangeListener() {
            @Override
            public void onTabSelected(int i) {

                assert getSupportActionBar()!=null;
                switch (i){
                    case 0:

                        getSupportActionBar().setTitle(R.string.tab_text_1);
                        break;
                    case 1:
                        getSupportActionBar().setTitle(R.string.tab_text_2);
                        break;

                }
            }
        });



    }


    private void receiveData(){

        Intent intent = getIntent();
        stationList = intent.getStringArrayListExtra("stationList");

        Log.d("stationListRoute : ",stationList.toString());

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}