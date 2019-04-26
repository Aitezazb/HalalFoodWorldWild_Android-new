package com.example.a.halalfoodworldwide;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
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

import java.util.HashMap;
import java.util.Map;

public class RateRestaurantActivity extends AppCompatActivity {

    private String id;
    private String restaurantName;

    private TextView restaurantNameTextView;
    private Button submitBtn,HomeBtn;
    private EditText commentEditText;
    private RatingBar ratingBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_restaurant);

        //Getting UI references
        restaurantNameTextView = (TextView) findViewById(R.id.RestaurantName_txt);
        submitBtn= (Button) findViewById(R.id.Submit_btn);
        HomeBtn = (Button) findViewById(R.id.Home_btn);
        commentEditText = (EditText) findViewById(R.id.UserComment);
        ratingBar = (RatingBar) findViewById(R.id.UserRatingBar);

        //Setting Click Listener for buttons
        submitBtn.setOnClickListener(SubmitBtnClickListener);
        HomeBtn.setOnClickListener(HomeBtnClickListener);

        //Getting all restaurant details
        final Intent intent = getIntent();
        restaurantName = intent.getStringExtra("restaurantName");
        restaurantName = intent.getStringExtra("restaurantId");

        //Setting restaurant Name
        restaurantNameTextView.setText(restaurantName);


    }

    private View.OnClickListener SubmitBtnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(UIFieldsNotEmpty()){

            }
            else{
                Toast.makeText(getApplicationContext(),"please fill the form",Toast.LENGTH_LONG).show();
            }
            sendRequest();
        }
    };

    private Boolean UIFieldsNotEmpty(){
        return ratingBar.getRating() != 0 && !commentEditText.getText().toString().isEmpty() ? true:false;
    }
    private View.OnClickListener HomeBtnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent mainActivity = new Intent(RateRestaurantActivity.this,MainActivity.class);
            startActivity(mainActivity);
            finish();
        }
    };

    //Api request
    private void sendRequest(){
        String url = APIUrl.Url + "/api/Ratings?restaurantId=2&restaurantName=lolol&star=4&comment=this is good&userEmail=aitezazbilal95@gmail.com";
        StringRequest stringRequest = new StringRequest(Request.Method.POST,url
                , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                    Intent mainActivityIntent = new Intent(RateRestaurantActivity.this,MainActivity.class);
                    startActivity(mainActivityIntent);
                    finish();
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(RateRestaurantActivity.this,"error in response",Toast.LENGTH_LONG).show();
                    }
                })
        {
//            @Override
//            protected Map<String,String> getParams(){
//                Map<String,String> params = new HashMap<String, String>();
//                params.put("restaurantId","2");
//                params.put("restaurantName","lol");
//                params.put("star","4");
//                params.put("comment","this is a comment from android");
//                params.put("userEmail",_User.getInstance().getEmail());
//                return params;
//            }

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
                Toast.makeText(RateRestaurantActivity.this,error.networkResponse.toString(),Toast.LENGTH_LONG);
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

}
