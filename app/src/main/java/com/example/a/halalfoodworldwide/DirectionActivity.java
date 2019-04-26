package com.example.a.halalfoodworldwide;

import android.app.Dialog;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.a.halalfoodworldwide.Helper.FetchURL;
import com.example.a.halalfoodworldwide.Helper.TaskLoadedCallback;
import com.example.a.halalfoodworldwide.Models.RestaurantModel;
import com.example.a.halalfoodworldwide.Models._User;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

public class DirectionActivity extends FragmentActivity implements OnMapReadyCallback, TaskLoadedCallback {

    private GoogleMap mMap;

    private RestaurantModel restaurantModel;
    private double centerLat,centerLng;

    private Button reach;
    private Dialog userLog_Popup;


    private Polyline currentPolyline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_direction);
        userLog_Popup = new Dialog(this);
        restaurantModel = new RestaurantModel();
        final Intent intent = getIntent();

        //Getting all the info of restaurant from called Activity
        restaurantModel.name = intent.getStringExtra("restaurantName");
        restaurantModel.place_id = intent.getStringExtra("restaurantId");
        restaurantModel.address = intent.getStringExtra("restaurantAddress");
        restaurantModel.location.lat = intent.getDoubleExtra("restaurantLat",0);
        restaurantModel.location.lng = intent.getDoubleExtra("restaurantLng",0);
        centerLat = intent.getDoubleExtra("currentLat",0);
        centerLng = intent.getDoubleExtra("currentLng",0);
        restaurantModel.rating = intent.getDoubleExtra("restaurantRate",0);
        restaurantModel.user_ratings_total = intent.getIntExtra("restaurantTotalRating",0);

        // button for calling rating activity
        reach = (Button) findViewById(R.id.reach);
        reach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(_User.getInstance().IsEmpty()){
                    ShowUserLogPopUp();
                }
                else{
                Intent rateActivity = new Intent(DirectionActivity.this, RateRestaurantActivity.class);
                rateActivity.putExtra("restaurantName",restaurantModel.name);
                rateActivity.putExtra("restaurantId",restaurantModel.place_id);
                startActivity(rateActivity);
                finish();
                }
            }
        });

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //Adding marker for user location
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(centerLat,centerLng), 13.2f));
        mMap.addMarker(new MarkerOptions().position(new LatLng(centerLat,centerLng)).title("You"));

        //Adding marker for restaurant location
        mMap.addMarker(new MarkerOptions().position(new LatLng(restaurantModel.location.lat,restaurantModel.location.lng))
                .title(restaurantModel.name));

        new FetchURL(DirectionActivity.this).execute(
                getUrl(new LatLng(centerLat,centerLng),new LatLng(restaurantModel.location.lat,restaurantModel.location.lng),
                "driving"), "driving");
    }

    private String getUrl(LatLng origin, LatLng dest, String directionMode) {
        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        // Mode
        String mode = "mode=" + directionMode;
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + mode;
        // Output format
        String output = "json";
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + getString(R.string.google_maps_key);
        return url;
    }

    @Override
    public void onTaskDone(Object... values) {
        if (currentPolyline != null)
            currentPolyline.remove();
        currentPolyline = mMap.addPolyline((PolylineOptions) values[0]);
    }

    private void ShowUserLogPopUp(){
        userLog_Popup.setContentView(R.layout.user_log_popup);
        Button Login = (Button) userLog_Popup.findViewById(R.id.login);
        Button SignUp = (Button) userLog_Popup.findViewById(R.id.signup);
        TextView closePopup = (TextView) userLog_Popup.findViewById(R.id.closepopup);
        closePopup.setOnClickListener(closeBtnListenser);
        Login.setOnClickListener(LoginBanListener);
        SignUp.setOnClickListener(SignUpBtnListener);
        userLog_Popup.show();
    }

    //close user log pop up event handler
    private TextView.OnClickListener closeBtnListenser = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            userLog_Popup.dismiss();
        }
    };

    //Login button event handler
    private Button.OnClickListener LoginBanListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent loginIntent = new Intent(DirectionActivity.this,LoginActivity.class);
            startActivity(loginIntent);
            finish();
        }
    };

    //Sign up button event handler
    private Button.OnClickListener SignUpBtnListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent signUpIntent = new Intent(DirectionActivity.this,SignUpActivity.class);
            startActivity(signUpIntent);
            finish();
        }
    };
}
