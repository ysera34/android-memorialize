package com.memorial.altar.view.activity;

import android.content.Context;
import android.content.Intent;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.memorial.altar.R;
import com.memorial.altar.model.LastWill;
import com.memorial.altar.model.User;
import com.memorial.altar.view.fragment.AltarContactFragment;
import com.memorial.altar.view.fragment.AltarCreateFragment;
import com.memorial.altar.view.fragment.AltarPrivateLastWillFragment;
import com.memorial.altar.view.fragment.AltarPublicLastWillFragment;
import com.memorial.altar.view.fragment.AltarReadFragment;
import com.memorial.altar.view.fragment.FriendListFragment;
import com.memorial.altar.view.fragment.FriendReadyFragment;
import com.memorial.altar.view.fragment.ObituaryFragment;
import com.memorial.altar.view.fragment.PermissionHeadlessFragment;

import java.util.ArrayList;

import static com.memorial.altar.common.Common.ALTAR_CREATE_PREVIEW_REQUEST_CODE;
import static com.memorial.altar.common.Common.ALTAR_PREVIEW_RESULT;
import static com.memorial.altar.common.Common.NAV_REQUEST_FAQ;
import static com.memorial.altar.common.Common.NAV_REQUEST_NOTIFICATIONS;
import static com.memorial.altar.common.Common.NAV_REQUEST_SETTINGS;
import static com.memorial.altar.view.fragment.AltarCreateFragment.ALTAR_CREATE_CONTACT_PERMISSION_REQUEST;
import static com.memorial.altar.view.fragment.AltarCreateFragment.ALTAR_CREATE_STORAGE_PERMISSION_REQUEST;
import static com.memorial.altar.view.fragment.AltarPrivateLastWillListFragment.ALTAR_PRIVATE_LAST_WILL_CONTACT_PERMISSION_REQUEST;
import static com.memorial.altar.view.fragment.ObituaryFragment.OBITUARY_SUBMIT_STORAGE_PERMISSION_REQUEST;

