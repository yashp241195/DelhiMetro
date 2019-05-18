package com.yash.delhimetro;

import android.app.SearchManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.SearchView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yash.delhimetro.DataProviders.StationDetails;
import com.yash.delhimetro.ViewAdapters.StationListAdapter;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

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



        receiveData();
        setUpList();
        setupHint();


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


    private void setupHint(){
        ImageView imageView = (ImageView)findViewById(R.id.imageViewhint);

        Bitmap bitmap  = drawHintImage();
        imageView.setImageBitmap(bitmap);
    }



    private Bitmap drawHintImage(){

        // Initialize a new Bitmap object
        Bitmap bitmap = Bitmap.createBitmap(
                770, // Width
                400, // Height
                Bitmap.Config.ARGB_8888 // Config
        );


        Canvas canvas = new Canvas(bitmap);

        // drawing ..

        LinkedHashMap<String,Integer> LineColor = new LinkedHashMap<>();


        LineColor.put("Red", Color.RED);
        LineColor.put("Blue",Color.parseColor("#003399"));
        LineColor.put("Yellow",Color.parseColor("#ffcc00"));
        LineColor.put("Green",Color.parseColor("#7CFC00"));
        LineColor.put("Magenta",Color.parseColor("#ff66ff"));
        LineColor.put("Pink",Color.parseColor("#ff0088"));
        LineColor.put("Voilet",Color.parseColor("#9400D3"));

        LineColor.put("Airport express (New Delhi) ",Color.parseColor("#FF6600"));
        LineColor.put("Rapid (Gurugram)",Color.parseColor("#696969"));
        LineColor.put("Aqua (Noida)",Color.parseColor("#00FFFF"));


        try {

            int i=2, x = 30, y = 130, radius = 12;
            canvas.drawText("Metro",x-20,y-50,getTextPaint(Color.RED,50));

            for (Map.Entry<String,Integer> entry : LineColor.entrySet()){

                canvas.drawCircle(x,y,radius,getPaint(entry.getValue()));
                canvas.drawText(entry.getKey(),x+30,y+5,getTextPaint(Color.BLACK,28));

                if(i%3 == 0)
                {x += 160; y = 70;}
                else{  y += 50; }

                i++;
            }

            x = 20;
            y = 250;


            String hintText = "How to Reach via Delhi Metro Main Network";
            canvas.drawText(hintText,x,y,getTextPaint(Color.RED,33));

            hintText = "Aqua: connected via Noida Sec 52 station (Blue)";
            canvas.drawCircle(x+15,y+35,radius,getPaint(LineColor.get("Aqua (Noida)")));
            canvas.drawText(hintText,x+35,y+45,getTextPaint(Color.BLACK,24));

            hintText = "Airport: connected via New Delhi(Yellow) and Dwarka Sec 21(Blue)";
            canvas.drawCircle(x+15,y+75,radius,getPaint(LineColor.get("Airport express (New Delhi) ")));
            canvas.drawText(hintText,x+35,y+85,getTextPaint(Color.BLACK,24));


            hintText = "Rapid: connected via Sikandarpur(Yellow)";
            canvas.drawCircle(x+15,y+120,radius,getPaint(LineColor.get("Rapid (Gurugram)")));
            canvas.drawText(hintText,x+35,y+125,getTextPaint(Color.BLACK,24));

        }catch (Exception e){
            e.printStackTrace();
        }



        return bitmap;
    }



    private Paint getTextPaint(int color, int size){
        // Initialize a new Paint instance to draw the Circle
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(color);
        paint.setTextSize(size);
        paint.setAntiAlias(true);

        return paint;
    }


    private Paint getPaint(int color){
        // Initialize a new Paint instance to draw the Circle
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(color);
        paint.setAntiAlias(true);

        return paint;
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
