package com.patachadmin.patachou.Admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;

import com.google.android.material.tabs.TabLayout;
import com.patachadmin.patachou.Adapter.ViewPagerAdapter;
import com.patachadmin.patachou.AdminProductFragment.AdminBreadFragment;
import com.patachadmin.patachou.AdminProductFragment.AdminPasteryFragment;
import com.patachadmin.patachou.R;

public class AdminProductActivity extends AppCompatActivity {
    ViewPager viewPager;
    TabLayout tabLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_product);
        tabLayout=findViewById(R.id.tab_layout);
        viewPager=findViewById(R.id.may_viewpager);
        setUpViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    }
    private void setUpViewPager(ViewPager viewPager){
        ViewPagerAdapter viewPagerAdapter=new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(new AdminBreadFragment(),"Bread");
        viewPagerAdapter.addFragment(new AdminPasteryFragment(),"Pastry");
        viewPager.setAdapter(viewPagerAdapter);
    }

    public void finish(View view) {
        finish();
    }
}