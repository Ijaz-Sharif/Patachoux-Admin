package com.patachadmin.patachou.Screens;

import static com.patachadmin.patachou.Utils.Constant.getAdminLoginStatus;
import static com.patachadmin.patachou.Utils.Constant.getSuperAdminLoginStatus;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;

import com.patachadmin.patachou.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        Thread thread=new Thread(){
            @Override
            public void run() {
                try {
                    sleep(3000);
                    if(getAdminLoginStatus(SplashActivity.this)){
                        startActivity(new Intent(SplashActivity.this, AdminMainActivity.class));
                        finish();
                    }
                    else  if(getSuperAdminLoginStatus(SplashActivity.this)){
                        startActivity(new Intent(SplashActivity.this, SuperAdminMainActivity.class));
                        finish();
                    }else{
                        startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                        finish();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
    }
}