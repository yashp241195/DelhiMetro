package com.yash.delhimetro;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.google.gson.Gson;
import com.yash.delhimetro.DataProviders.FareMetro;
import com.yash.delhimetro.DataProviders.MetroFareDBHandler;
import com.yash.delhimetro.DataProviders.NeighbourList;
import com.yash.delhimetro.DataProviders.PlaceDetails;
import com.yash.delhimetro.DataProviders.StationDetails;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

public class SplashScreen extends AppCompatActivity {

    private ArrayList<StationDetails> stationDetailsArrayList = new ArrayList<>();
    private ArrayList<PlaceDetails> placeDetailsArrayList = new ArrayList<>();
    private HashMap<String,Integer> nameToIndexStation = new HashMap<>();
    private ArrayList<NeighbourList> neighbourListArrayList = new ArrayList<>();

    private JSONObject AdjListJson;

    private static final String TAG = "Splash Screen";
    private static final String MANUAL_TEST = "MANUAL TEST";
    private static final String MY_PREFS_NAME = "Loaded Data";
    private static int SLEEP_TIME = 15000;

    private final String YES = "Yes";
    private final String NO = "No";

    private SliderLayout mDemoSlider;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        if(isLoaded("fareDetailsLoaded")){
            SLEEP_TIME = 500;
            // testing fare details
            String From = "VAISHALI";
            String To = "RAJIV CHOWK";

            getFareFromDB(this,From,To);

        }else {
            setUpSlider();
        }

