package com.yash.delhimetro;

import android.app.SearchManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.SearchView;
import android.util.Log;
import android.view.Menu;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yash.delhimetro.DataProviders.StationDetails;
import com.yash.delhimetro.ViewAdapters.StationListAdapter;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class AllStations extends AppCompatActivity {

    private StationListAdapter mAdapter;

    private ArrayList<StationDetails> stationDetailsArrayList,stationDetailsScrollList;

    private static final String MY_PREFS_NAME = "Loaded Data";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_stations);

        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);   //show back button

        receiveData();
        setUpList();

    }



    private void receiveData(){

        LoadDataFromSharedPref("stationDetails");

    }

    private void setUpList(){

        mAdapter = new StationListAdapter(stationDetailsArrayList);

        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.recyclerViewstationList);
        mRecyclerView.setHasFixedSize(true);

        mRecyclerView.addItemDecoration(new DividerItemDecoration(
                getApplicationContext(),
                DividerItemDecoration.VERTICAL));

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemViewCacheSize(20);
        mRecyclerView.setDrawingCacheEnabled(true);
        mRecyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!recyclerView.canScrollVertically(1))
                    onScrolledToBottom();
            }
        });




    }

    private void onScrolledToBottom() {
        if (stationDetailsScrollList.size() < stationDetailsArrayList.size()) {
            int x, y;
            if ((stationDetailsArrayList.size() - stationDetailsScrollList.size()) >= 50) {
                x = stationDetailsScrollList.size();
                y = x + 50;
            } else {
                x = stationDetailsScrollList.size();
                y = x + stationDetailsArrayList.size() - stationDetailsScrollList.size();
            }
            for (int i = x; i < y; i++) {
                stationDetailsScrollList.add(stationDetailsArrayList.get(i));
            }

            mAdapter.notifyDataSetChanged();
        }


    }


    // Receive data from shared preference

    private void LoadDataFromSharedPref(String Opt){
        SharedPreferences pref = getApplicationContext().
                getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);

        Gson gson = new Gson();
        Type type;

        if ("stationDetails".equals(Opt)) {
            type = new TypeToken<ArrayList<StationDetails>>() {
            }.getType();
            stationDetailsArrayList = gson.fromJson(
                    pref.getString("stationDetails", ""),
                    type);
        }
    }

    // end of receive data from shared preference






    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);


            // Associate searchable configuration with the SearchView
            SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

            SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();

            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
            searchView.setMaxWidth(Integer.MAX_VALUE);

            // listening to search query text change
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String query) {
                    // filter recycler view when text is changed
                    mAdapter.getFilter().filter(query);
                    return true;
                }
            });



        return true;

    }


}
