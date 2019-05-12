package com.yash.delhimetro.ui.main;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.yash.delhimetro.R;

import java.util.ArrayList;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.tab_text_1, R.string.tab_text_2};
    private final Context mContext;
    private ArrayList<String> stationNameList;

    public SectionsPagerAdapter(Context context,
                                FragmentManager fm,
                                ArrayList<String> stationNameList

    ) {
        super(fm);
        mContext = context;
        this.stationNameList = stationNameList;
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragmentRoutes (defined as a static inner class below).
        Fragment fragment;
        if (position == 0) {
            fragment = PlaceholderFragmentRoutes.newInstance(position + 1,stationNameList);
        } else {
            fragment = PlaceholderFragmentPlaceNearby.newInstance(position + 1,stationNameList);
        }

        return fragment;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        // Show 2 total pages.
        return 2;
    }
}