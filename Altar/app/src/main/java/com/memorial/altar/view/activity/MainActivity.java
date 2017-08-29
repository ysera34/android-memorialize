package com.memorial.altar.view.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.memorial.altar.R;
import com.memorial.altar.view.fragment.AltarFragment;
import com.memorial.altar.view.fragment.FriendListFragment;
import com.memorial.altar.view.fragment.ObituaryFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {

    private Toolbar mToolbar;
    private TextView mToolbarStarTextView;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);

        mToolbarStarTextView = mToolbar.findViewById(R.id.toolbar_star_text_view);
        mToolbarStarTextView.setOnClickListener(this);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mTabLayout = (TabLayout) findViewById(R.id.tab_layout);
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        setupViewPager(mViewPager);
        mTabLayout.setupWithViewPager(mViewPager);
        setupTabLayoutIcons();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.toolbar_star_text_view:
                Toast.makeText(getApplicationContext(), "star_text_view", Toast.LENGTH_SHORT).show();
                mToolbarStarTextView.setText(String.valueOf(20));
                break;
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_notifications:
                startActivity(ConfigActivity.newIntent(getApplicationContext(), 0));
                break;
            case R.id.nav_stars:
                startActivity(ConfigActivity.newIntent(getApplicationContext(), 1));
                break;
            case R.id.nav_settings:
                startActivity(ConfigActivity.newIntent(getApplicationContext(), 2));
                break;
            case R.id.nav_faq:
                startActivity(ConfigActivity.newIntent(getApplicationContext(), 3));
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(FriendListFragment.newInstance(), getString(R.string.title_friends));
        adapter.addFragment(AltarFragment.newInstance(), getString(R.string.title_altar));
        adapter.addFragment(ObituaryFragment.newInstance(), getString(R.string.title_obituary));
        viewPager.setAdapter(adapter);
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    private void setupTabLayoutIcons() {
        TextView tabOne = (TextView) LayoutInflater.from(this).inflate(R.layout.tab_item_text_view, null);
        tabOne.setText(getString(R.string.title_friends));
        tabOne.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_tab_friends, 0, 0);
        mTabLayout.getTabAt(0).setCustomView(tabOne);

        TextView tabTwo = (TextView) LayoutInflater.from(this).inflate(R.layout.tab_item_text_view, null);
        tabTwo.setText(getString(R.string.title_altar));
        tabTwo.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_tab_altar, 0, 0);
        mTabLayout.getTabAt(1).setCustomView(tabTwo);

        TextView tabThree = (TextView) LayoutInflater.from(this).inflate(R.layout.tab_item_text_view, null);
        tabThree.setText(getString(R.string.title_obituary));
        tabThree.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_tab_obituary, 0, 0);
        mTabLayout.getTabAt(2).setCustomView(tabThree);
    }
}
