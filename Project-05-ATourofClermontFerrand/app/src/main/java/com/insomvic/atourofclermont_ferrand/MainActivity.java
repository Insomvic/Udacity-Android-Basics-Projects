package com.insomvic.atourofclermont_ferrand;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Put fragments on viewpager within layout
        ViewPager viewPager = findViewById(R.id.viewpager);
        CategoryAdapter adapter = new CategoryAdapter(this, getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        // Set tabs on layout
        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

}