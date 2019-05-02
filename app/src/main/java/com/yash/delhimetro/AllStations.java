package com.yash.delhimetro;

import android.app.SearchManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.SearchView;
import android.view.Menu;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yash.delhimetro.DataProviders.StationDetails;
import com.yash.delhimetro.ViewAdapters.StationListAdapter;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class AllStations extends AppCompatActivity {


    private ArrayList<StationDetails> stationDetailsArrayList,stationDetailsScrollList;

    private StationListAdapter mAdapter;

    private static final String MY_PREFS_NAME = "Loaded Data";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_stations);

        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);   //show back button



        new Handler().postDelayed(new Runnable() {
            public void run() {

                receiveData();
                setUpList();
            }
        }, 50);

    }



    private void receiveData(){

        LoadDataFromSharedPref("stationDetails");

    }

    private void setUpList(){



        mAdapter = new StationListAdapter(stationDetailsArrayList);

        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.recyclerViewstationList);


        mRecyclerView.addItemDecoration(new DividerItemDecoration(
                getApplicationContext(),
                DividerItemDecoration.VERTICAL));


        mRecyclerView.setItemViewCacheSize(20);
        mRecyclerView.setDrawingCacheEnabled(true);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager);


        mRecyclerView.setAdapter(mAdapter);





    }

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
