package com.yash.delhimetro;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;

import java.util.LinkedHashMap;

public class Tutorial extends AppCompatActivity {

    private SliderLayout mDemoSlider;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);

        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);   //show back button


        setUpSlider();
    }



    private void setUpSlider(){

        mDemoSlider = (SliderLayout)findViewById(R.id.slider);

        LinkedHashMap<String,Integer> file_maps = new LinkedHashMap<>();

        file_maps.put("Select the metro network", R.mipmap.intro_3);
        file_maps.put("Airport Express", R.mipmap.intro_4);
        file_maps.put("Find all possible routes", R.mipmap.intro_5);
        file_maps.put("Get the route", R.mipmap.intro_6);
        file_maps.put("Get nearby places", R.mipmap.intro_2of6);
        file_maps.put("Explore place on web", R.mipmap.intro_7);
        file_maps.put("Query using web browser", R.mipmap.intro_8);
        file_maps.put("Search all available places", R.mipmap.intro_9);
        file_maps.put("Pinch Zoom the map", R.mipmap.intro_10);


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
        mDemoSlider.setDuration(2500);


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

    @Override
    protected void onStop() {
        // To prevent a memory leak on rotation, make sure to call stopAutoCycle() on the slider before activity or fragment is destroyed

        if(mDemoSlider!=null)
            mDemoSlider.stopAutoCycle();


        super.onStop();
    }

}
