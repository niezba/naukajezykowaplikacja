package com.example.mniez.myapplication.StudentModule.ActivityAdapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.mniez.myapplication.StudentModule.Fragments.AllCoursesFragment;
import com.example.mniez.myapplication.StudentModule.Fragments.SearchCoursesFragment;

/**
 * Created by mniez on 25.10.2017.
 */

public class CoursesSearchTabAdapter extends FragmentStatePagerAdapter {

    int mNumOfTabs;
    private String tabTitles[] = new String[]{"Wszystkie", "Wyszukaj"};

    public CoursesSearchTabAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch(position) {
            case 0:
                AllCoursesFragment tab1 = new AllCoursesFragment();
                return tab1;
            case 1:
                SearchCoursesFragment tab2 = new SearchCoursesFragment();
                return tab2;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }

}
