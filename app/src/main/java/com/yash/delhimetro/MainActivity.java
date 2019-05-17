package com.yash.delhimetro;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yash.delhimetro.DataProviders.PlaceDetails;
import com.yash.delhimetro.DataProviders.StationDetails;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private ArrayList<String> optionMenu = new ArrayList<>(
            Arrays.asList("Metro (Delhi)","Airport Express"
                    ,"Noida Aqua Line","Gurgaon Rapid Metro"
            )
    );



    private Button submit;
    private HashMap<String, Integer> nameToIndexStation;
    private ArrayList<StationDetails> stationDetailsArrayList;
    private ArrayList<PlaceDetails> placeDetailsArrayList;
    private HashMap<String,String> placeNearbyMetro = new HashMap<>();


    private ArrayList<String> stationNameArrayList = new ArrayList<>();
    private ArrayList<String> placeNameArrayList = new ArrayList<>();


    private ArrayList<String> airport_stationNameArrayList =
            new ArrayList<>();

    private ArrayList<String> metro_delhi_stationNameArrayList =
            new ArrayList<>();

    private ArrayList<String> aqua_stationNameArrayList =
            new ArrayList<>();

    private ArrayList<String> rapid_stationNameArrayList =
            new ArrayList<>();

    private ArrayList<String> airport_placeNameArrayList =
            new ArrayList<>();

    private ArrayList<String> metro_delhi_placeNameArrayList =
            new ArrayList<>();

    private ArrayList<String> aqua_placeNameArrayList =
            new ArrayList<>();

    private ArrayList<String> rapid_placeNameArrayList =
            new ArrayList<>();

    private static final String MY_PREFS_NAME = "Loaded Data";
    private static final String TAG = "MAIN ACTIVITY : ";

    private AutoCompleteTextView acTvFrom, acTvTo;
    private ArrayAdapter<String> stationAdapter,placeAdapter;
    private RadioGroup OptRadioGroup;

    private String ErrorText = "Error : ";

    private FloatingActionButton fab;
    private int themeColor = Color.RED;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,MetroMap.class);
                startActivity(intent);
            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        submit = (Button)findViewById(R.id.actM_searchRoutes);

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
                        acTvTo.setHint(stationNameArrayList.get(1));

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




        acTvFrom.setOnTouchListener(clear_acTvText("FromClear"));
        acTvTo.setOnTouchListener(clear_acTvText("ToClear"));

    }

    // inner class to invoke the clear Text when clicked drawable right

    private View.OnTouchListener clear_acTvText(final String opt){
        return new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= (acTvTo.getRight() - acTvTo.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // your action here

                        if(opt.equals("FromClear"))
                        {
                            acTvFrom.setText("");
                        }
                        else {
                            acTvTo.setText("");
                        }

                        return true;
                    }
                }
                return false;
            }
        };
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

        if(optId == R.id.optToPlace) {
            intent.putExtra("placeNearbyMetro", placeNearbyMetro.get(To));
        }

        startActivity(intent);
    }

    // when clicked on Search Route

    private void OnSubmitSearchRoute(){



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


    private boolean belongsToAirportLine(StationDetails stationDetails){
        ArrayList<String> line = stationDetails.getLine();

        if(line.size() == 1){
            return line.get(0).equals("airport");
        }
        return false;
    }

    private boolean belongsToAqua(StationDetails stationDetails){
        ArrayList<String> line = stationDetails.getLine();

        if(line.size() == 1){
            return line.get(0).equals("aqua");
        }
        return false;
    }

    private boolean belongsToRapid(StationDetails stationDetails){
        ArrayList<String> line = stationDetails.getLine();

        if(line.size() == 1){
            return line.get(0).equals("rapid");
        }
        return false;
    }

    // Load data from resources

    private void LoadData(){

        LoadDataFromSharedPref("stationDetails");
        LoadDataFromSharedPref("placeDetails");
        LoadDataFromSharedPref("nameToIndexStation");


        for (StationDetails stationDetails: stationDetailsArrayList){
            if(belongsToAirportLine(stationDetails)){
                airport_stationNameArrayList.add(stationDetails.getStationName());
            }
            else if(belongsToAqua(stationDetails)){
                aqua_stationNameArrayList.add(stationDetails.getStationName());
            }
            else if(belongsToRapid(stationDetails)){
                rapid_stationNameArrayList.add(stationDetails.getStationName());
            }
            else{
                metro_delhi_stationNameArrayList.add(stationDetails.getStationName());
            }


        }



        for (PlaceDetails placeDetails: placeDetailsArrayList){

            placeNearbyMetro.put(placeDetails.getPlaceName(),
                    placeDetails.getNearbyMetroStation());

            String pd = placeDetails.getNearbyMetroStation();

            int nameToIdx = nameToIndexStation.get(pd);

            if(belongsToAirportLine(stationDetailsArrayList.get(nameToIdx))){
                airport_placeNameArrayList.add(placeDetails.getPlaceName());
            }
            else if(belongsToAqua(stationDetailsArrayList.get(nameToIdx))){
                aqua_placeNameArrayList.add(placeDetails.getPlaceName());
            }
            else if(belongsToRapid(stationDetailsArrayList.get(nameToIdx))){
                rapid_placeNameArrayList.add(placeDetails.getPlaceName());
            }

            else{
                metro_delhi_placeNameArrayList.add(placeDetails.getPlaceName());
            }

        }


        // default
        stationNameArrayList.addAll(metro_delhi_stationNameArrayList);
        placeNameArrayList.addAll(metro_delhi_placeNameArrayList);

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
                        pref.getString("nameToIndexStation", ""), type);
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
        getMenuInflater().inflate(R.menu.activity_main_menu, menu);

        MenuItem item = menu.findItem(R.id.spinner);
        Spinner spinner = (Spinner) item.getActionView();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
                R.layout.spinner_item,optionMenu);

        spinner.setAdapter(adapter);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                stationNameArrayList = new ArrayList<>();
                placeNameArrayList = new ArrayList<>();


                String opt  = optionMenu.get(position);



                switch (opt){
                    case "Metro (Delhi)":

                        themeColor = Color.RED;

                        stationNameArrayList.addAll(metro_delhi_stationNameArrayList);
                        placeNameArrayList.addAll(metro_delhi_placeNameArrayList);

                        break;

                    case "Airport Express":

                        themeColor = Color.parseColor("#FF6600");

                        stationNameArrayList.addAll(airport_stationNameArrayList);
                        placeNameArrayList.addAll(airport_placeNameArrayList);

                        break;


                    case "Noida Aqua Line":


                        themeColor = Color.parseColor("#20B2AA");


                        stationNameArrayList.addAll(aqua_stationNameArrayList);
                        placeNameArrayList.addAll(aqua_placeNameArrayList);

                        break;

                    case "Gurgaon Rapid Metro":

                        themeColor = Color.parseColor("#1B4F72");

                        stationNameArrayList.addAll(rapid_stationNameArrayList);
                        placeNameArrayList.addAll(rapid_placeNameArrayList);

                        break;
                }


                try {
                    getSupportActionBar().setBackgroundDrawable(
                            new ColorDrawable(themeColor));

                    fab.setBackgroundTintList(ColorStateList.valueOf(themeColor));
                    submit.setBackgroundColor(themeColor);

                    TextView textViewFrom = (TextView)findViewById(R.id.actM_labelFrom);
                    textViewFrom.setTextColor(themeColor);

                    TextView textViewTo =(TextView)findViewById(R.id.actM_labelTo);
                    textViewTo.setTextColor(themeColor);


                    int size = OptRadioGroup.getChildCount();

                    if(Build.VERSION.SDK_INT>=21) {

                        ColorStateList colorStateList = new ColorStateList(
                                new int[][]{

                                        new int[]{-android.R.attr.state_enabled}, //disabled
                                        new int[]{android.R.attr.state_enabled} //enabled
                                },
                                new int[] {

                                        Color.BLACK //disabled
                                        ,themeColor //enabled

                                }
                        );

                        for (int i = 0; i < size; i++) {

                            RadioButton rb = (RadioButton) OptRadioGroup.getChildAt(i);
                            rb.setButtonTintList(colorStateList);

                        }
                    }

                }catch (Exception e){
                    e.printStackTrace();
                }

                // use the base context so that the background dropdown stay same

                stationAdapter = new ArrayAdapter<String>(getBaseContext(),
                        android.R.layout.simple_list_item_1,
                        android.R.id.text1, stationNameArrayList);

                placeAdapter = new ArrayAdapter<String>(
                        getBaseContext(),
                        android.R.layout.simple_list_item_1,
                        android.R.id.text1, placeNameArrayList);

                acTvFrom.setAdapter(stationAdapter);
                acTvFrom.setHint(stationNameArrayList.get(0));
                acTvFrom.setThreshold(1);
                acTvFrom.setText("");


                OptRadioGroup.check(R.id.optToStation);

                acTvTo.setAdapter(stationAdapter);
                acTvTo.setHint(stationNameArrayList.get(1));
                acTvTo.setThreshold(1);
                acTvTo.setText("");


//                Log.d("stnList",stationNameArrayList.toString());
                Log.d("size",Integer.valueOf(stationNameArrayList.size()).toString());

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });




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

        if (id == R.id.nav_map) {
            nextActivity = MetroMap.class;

        } else if (id == R.id.nav_webQ) {
            nextActivity = WebQuery.class;

        } else if (id == R.id.nav_allStation) {
            nextActivity = AllStations.class;


        } else if (id == R.id.nav_allPlace) {
            nextActivity = AllPlaces.class;

        } else if (id == R.id.nav_recharge) {

            try {

                Uri uri = Uri.parse("https://paytm.com/metro-card-recharge"); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
            catch(Exception e) {
                e.printStackTrace();
            }


        } else if (id == R.id.nav_share) {

            try {

                String appName = getResources().getString(R.string.app_name);
                Intent shareIntent = new Intent(Intent.ACTION_SEND);

                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, appName);

                String shareMessage= "\nDownload "+appName+" application \n\n";
                shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID +"\n\n";

                shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                startActivity(Intent.createChooser(shareIntent, "choose one"));
            }
            catch(Exception e) {
                e.printStackTrace();
            }

        }


        if(nextActivity!=null)
            startActivity(new Intent(this,nextActivity));

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
