package com.example.myapplication.Model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.List;

public class AutocompleteAdapter extends ArrayAdapter<PlayerItem> {
    private final List<PlayerItem> allPlacesList;

    public AutocompleteAdapter(@NonNull Context context, @NonNull List<PlayerItem> placesList)
    {
        super(context, 0, placesList);

        allPlacesList = new ArrayList<>(placesList);
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return placeFilter;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.autocomplete_item, parent, false
            );
        }

        ImageView placeImage = convertView.findViewById(R.id.autocomplete_item_place_image);
        TextView usernameLabel = convertView.findViewById(R.id.autocomplete_item_username_label);
        TextView positionLabel = convertView.findViewById(R.id.autocomplete_item_position_label);

        PlayerItem place = getItem(position);
        if (place != null) {
            positionLabel.setText(place.getPosition());
            usernameLabel.setText(place.getUsername());
            Glide.with(convertView).load(place.getImageUrl()).into(placeImage);
        }

        return convertView;
    }

    private final Filter placeFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();

            List<PlayerItem> filteredPlacesList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredPlacesList.addAll(allPlacesList);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (PlayerItem player: allPlacesList) {
                    if (player.getUsername().toLowerCase().contains(filterPattern)) {
                        filteredPlacesList.add(player);
                    }
                }
            }

            results.values = filteredPlacesList;
            results.count = filteredPlacesList.size();

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            clear();
            addAll((List) results.values);
            notifyDataSetChanged();
        }

        @Override
        public CharSequence convertResultToString(Object resultValue) {
            return ((PlayerItem) resultValue).getUsername();
        }
    };
}
