package org.ananasit.arzymo.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import org.ananasit.arzymo.AddPostFragment;
import org.ananasit.arzymo.AddReklamaFragment;
import org.ananasit.arzymo.ReklamasFragment;
import org.ananasit.arzymo.CategoriesFragment;
import org.ananasit.arzymo.util.Constants;

public class ViewPagerAdapter extends FragmentStatePagerAdapter
{

    CharSequence[] Titles; // This will Store the Titles of the Tabs which are Going to be passed when ViewPagerAdapter is created
    int NumbOfTabs; // Store the number of tabs, this will also be passed when the ViewPagerAdapter is created

    String mode = "";

    // Build a Constructor and assign the passed Values to appropriate values in the class
    public ViewPagerAdapter(FragmentManager fm, CharSequence[] mTitles, int mNumbOfTabsumb, String _mode)
    {
        super(fm);
        this.Titles = mTitles;
        this.NumbOfTabs = mTitles.length;
        this.mode = _mode;
    }

    //This method return the fragment for the every position in the View Pager
    @Override
    public Fragment getItem(int position)
    {
        if(mode.equals(Constants.HOME_MODE))
        {
            if(position == 0) // if the position is 0 we are returning the First tab
            {
                ReklamasFragment tab1 = new ReklamasFragment();
                return tab1;
            }
            else             // As we are having 2 tabs if the position is now 0 it must be 1 so we are returning second tab
            {
                CategoriesFragment tab2 = new CategoriesFragment();
                return tab2;
            }
        }
        if(mode.equals(Constants.ADD_POST_MODE))
        {
            if(position == 0) // if the position is 0 we are returning the First tab
            {
                AddReklamaFragment tab1 = new AddReklamaFragment();
                return tab1;
            }
            else             // As we are having 2 tabs if the position is now 0 it must be 1 so we are returning second tab
            {
                AddPostFragment tab2 = new AddPostFragment();
                return tab2;
            }
        }

        return null;
    }

    // This method return the titles for the Tabs in the Tab Strip

    @Override
    public CharSequence getPageTitle(int position) {
        return Titles[position];
    }

    // This method return the Number of tabs for the tabs Strip

    @Override
    public int getCount() {
        return NumbOfTabs;
    }
}
