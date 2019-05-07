package com.example.a.halalfoodworldwide.Helper;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.a.halalfoodworldwide.Models.TaggedRestaurants;
import com.example.a.halalfoodworldwide.R;

import java.util.ArrayList;

public final class TaggedRestaurantAdapter extends ArrayAdapter<TaggedRestaurants> {

    Context context;
    ArrayList<TaggedRestaurants> taggedRestaurantsArrayList;

    public TaggedRestaurantAdapter(Context context, ArrayList<TaggedRestaurants> _taggedRestaurantsArrayList){
        super(context, R.layout.tagging_list_item_,R.id.RatingItem_RestaurantName,_taggedRestaurantsArrayList);
        this.context = context;
        this.taggedRestaurantsArrayList = _taggedRestaurantsArrayList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater)context.getApplicationContext().getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View taggingRestaurantListView = layoutInflater.inflate(R.layout.tagging_list_item_,parent,false);
        TextView restaurantNameTextView = (TextView) taggingRestaurantListView.findViewById(R.id.TagItem_RestaurantName);
        TextView restaurantAddressTextView = (TextView) taggingRestaurantListView.findViewById(R.id.TagItem_RestaurantAddress);
        TextView cityNameTextView = (TextView) taggingRestaurantListView.findViewById(R.id.TagItem_CityName);

        restaurantNameTextView.setText(taggedRestaurantsArrayList.get(position).getRestaurantName());
        restaurantAddressTextView.setText(taggedRestaurantsArrayList.get(position).getRestaurantAddress());
        cityNameTextView.setText(taggedRestaurantsArrayList.get(position).getCityName());

        return taggingRestaurantListView;
    }


}