        LoadDataOnDifferentThread();

    }


    private void setUpSlider(){
        mDemoSlider = (SliderLayout)findViewById(R.id.slider);


        HashMap<String,Integer> file_maps = new HashMap<String, Integer>();
        file_maps.put("Hannibal",R.drawable.rcv_ic_clock);
        file_maps.put("Big Bang Theory",R.drawable.rcv_ic_mall);
        file_maps.put("House of Cards",R.drawable.rcv_ic_parking);
        file_maps.put("Game of Thrones", R.drawable.rcv_ic_toilet);

        for(String name : file_maps.keySet()){
            TextSliderView textSliderView = new TextSliderView(this);
            // initialize a SliderLayout
            textSliderView
                    .description(name)
                    .image(file_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit);

            mDemoSlider.addSlider(textSliderView);
        }

        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mDemoSlider.setCustomAnimation(new DescriptionAnimation());
        mDemoSlider.setDuration(2000);


    }


    @Override
    protected void onStop() {
        // To prevent a memory leak on rotation, make sure to call stopAutoCycle() on the slider before activity or fragment is destroyed

        if(mDemoSlider!=null)
            mDemoSlider.stopAutoCycle();


        super.onStop();
    }



    private boolean isLoaded(String choice){
        SharedPreferences pref = getApplicationContext().
                getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);

        String LoadStatus = pref.getString(choice,null);

        if(LoadStatus == null) {

            return false;
        }
        return LoadStatus.equals(YES);
    }


    private void saveDataToSharedPref(){

        SharedPreferences pref = getApplicationContext().
                getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        Gson gson = new Gson();




        // Saving data to shared preference

        if(!isLoaded("stationDetailsLoaded")) {

            editor.putString("stationDetails",
                    gson.toJson(stationDetailsArrayList));

            editor.putString("stationDetailsLoaded", YES);
            Log.d("Loading status : ","stationDetailsLoaded YES");
        }
        else{
            Log.d("Loading status : ","stationDetailsLoaded YES already");
        }
        if(!isLoaded("placeDetailsLoaded")) {
            editor.putString("placeDetails",
                    gson.toJson(placeDetailsArrayList));

            editor.putString("placeDetailsLoaded", YES);
            Log.d("Loading status : ","placeDetailsLoaded YES");

        }
        else{
            Log.d("Loading status : ","placeDetailsLoaded YES already");
        }
        if(!isLoaded("nameToIndexStationLoaded")) {

            editor.putString("nameToIndexStation",
                    gson.toJson(nameToIndexStation));

            editor.putString("nameToIndexStationLoaded", YES);
            Log.d("Loading status : ","nameToIndexStationLoaded YES");

        }
        else{
            Log.d("Loading status : ","nameToIndexStationLoaded YES already");
        }
        if(!isLoaded("neighbourDetailsLoaded")) {
            editor.putString("neighbourDetails",
                    gson.toJson(neighbourListArrayList));

            editor.putString("neighbourDetailsLoaded", YES);
            Log.d("Loading status : ","neighbourDetailsLoaded YES");

        }else{
            Log.d("Loading status : ","neighbourDetailsLoaded YES already");
        }

        if(!isLoaded("fareDetailsLoaded")){
            String From = "DHAULA KUAN";
            String To = "DWARKA SEC 21 AIRPORT";

            readFareFromAsset(this);
            getFareFromDB(this,From,To);

            editor.putString("fareDetailsLoaded", YES);
            Log.d("Loading status : ","fareDetailsLoaded YES");

        }else{
            Log.d("Loading status : ","fareDetailsLoaded YES already");
        }

        editor.apply();


    }

    private void getFareFromDB(Context context, String From,String To){

        MetroFareDBHandler metroFareDBHandler = new MetroFareDBHandler(context);

        Log.d("Fetched Fare : ", metroFareDBHandler.
                getFareMetro(From,To).toString());

    }

    private void readFareFromAsset(Context context){


        SharedPreferences pref = getApplicationContext().
                getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);

        SharedPreferences.Editor edit = pref.edit();

        BufferedReader reader = null;
        try {
            MetroFareDBHandler metroFareDBHandler = new MetroFareDBHandler(context);

            reader = new BufferedReader(
                    new InputStreamReader(
                            getAssets().open("fareList.txt")
                    )
            );




            // do reading, usually loop until end of file reading
            String mLine;
            while ((mLine = reader.readLine()) != null) {

                // process line


                String[] row = mLine.split("->");

                String From = row[0];
                String To = row[1];
                row[2] = row[2].equals("Null")?"-1":row[2];
                int fare = Integer.parseInt(row[2]);

                FareMetro fareMetro = new FareMetro(From,To,fare);
                Log.d("Fare : ",fareMetro.toString());
                metroFareDBHandler.addFareMetro(fareMetro);



            }

            edit.putString("fareDetailsLoaded",YES);

        } catch (Exception e) {
            //log the exception

            edit.clear();
            e.printStackTrace();
            edit.putString("fareDetailsLoaded",NO);
            edit.apply();

        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (Exception e) {
                    //log the exception

                    e.printStackTrace();
                    edit.putString("fareDetailsLoaded",NO);


                }
            }
        }

        edit.apply();

    }



    // Please don't uncomment : Copy Paste as it is across different activities

    // Receive data from shared preference

    /*

        private HashMap<String,Integer> hashMapFare = new HashMap<>();
        private ArrayList<StationDetails> stationDetailsArrayList = new ArrayList<>();
        private ArrayList<PlaceDetails> placeDetailsArrayList = new ArrayList<>();
        private HashMap<String,Integer> nameToIndexStation = new HashMap<>();
        private ArrayList<NeighbourList> neighbourListArrayList = new ArrayList<>();
    */

    //

    /*
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

            case "hashMapFare":
                type = new TypeToken<HashMap<String,Integer>>(){}.getType();
                hashMapFare = gson.fromJson(
                        pref.getString("hashMapFare",""),type);
                break;

            case "nameToIndexStation":
                type = new TypeToken<HashMap<String,Integer>>(){}.getType();
                nameToIndexStation = gson.fromJson(
                        pref.getString("nameToIndexStation",""),type);
                break;

            case "neighbourDetails":
                type = new TypeToken<ArrayList<NeighbourList>>(){}.getType();
                neighbourListArrayList = gson.fromJson(
                        pref.getString("neighbourDetails",""),type);
                break;
        }

    }

    */

    // end of receive data from shared preference

    // setup the next activity details
    private void GoToNextActivity(){
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
    }

    // Test the correctness of loaded data manually

    private void ManualTest(long time){
        String result = "\tLoad Time : "+time+"\n";
        Log.d(MANUAL_TEST,result);
    }

    // Load data on different thread to reduce the load on UI or main Thread
    // Load data from asset folder to Java Class and Java Class to
    // Shared Preferences using GSON Object implemented via GSON library


    private void LoadDataOnDifferentThread(){





        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity



                long t1 = System.currentTimeMillis();
                LoadDataFromAssets();

                saveDataToSharedPref();

                long t2 = System.currentTimeMillis();

                // testing the loaded data correctness manually

                ManualTest(t2-t1);

               GoToNextActivity();

            }
        }, SLEEP_TIME);




    }

    // used to create Query string for the hashMapFare of fare data

    private String getFareQuery(String FromStn,String ToStn){
        return (FromStn.compareTo(ToStn)>0)?
                ToStn+"->"+FromStn:FromStn+"->"+ToStn;
    }

    // Load Data from Assets sequentially

    private void LoadDataFromAssets(){
        LoadStationAssets();
        LoadPlaceAssets();
        LoadAdjList();
        populateNeighbours();

    }

    /*
    *
    * Load Data from Assets Folder to the Java Data Providers
    * for the further usage of data in the application
    *
    * */


    private void LoadStationAssets(){
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(
                    new InputStreamReader(getAssets().open("stationList.txt")));

            // do reading, usually loop until end of file reading
            String mLine, stationName, hasToilet, hasParking;
            String [] row;

            // skipping first line used for table columns
            reader.readLine();

            int i = 0;

            while ((mLine = reader.readLine()) != null) {
                //process line
                row = mLine.split("->");

                stationName = row[0].trim();
                hasToilet = row[1].trim();
                hasParking = row[2].trim();

                StationDetails stationDetails = new StationDetails();

                stationDetails.setStationName(stationName);
                stationDetails.setHasToilet(hasToilet.equals("true"));
                stationDetails.setHasParking(hasParking.equals("true"));
                stationDetails.setHasNearbyMall(false);

                nameToIndexStation.put(stationName,i);
                stationDetailsArrayList.add(stationDetails);

                i++;


            }
        } catch (IOException e) {
            //log the exception

        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    //log the exception
                }
            }
        }
    }





    private void LoadPlaceAssets(){

        BufferedReader reader = null;
        try {
            reader = new BufferedReader(
                    new InputStreamReader(getAssets().open("placeList.txt")));

            // do reading, usually loop until end of file reading
            String mLine, placeName, nearbyStation, distance, placeType,placeImage;
            String [] row;

            // skip the first column label line
            reader.readLine();

            while ((mLine = reader.readLine()) != null) {
                //process line

                row = mLine.split("->");

                placeName = row[0].trim();
                nearbyStation = row[1].trim();
                distance = row[2].trim().split(" ")[0];
                placeType = row[3].trim();
                placeImage = row[4].trim();

                // don't add if nearby station doesn't exist in stationDetailList

                if (nameToIndexStation.containsKey(nearbyStation)){

                    PlaceDetails placeDetails = new PlaceDetails();

                    placeDetails.setPlaceName(placeName);
                    placeDetails.setNearbyMetroStation(nearbyStation);
                    float d = Float.parseFloat(distance);
                    placeDetails.setDistance(d);
                    if(d<=1){
                        int id = nameToIndexStation.get(nearbyStation);
                        stationDetailsArrayList.get(id).setHasNearbyMall(true);
                    }
                    placeDetails.setPlaceType(placeType);
                    placeDetails.setPlaceImage(placeImage);

                    placeDetailsArrayList.add(placeDetails);
                }


            }
        } catch (IOException e) {
            //log the exception
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    //log the exception
                }
            }
        }
    }

    private void LoadAdjList(){


        BufferedReader reader = null;
        try {
            reader = new BufferedReader(
                    new InputStreamReader(getAssets().open("adjList.json")));

            // do reading, usually loop until end of file reading
            String mLine;
            String jsonString = "";

            while ((mLine = reader.readLine()) != null) {
                //process line
                jsonString = jsonString.concat(mLine);

            }
            AdjListJson = new JSONObject(jsonString);

        } catch (IOException e) {
            //log the exception
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    //log the exception
                }
            }
        }
    }

    private void populateNeighbours(){

        // neighbourList
        try {
            JSONObject jsonObject = AdjListJson;
            JSONArray list = jsonObject.getJSONArray("adjList");

            for (int i = 0; i < list.length(); i++) {
                NeighbourList neighbourList = new NeighbourList();

                JSONObject currentList =  list.getJSONObject(i);

                String line = currentList.getString("line").trim();
                int timeInterval = Integer.parseInt(
                        currentList.getString("timeInterval").trim().split(" ")[0]);

                neighbourList.setLine(line);
                neighbourList.setTimeInterval(timeInterval);

                JSONArray jsonArrayStations = currentList.getJSONArray("stationList");
                ArrayList<String> stationList = new ArrayList<>();

                String stationName;
                int idx;

                for (int j = 0; j < jsonArrayStations.length(); j++) {
                    stationName = jsonArrayStations.getString(j).trim();
                    idx = nameToIndexStation.get(stationName);
                    stationDetailsArrayList.get(idx).getLine().add(line);
                    stationList.add(stationName);
                }

                neighbourList.setStationList(stationList);

                neighbourListArrayList.add(neighbourList);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }




}
