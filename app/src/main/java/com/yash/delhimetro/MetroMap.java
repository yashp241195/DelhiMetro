package com.yash.delhimetro;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.github.chrisbanes.photoview.PhotoView;
import com.github.chrisbanes.photoview.PhotoViewAttacher;

public class MetroMap extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_metro_map);

        PhotoView photoView = (PhotoView) findViewById(R.id.metro_map);
        photoView.setImageResource(R.mipmap.dmrc);

        photoView.setScaleLevels(7f,
                8f,12f);


    }
}
