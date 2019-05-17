package com.yash.delhimetro;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

import java.util.HashMap;

public class WebViewExplorePlace extends AppCompatActivity {

    private String placeName, placeType, placeNearbyStationText;
    private HashMap<String,String> urlHash = new HashMap<>();

    private WebView webView;

    private boolean screenSizeFull = false;
    private int btnGroupHeight,HeadingHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view_explore_place);

        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);   //show back button


        receiveData();
        setUpUrlHash();
        SetupWebView();

        LoadWidget();

        LoadButtonGroupUpperWidgets();
        LoadButtonGroupLowerWidgets();


    }

    private void receiveData(){

        Intent intent = getIntent();

        placeName = intent.getStringExtra("placeName");
        placeType = intent.getStringExtra("placeType");
        placeNearbyStationText = intent.
                getStringExtra("placeNearbyMetro");


    }


    private void LoadWidget(){

        TextView placeNameTv = (TextView) findViewById(R.id.actW_placeNameTv);
        placeNameTv.setText(placeName);

        TextView placeTypeTv = (TextView) findViewById(R.id.actW_placeTypeTv);
        placeTypeTv.setText(placeType);

        TextView placeNearbyStationTextTv = (TextView) findViewById(R.id.actW_placeNearbyMetroTv);
        placeNearbyStationTextTv.setText(placeNearbyStationText);
    }


    private void setUpUrlHash(){

        urlHash.put("home","https://www.google.com/search?q="+placeName);


        urlHash.put("btnTimings","https://www.google.com/search?q="+placeName+"+timings");
        urlHash.put("btnNearbyMetro","https://www.google.com/search?q=metro+stations+near+"+placeName);
        urlHash.put("btnPetrol","https://www.google.com/search?q=petrol+pump+near+"+placeName);



        urlHash.put("btnImages","https://www.google.com/search?tbm=isch&q="+placeName);
        urlHash.put("btnATM","https://www.google.com/search?q=atm+near+"+placeName);
        urlHash.put("btnYoutube","https://www.youtube.com/results?search_query="+placeName);



    }

    @SuppressLint("SetJavaScriptEnabled")
    private void SetupWebView(){

        webView = (WebView)findViewById(R.id.actW_webView);

        ViewGroup.LayoutParams params = webView.getLayoutParams();


        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        webView.setLayoutParams(params);
        webView.requestLayout();


        webView.setWebViewClient(new WebViewClient());
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        // default home page
        webView.loadUrl(urlHash.get("home"));

    }

    private void LoadButtonGroupUpperWidgets(){

        Button switchWindowSizeBtn = (Button)findViewById(R.id.buttonFull);
        Button backBtn = (Button)findViewById(R.id.buttonBack);
        Button fwdBtn = (Button)findViewById(R.id.buttonForward);
        Button refreshBtn = (Button)findViewById(R.id.buttonRefresh);

        ConstraintLayout constraintLayout = (ConstraintLayout)
                findViewById(R.id.constraintLayoutBtnGroup);

        ConstraintLayout constraintLayoutH = (ConstraintLayout)
                findViewById(R.id.constraintLayoutHeading);

        btnGroupHeight = constraintLayout.getMaxHeight();
        HeadingHeight = constraintLayoutH.getMaxHeight();

        switchWindowSizeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch_window_size();

            }
        });


        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Go_back();
            }
        });

        fwdBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Go_Fwd();

            }
        });

        refreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                do_reload();
            }
        });

    }

    private void LoadButtonGroupLowerWidgets(){

        Button btnTimings = (Button)findViewById(R.id.buttonTimings);
        Button btnNearbyMetro = (Button)findViewById(R.id.buttonNearbyMetro);
        Button btnPetrol = (Button)findViewById(R.id.buttonPetrol);

        // row 1


        btnTimings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadUrlFrom(urlHash.get("btnTimings"));
            }
        });

        btnNearbyMetro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadUrlFrom(urlHash.get("btnNearbyMetro"));
            }
        });

        btnPetrol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadUrlFrom(urlHash.get("btnPetrol"));
            }
        });

        // row 2

        Button btnImages = (Button)findViewById(R.id.buttonImages);
        Button btnATM = (Button)findViewById(R.id.buttonATM);
        Button btnYoutube = (Button)findViewById(R.id.buttonYoutube);



        btnImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadUrlFrom(urlHash.get("btnImages"));

            }
        });

        btnATM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadUrlFrom(urlHash.get("btnATM"));
            }
        });

        btnYoutube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadUrlFrom(urlHash.get("btnYoutube"));
            }
        });



    }


    // Implementation details of each button


    public void switch_window_size(){

        ConstraintLayout constraintLayout = (ConstraintLayout)
                findViewById(R.id.constraintLayoutBtnGroup);

        ConstraintLayout constraintLayoutH = (ConstraintLayout)
                findViewById(R.id.constraintLayoutHeading);


        Button switchWindowSizeBtn = (Button)findViewById(R.id.buttonFull);
        String btnText;
        int screenSizeIcon;

        if(screenSizeFull){

            constraintLayoutH.setVisibility(View.VISIBLE);
            constraintLayoutH.setMaxHeight(HeadingHeight);

            constraintLayout.setVisibility(View.VISIBLE);
            constraintLayout.setMaxHeight(btnGroupHeight);
            // Go to windowed screen

            screenSizeFull = false;

            // Now btn should display hint to go to full screen
            btnText = "FULL SCREEN";
            screenSizeIcon = R.drawable.ic_unfold_more_black_24dp;

        }
        else{
            constraintLayoutH.setVisibility(View.INVISIBLE);
            constraintLayoutH.setMaxHeight(0);

            constraintLayout.setVisibility(View.INVISIBLE);
            constraintLayout.setMaxHeight(0);
            // Go to full screen
            screenSizeFull = true;

            // Now btn should display hint to go to Window screen
            btnText = "WINDOW";
            screenSizeIcon = R.drawable.ic_unfold_less_black_24dp;

        }

        Drawable drawableRight = getResources().getDrawable(screenSizeIcon);

        switchWindowSizeBtn.setText(btnText);

        switchWindowSizeBtn.setCompoundDrawablesWithIntrinsicBounds(null,null,
                drawableRight,null);

    }


    private void do_reload(){

        webView.setWebViewClient(new WebViewClient());
        webView.reload();

    }


    private void Go_back(){
        webView.setWebViewClient(new WebViewClient());

        if (webView.canGoBack()){
            webView.goBack();
        }

    }


    private void Go_Fwd(){
        webView.setWebViewClient(new WebViewClient());

        if (webView.canGoForward()){
            webView.goForward();
        }

    }


    // load data from url


    private void LoadUrlFrom(String url){

        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(url);

    }




    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
