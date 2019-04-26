package com.example.a.halalfoodworldwide;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

    EditText ET_email, ET_password, ET_confirmPassword;

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
        ET_email = (EditText) findViewById(R.id.signUpEmail);
        ET_password = (EditText) findViewById(R.id.signUpPassword);
        ET_confirmPassword = (EditText) findViewById(R.id.signUpConfirmPassword) ;

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
                sendRequest();
            }else{
                Toast.makeText(SignUpActivity.this,"Please Insect correct password",Toast.LENGTH_LONG)
                        .show();
                return;
            }
        }
    };

    private boolean IsCorrectFormat(){
        email = ET_email.getText().toString();
        password =  ET_password.getText().toString();
        confirmPassword = ET_confirmPassword.getText().toString();
        if(isEmailValid(email) && isPasswordValid(password) && isConfirmPasswordMatch(confirmPassword)){
                return true;
        }
        return false;
    }

     private boolean isEmailValid(CharSequence email) {
        boolean s = android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
        if(!s)Toast.makeText(SignUpActivity.this,"wrong email",Toast.LENGTH_LONG)
                .show();

        return s;

    }

    private boolean isPasswordValid(String password){
        boolean s = password.matches(PASSWORD_PATTERN);
        if(!s)Toast.makeText(SignUpActivity.this,"wrong password",Toast.LENGTH_LONG).show();
        return s;
    }

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
                        Intent loginIntent = new Intent(SignUpActivity.this,LoginActivity.class);
                        startActivity(loginIntent);
                        finish();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(SignUpActivity.this,"error in response",Toast.LENGTH_LONG).show();
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
}
