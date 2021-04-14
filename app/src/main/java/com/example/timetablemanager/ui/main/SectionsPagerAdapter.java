package com.example.timetablemanager.ui.main;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.timetablemanager.DayFragment;
import com.example.timetablemanager.R;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.monday, R.string.tuesday,R.string.wednesday,R.string.thursday,R.string.friday,R.string.saturday,R.string.sunday};
    private final Context mContext;
    private final String mUsername;

    public SectionsPagerAdapter(Context context, FragmentManager fm, String username) {
        super(fm);
        mContext = context;
        this.mUsername = username;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new DayFragment().newInstance(mUsername, "mon");
            case 1:
                return new DayFragment().newInstance(mUsername, "tue");
            case 2:
                return new DayFragment().newInstance(mUsername, "wed");
            case 3:
                return new DayFragment().newInstance(mUsername, "thu");
            case 4:
                return new DayFragment().newInstance(mUsername, "fri");
            case 5:
                return new DayFragment().newInstance(mUsername, "sat");
            case 6:
                return new DayFragment().newInstance(mUsername, "sun");
            default:
                return new PlaceholderFragment();
        }
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        return 7;
    }
}