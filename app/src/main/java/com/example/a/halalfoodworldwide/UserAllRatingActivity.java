package com.example.a.halalfoodworldwide;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
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
import com.example.a.halalfoodworldwide.Helper.RatingItemAdapter;
import com.example.a.halalfoodworldwide.Models.ParseJSON;
import com.example.a.halalfoodworldwide.Models.RatingListViewModel;
import com.example.a.halalfoodworldwide.Models._User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UserAllRatingActivity extends AppCompatActivity {

    //UI
    private ListView ratingListView;
    private BottomNavigationView bottomNavigationView;
    private ProgressBar progressBar;

    //Model
    private ArrayList<RatingListViewModel> ratingListViewModels;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_all_rating);

        //Getting references
        ratingListView = (ListView) findViewById(R.id.RatingListView);
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomMenu);
        progressBar = (ProgressBar) findViewById(R.id.UserAllRatingProgressBar);

        //Adding click listener
        bottomNavigationView.setOnNavigationItemSelectedListener(nav_Listener);

        //Adding selected default item
        bottomNavigationView.setSelectedItemId(R.id.nav_Ratings);

        //Api request to get All ratings
        sendRequest();
        showProgressBar();


    }

    private void showProgressBar(){
        progressBar.setVisibility(View.VISIBLE);

        //Removing user interaction
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    private void hideProgressBar(){
        progressBar.setVisibility(View.GONE);

        //Adding user interaction
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }
    //Api request
    private void sendRequest(){
        String url = APIUrl.Url + "/api/Ratings?userEmail="+_User.getEmail();
        StringRequest stringRequest = new StringRequest(Request.Method.GET,url
                , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ResponseToModel(response);
                hideProgressBar();
                //RatingItemAdapter ratingItemAdapter = new RatingItemAdapter(this,restaurantName,rating);

                final RatingItemAdapter ratingItemAdapter = new RatingItemAdapter(UserAllRatingActivity.this,ratingListViewModels);

                ratingListView.setAdapter(ratingItemAdapter);

                ratingListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(final AdapterView<?> parent, View view,final int position, long id) {
                        new AlertDialog.Builder(UserAllRatingActivity.this).setTitle("Delete")
                                .setMessage("Are you sure? This cannot be undone")
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        RatingListViewModel ratingListViewModel = (RatingListViewModel)parent.getItemAtPosition(position);
                                        ratingItemAdapter.remove(ratingListViewModel);
                                        ratingItemAdapter.notifyDataSetChanged();
                                        DeleteRestaurantRatingApiRequest(ratingListViewModel.getId());
                                    }
                                })
                                .setNegativeButton("No",null).create().show();

                    }
                });

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        hideProgressBar();
                        Toast.makeText(getApplicationContext(),"error in response",Toast.LENGTH_LONG).show();
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
                Toast.makeText(getApplicationContext(),error.networkResponse.toString(),Toast.LENGTH_LONG);
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


    private void DeleteRestaurantRatingApiRequest(String id){
        String url = APIUrl.Url + "/api/Ratings?id="+id;
        StringRequest stringRequest = new StringRequest(Request.Method.DELETE,url
                , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(),"Removed! " ,Toast.LENGTH_LONG).show();

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(getApplicationContext(),"error in response",Toast.LENGTH_LONG).show();
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
                Toast.makeText(getApplicationContext(),error.networkResponse.toString(),Toast.LENGTH_LONG);
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }
    private void ResponseToModel(String response){
        ParseJSON ratingParseJSON = new ParseJSON(response);
        ratingListViewModels = ratingParseJSON.parseJSON_Rating();
    }
    //Bottom navigation onClick method
    private BottomNavigationView.OnNavigationItemSelectedListener nav_Listener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            switch (menuItem.getItemId()){
                case R.id.nav_home:{
                    Intent mainActivity  =  new Intent(UserAllRatingActivity.this,MainActivity.class);
                    startActivity(mainActivity);
                    finish();
                    break;
                }
                case R.id.nav_Ratings:{
                    break;
                }
                case R.id.nav_tag:{
                    Intent userAllRatingIntent = new Intent(UserAllRatingActivity.this,UserAllTagging.class);
                    startActivity(userAllRatingIntent);
                    finish();
                    break;
                }
                case R.id.nav_setting :{
                    Intent userAllRatingIntent = new Intent(UserAllRatingActivity.this,Setting.class);
                    startActivity(userAllRatingIntent);
                    finish();
                    break;
                }
            }
            return true;
        }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent mainActivity = new Intent(UserAllRatingActivity.this,MainActivity.class);
        startActivity(mainActivity);
        finish();
    }
}
