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
