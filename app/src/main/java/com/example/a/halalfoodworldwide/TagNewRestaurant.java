package com.example.a.halalfoodworldwide;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.a.halalfoodworldwide.Helper.APIUrl;
import com.example.a.halalfoodworldwide.Models._User;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class TagNewRestaurant extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    private double center_lat,center_lng;

    private double taggedLat,taggedLng;

    private String countryName,cityName;

    //UI buttons
    Button saveButton,backButton;

    private String restaurantName,restaurantAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag_new_restaurant);

        //Get Current Location
        GetLocation();



        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.TagNewRestaurantMap);
        mapFragment.getMapAsync(this);


        //Get Button reference
        saveButton = (Button) findViewById(R.id.TagNewRestaurantSave);
        backButton = (Button) findViewById(R.id.TagNewRestaurantBack);

        //Assigning button event
        saveButton.setOnClickListener(saveButtonListener);
        backButton.setOnClickListener(backButtonListener);
    }

    View.OnClickListener saveButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            GetDataFromUI();

            if(IsFieldNotNull())
            {
                sendRequest();
            }
            else
            {
                Toast.makeText(getApplicationContext(),"Please Enter all the information",Toast.LENGTH_LONG).show();
            }
        }
    };

    private void GetDataFromUI() {
        //Get reference
        EditText restaurantNameEditText = (EditText) findViewById(R.id.TagNewRestaurantName);
        EditText restaurantAddressEditText = (EditText) findViewById(R.id.TagNewRestaurantAddress);

        //Get value from reference
        restaurantAddress = restaurantAddressEditText.getText().toString();
        restaurantName = restaurantNameEditText.getText().toString();
    }

    private boolean IsFieldNotNull() {
        if(taggedLat != 0 && taggedLng != 0 && restaurantAddress != null && restaurantName != null) return true;
        else return false;
    }

    View.OnClickListener backButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };

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
            }catch (Exception ex){
                Toast.makeText(this,"No location Fount",Toast.LENGTH_LONG).show();
            }
        }
    }

    private void hereLocation(double lat, double lng){
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
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
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //LatLng location = new LatLng(center_lat, center_lng);
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(location));

        //Move the camera to the user's location and zoom in!
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(center_lat,center_lng), 10.7f));
        mMap.getUiSettings().setAllGesturesEnabled(false);

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
//                Toast.makeText(getApplicationContext(),Double.toString(latLng.latitude) + " "+Double.toString(latLng.longitude),Toast.LENGTH_LONG).show();
                taggedLat = latLng.latitude;
                taggedLng = latLng.longitude;
            }
        });

    }

    //Api request
    private void sendRequest(){
        String url = APIUrl.Url + "/api/TaggedPlaces?email=aitezazbilal95@gmail.com&cityName=Lahore&restaurantName=test&restaurantAdd=testaddress&placeLat= 1.23232&placeLng=-1.1212121";
        StringRequest stringRequest = new StringRequest(Request.Method.POST,url
                , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(TagNewRestaurant.this,"Tagged Successfully",Toast.LENGTH_LONG).show();
                finish();
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(TagNewRestaurant.this,"error in response",Toast.LENGTH_LONG).show();
                    }
                })
        {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> headers = new HashMap<String, String>();
                String s = _User.getInstance().getToken();
                headers.put("Authorization", "Bearer " + _User.getInstance().getToken());
                return headers;
            }

            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };
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
                Toast.makeText(TagNewRestaurant.this,error.networkResponse.toString(),Toast.LENGTH_LONG);
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

}

