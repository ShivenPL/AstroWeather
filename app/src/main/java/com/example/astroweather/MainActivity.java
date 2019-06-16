package com.example.astroweather;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Surface;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);



        float yInches= metrics.heightPixels/metrics.ydpi;
        float xInches= metrics.widthPixels/metrics.xdpi;
        double diagonalInches = Math.sqrt(xInches*xInches + yInches*yInches);
        if (diagonalInches>=6.5){
            if(getWindowManager().getDefaultDisplay().getRotation() == Surface.ROTATION_0 || getWindowManager().getDefaultDisplay().getRotation() == Surface.ROTATION_180)
            {
                setContentView(R.layout.tablet_layout);
                Toolbar toolbar = findViewById(R.id.toolbar);
                setSupportActionBar(toolbar);
            }
            else
            {
                setContentView(R.layout.activity_main);
                ViewPager viewPager = findViewById(R.id.view_pager);
                viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager()));

                Toolbar toolbar = findViewById(R.id.toolbar);
                setSupportActionBar(toolbar);
            }
        }else{
            setContentView(R.layout.activity_main);
            ViewPager viewPager = findViewById(R.id.view_pager);
            viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager()));

            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.action_settings:
                startActivity(new Intent(this, Settings.class));
                return true;

                default:
                    return super.onOptionsItemSelected(item);
        }

    }
}