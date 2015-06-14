package com.vaultshare.play;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.github.florent37.materialviewpager.MaterialViewPager;
import com.squareup.otto.Subscribe;
import com.vaultshare.play.activities.BaseActivity;

import butterknife.InjectView;


public class MainActivity extends BaseActivity
        implements NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    @InjectView(R.id.drawer)
    DrawerLayout drawerLayout;
    @InjectView(R.id.toolbar_actionbar)
    Toolbar      toolbar;

    @Override
    public int getLayout() {
        return R.layout.activity_main_old;
    }

    @Override
    public void initUI() {


        setTitle("");
//        mViewPager = (MaterialViewPager) findViewById(R.id.materialViewPager);
//        toolbar = (Toolbar) findViewById(R.id.toolbar_actionbar); //mViewPager.getToolbar();
        setSupportActionBar(toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);

            final ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setDisplayShowHomeEnabled(true);
                actionBar.setDisplayShowTitleEnabled(true);
                actionBar.setDisplayUseLogoEnabled(false);
                actionBar.setHomeButtonEnabled(true);
            }
        }

//        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawer, 0, 0);
//        mDrawer.setDrawerListener(mDrawerToggle);


        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.fragment_drawer);

        // Set up the drawer.
        mNavigationDrawerFragment.setup(R.id.fragment_drawer, drawerLayout, toolbar);
        // populate the navigation drawer
        mNavigationDrawerFragment.setUserData("John Doe", "johndoe@doe.com", BitmapFactory.decodeResource(getResources(), R.drawable.avatar));
    }
//
//    @InjectView(R.id.toolbar_actionbar)
//    Toolbar mToolbar;

    public void setToolbarVisibility(int v) {
        toolbar.setVisibility(v);
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        Toast.makeText(this, "Menu item selected -> " + position, Toast.LENGTH_SHORT).show();
//        toolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        Fragment f = null;
        switch (position) {
            case 0: // Following/Newsfeed
                f = new BrowseStationsFragment();
//                toolbar.setVisibility(View.GONE);
                break;
            case 1: // Vault
                f = new DJFragment();
//                toolbar.setVisibility(View.VISIBLE);
//                f = new DigFragment();
                break;
            case 2: // Mixing
                f = new VaultFragment();
//                toolbar.setVisibility(View.VISIBLE);
//                f = new SettingsFragment();
                break;
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, f)
                .commit();
    }


    @Override
    public void onBackPressed() {
        if (mNavigationDrawerFragment.isDrawerOpen())
            mNavigationDrawerFragment.closeDrawer();
        else
            super.onBackPressed();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private MaterialViewPager mViewPager;

    //    private DrawerLayout          mDrawer;
//    private ActionBarDrawerToggle mDrawerToggle;
//    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
//        mDrawerToggle.syncState();
    }

    public Toolbar getToolbar() {
        return toolbar;
    }

    @Subscribe
    public void onDrawerToggle(DrawerToggle e) {
        onDrawerToggle();
    }

    private void onDrawerToggle() {
        if (mNavigationDrawerFragment.isDrawerOpen())
            mNavigationDrawerFragment.closeDrawer();
        else mNavigationDrawerFragment.openDrawer();
    }
}
