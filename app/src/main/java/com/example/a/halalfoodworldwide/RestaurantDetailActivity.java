package com.example.a.halalfoodworldwide;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.a.halalfoodworldwide.Helper.APIUrl;
import com.example.a.halalfoodworldwide.Models.MenuItemModel;
import com.example.a.halalfoodworldwide.Models.ParseJSON;
import com.example.a.halalfoodworldwide.Models.RestaurantModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class RestaurantDetailActivity extends AppCompatActivity {


    private RestaurantModel restaurantModel;
    private double centerLat,centerLng;

    private TextView name,address;
    private RatingBar rating;
    private Button direction;
    private ListView menuListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_detail);

        restaurantModel = new RestaurantModel();
        Intent intent = getIntent();

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



        //Getting all the references of layout
        name = (TextView) findViewById(R.id.rname);
        address = (TextView) findViewById(R.id.raddress);
        rating = (RatingBar) findViewById(R.id.rrating);
        direction = (Button) findViewById(R.id.direction);
        menuListView = (ListView) findViewById(R.id.menulist);


        //Setting Layout values
        rating.setRating((float)restaurantModel.rating);
        address.setText(restaurantModel.address);
        name.setText(restaurantModel.name);

        //Getting all menu items from apis
        sendRequest();

        direction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent directionActivity = new Intent(RestaurantDetailActivity.this,DirectionActivity.class);
                directionActivity.putExtra("restaurantId",restaurantModel.place_id);
                directionActivity.putExtra("restaurantName",restaurantModel.name);
                directionActivity.putExtra("restaurantAddress",restaurantModel.address);
                directionActivity.putExtra("restaurantLat",restaurantModel.location.lat);
                directionActivity.putExtra("restaurantLng",restaurantModel.location.lng);
                directionActivity.putExtra("currentLat",centerLat);
                directionActivity.putExtra("currentLng",centerLng);
                directionActivity.putExtra("restaurantRate",restaurantModel.rating);
                directionActivity.putExtra("restaurantTotalRating",restaurantModel.user_ratings_total);
                startActivity(directionActivity);
                finish();
            }
        });
    }

    //Api request
    private void sendRequest(){
        String url = APIUrl.Url+ "/api/Restaurant/7/MenuItem";
        StringRequest stringRequest = new StringRequest(url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        GetMenuItems(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(RestaurantDetailActivity.this,"error in response",Toast.LENGTH_LONG).show();
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
                Toast.makeText(RestaurantDetailActivity.this,error.networkResponse.toString(),Toast.LENGTH_LONG);
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void GetMenuItems(String json){
        ParseJSON pj = new ParseJSON(json);
        MenuItemModel menuItemModel = pj.parseJSON_MenuItem();
        if(menuItemModel == null) {
            Toast.makeText(RestaurantDetailActivity.this, "No menu fount", Toast.LENGTH_SHORT).show();
            return;
        }
        List<HashMap<String,String>> listItems = new ArrayList<>();
        SimpleAdapter adapter = new SimpleAdapter(this,listItems,R.layout.list_item,
                new String[]{"First Line","Second Line"},new int[]{R.id.itemname,R.id.itemprice});

        Iterator it = menuItemModel.MenuItems.entrySet().iterator();
        while(it.hasNext()){
            HashMap<String,String> result = new HashMap<>();
            Map.Entry pair = (Map.Entry) it.next();
            result.put("First Line",pair.getKey().toString());
            result.put("Second Line",pair.getValue().toString());
            listItems.add(result);
        }

        menuListView.setAdapter(adapter);
    }
}
