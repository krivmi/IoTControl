package com.example.iotcontrol.fragments.home_fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.core.content.ContextCompat;

import com.example.iotcontrol.R;

import java.util.ArrayList;

public class ItemsAdapter extends ArrayAdapter<Item> {
    private Context _context;
    public ItemsAdapter(Context context, ArrayList<Item> users) {
        super(context, 0, users);
        _context = context;
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

        SharedPreferences sh = _context.getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
        boolean nightModeOn = sh.getBoolean("night", false);

        if(nightModeOn) {
            TextView _name = (TextView) convertView.findViewById(R.id.itemName);
            TextView _status = (TextView) convertView.findViewById(R.id.itemStatus);

            _name.setTextColor(_context.getResources().getColor(R.color.colorWhite));
            _status.setTextColor(_context.getResources().getColor(R.color.colorWhite));
        }
        return convertView;
    }
}