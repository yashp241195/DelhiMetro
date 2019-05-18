package com.yash.delhimetro;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

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
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class SplashScreen extends AppCompatActivity {

    private ArrayList<StationDetails> stationDetailsArrayList = new ArrayList<>();
    private ArrayList<PlaceDetails> placeDetailsArrayList = new ArrayList<>();
    private HashMap<String,Integer> nameToIndexStation = new HashMap<>();
    private ArrayList<NeighbourList> neighbourListArrayList = new ArrayList<>();

    private JSONObject AdjListJson;

    private static final String TAG = "Splash Screen";
    private static final String MY_PREFS_NAME = "Loaded Data";

    private final String YES = "Yes";
    private final String NO = "No";

    private SliderLayout mDemoSlider;

    private ArrayList<FareMetro> fareMetroArrayList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        if(isUnLoaded("AllDetailsLoaded")) {
            setUpSlider();
        }else{
            removeDisplay();
        }

        DownloadFilesTask myAsyncTasks = new DownloadFilesTask();
        myAsyncTasks.execute();

    }


    private void removeDisplay(){
        ProgressBar progressBar = (ProgressBar)findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        mDemoSlider = (SliderLayout)findViewById(R.id.slider);
        mDemoSlider.setVisibility(View.GONE);
        TextView textView = (TextView)findViewById(R.id.tv2);
        textView.setVisibility(View.GONE);
    }


    /*
    * Image slider
    * */


    private void setUpSlider(){

        mDemoSlider = (SliderLayout)findViewById(R.id.slider);

        LinkedHashMap<String,Integer> file_maps = new LinkedHashMap<>();


        file_maps.put("Welcome to DMRC", R.mipmap.intro1);
        file_maps.put("Go Places", R.mipmap.intro2);
        file_maps.put("Select the metro network", R.mipmap.intro3);
        file_maps.put("Delhi metro network selected", R.mipmap.intro4);
        file_maps.put("Explore all routes", R.mipmap.intro5);
        file_maps.put("Selected roue stations", R.mipmap.intro6);
        file_maps.put("Nearby Places", R.mipmap.intro7);
        file_maps.put("Explore place on internet", R.mipmap.intro8);
        file_maps.put("List of all places", R.mipmap.intro9);
        file_maps.put("List of all stations", R.mipmap.intro10);
        file_maps.put("pinch zoom map", R.mipmap.intro11);
        file_maps.put("Query on in built web browser", R.mipmap.intro12);
        file_maps.put("Airport Express metro network selected", R.mipmap.intro13);
        file_maps.put("Rapid metro network selected", R.mipmap.intro14);
        file_maps.put("Aqua metro network selected", R.mipmap.intro15);



        for(String name : file_maps.keySet()){
            TextSliderView textSliderView = new TextSliderView(this);
            // initialize a SliderLayout
            textSliderView
                    .description(name)
                    .image(file_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.FitCenterCrop);

            mDemoSlider.addSlider(textSliderView);
        }

        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mDemoSlider.setCustomAnimation(new DescriptionAnimation());
        mDemoSlider.setDuration(2500);


    }


    @Override
    protected void onStop() {
        // To prevent a memory leak on rotation, make sure to call stopAutoCycle() on the slider before activity or fragment is destroyed

        if(mDemoSlider!=null)
            mDemoSlider.stopAutoCycle();


        super.onStop();
    }

/*
* Loading data from assets and saving into Shared Preferences
* of the application
* */


    private boolean isUnLoaded(String choice){
        SharedPreferences pref = getApplicationContext().
                getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);

        String LoadStatus = pref.getString(choice,null);

        if(LoadStatus == null) {

            return true;
        }
        return !LoadStatus.equals(YES);
    }


    private synchronized void  saveDataToSharedPref(){

        SharedPreferences pref = getApplicationContext().
                getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();



        if(isUnLoaded("stationDetailsLoaded")) {

            LoadStationAssets();
            LoadPlaceAssets();
            LoadAdjList();
            populateNeighbours();
            readFareFromAsset(this);

            Gson gson = new Gson();

            editor.putString("stationDetails", gson.toJson(stationDetailsArrayList));
            editor.putString("placeDetails",gson.toJson(placeDetailsArrayList));
            editor.putString("nameToIndexStation", gson.toJson(nameToIndexStation));
            editor.putString("neighbourDetails", gson.toJson(neighbourListArrayList));
            editor.putString("neighbourDetails", gson.toJson(neighbourListArrayList));

            editor.putString("AllDetailsLoaded", YES);

        }

        editor.apply();


    }

    private void getFareFromDB(Context context, String From,String To){

        MetroFareDBHandler metroFareDBHandler = new MetroFareDBHandler(context);

        Log.d("Fetched Fare : ", metroFareDBHandler.
                getFareMetro(From,To).toString());

    }

    // setup the next activity details
    private void GoToNextActivity(){
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
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
                fareMetroArrayList.add(fareMetro);

                Log.d("fare fetch",fareMetro.toString());


            }

            metroFareDBHandler.addAll(fareMetroArrayList);
            edit.putString("fareDetailsLoaded",YES);

        } catch (Exception e) {
            //log the exception

            edit.clear();
            e.printStackTrace();
            edit.apply();

        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (Exception e) {
                    //log the exception

                    e.printStackTrace();


                }
            }
        }

        edit.apply();

    }



    /*
    *
    * Download files from Assets folder in Background
    * */

    private class DownloadFilesTask extends AsyncTask<URL, Integer, Integer> {

        private long t1,t2;

        private TextView textView;

        private static final String LOAD_COMPLETE = "Loading Complete";
        private static final String LOAD_FIRST_TIME = "Please wait .. " +
                "\ninitializing resources for the first time";


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            this.textView = (TextView)findViewById(R.id.tv2);

            if(isUnLoaded("AllDetailsLoaded")) {
                this.textView.setText(LOAD_FIRST_TIME);
            }

            t1 = System.currentTimeMillis();

        }

        protected Integer doInBackground(URL... urls) {

            // code that will run in the background

            if(isUnLoaded("AllDetailsLoaded")){

                // Loading data for first time

                saveDataToSharedPref();
            }else {
                try {

                    // Loading data when resources loaded already

                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            return 1;
        }

        protected void onProgressUpdate(Integer... progress) {

            // receive progress updates from doInBackground


        }

        @Override
        protected void onPostExecute(Integer integer) {
            t2 = System.currentTimeMillis();
            Log.d("Loading Time (Res) : ",Long.valueOf(t2-t1).toString());

            // Testing fare DB working ..
            getFareFromDB(SplashScreen.this,"DHAULA KUAN","DWARKA SEC 21 AIRPORT LINE");

            textView.setText(LOAD_COMPLETE);


            try {
                   Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }finally {


                GoToNextActivity();
            }

        }
    }




}
