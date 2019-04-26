package com.example.a.halalfoodworldwide.Models;

import com.example.a.halalfoodworldwide.Helper.RatingItemAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class ParseJSON {

    public static final String KEY_RESULT = "results";
    public static final String KEY_GEOMETRY = "geometry";
    public static final String KEY_LOCATION = "location";
    public static final String KEY_ADDRESS ="formatted_address";
    public static final String KEY_ICON = "icon";
    public static final String KEY_NAME = "name";
    public static final String KEY_PLACEID = "place_id";
    public static final String KEY_RATING = "rating";
    public static final String KEY_REFERENCE = "reference";
    public static final String KEY_USERTOTALRATING = "user_ratings_total";

   public static final String LAT = "lat";
   public static final String LNG = "lng";

    private JSONArray results = null;
    private String json;

    public ParseJSON(String json){
        this.json = json;
    }
    public ArrayList<RestaurantModel> parseJSON_Restaurant(){
        JSONObject jsonObject=null;
        try {
            //Getting all values of Api result
            jsonObject = new JSONObject(json);

            // Getting Results array list from json
            results = jsonObject.getJSONArray(KEY_RESULT);
            ArrayList<RestaurantModel> restaurantModels = new ArrayList<RestaurantModel>();

            // looping through all Results
            for(int i =0 ;i<results.length();i++){
                RestaurantModel restaurantModel = new RestaurantModel();
                JSONObject result = results.getJSONObject(i);
                restaurantModel.address = result.getString(KEY_ADDRESS);
                restaurantModel.icon = result.getString(KEY_ICON);
                restaurantModel.name = result.getString(KEY_NAME);
                restaurantModel.place_id = result.getString(KEY_PLACEID);
                restaurantModel.rating = result.getDouble(KEY_RATING);
                restaurantModel.reference = result.getString(KEY_REFERENCE);
                restaurantModel.user_ratings_total = result.getInt(KEY_USERTOTALRATING);

                JSONObject geometry = result.getJSONObject(KEY_GEOMETRY);
                JSONObject Location = geometry.getJSONObject(KEY_LOCATION);

                restaurantModel.location.lat = Location.getDouble(LAT);
                restaurantModel.location.lng = Location.getDouble(LNG);
                restaurantModels.add(restaurantModel);
            }
            return restaurantModels;

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public MenuItemModel parseJSON_MenuItem(){
        try {
            //Getting all values of Api result
            JSONObject jsonObject = new JSONObject(json);

            // Getting Results array list from json
            results = jsonObject.getJSONArray("menuItems");

            MenuItemModel menuItem = new MenuItemModel();

            // looping through all Results
            for(int i =0 ;i<results.length();i++){
                RestaurantModel restaurantModel = new RestaurantModel();
                JSONObject Item = results.getJSONObject(i);
                menuItem.MenuItems.put(Item.getString("itemName"),Item.getString("itemPrice"));
            }
            return menuItem;

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<RatingListViewModel> parseJSON_Rating(){
        JSONObject jsonObject=null;
        try {

            ArrayList<RatingListViewModel> ratingListViewModels = new ArrayList<>();

            //Getting all values of Api result
            jsonObject = new JSONObject(json);

            JSONArray ratingsModel = jsonObject.getJSONArray("ratingsModel");

            for (int i=0;i<ratingsModel.length();i++){
                RatingListViewModel ratingListViewModel = new RatingListViewModel();
                JSONObject Item = ratingsModel.getJSONObject(i);
                ratingListViewModel.setId(Item.getString("id"));
                ratingListViewModel.setRestaurantName(Item.getString("restaurantName"));
                ratingListViewModel.setStar(Item.getString("star"));
                ratingListViewModel.setComment(Item.getString("comment"));
                ratingListViewModels.add(ratingListViewModel);
            }

            return ratingListViewModels;

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

}
