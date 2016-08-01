package in.silive.clime.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;

import java.util.List;

import in.silive.clime.Fragments.HourFragment;
import in.silive.clime.Fragments.WeekFragment;

/**
 * Created by akriti on 29/7/16.
 */
public class VPagerAdapter extends FragmentPagerAdapter {
    private static int NUM_ITEMS = 2;
    public static List hlist;
    public static List wlist;

    public VPagerAdapter(FragmentManager fm,List l,List w) {
        super(fm);
        this.hlist = l;
        this.wlist = w;
    }

    @Override
    public int getCount() {
        return NUM_ITEMS;
    }


    @Override
    public boolean isViewFromObject(View view, Object object) {
        return false;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = new HourFragment();
        switch (position) {

            case 0:
                 HourFragment fg = new HourFragment();
                fg.setList(hlist);
                return fg;

            case 1:
                WeekFragment wf = new WeekFragment();
                wf.setList(wlist);
                return wf;


        }
        return fragment;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title = "Hourly Update";
        switch (position) {

            case 0:
                title = "Hourly Update";
                    break;

            case 1:
                title = "Weekly Update";
                break;


        }

        return title;
    }

}
