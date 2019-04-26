package com.example.a.halalfoodworldwide;

import android.content.Intent;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {


    private EditText ET_email, ET_password;

    private Button loginBtn, signUpBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getAllUIReferences();
    }

    private void getAllUIReferences(){
        ET_email = (EditText) findViewById(R.id.loginEmail);
        ET_password = (EditText) findViewById(R.id.loginPassword);

        loginBtn = (Button) findViewById(R.id.login_btn);
        signUpBtn = (Button) findViewById(R.id.loginSignUp_btn);

        loginBtn.setOnClickListener(loginBtn_Listener);
        signUpBtn.setOnClickListener(signUpBtn_Listener);
    }

    //login click listener
    private View.OnClickListener loginBtn_Listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(isEmailValid(ET_email.getText().toString()) && ET_password.getText().toString() != null){

            }sendRequest();

        }
    };

    private boolean isEmailValid(CharSequence email) {
        boolean s = android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
        if(!s) Toast.makeText(LoginActivity.this,"wrong email",Toast.LENGTH_LONG)
                .show();

        return s;

    }

    //sign up click listener
    private View.OnClickListener signUpBtn_Listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent signUpIntent = new Intent(LoginActivity.this,SignUpActivity.class);
            startActivity(signUpIntent);
            finish();
        }
    };


    //Api request
    private void sendRequest(){
        String url = APIUrl.Url + "/token";

        StringRequest stringRequest = new StringRequest(Request.Method.POST,url
                , new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Toast.makeText(LoginActivity.this,response,Toast.LENGTH_LONG).show();

                        try {
                            SetToken(response);
                            Intent mainActivityIntent = new Intent(LoginActivity.this,MainActivity.class);
                            startActivity(mainActivityIntent);
                            finish();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(LoginActivity.this,"error in response",Toast.LENGTH_LONG).show();
                    }
                })
        {
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("username","aitezazbilal95@gmail.com");
                params.put("password","Abc@123");
                params.put("grant_type","password");
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
//                params.put("Content-Type","application/json");
                return params;
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
                Toast.makeText(LoginActivity.this,error.networkResponse.toString(),Toast.LENGTH_LONG);
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


    private void SetToken(String response) throws JSONException {
        JSONObject tokenResponse = new JSONObject(response);
        String token = tokenResponse.getString("access_token");
        String email = tokenResponse.getString("userName");
        _User.getInstance().setToken(token);
        _User.getInstance().setEmail(email);
    }

}
