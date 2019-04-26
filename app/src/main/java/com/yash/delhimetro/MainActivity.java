package com.yash.delhimetro;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.RadioGroup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yash.delhimetro.DataProviders.PlaceDetails;
import com.yash.delhimetro.DataProviders.StationDetails;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ArrayList<StationDetails> stationDetailsArrayList;
    private ArrayList<PlaceDetails> placeDetailsArrayList;
    private HashMap<String,Integer> nameToIndexStation;

    private ArrayList<String> stationNameArrayList = new ArrayList<>();
    private ArrayList<String> placeNameArrayList = new ArrayList<>();

    private static final String MY_PREFS_NAME = "Loaded Data";
    private static final String TAG = "MAIN ACTIVITY : ";

    private AutoCompleteTextView acTvFrom, acTvTo;
    private ArrayAdapter<String> stationAdapter,placeAdapter;
    private RadioGroup OptRadioGroup;

    private String ErrorText = "Error : ";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);


        LoadData();

        LoadWidgets();

        OnSubmitSearchRoute();


    }



    // Load Widgets

    private void LoadWidgets(){

        acTvFrom = (AutoCompleteTextView)findViewById(R.id.actM_actvFrom);
        acTvTo = (AutoCompleteTextView)findViewById(R.id.actM_actvTo);

        stationAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                android.R.id.text1, stationNameArrayList);

        placeAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                android.R.id.text1, placeNameArrayList);


        acTvFrom.setAdapter(stationAdapter);
        acTvFrom.setHint(stationNameArrayList.get(0));
        acTvFrom.setThreshold(1);

        acTvTo.setAdapter(stationAdapter);
        acTvTo.setHint(stationNameArrayList.get(1));
        acTvTo.setThreshold(1);


        OptRadioGroup = (RadioGroup)findViewById(R.id.actM_rb_opt);
        OptRadioGroup.check(R.id.optToStation);


        OptRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                acTvTo.setText("");
                switch (checkedId){

                    case R.id.optToStation:

                        acTvTo.setVisibility(View.VISIBLE);
                        acTvTo.setAdapter(stationAdapter);
                        acTvTo.setHint(stationNameArrayList.get(0));

                        break;

                    case R.id.optToPlace:

                        acTvTo.setVisibility(View.VISIBLE);
                        acTvTo.setAdapter(placeAdapter);
                        acTvTo.setHint(placeNameArrayList.get(0));

                        break;

                    case R.id.optToParking:
                        acTvTo.setVisibility(View.INVISIBLE);

                        break;

                    case R.id.optToToilet:
                        acTvTo.setVisibility(View.INVISIBLE);

                        break;

                }
            }
        });




        acTvFrom.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= (acTvFrom.getRight() - acTvFrom.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // your action here
                        acTvFrom.setText("");
                        return true;
                    }
                }
                return false;
            }
        });

        acTvTo.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= (acTvTo.getRight() - acTvTo.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // your action here
                        acTvTo.setText("");
                        return true;
                    }
                }
                return false;
            }
        });


    }

    // next activity config

    private void GoToNextActivity(){

        String From = acTvFrom.getText().toString();
        String To = acTvTo.getText().toString();
        int optId = OptRadioGroup.getCheckedRadioButtonId();

        Intent intent = new Intent(this,Explore.class);

        intent.putExtra("FromStation",From);
        intent.putExtra("opt_id",optId);
        intent.putExtra("To",To);

        startActivity(intent);
    }

    // when clicked on Search Route

    private void OnSubmitSearchRoute(){

        Button submit = (Button)findViewById(R.id.actM_searchRoutes);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(ValidateInputs()){
                    GoToNextActivity();
                }else{
                    CoordinatorLayout coordinatorLayout = (CoordinatorLayout)
                            findViewById(R.id.actM_coordinatorLayout);

                    Snackbar snackbar = Snackbar
                            .make(coordinatorLayout, ErrorText, Snackbar.LENGTH_LONG);
                    snackbar.show();
                }

            }
        });

    }

    // Testing the inputs

    private boolean ValidateInputs(){

        String From = acTvFrom.getText().toString();
        String To = acTvTo.getText().toString();
        int optId = OptRadioGroup.getCheckedRadioButtonId();

        boolean isValid = false;

        if(From.equals("")){
            ErrorText = "Please don't leave From station empty";
            isValid = false;
        }

        if(To.equals("")){

            String optVal = (optId == R.id.optToStation)?
                "station":(optId == R.id.optToPlace)?"place":"";

            ErrorText = "Please don't leave To "+optVal+" empty";
            isValid = false;
        }

        if(stationNameArrayList.contains(From)){
            Log.d(TAG,"valid From station");

            switch (optId){
                case R.id.optToStation:

                    if(stationNameArrayList.contains(To)) {
                        isValid = true;
                        Log.d(TAG, "valid To station");

                    }else {
                        ErrorText = "Please Enter valid To Station";
                    }

                    break;

                case R.id.optToPlace:

                    if(placeNameArrayList.contains(To)) {
                        isValid = true;
                        Log.d(TAG, "valid To place");
                    }
                    else {
                        ErrorText = "Please Enter valid To Place";
                    }

                    break;

                case R.id.optToParking:

                    isValid = true;
                    Log.d(TAG, "valid To parking");
                    ErrorText="";

                    break;
                case R.id.optToToilet:

                    isValid = true;
                    Log.d(TAG, "valid To Toilet");
                    ErrorText="";

                    break;

            }
        }
        else{

            ErrorText = "Error : Please enter the valid input for From Station";

        }

        return isValid;

    }


    // Load data from resources

    private void LoadData(){

        LoadDataFromSharedPref("stationDetails");
        LoadDataFromSharedPref("placeDetails");
        LoadDataFromSharedPref("nameToIndexStation");

        for (StationDetails stationDetails: stationDetailsArrayList){
            stationNameArrayList.add(stationDetails.getStationName());
        }

        for (PlaceDetails placeDetails: placeDetailsArrayList){
            placeNameArrayList.add(placeDetails.getPlaceName());
        }

    }

    private void LoadDataFromSharedPref(String Opt){
        SharedPreferences pref = getApplicationContext().
                getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);

        Gson gson = new Gson();
        Type type;

        switch (Opt){
            case "stationDetails":
                type =  new TypeToken<ArrayList<StationDetails>>(){}.getType();
                stationDetailsArrayList = gson.fromJson(
                        pref.getString("stationDetails",""),
                        type);
                break;

            case "placeDetails":
                type =  new TypeToken<ArrayList<PlaceDetails>>(){}.getType();
                placeDetailsArrayList = gson.fromJson(
                        pref.getString("placeDetails",""),type);
                break;


            case "nameToIndexStation":
                type = new TypeToken<HashMap<String,Integer>>(){}.getType();
                nameToIndexStation = gson.fromJson(
                        pref.getString("hashMapFare",""),type);
                break;

        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Class nextActivity = null;

        if (id == R.id.map) {
            nextActivity = MetroMap.class;

        } else if (id == R.id.webQ) {
            nextActivity = WebViewExplorePlace.class;

        } else if (id == R.id.allStation) {
            nextActivity = AllStations.class;


        } else if (id == R.id.allPlace) {
            nextActivity = AllPlaces.class;

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        Intent intent = new Intent(this,nextActivity);

        if(nextActivity!=null)
            startActivity(intent);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
