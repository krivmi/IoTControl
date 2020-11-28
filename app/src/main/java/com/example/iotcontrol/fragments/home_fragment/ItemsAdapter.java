package com.example.iotcontrol.fragments.home_fragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.iotcontrol.R;

import java.util.ArrayList;

public class ItemsAdapter extends ArrayAdapter<Item> {
    public ItemsAdapter(Context context, ArrayList<Item> users) {
        super(context, 0, users);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Item item = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_adapter, parent, false);
        }
        // Lookup view for data population
        TextView itemName = (TextView) convertView.findViewById(R.id.itemName);
        TextView itemStatus = (TextView) convertView.findViewById(R.id.itemStatus);
        // Populate the data into the template view using the data object
        ImageView icon = (ImageView) convertView.findViewById(R.id.icon);
        icon.setImageResource(item.resImg);

        itemName.setText(item.name);
        itemStatus.setText(item.status.toUpperCase());
        // Return the completed view to render on screen
        return convertView;
    }
}