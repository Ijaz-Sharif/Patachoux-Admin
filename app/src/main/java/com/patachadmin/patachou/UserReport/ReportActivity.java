package com.patachadmin.patachou.UserReport;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.patachadmin.patachou.R;

public class ReportActivity extends AppCompatActivity {
        String userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        userId=getIntent().getStringExtra("userId");
    }

    public void finish(View view) {
        finish();
    }
}