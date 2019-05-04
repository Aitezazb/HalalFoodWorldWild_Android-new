package com.example.a.halalfoodworldwide;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

public class UserAllTagging extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_all_tagging);

        //bottom menu
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomMenu);
        bottomNavigationView.setOnNavigationItemSelectedListener(nav_Listener);

        //Adding selected default item
        bottomNavigationView.setSelectedItemId(R.id.nav_tag);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener nav_Listener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            switch (menuItem.getItemId()){
                case R.id.nav_home:{
                    Intent mainActivity  =  new Intent(UserAllTagging.this,MainActivity.class);
                    startActivity(mainActivity);
                    finish();
                    break;
                }
                case R.id.nav_Ratings:{
                    Intent userAllRatingIntent = new Intent(UserAllTagging.this,UserAllRatingActivity.class);
                    startActivity(userAllRatingIntent);
                    finish();
                    break;
                }
                case R.id.nav_tag:{

                    break;
                }
                case R.id.nav_setting :{
                    break;
                }
            }
            return true;
        }
    };
}
