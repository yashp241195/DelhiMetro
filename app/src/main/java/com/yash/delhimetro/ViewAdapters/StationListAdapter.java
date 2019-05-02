package com.yash.delhimetro.ViewAdapters;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.yash.delhimetro.DataProviders.StationDetails;
import com.yash.delhimetro.R;

import java.util.ArrayList;
import java.util.HashMap;

public class StationListAdapter extends
        RecyclerView.Adapter<StationListAdapter.MyViewHolder>
        implements Filterable
{

    private ArrayList<StationDetails> stationDetailsListFull,
            stationDetailsListFiltered;


    public StationListAdapter(ArrayList<StationDetails> stationList) {

        this.stationDetailsListFull = stationList;
        this.stationDetailsListFiltered = stationList;


    }

    @Override
    public int getItemCount() {
        return stationDetailsListFiltered.size();
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.row_station_listitem,
                        viewGroup,
                        false
                );

        return new MyViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {

        StationDetails stationDetails = stationDetailsListFiltered.get(i);

            String name = stationDetails.getStationName();
            Boolean hasNearbyMall = stationDetails.getHasNearbyMall();
            Boolean hasParking = stationDetails.getHasParking();
            Boolean hasToilet = stationDetails.getHasToilet();
            ArrayList<String> LineInfo = stationDetails.getLine();

            myViewHolder.stnName.setText(name);

            myViewHolder.lineIndicator.setImageBitmap(getLineIndicatorBitmap(LineInfo));


            int transparent = 0;

            int hasNearbyMallText = (hasNearbyMall)?R.drawable.rcv_ic_mall:transparent;
            int hasToiletText = (hasNearbyMall)?R.drawable.rcv_ic_toilet:transparent;
            int hasParkingText = (hasNearbyMall)?R.drawable.rcv_ic_parking:transparent;

            myViewHolder.toilet.setImageResource(hasToiletText);
            myViewHolder.mall.setImageResource(hasNearbyMallText);
            myViewHolder.parking.setImageResource(hasParkingText);



    }





    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView stnName;
        ImageView lineIndicator,toilet, parking, mall;


        MyViewHolder(View view) {

            super(view);

            stnName = (TextView) view.findViewById(R.id.row_li_stationName);
            lineIndicator = (ImageView) view.findViewById(R.id.row_li_line_indicator);

            toilet = (ImageView) view.findViewById(R.id.row_li_toilet);
            parking = (ImageView) view.findViewById(R.id.row_li_parking);
            mall = (ImageView) view.findViewById(R.id.row_li_mall);


        }
    }


    @Override
    public Filter getFilter() {

        return new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                    String charString = charSequence.toString().toUpperCase();

                    if (charString.isEmpty()) {
                        stationDetailsListFiltered = stationDetailsListFull;
                    } else {
                        ArrayList<StationDetails> filteredList = new ArrayList<>();
                        for (StationDetails row : stationDetailsListFull) {

                            if (row.getStationName().toUpperCase().contains(charString)
                            ) {
                                filteredList.add(row);
                            }
                        }
                        stationDetailsListFiltered = filteredList;
                    }

                    FilterResults filterResults = new FilterResults();
                    filterResults.values = stationDetailsListFiltered;

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {

                stationDetailsListFiltered = (ArrayList<StationDetails>)filterResults.values;

                // refresh the list with filtered data
                notifyDataSetChanged();

            }
        };
    }


    private Bitmap getLineIndicatorBitmap(ArrayList<String> lineIndicatorList){

        // Initialize a new Bitmap object
        Bitmap bitmap = Bitmap.createBitmap(
                80, // Width
                30, // Height
                Bitmap.Config.ARGB_8888 // Config
        );

        HashMap<String,Integer> LineColor = new HashMap<>();
        LineColor.put("red",Color.RED);
        LineColor.put("blue",Color.BLUE);

        int x = 15;

        for (int i = 0; i < 4; i++) {

            Canvas canvas = new Canvas(bitmap);

            String line = (i < lineIndicatorList.size())?
                    lineIndicatorList.get(i):null;

            int color = (LineColor.get(line) != null)?
                    LineColor.get(line):Color.TRANSPARENT;

            drawCircle(canvas,color,x);
            x += 17;

        }

        return bitmap;
    }


    private void drawCircle(Canvas canvas, int color, int x){

        // Initialize a new Paint instance to draw the Circle
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(color);
        paint.setAntiAlias(true);

        // Initialize a new Canvas instance
        // Calculate the available radius of canvas

        // Set a pixels value to padding around the circle
        int padding = 2;
        int y = canvas.getHeight() / 2;
        int radius = 6;

        // Finally, draw the circle on the canvas
        canvas.drawCircle(
                x, // cx
                y, // cy
                radius, // Radius
                paint // Paint
        );


    }






}
