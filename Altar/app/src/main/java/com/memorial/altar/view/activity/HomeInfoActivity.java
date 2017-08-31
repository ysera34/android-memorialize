package com.memorial.altar.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.memorial.altar.R;
import com.memorial.altar.view.fragment.HomeInfoFragment;

import java.util.ArrayList;

/**
 * Created by yoon on 2017. 8. 31..
 */

public class HomeInfoActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = HomeInfoActivity.class.getSimpleName();

    public static Intent newIntent(Context packageContext) {
        Intent intent = new Intent(packageContext, HomeInfoActivity.class);
        return intent;
    }

    private ViewPager mInfoViewPager;
    private InfoFragmentAdapter mInfoFragmentAdapter;
    private InfoViewPagerChangeListener mInfoViewPagerChangeListener;
    private LinearLayout mInfoIndicatorLayout;
    private TextView mSkipButtonTextView;
    private ArrayList<TextView> mInfoIndicatorTextViews;
    private ArrayList<Fragment> mInfoFragments;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_info);
        mInfoFragments = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            mInfoFragments.add(HomeInfoFragment.newInstance(i));
        }
        mInfoViewPager = (ViewPager) findViewById(R.id.home_info_view_pager);
        mInfoFragmentAdapter = new InfoFragmentAdapter(getSupportFragmentManager());
        mInfoViewPager.setAdapter(mInfoFragmentAdapter);
        mInfoViewPagerChangeListener = new InfoViewPagerChangeListener();
        mInfoIndicatorLayout = (LinearLayout) findViewById(R.id.home_info_indicator_layout);
        mInfoIndicatorTextViews = new ArrayList<>();
        setViewPagerIndicator(mInfoFragments.size());
        mSkipButtonTextView = (TextView) findViewById(R.id.home_info_skip_button_text_view);
        mSkipButtonTextView.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mInfoViewPager.addOnPageChangeListener(mInfoViewPagerChangeListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mInfoViewPager.removeOnPageChangeListener(mInfoViewPagerChangeListener);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.home_info_skip_button_text_view:
//                UserSharedPreferences.setStoredHomeIntroSlide(getApplicationContext(), true);
                finish();
                break;
        }
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }

    private class InfoFragmentAdapter extends FragmentStatePagerAdapter {

        public InfoFragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mInfoFragments.get(position);
        }

        @Override
        public int getCount() {
            return mInfoFragments.size();
        }
    }

    private class InfoViewPagerChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            setIndicatorBackground(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    protected void setViewPagerIndicator(int size) {
        for (int i = 0; i < size; i++) {
            TextView indicatorTextView = new TextView(getApplicationContext());
            specifyIndicatorSize(indicatorTextView);

            if (i != size - 1) {
                LinearLayout.LayoutParams params =
                        (LinearLayout.LayoutParams) indicatorTextView.getLayoutParams();
                params.setMarginEnd(30);
                indicatorTextView.setLayoutParams(params);
            }
            mInfoIndicatorLayout.addView(indicatorTextView);
            mInfoIndicatorTextViews.add(indicatorTextView);
        }
        setIndicatorBackground(0);
    }

    protected void setIndicatorBackground(int position) {
        for (int i = 0; i < mInfoIndicatorTextViews.size(); i++) {
            if (i == position) {
                mInfoIndicatorTextViews.get(i)
                        .setBackgroundResource(R.drawable.widget_home_info_selected_indicator_circle);
            } else {
                mInfoIndicatorTextViews.get(i)
                        .setBackgroundResource(R.drawable.widget_home_info_unselected_indicator_circle);
            }
        }
    }

    private void specifyIndicatorSize(TextView indicatorTextView) {
        indicatorTextView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        indicatorTextView.getLayoutParams().width =
                (int) getResources().getDimension(R.dimen.view_pager_indicator_width);
        indicatorTextView.getLayoutParams().height =
                (int) getResources().getDimension(R.dimen.view_pager_indicator_height);
    }
}
