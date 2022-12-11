package com.patachadmin.patachou.User;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;

import com.google.android.material.tabs.TabLayout;
import com.patachadmin.patachou.Adapter.ViewPagerAdapter;
import com.patachadmin.patachou.R;
import com.patachadmin.patachou.UserProductFragment.UserBreadFragment;
import com.patachadmin.patachou.UserProductFragment.UserPasteryFragment;

public class UserProductActivity extends AppCompatActivity {
    ViewPager viewPager;
    TabLayout tabLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_product);
        tabLayout=findViewById(R.id.tab_layout);
        viewPager=findViewById(R.id.may_viewpager);
        setUpViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    }
    private void setUpViewPager(ViewPager viewPager){
        ViewPagerAdapter viewPagerAdapter=new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(new UserBreadFragment(),"Bread");
        viewPagerAdapter.addFragment(new UserPasteryFragment(),"Pastry");
        viewPager.setAdapter(viewPagerAdapter);
    }

    public void finish(View view) {
        finish();}
}