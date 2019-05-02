package com.yash.delhimetro;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.github.chrisbanes.photoview.PhotoView;

public class MetroMap extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_metro_map);

        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);   //show back button

        new Handler().postDelayed(new Runnable() {
            public void run() {

                PhotoView photoView = (PhotoView) findViewById(R.id.metro_map);
                photoView.setImageResource(R.mipmap.dmrc);

                photoView.setScaleLevels(7f,
                        8f,12f);

            }
        }, 50);


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
