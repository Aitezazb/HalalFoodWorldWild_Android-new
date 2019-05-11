package com.example.a.halalfoodworldwide;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.a.halalfoodworldwide.Models._User;

import org.w3c.dom.Text;

public class Setting extends AppCompatActivity {

    //AlertDialog Box
    AlertDialog.Builder termAndConditionDialog,reportDialog,contactUsDialog;

    //TextView from UI
    TextView termAndConditionTextView,reportTextView,contactUsTextView,signOutTextView;

    //Bottom menu
    BottomNavigationView bottomNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        //Getting TextView references
        termAndConditionTextView = (TextView) findViewById(R.id.SettingTermAndCondition);
        reportTextView =(TextView) findViewById(R.id.SettingReport);
        contactUsTextView = (TextView) findViewById(R.id.SettingContactUs);
        signOutTextView = (TextView) findViewById(R.id.SettingSignOut);

        //bottom menu
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.SettingbottomMenu);

        //Adding selected default item
        bottomNavigationView.setSelectedItemId(R.id.nav_setting);

        //Adding user email to UI field
        TextView email = (TextView) findViewById(R.id.SettingEmail);

        email.setText(_User.getEmail());

        //Assigning click listener
        termAndConditionTextView.setOnClickListener(termAndConditionListener);
        reportTextView.setOnClickListener(reportTextViewListener);
        contactUsTextView.setOnClickListener(contactUsTextViewListener);
        signOutTextView.setOnClickListener(signOutTextViewListener);
        bottomNavigationView.setOnNavigationItemSelectedListener(nav_Listener);


        termAndConditionDialog=reportDialog=contactUsDialog = new AlertDialog.Builder(this);
    }

    View.OnClickListener termAndConditionListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            termAndConditionDialog.setMessage("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.")
                    .setTitle("Terms And Conditions").setNeutralButton("OK",null).create().show();
        }
    };

    View.OnClickListener reportTextViewListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            reportDialog.setMessage("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.")
                    .setTitle("Report").setNeutralButton("OK",null).create().show();
        }
    };

    View.OnClickListener contactUsTextViewListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            contactUsDialog.setMessage("Email me @ aitezazBilal95@gmai.com")
                    .setTitle("Contact Us").setNeutralButton("OK",null).create().show();
        }
    };

    View.OnClickListener signOutTextViewListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            _User.setEmail(null);
            _User.setToken(null);
            Intent MainActivity = new Intent(Setting.this, com.example.a.halalfoodworldwide.MainActivity.class);
            startActivity(MainActivity);
            finish();
        }
    };

    private BottomNavigationView.OnNavigationItemSelectedListener nav_Listener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            switch (menuItem.getItemId()){
                case R.id.nav_home:{
                    Intent userAllRatingIntent = new Intent(Setting.this,MainActivity.class);
                    startActivity(userAllRatingIntent);
                    finish();
                    break;
                }
                case R.id.nav_Ratings:{
                        Intent userAllRatingIntent = new Intent(Setting.this,UserAllRatingActivity.class);
                        startActivity(userAllRatingIntent);
                        finish();

                }
                case R.id.nav_tag:{
                        Intent userAllRatingIntent = new Intent(Setting.this,UserAllTagging.class);
                        startActivity(userAllRatingIntent);
                        finish();
                    break;
                }
                case R.id.nav_setting :{
                        Intent userAllRatingIntent = new Intent(Setting.this,Setting.class);
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
        Intent mainActivity = new Intent(Setting.this,MainActivity.class);
        startActivity(mainActivity);
        finish();
    }
}
