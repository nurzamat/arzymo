package org.ananasit.arzymo;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import org.ananasit.arzymo.model.User;
import org.ananasit.arzymo.util.Constants;

public class MainActivity extends ActionBarActivity {

    //First We Declare Titles And Icons For Our Navigation Drawer List View
    //This Icons And Titles Are holded in an Array as you can see
    String TITLES[] = {"История вызовов", "Ваши таксисты", "Настройки", "Выход"};
    int ICONS[] = {R.drawable.call_history, R.drawable.car, R.drawable.settings, R.drawable.ic_exit};

    //Similarly we Create a String Resource for the name and email in the header view
    //And we also create a int resource for profile picture in the header view
    public boolean isexit = false;
    String NAME = "";
    String EMAIL = "";
    int PROFILE = R.drawable.aka;

    public Toolbar toolbar;                              // Declaring the Toolbar Object

    RecyclerView mRecyclerView;                           // Declaring RecyclerView
    RecyclerView.Adapter mAdapter;                        // Declaring Adapter For Recycler View
    RecyclerView.LayoutManager mLayoutManager;            // Declaring Layout Manager as a linear layout manager
    DrawerLayout Drawer;                                  // Declaring DrawerLayout
    ActionBarDrawerToggle mDrawerToggle;                  // Declaring Action Bar Drawer Toggle
    User u = null;
    SharedPreferences sp;
    AppController appcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        appcon = AppController.getInstance();
        u = appcon.getUser();
        if (u == null) {
            //createUser();
        }

        if (u != null) {
            //
            NAME = u.getName();
            //EMAIL = u.getEmail();
            EMAIL = u.getPhone();
        }
    /* Assinging the toolbar object ot the view
    and setting the the Action bar to our toolbar
     */
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        toolbar.setLogo(R.drawable.logo_toolbar);
        //toolbar.setTitle("title");
        toolbar.setSubtitle("Arzymo");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setDisplayShowHomeEnabled(true);

        mRecyclerView = (RecyclerView) findViewById(R.id.RecyclerView); // Assigning the RecyclerView Object to the xml View

        mRecyclerView.setHasFixedSize(true);                            // Letting the system know that the list objects are of fixed size

        mAdapter = new PelicanAdapter(TITLES, ICONS, NAME, EMAIL, PROFILE, this);       // Creating the Adapter of PelicanAdapter class(which we are going to see in a bit)
        // And passing the titles,icons,header view name, header view email,
        // and header view profile picture

        mRecyclerView.setAdapter(mAdapter);                              // Setting the adapter to RecyclerView

        final GestureDetector mGestureDetector = new GestureDetector(MainActivity.this, new GestureDetector.SimpleOnGestureListener() {

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }

        });

        mRecyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {
                View child = recyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());

                if (child != null && mGestureDetector.onTouchEvent(motionEvent)) {
                    Drawer.closeDrawers();
                    //Toast.makeText(MainActivity.this,"The Item Clicked is: "+recyclerView.getChildPosition(child),Toast.LENGTH_SHORT).show();
                    displayView(recyclerView.getChildPosition(child));
                    return true;
                }

                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {

            }
        });

        mLayoutManager = new LinearLayoutManager(this);                 // Creating a layout Manager
        mRecyclerView.setLayoutManager(mLayoutManager);                 // Setting the layout Manager
        Drawer = (DrawerLayout) findViewById(R.id.DrawerLayout);        // Drawer object Assigned to the view
        mDrawerToggle = new ActionBarDrawerToggle(this, Drawer, toolbar, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                // code here will execute once the drawer is opened( As I dont want anything happened whe drawer is
                // open I am not going to put anything here)
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                // Code here will execute once drawer is closed
            }

        }; // Drawer Toggle Object Made
        Drawer.setDrawerListener(mDrawerToggle); // Drawer Listener set to the Drawer toggle
        mDrawerToggle.syncState();               // Finally we set the drawer toggle sync State

        if (savedInstanceState == null) {
            displayView(Constants.def_home);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onResume() {
        super.onResume();

        //onResumeMethod();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void onResumeMethod() {
        try {
            u = appcon.getUser();
            if (u == null) {
                //createUser();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent _MainActivity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.action_add_post) {
            Intent in = new Intent(MainActivity.this, AddPostActivity.class);
            startActivity(in);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }


    public void displayView(int position) {
        Fragment fragment = null;

        switch (position) {
            case 0: {
                //fragment = new ProfileFragment();
                break;
            }
            case 1: {
                //Intent in = new Intent(MainActivity.this, HistoryActivity.class);
                //startActivity(in);
                /*
                mDrawerToggle.setDrawerIndicatorEnabled(false);
                fragment = new HistoryFragment();
                  */
                break;
            }
            case 2: {
                //Intent in = new Intent(MainActivity.this, DriversActivity.class);
                //startActivity(in);
                break;
            }
            case 3: {
                //Intent in = new Intent(MainActivity.this, SettingsActivity.class);
                //startActivity(in);
                break;
            }
            case 4: {
                SharedPreferences sp = getSharedPreferences(Constants.PELIKAN, 0);
                SharedPreferences.Editor Ed = sp.edit();
                Ed.putBoolean(Constants.PELIKAN_LOGEDIN, false);
                Ed.commit();

                //Intent in = new Intent(MainActivity.this, LoginActivity.class);
                //startActivity(in);
                finish();
                return;

            }
            case Constants.def_home: {
                mDrawerToggle.setDrawerIndicatorEnabled(true);
                fragment = new HomeFragment();
            }
            default:
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();

            if (position != Constants.def_home) {
                fragmentManager.beginTransaction().addToBackStack(null).replace(R.id.content_frame, fragment).commit();
            } else {
                fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
            }
        }

        // update selected item and title, then close the drawer

        //mRecyclerView.setItemChecked(position, true);
        // mDrawerList.setSelection(position);
        // mDrawerLayout.closeDrawer(mDrawerList);
    }

    @Override
    public void onBackPressed() {
        if (isexit)
            super.onBackPressed();
        else {
            final AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
            dialog.setMessage("Хотите выйти?");
            dialog.setPositiveButton("Да", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    isexit = true;
                    onBackPressed();
                }
            });
            dialog.setNegativeButton(MainActivity.this.getString(R.string.cancel), new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    // TODO Auto-generated method stub

                }
            });
            dialog.show();
        }

        // turn on the Navigation Drawer image;
        // this is called in the LowerLevelFragments

        // mDrawerToggle.setDrawerIndicatorEnabled(true);
    }

}

