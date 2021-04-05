package com.example.timetablemanager;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.timetablemanager.ui.main.SectionsPagerAdapter;
import com.vishnusivadas.advanced_httpurlconnection.FetchData;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    NavigationView navView;
    Toolbar toolbar;
    String username;
//    ImageView menu;
//    Dialog About;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        drawerLayout = findViewById(R.id.drawer_layout);
        navView = findViewById(R.id.navigation_view);
        toolbar = findViewById(R.id.toolbar);
//        menu = findViewById(R.id.menu);

        setSupportActionBar(toolbar);
        toolbar.setTitleTextAppearance(this, R.style.ToolbarTitle);

        navView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.nav_drawer_open, R.string.nav_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navView.setCheckedItem(R.id.nav_home);
        navView.setNavigationItemSelectedListener(this);

//        menu.setOnClickListener(new View.OnClickListener(){
//            public void onClick(View v) {
//                // open drawer here
//                drawerLayout.openDrawer(GravityCompat.START);
//            }
//        });

        username = getIntent().getStringExtra(LoginActivity.USERNAME_EXTRA);

            String tag = "android:switcher:" + R.id.view_pager + ":" + viewPager.getCurrentItem();
            DayFragment f = (DayFragment) getSupportFragmentManager().findFragmentByTag(tag);
            if(f != null) {
                f.getUsername(username);
            }

    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch(item.getItemId()) {
            case R.id.nav_home:
                break;
            case R.id.nav_about:
                Dialog about = new Dialog(this);
                about.setContentView(R.layout.about_popup);
                TextView closeBtn = about.findViewById(R.id.close);
                closeBtn.setOnClickListener(v -> {
                    navView.setCheckedItem(R.id.nav_home);
                    about.dismiss();
                });
                about.show();
                break;
            case R.id.nav_logout:
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
                break;

        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

}