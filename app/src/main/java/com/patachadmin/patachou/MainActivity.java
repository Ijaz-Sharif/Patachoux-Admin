package com.patachadmin.patachou;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;

import com.google.android.material.tabs.TabLayout;
import com.patachadmin.patachou.Adapter.ViewPagerAdapter;
import com.patachadmin.patachou.Fragments.BreadFragment;
import com.patachadmin.patachou.Fragments.PasteryFragment;

public class MainActivity extends AppCompatActivity {
    ViewPager viewPager;
    TabLayout tabLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tabLayout=findViewById(R.id.tab_layout);
        viewPager=findViewById(R.id.may_viewpager);
        setUpViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    }
    private void setUpViewPager(ViewPager viewPager){
        ViewPagerAdapter viewPagerAdapter=new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(new BreadFragment(),"Bread");
        viewPagerAdapter.addFragment(new PasteryFragment(),"Pastry");
        viewPager.setAdapter(viewPagerAdapter);
    }

    public void finish(View view) {
        finish();
    }
}