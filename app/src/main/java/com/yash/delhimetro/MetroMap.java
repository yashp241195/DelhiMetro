package com.yash.delhimetro;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.github.chrisbanes.photoview.PhotoView;

public class MetroMap extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_metro_map);

        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);   //show back button


        PhotoView photoView = (PhotoView) findViewById(R.id.metro_map);
        photoView.setImageResource(R.mipmap.dmrc);

        photoView.setScaleLevels(7f,
                8f,12f);


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
