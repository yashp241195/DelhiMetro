package com.yash.delhimetro;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class Explore extends AppCompatActivity {

    private String FromStation,ToStation,ToPlace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore);

        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);   //show back button

        receiveData();
        LoadWidgets();



    }


    private void receiveData(){
        Intent intent = getIntent();
        FromStation = intent.getStringExtra("FromStation");

        int optId = intent.getIntExtra("opt_id",0);

        switch (optId){
            case R.id.optToStation:
                ToStation = intent.getStringExtra("To");
                ToPlace = "None";
                break;

            case R.id.optToPlace:
                ToStation = "None";
                ToPlace = intent.getStringExtra("To");
                break;

        }

    }

    private void LoadWidgets(){
        TextView FromStationTv = (TextView)findViewById(R.id.actE_valFromStation);
        TextView ToStationTv = (TextView)findViewById(R.id.actE_valToStation);
        TextView ToPlaceTv = (TextView)findViewById(R.id.actE_valToPlace);
        TextView ToPlaceLabelTv = (TextView)findViewById(R.id.actE_labelToPlace);


        FromStationTv.setText(FromStation);
        ToStationTv.setText(ToStation);
        ToPlaceTv.setText(ToPlace);

        if(ToPlace.equals("None")){
            ToPlaceLabelTv.setVisibility(View.INVISIBLE);
            ToPlaceTv.setVisibility(View.INVISIBLE);
        }




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
