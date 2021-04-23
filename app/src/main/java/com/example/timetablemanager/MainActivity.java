package com.example.timetablemanager;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.timetablemanager.ui.main.SectionsPagerAdapter;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URI;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final int RESULT_LOAD_IMAGE = 1;

    DrawerLayout drawerLayout;
    NavigationView navView;
    Dialog changePict;
    Toolbar toolbar;
    String username;
    ImageView Picture;
//    ImageView menu;
//    Dialog About;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        username = getIntent().getStringExtra(LoginActivity.USERNAME_EXTRA);

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager(), username);
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

        View headerLayout = navView.inflateHeaderView(R.layout.header);
        TextView userName = headerLayout.findViewById(R.id.username);

        userName.setText(username);

//        String tag = "android:switcher:" + R.id.view_pager + ":" + viewPager.getCurrentItem();
//        DayFragment f = (DayFragment) getSupportFragmentManager().findFragmentByTag(tag);
//        if(f != null) {
//            f.setUsername(username);
//        }

        //set dialog box
        changePict = new Dialog(MainActivity.this);
        changePict.setContentView(R.layout.changepic_layout);

        Picture = headerLayout.findViewById(R.id.pict);

        //Set header profile picture dialog box
        CardView ProfPict = headerLayout.findViewById(R.id.profpict);

        ProfPict.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                changePict.findViewById(R.id.change).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                        photoPickerIntent.setType("image/*");
                        startActivityForResult(photoPickerIntent, RESULT_LOAD_IMAGE );
                        changePict.dismiss();
                    }
                });

                changePict.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        changePict.dismiss();
                    }
                });

                changePict.show();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null) {
            try {
                Uri imageUri = data.getData();
                InputStream imageStream = getContentResolver().openInputStream(imageUri);
                Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                Picture.setImageBitmap(selectedImage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
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