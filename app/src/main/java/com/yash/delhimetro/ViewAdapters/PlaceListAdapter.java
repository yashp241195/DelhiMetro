package com.yash.delhimetro.ViewAdapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.yash.delhimetro.DataProviders.PlaceDetails;
import com.yash.delhimetro.R;
import com.yash.delhimetro.WebViewExplorePlace;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class PlaceListAdapter extends RecyclerView.Adapter<
        PlaceListAdapter.MyViewHolder> implements Filterable {

    private List<PlaceDetails> placeDetailsList,
            placeDetailsListFiltered;

    // context required for getting images from assets folder
    private Context context;


    public PlaceListAdapter(Context context,
            ArrayList<PlaceDetails> placeList) {
        this.placeDetailsList = placeList;
        this.placeDetailsListFiltered = placeList;
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return placeDetailsListFiltered.size();
    }




    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView placeName,placeType,placeNearbyMetro;
        ImageView placeImage;

        MyViewHolder(View view) {
            super(view);

            placeName = (TextView) view.findViewById(R.id.row_li_placeName);
            placeType = (TextView) view.findViewById(R.id.row_li_val_placeType);
            placeNearbyMetro = (TextView) view.findViewById(R.id.row_li_val_nearby_metro);

            placeImage =(ImageView)view.findViewById(R.id.row_li_place_imageView);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onClickAction(v,getAdapterPosition());
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup,int i) {

        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.row_place_listitem_cardview, viewGroup,
                        false);

        return new MyViewHolder(itemView);
    }

    private void onClickAction(View v, int i){
        Context context = v.getContext();
        Intent intent = new Intent(context,WebViewExplorePlace.class);

        PlaceDetails placeDetails =  placeDetailsListFiltered.get(i);

        intent.putExtra("placeName",placeDetails.getPlaceName());
        intent.putExtra("placeType",placeDetails.getPlaceType());
        intent.putExtra("placeNearbyMetro",
                placeDetails.getNearbyMetroStation()+" ("
                        +placeDetails.getDistance()+" km) "
        );

        context.startActivity(intent);
    }




    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, int i) {

        PlaceDetails placeDetails = placeDetailsListFiltered.get(i);

//        Log.d("placeDetails",placeDetails.toString());

        String name = placeDetails.getPlaceName();
        String type = placeDetails.getPlaceType();
        String placeImage = placeDetails.getPlaceImage();
        String nearbyMetroText = placeDetails.getNearbyMetroStation()
                +" ("+placeDetails.getDistance()+" km) ";

        myViewHolder.placeName.setText(name);
        myViewHolder.placeType.setText(type);
        myViewHolder.placeNearbyMetro.setText(nearbyMetroText);

//        Log.d("placeImage : ",placeImage);
        String imageFilePath = "placeImages/"+placeImage;

        try {
            InputStream ims = context.getAssets().open(imageFilePath);

            // load image as Drawable
            Drawable d = Drawable.createFromStream(ims, null);

            myViewHolder.placeImage.setImageDrawable(d);

            ims .close();

        } catch (IOException e) {

            Log.e("placeAdapter Error",imageFilePath+" image not found");
            myViewHolder.placeImage.setImageResource(R.drawable.rcv_ic_mall);
        }


    }





    @Override
    public Filter getFilter() {

        return new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String charString = charSequence.toString().toUpperCase();

                if (charString.isEmpty()) {
                    placeDetailsListFiltered = placeDetailsList;
                } else {
                    ArrayList<PlaceDetails> filteredList = new ArrayList<>();
                    for (PlaceDetails row : placeDetailsList) {

                        if (row.getPlaceName().toUpperCase().contains(charString) ||
                            row.getNearbyMetroStation().toUpperCase().contains(charString)
                        ){
                            filteredList.add(row);
                        }
                    }

                    placeDetailsListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = placeDetailsListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                placeDetailsListFiltered = (ArrayList<PlaceDetails>) filterResults.values;

                // refresh the list with filtered data
                notifyDataSetChanged();
            }
        };
    }





}
