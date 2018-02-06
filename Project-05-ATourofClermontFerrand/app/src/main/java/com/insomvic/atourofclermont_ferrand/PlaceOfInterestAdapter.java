package com.insomvic.atourofclermont_ferrand;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class PlaceOfInterestAdapter extends ArrayAdapter<PlaceOfInterest> {

    private int newColorResourceId;

    public PlaceOfInterestAdapter(Context context, ArrayList<PlaceOfInterest> placeOfInterests, int colorResourceId) {
        super(context, 0, placeOfInterests);
        newColorResourceId = colorResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Provide listitemview so views can be recycled
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }
        // Get current place name and set it on layout
        PlaceOfInterest currentPlaceOfInterest = getItem(position);
        TextView textView = listItemView.findViewById(R.id.name_of_place);
        textView.setText(currentPlaceOfInterest.getNameOfPlaceId());
        // Get current place description and set it on layout
        textView = listItemView.findViewById(R.id.description_of_place);
        textView.setText(currentPlaceOfInterest.getDescriptionOfPlaceId());
        // Get current place image and set it on layout
        ImageView imageView = listItemView.findViewById(R.id.image);
        // Check if image actually exists first
        if (currentPlaceOfInterest.hasImage()) {
            imageView.setImageResource(currentPlaceOfInterest.getImageResourceId());
            imageView.setVisibility(View.VISIBLE);
        } else {
            imageView.setVisibility(View.GONE);
        }
        // Set layout of individual categories to a unique background color
        View textContainer = listItemView.findViewById(R.id.text_container);
        int color = ContextCompat.getColor(getContext(), newColorResourceId);
        textContainer.setBackgroundColor(color);
        return listItemView;
    }

}