package com.patachadmin.patachou.UserReport;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.patachadmin.patachou.R;

public class ReportDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repoert_detail);
    }

    public void finish(View view) {
        finish();
    }
}