package com.example.a.halalfoodworldwide;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.a.halalfoodworldwide.Helper.APIUrl;

public class SignUpActivity extends AppCompatActivity {

    EditText emailEditText, passwordEditText, confirmPasswordEditText;

    ProgressBar progressBar;

    Button login,SignUp;

    private String email,password,confirmPassword;

    private static final String PASSWORD_PATTERN = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        getAllUIReferences();
    }

    private void getAllUIReferences() {
        emailEditText = (EditText) findViewById(R.id.signUpEmail);
        passwordEditText = (EditText) findViewById(R.id.signUpPassword);
        confirmPasswordEditText = (EditText) findViewById(R.id.signUpConfirmPassword);
        progressBar = (ProgressBar) findViewById(R.id.SignUpProgressBar);

        login = (Button) findViewById(R.id.signUpLogin_btn);
        SignUp = (Button) findViewById(R.id.signUp_btn);

        login.setOnClickListener(loginBtn_listener);
        SignUp.setOnClickListener(SignUpBtn_Listener);
    }

    //login button event handler
    private View.OnClickListener loginBtn_listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent loginIntent = new Intent(SignUpActivity.this,LoginActivity.class);
            startActivity(loginIntent);
            finish();
        }
    };

    //Sign up button event handler
    private View.OnClickListener SignUpBtn_Listener  = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(IsCorrectFormat()) {
                showProgressBar();
                sendRequest();
            }else{
                Toast.makeText(SignUpActivity.this,"Please Insect correct password",Toast.LENGTH_LONG)
                        .show();
                return;
            }
        }
    };

    //Hide or show bar and enable or disable user interactive
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

    //Checking if every UI field is correct
    private boolean IsCorrectFormat(){
        email = emailEditText.getText().toString();
        password =  passwordEditText.getText().toString();
        confirmPassword = confirmPasswordEditText.getText().toString();
        if(isEmailValid(email) && isPasswordValid(password) && isConfirmPasswordMatch(confirmPassword)){
                return true;
        }
        return false;
    }

    //Checking if email matches the email pattern
     private boolean isEmailValid(CharSequence email) {
        boolean s = android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
        if(!s)Toast.makeText(SignUpActivity.this,"wrong email",Toast.LENGTH_LONG)
                .show();

        return s;

    }

    //Checking if password matches the password pattern
    private boolean isPasswordValid(String password){
        boolean s = password.matches(PASSWORD_PATTERN);
        if(!s)Toast.makeText(SignUpActivity.this,"password must be 8 character long with at least one special character and at least one capital character",Toast.LENGTH_LONG).show();
        return s;
    }

    //Checking if confirm password matches the password
    private boolean isConfirmPasswordMatch(String confirmPassword){
        if (password.equals(confirmPassword)) {
            Toast.makeText(SignUpActivity.this,"wrong confirm",Toast.LENGTH_LONG).show();
            return true;
        }
        else return false;
    }

    //Api request
    private void sendRequest(){
        String url = APIUrl.Url + "/api/account/Register?email="+email+"&password="+password+
                "&confirmPassword="+confirmPassword;
        StringRequest stringRequest = new StringRequest(Request.Method.POST,url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        hideProgressBar();
                        Intent loginIntent = new Intent(SignUpActivity.this,LoginActivity.class);
                        startActivity(loginIntent);
                        finish();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        hideProgressBar();
                        Toast.makeText(SignUpActivity.this,"Please Insect correct password or try again",Toast.LENGTH_LONG).show();
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
                Toast.makeText(SignUpActivity.this,error.networkResponse.toString(),Toast.LENGTH_LONG);
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent mainActivity = new Intent(SignUpActivity.this,MainActivity.class);
        startActivity(mainActivity);
        finish();
    }
}
