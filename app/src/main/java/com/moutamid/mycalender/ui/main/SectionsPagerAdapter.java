package com.moutamid.mycalender.ui.main;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.moutamid.mycalender.AllDiaryFragment;
import com.moutamid.mycalender.AllNotesFragment;
import com.moutamid.mycalender.AllToDoFragment;
import com.moutamid.mycalender.R;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    int totalTabs;
    private final Context mContext;

    public SectionsPagerAdapter(Context context, FragmentManager fm, int totalTabs) {
        super(fm);
        mContext = context;
        this.totalTabs = totalTabs;
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        switch (position) {
            case 0:
                AllDiaryFragment DiaryFragment = new AllDiaryFragment();
                return DiaryFragment;
            case 1:
                AllNotesFragment ALlNotes = new AllNotesFragment();
                return ALlNotes;

            case 2:
                AllToDoFragment AllToTo = new AllToDoFragment();
                return AllToTo;
            default:
                return null;
        }

    }


    @Override
    public int getCount() {
        // Show 2 total pages.
        return 3;
    }
}