package com.yash.delhimetro;

import android.annotation.SuppressLint;
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

import java.util.HashMap;

public class WebQuery extends AppCompatActivity {

    private WebView webView;

    private HashMap<String,String> urlHash = new HashMap<>();

    private boolean screenSizeFull = false;

    private int btnGroupHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_query);

        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);   //show back button


        setUpUrlHash();
        SetupWebView();


        LoadWidget();



        LoadButtonGroupWidgets();


    }


    private void setUpUrlHash(){

        urlHash.put("home","https://www.google.com");
        urlHash.put("metroOfficial","");
        urlHash.put("foodQ","");
        urlHash.put("foodYt","");
        urlHash.put("travelQ","");
        urlHash.put("travelYt","");
        urlHash.put("delhiWiki","");

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

    private void LoadWidget(){

        Button switchWindowSizeBtn = (Button)findViewById(R.id.buttonFull);
        Button backBtn = (Button)findViewById(R.id.buttonBack);
        Button fwdBtn = (Button)findViewById(R.id.buttonForward);
        Button refreshBtn = (Button)findViewById(R.id.buttonRefresh);

        ConstraintLayout constraintLayout = (ConstraintLayout)
                findViewById(R.id.constraintLayoutBtnGroup);

        btnGroupHeight = constraintLayout.getMaxHeight();

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

    private void LoadButtonGroupWidgets(){

        // row 1

        Button showMetroOfficialBtn = (Button)findViewById(R.id.buttonMetroOfficial);
        Button showFoodYtBtn = (Button)findViewById(R.id.buttonFoodYt);

        showMetroOfficialBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadUrlFrom(urlHash.get("metroOfficial"));

            }
        });

        showFoodYtBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadUrlFrom(urlHash.get("foodYt"));

            }
        });

        // row 2
        Button showTravelQBtn = (Button)findViewById(R.id.buttonDelhiTour);
        Button showWikiBtn = (Button)findViewById(R.id.buttonDelhiWiki);


        showTravelQBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadUrlFrom(urlHash.get("travelQ"));

            }
        });



        showWikiBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadUrlFrom(urlHash.get("delhiWiki"));
            }
        });

    }


    // Implementation details of each button


    public void switch_window_size(){

        ConstraintLayout constraintLayout = (ConstraintLayout)
                findViewById(R.id.constraintLayoutBtnGroup);

        Button switchWindowSizeBtn = (Button)findViewById(R.id.buttonFull);
        String btnText;
        int screenSizeIcon;

        if(screenSizeFull){
            constraintLayout.setVisibility(View.VISIBLE);
            constraintLayout.setMaxHeight(btnGroupHeight);
            // Go to windowed screen

            screenSizeFull = false;

            // Now btn should display hint to go to full screen
            btnText = "FULL";
            screenSizeIcon = R.drawable.ic_unfold_more_black_24dp;

        }
        else{
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