public class HomeReadyActivity extends AppCompatActivity
        implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener,
        PermissionHeadlessFragment.PermissionCallbackListener,
        AltarContactFragment.OnAltarContactDialogDismissListener,
        AltarPublicLastWillFragment.OnAltarPublicLastWillDialogDismissListener,
        AltarPrivateLastWillFragment.OnAltarPrivateLastWillDialogDismissListener {

    private static final String TAG = HomeReadyActivity.class.getSimpleName();

    public static Intent newIntent(Context packageContext) {
        Intent intent = new Intent(packageContext, HomeReadyActivity.class);
        return intent;
    }

    private Toolbar mToolbar;
    private TextView mToolbarStarTextView;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private HomeViewPagerAdapter mHomeViewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        if (!UserSharedPreferences.getStoredHomeIntroSlide(getApplicationContext())) {
            startActivity(HomeInfoActivity.newIntent(getApplicationContext()));
//        }

        setContentView(R.layout.activity_home);

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
                startActivity(BillingStarActivity.newIntent(getApplicationContext()));
                break;
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_notifications:
                startActivity(ConfigActivity.newIntent(getApplicationContext(), NAV_REQUEST_NOTIFICATIONS));
                break;
            case R.id.nav_stars:
                startActivity(BillingStarActivity.newIntent(getApplicationContext()));
                break;
            case R.id.nav_settings:
                startActivity(ConfigActivity.newIntent(getApplicationContext(), NAV_REQUEST_SETTINGS));
                break;
            case R.id.nav_faq:
                startActivity(ConfigActivity.newIntent(getApplicationContext(), NAV_REQUEST_FAQ));
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setupViewPager(ViewPager viewPager) {
        mHomeViewPagerAdapter = new HomeViewPagerAdapter(getSupportFragmentManager());
//        mHomeViewPagerAdapter.addFragment(FriendListFragment.newInstance(), getString(R.string.title_friends));
//        adapter.addFragment(AltarUpdateFragment.newInstance(), getString(R.string.title_altar));
//        mHomeViewPagerAdapter.addFragment(AltarCreateFragment.newInstance(), getString(R.string.title_altar));
//        mHomeViewPagerAdapter.addFragment(ObituaryFragment.newInstance(), getString(R.string.title_obituary));
        ArrayList<Fragment> fragments = new ArrayList<>();
        ArrayList<String> fragmentTitles = new ArrayList<>();
        fragments.add(FriendReadyFragment.newInstance());
        fragments.add(AltarCreateFragment.newInstance());
        fragments.add(ObituaryFragment.newInstance());
        fragmentTitles.add(getString(R.string.title_friends));
        fragmentTitles.add(getString(R.string.title_altar));
        fragmentTitles.add(getString(R.string.title_obituary));
        mHomeViewPagerAdapter.setFragments(fragments, fragmentTitles);

        viewPager.setOffscreenPageLimit(mHomeViewPagerAdapter.getCount() - 1);
        viewPager.setAdapter(mHomeViewPagerAdapter);
    }

    private class HomeViewPagerAdapter extends FragmentPagerAdapter {
        private ArrayList<Fragment> mFragmentList = new ArrayList<>();
        private ArrayList<String> mFragmentTitleList = new ArrayList<>();
        private FragmentManager mFragmentManager;

        public HomeViewPagerAdapter(FragmentManager manager) {
            super(manager);
            mFragmentManager = manager;
        }

        @Override
        public Fragment getItem(int position) {
            if (position >= mFragmentList.size()) {
                return null;
            }
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

        public void setFragments(ArrayList<Fragment> fragments, ArrayList<String> fragmentTitles) {
//            mFragmentList.clear();
//            mFragmentTitleList.clear();
//            mFragmentList.addAll(fragments);
//            mFragmentTitleList.addAll(fragmentTitles);
            mFragmentList = null;
            mFragmentTitleList = null;
            mFragmentList = fragments;
            mFragmentTitleList = fragmentTitles;
            notifyDataSetChanged();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    private void setupTabLayoutIcons() {
        TextView tabOne = (TextView) LayoutInflater.from(this).inflate(R.layout.tab_item_text_view, null);
        tabOne.setText(getString(R.string.title_friends));
        tabOne.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_tab_friend, 0, 0);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case ALTAR_CREATE_PREVIEW_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    Log.i(TAG, "onActivityResult: requestCode" + requestCode);
                    Log.i(TAG, "onActivityResult: resultCode" + resultCode);
                    User user = (User) data.getSerializableExtra(ALTAR_PREVIEW_RESULT);

                    ArrayList<Fragment> fragments = new ArrayList<>();
                    ArrayList<String> fragmentTitles = new ArrayList<>();
                    fragments.add(FriendListFragment.newInstance());
                    fragments.add(AltarReadFragment.newInstance(user));
                    fragments.add(ObituaryFragment.newInstance());
                    fragmentTitles.add(getString(R.string.title_friends));
                    fragmentTitles.add(getString(R.string.title_altar));
                    fragmentTitles.add(getString(R.string.title_obituary));
                    mHomeViewPagerAdapter.setFragments(fragments, fragmentTitles);
                    setupTabLayoutIcons();
//                    mHomeViewPagerAdapter.mFragmentList.set(1, altarReadFragment);
//                    mHomeViewPagerAdapter.mFragmentManager.beginTransaction()
//                            .remove(mHomeViewPagerAdapter.mFragmentList.get(1))
//                            .add(altarReadFragment, "altar_read")
//                            .commit();
//                    mViewPager.invalidate();
                }
                break;
        }
    }

    @Override
    public void onPermissionCallback(int requestCode, int requestPermissionId, boolean isGranted) {
        switch (requestCode) {
            case ALTAR_CREATE_STORAGE_PERMISSION_REQUEST:
            case ALTAR_CREATE_CONTACT_PERMISSION_REQUEST:
                ((AltarCreateFragment) mHomeViewPagerAdapter.getItem(1))
                        .onPermissionCallback(requestCode, requestPermissionId, isGranted);
                break;
            case ALTAR_PRIVATE_LAST_WILL_CONTACT_PERMISSION_REQUEST:
                ((AltarCreateFragment) mHomeViewPagerAdapter.getItem(1))
                        .onPermissionCallback(requestCode, requestPermissionId, isGranted);
                break;
            case OBITUARY_SUBMIT_STORAGE_PERMISSION_REQUEST:
                ((ObituaryFragment) mHomeViewPagerAdapter.getItem(2))
                        .onPermissionCallback(requestCode, requestPermissionId, isGranted);
                break;
        }  
    }

    @Override
    public void onAltarContactDialogDismissed(String contactName) {
        ((AltarCreateFragment) mHomeViewPagerAdapter.getItem(1))
                .onAltarContactDialogDismissed(contactName);
    }

    @Override
    public void onAltarPublicLastWillDialogDismissed(String publicLastWillMessage) {
        ((AltarCreateFragment) mHomeViewPagerAdapter.getItem(1))
                .onAltarPublicLastWillDialogDismissed(publicLastWillMessage);
    }

    @Override
    public void onAltarPrivateLastWillDialogDismissed(LastWill lastWill) {
        ((AltarCreateFragment) mHomeViewPagerAdapter.getItem(1))
                .onAltarPrivateLastWillDialogDismissed(lastWill);
    }
}
