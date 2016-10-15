package com.mich.airlines.view;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.mich.airlines.R;
import com.mich.airlines.view.list.AirlineListFragment;

public class SectionsPagerAdapter extends FragmentPagerAdapter {

    private final CharSequence airlinesSectionName;
    private final CharSequence favoritesSectionName;

    public SectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        airlinesSectionName = context.getString(R.string.section_airlines);
        favoritesSectionName = context.getString(R.string.section_favorites);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return AirlineListFragment.newInstance(false);
            case 1:
                return AirlineListFragment.newInstance(true);
            default:
                throw new IllegalArgumentException("Wrong position: " + position);
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return airlinesSectionName;
            case 1:
                return favoritesSectionName;
        }
        return null;
    }
}

