package com.example.mniez.myapplication.TeacherModule;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.example.mniez.myapplication.LoginActivity;
import com.example.mniez.myapplication.R;
import com.example.mniez.myapplication.StudentModule.CourseBrowseActivity;
import com.example.mniez.myapplication.StudentModule.GradesActivity;

/**
 * Created by mniez on 18.10.2017.
 */

public class TeacherBaseDrawerActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    SharedPreferences sharedpreferences;
    private static final String MY_PREFERENCES = "DummyLangPreferences";
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    FrameLayout frameLayout;
    NavigationView navigationView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_teacher_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        frameLayout = (FrameLayout) findViewById(R.id.content_frame);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        //to prevent current item select over and over
        if (item.isChecked()){
            drawerLayout.closeDrawer(GravityCompat.START);
            return false;
        }

        if (id == R.id.nav_camera) {
            // Handle the camera action
            startActivity(new Intent(getApplicationContext(), TeacherMainActivity.class));
        } else if (id == R.id.nav_gallery) {
            startActivity(new Intent(getApplicationContext(), CourseBrowseActivity.class));
        } else if (id == R.id.nav_slideshow) {
            startActivity(new Intent(getApplicationContext(), GradesActivity.class));
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {
            sharedpreferences = getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);
            sharedpreferences.edit().clear().commit();
            TeacherBaseDrawerActivity.this.deleteDatabase("dummyDatabase");
            Intent intent = new Intent(TeacherBaseDrawerActivity.this, LoginActivity.class);
            startActivity(intent);
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

}
