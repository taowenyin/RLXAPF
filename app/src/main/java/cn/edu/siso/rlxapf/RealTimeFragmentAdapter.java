package cn.edu.siso.rlxapf;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class RealTimeFragmentAdapter extends FragmentPagerAdapter {

    private Fragment[] fragments = null;
    private int[] titles = null;

    private Context context = null;

    public RealTimeFragmentAdapter(FragmentManager fm, Fragment[] fragments, int[] titles, Context context) {
        super(fm);

        this.fragments = fragments;
        this.titles = titles;
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments[position];
    }

    @Override
    public int getCount() {
        return fragments.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return context.getResources().getString(titles[position]);
    }
}
