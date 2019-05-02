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
import android.view.Menu;
import android.widget.SearchView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yash.delhimetro.DataProviders.PlaceDetails;
import com.yash.delhimetro.ViewAdapters.PlaceListAdapter;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class AllPlaces extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;

    private PlaceListAdapter mAdapter;
    private ArrayList<PlaceDetails> placeDetailsArrayList = new ArrayList<>();

    private static final String MY_PREFS_NAME = "Loaded Data";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_places);

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

        LoadDataFromSharedPref("placeDetails");


    }

    private void setUpList(){


       mRecyclerView = (RecyclerView) findViewById(R.id.recyclerViewplaceList);
       mRecyclerView.setHasFixedSize(true);

       mLayoutManager = new LinearLayoutManager(this);
       mRecyclerView.setLayoutManager(mLayoutManager);

       mRecyclerView.addItemDecoration(new DividerItemDecoration(
               getApplicationContext(),
               DividerItemDecoration.VERTICAL));

       mAdapter = new PlaceListAdapter(placeDetailsArrayList);
       mRecyclerView.setAdapter(mAdapter);




   }


    // Receive data from shared preference

    private void LoadDataFromSharedPref(String Opt){
        SharedPreferences pref = getApplicationContext().
                getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);

        Gson gson = new Gson();
        Type type;

        if ("placeDetails".equals(Opt)) {
            type = new TypeToken<ArrayList<PlaceDetails>>() {
            }.getType();
            placeDetailsArrayList = gson.fromJson(
                    pref.getString("placeDetails", ""), type);
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
                    // filter recycler view when query submitted
                    mAdapter.getFilter().filter(query);
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
