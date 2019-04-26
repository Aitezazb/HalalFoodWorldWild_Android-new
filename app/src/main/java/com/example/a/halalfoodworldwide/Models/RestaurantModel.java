package com.example.a.halalfoodworldwide.Models;

import java.io.Serializable;

public class RestaurantModel {
    public RestaurantModel() {

        location = new Location();
    }

    public String address;
    public Location location;
    public String icon;
    public String name;
    public String place_id;
    public double rating;
    public String reference;
    public int user_ratings_total;

    public class Location {
        public double lat;
        public double lng;
    }

}
