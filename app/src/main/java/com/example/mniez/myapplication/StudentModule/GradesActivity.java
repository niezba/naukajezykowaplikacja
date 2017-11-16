package com.example.mniez.myapplication.StudentModule;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import com.example.mniez.myapplication.R;
import com.example.mniez.myapplication.StudentModule.ActivityAdapter.CoursesSearchTabAdapter;

import java.util.ArrayList;

public class GradesActivity extends BaseDrawerActivity {

    SharedPreferences sharedpreferences;
    private static final String MY_PREFERENCES = "DummyLangPreferences";
    private static final String PREFERENCES_USERNAME = "loggedUserLogin";
    private static final String PREFERENCES_PASSWORD = "loggedUserPassword";
    private static final String PREFERENCES_NAMESURNAME = "loggedUserNameSurname";
    private static final String PREFERENCES_ROLE = "loggedUserMainRole";
    private static final String PREFERENCES_ID = "loggedUserId";
    private static final String PREFERENCES_OFFLINE = "isOffline";
    private static final String ADMIN_ROLE_NAME = "Administrator";
    private static final String TEACHER_ROLE_NAME = "Nauczyciel";
    private static final String STUDENT_ROLE_NAME = "Uczeń";

    Integer isOffline;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.content_grades, frameLayout);
        setTitle("Moje oceny");
        sharedpreferences = getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);
        String currentUsername = sharedpreferences.getString(PREFERENCES_USERNAME, "");
        String currentPassword = sharedpreferences.getString(PREFERENCES_PASSWORD, "");
        String currentNameSurname = sharedpreferences.getString(PREFERENCES_NAMESURNAME, "");
        String currentId = sharedpreferences.getString(PREFERENCES_ID, "");
        String currentRole = sharedpreferences.getString(PREFERENCES_ROLE, "");
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View navHeaderView= navigationView.getHeaderView(0);
        TextView navUsername = (TextView) navHeaderView.findViewById(R.id.loggedUsername);
        TextView navFullname = (TextView) navHeaderView.findViewById(R.id.loggedNameSurname);
        String userRole;
        switch(currentRole) {
            case "ROLE_ADMIN":
                userRole = ADMIN_ROLE_NAME;
                break;
            case "ROLE_TEACHER":
                userRole = TEACHER_ROLE_NAME;
                break;
            case "ROLE_STUDENT":
                userRole = STUDENT_ROLE_NAME;
                break;
            default:
                userRole = "Rola niezdefiniowana";
                break;
        }
        navUsername.setText(userRole);
        navFullname.setText(currentNameSurname);
        isOffline = sharedpreferences.getInt("isOffline", 0);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_offline, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if(isOffline == 1) {
            menu.findItem(R.id.action_offline).setChecked(true);
        }
        return true;
    }


    @Override
    protected void onResume() {
        super.onResume();
        navigationView.getMenu().getItem(2).setChecked(true);
    }
}
