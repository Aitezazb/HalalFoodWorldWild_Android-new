package com.example.a.halalfoodworldwide;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.a.halalfoodworldwide.Helper.APIUrl;
import com.example.a.halalfoodworldwide.Models.ParseJSON;
import com.example.a.halalfoodworldwide.Models.RestaurantModel;
import com.example.a.halalfoodworldwide.Models._User;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    private Dialog userLog_Popup;

    private double center_lat,center_lng;

    private String countryName,cityName;

    private ArrayList<RestaurantModel> restaurantModels;

    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //if location permission is Granted get location
         GetLocation();

         userLog_Popup = new Dialog(this);

        //bottom menu
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomMenu);
        bottomNavigationView.setOnNavigationItemSelectedListener(nav_Listener);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    private void GetLocation(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},1000);
        }else{
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            try{
                hereLocation(location.getLatitude(),location.getLongitude());
                center_lat = location.getLatitude();
                center_lng = location.getLongitude();
                //Toast.makeText(this,city + country,Toast.LENGTH_LONG).show();
                sendRequest();
            }catch (Exception ex){
                Toast.makeText(this,"No location Fount",Toast.LENGTH_LONG).show();
            }
        }
    }

    //Checking for location permission
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1000:{
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                    Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    try{
                        hereLocation(location.getLatitude(),location.getLongitude());
                    }catch (Exception ex){
                        Toast.makeText(this,"No location Fount",Toast.LENGTH_LONG).show();
                    }
                }
            }

        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //LatLng location = new LatLng(center_lat, center_lng);
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(location));

        //Move the camera to the user's location and zoom in!
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(center_lat,center_lng), 10.7f));


    }

    //Getting city and country name from current location
    private void hereLocation(double lat, double lng){
        Geocoder geocoder = new Geocoder(this,Locale.getDefault());
        List<Address> addresses;
        try{
            addresses = geocoder.getFromLocation(lat,lng,10);
            if(addresses.size() > 0)
            {
                for(Address adr: addresses){
                    if(adr.getLocality() != null && adr.getLocality().length() > 0){
                        cityName = adr.getLocality();
                        countryName = adr.getCountryName();
                        break;
                    }

                }
            }
        }catch (Exception ex) {
            ex.printStackTrace();
            }
    }

    //Api request
    private void sendRequest(){
        String url = "";//APIUrl.Url + "/api/Restaurant?cityName=Lahore&countyName=Pakistan"; // <- uncomment this
        StringRequest stringRequest = new StringRequest(url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        showJSON(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(MainActivity.this,"error in response of map",Toast.LENGTH_LONG).show();
                    }
                });
        stringRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {
                Toast.makeText(MainActivity.this,error.networkResponse.toString(),Toast.LENGTH_LONG);
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    //Api results
    private void showJSON(String json){
        ParseJSON pj = new ParseJSON(json);
        restaurantModels = pj.parseJSON_Restaurant();
        if(restaurantModels != null)
            addMarkersToMap();
        else Toast.makeText(this,"no restaurant",Toast.LENGTH_LONG).show();
    }

    //Api result restaurantModels
    private void addMarkersToMap() {
        for(RestaurantModel restaurantModel : restaurantModels){
            LatLng location = new LatLng(restaurantModel.location.lat, restaurantModel.location.lng);
            mMap.addMarker(new MarkerOptions().position(location).title(restaurantModel.name).snippet( "Click here for more detail")).setTag(restaurantModel.place_id);
        }

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override public void onInfoWindowClick(Marker marker) {
                String id = marker.getTag().toString();
                for(RestaurantModel restaurantModel: restaurantModels){
                    if(id == restaurantModel.place_id){
                        Intent intent = new Intent(MainActivity.this, RestaurantDetailActivity.class);

                        intent.putExtra("restaurantId",restaurantModel.place_id);
                        intent.putExtra("restaurantName",restaurantModel.name);
                        intent.putExtra("restaurantAddress",restaurantModel.address);
                        intent.putExtra("restaurantLat",restaurantModel.location.lat);
                        intent.putExtra("restaurantLng",restaurantModel.location.lng);
                        intent.putExtra("currentLat",center_lat);
                        intent.putExtra("currentLng",center_lng);
                        intent.putExtra("restaurantRate",restaurantModel.rating);
                        intent.putExtra("restaurantTotalRating",restaurantModel.user_ratings_total);

                        startActivity(intent);
                        finish();
                    }
                }

            }
        });
    }

    //bottom Navigation Onclick method
    private BottomNavigationView.OnNavigationItemSelectedListener nav_Listener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            switch (menuItem.getItemId()){
                case R.id.nav_home:{
                    break;
                }
                case R.id.nav_Ratings:{
                    if(_User.getInstance().IsEmpty()) {ShowUserLogPopUp();
                        //ReturnActivity.setReturnActivityName();
                        return false;
                    }else{
                        Intent userAllRatingIntent = new Intent(MainActivity.this,UserAllRatingActivity.class);
                        startActivity(userAllRatingIntent);
                        finish();
                    }

                    break;
                }
                case R.id.nav_tag:{
                    if(_User.getInstance().IsEmpty()) {
                        ShowUserLogPopUp();
                        return false;
                    }else{

                    }
                    break;
                }
                case R.id.nav_setting :{
                    if(_User.getInstance().IsEmpty()) {
                        ShowUserLogPopUp();
                        return false;
                    }else{

                    }
                    break;
                }
            }
            return true;
        }
    };

    private void ShowUserLogPopUp(){
        userLog_Popup.setContentView(R.layout.user_log_popup);
        Button Login = (Button) userLog_Popup.findViewById(R.id.login);
        Button SignUp = (Button) userLog_Popup.findViewById(R.id.signup);
        TextView closePopup = (TextView) userLog_Popup.findViewById(R.id.closepopup);
        closePopup.setOnClickListener(closeBtnListener);
        Login.setOnClickListener(LoginBanListener);
        SignUp.setOnClickListener(SignUpBtnListener);
        userLog_Popup.show();
    }

    //close user log pop up event handler
    private TextView.OnClickListener closeBtnListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            userLog_Popup.dismiss();
        }
    };

    //Login button event handler
    private Button.OnClickListener LoginBanListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
                Intent loginIntent = new Intent(MainActivity.this,LoginActivity.class);
                startActivity(loginIntent);
                finish();
        }
    };

    //Sign up button event handler
    private Button.OnClickListener SignUpBtnListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent signUpIntent = new Intent(MainActivity.this,SignUpActivity.class);
            startActivity(signUpIntent);
            finish();
        }
    };

}


