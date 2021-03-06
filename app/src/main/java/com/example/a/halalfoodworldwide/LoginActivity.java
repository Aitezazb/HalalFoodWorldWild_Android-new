package com.example.a.halalfoodworldwide;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
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
import com.example.a.halalfoodworldwide.Models._User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class LoginActivity extends AppCompatActivity {



    private EditText emailEditText, passwordEditText;

    private String email,password;

    private Button loginBtn, signUpBtn;

    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getAllUIReferences();
    }

    private void getAllUIReferences(){
        emailEditText = (EditText) findViewById(R.id.loginEmail);
        passwordEditText = (EditText) findViewById(R.id.loginPassword);

        progressBar = (ProgressBar) findViewById(R.id.LoginProgressBar);

        loginBtn = (Button) findViewById(R.id.login_btn);
        signUpBtn = (Button) findViewById(R.id.loginSignUp_btn);

        loginBtn.setOnClickListener(loginBtn_Listener);
        signUpBtn.setOnClickListener(signUpBtn_Listener);
    }

    private void removeUserInteraction()
    {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    private void AddUserInteraction(){
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    //login click listener
    private View.OnClickListener loginBtn_Listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(isEmailValid(emailEditText.getText().toString()) && !TextUtils.isEmpty(passwordEditText.getText().toString())){
                email = emailEditText.getText().toString();
                password = passwordEditText.getText().toString();
                progressBar.setVisibility(View.VISIBLE);
                removeUserInteraction();
                sendRequest();
            }
            else{
                Toast.makeText(LoginActivity.this,"Please fill password field correctly",Toast.LENGTH_LONG).show();
            }

        }
    };

    private boolean isEmailValid(CharSequence email) {
        boolean s = android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
        if(!s) Toast.makeText(LoginActivity.this,"Please fill email field correctly",Toast.LENGTH_LONG)
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
                            if(_User.getInstance().getToken() == null) {
                                progressBar.setVisibility(View.GONE);
                                AddUserInteraction();
                                Toast.makeText(getApplicationContext(), "Incorrect username or password", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            progressBar.setVisibility(View.GONE);
                            AddUserInteraction();
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
                        progressBar.setVisibility(View.GONE);
                        AddUserInteraction();
                        Toast.makeText(LoginActivity.this,"Incorrect UserName or password or try later",Toast.LENGTH_LONG).show();
                    }
                })
        {
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("username",email);
                params.put("password",password);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent mainActivity = new Intent(LoginActivity.this,MainActivity.class);
        startActivity(mainActivity);
        finish();
    }

}
