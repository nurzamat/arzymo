package org.ananasit.rekordo.pagedheader;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.ananasit.rekordo.R;
import org.ananasit.rekordo.pagedheader.fragments.BottomIndicatorFragment;
import org.ananasit.rekordo.pagedheader.fragments.ColdplayFragment;
import org.ananasit.rekordo.pagedheader.fragments.TopIndicatorFragment;


public class PagedHeaderActivity extends FragmentActivity implements ListView.OnItemClickListener {

    private String[] sectionTitles;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private ListView mDrawerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.header_activity_main);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.drawable.ic_ab_back_mtrl_am_alpha, R.string.drawer_open, R.string.drawer_close);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.drawable.ic_ab_back_mtrl_am_alpha, R.string.drawer_open, R.string.drawer_close) {
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu();
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }
        };

        //mDrawerLayout.setDrawerListener(mDrawerToggle);
        //getActionBar().setDisplayHomeAsUpEnabled(true);

        sectionTitles = getResources().getStringArray(R.array.fragment_names);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        //mDrawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_list_item, sectionTitles));
        //mDrawerList.setOnItemClickListener(this);

        selectItem(7);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        //mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        //mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }



        return super.onOptionsItemSelected(item);
    }

    private void selectItem(int position) {

        Fragment fragment = null;
        Bundle args = new Bundle();
        switch (position) {
            case 0:
                fragment = new TopIndicatorFragment();
                args.putString("type", "depth");
                fragment.setArguments(args);
                break;
            case 1:
                fragment = new TopIndicatorFragment();
                args.putString("type", "zoomout");
                fragment.setArguments(args);
                break;
            case 2:
                fragment = new TopIndicatorFragment();
                args.putString("type", "rotation");
                fragment.setArguments(args);
                break;
            case 3:
                fragment = new BottomIndicatorFragment();
                args.putString("type", "flip");
                fragment.setArguments(args);
                break;
            case 4:
                fragment = new BottomIndicatorFragment();
                args.putString("type", "scale");
                fragment.setArguments(args);
                break;
            case 5:
                fragment = new BottomIndicatorFragment();
                args.putString("type", "accordion");
                fragment.setArguments(args);
                break;
            case 6:
                fragment = new ColdplayFragment();
                break;
            case 7:
                fragment = new BottomIndicatorFragment();
                args.putString("type", "depth");
                fragment.setArguments(args);
                break;
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, fragment)
                .commit();

        //mDrawerList.setItemChecked(position, true);
       // mDrawerLayout.closeDrawer(mDrawerList);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        selectItem(position);
    }
}
