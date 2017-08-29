package com.memorial.altar.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.memorial.altar.R;
import com.memorial.altar.view.fragment.ConfigFAQFragment;
import com.memorial.altar.view.fragment.ConfigNotificationsFragment;
import com.memorial.altar.view.fragment.ConfigSettingsFragment;

import static com.memorial.altar.common.Common.NAV_REQUEST_FAQ;
import static com.memorial.altar.common.Common.NAV_REQUEST_NOTIFICATIONS;
import static com.memorial.altar.common.Common.NAV_REQUEST_SETTINGS;

/**
 * Created by yoon on 2017. 8. 28..
 */

public class ConfigActivity extends AppCompatActivity {

    private static final String TAG = ConfigActivity.class.getSimpleName();

    private static final String EXTRA_CONFIG_CODE = "com.memorial.altar.config_code";

    public static Intent newIntent(Context packageContext, int configCode) {
        Intent intent = new Intent(packageContext, ConfigActivity.class);
        intent.putExtra(EXTRA_CONFIG_CODE, configCode);
        return intent;
    }

    private int mConfigCode;
    private Toolbar mConfigToolbar;
    private Fragment mConfigFragment;
    private FragmentManager mConfigFragmentManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mConfigCode = getIntent().getIntExtra(EXTRA_CONFIG_CODE, -1);
        setContentView(R.layout.activity_config);
        mConfigFragmentManager = getSupportFragmentManager();

        mConfigToolbar = (Toolbar) findViewById(R.id.config_toolbar);
        setSupportActionBar(mConfigToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        handleConfigCode(mConfigCode);

    }

    private void handleConfigCode(int configCode) {
        switch (configCode) {
            case NAV_REQUEST_NOTIFICATIONS:
                setTitle("notifications");
                mConfigFragment = ConfigNotificationsFragment.newInstance();
                break;
            case NAV_REQUEST_SETTINGS:
                setTitle("settings");
                mConfigFragment = ConfigSettingsFragment.newInstance();
                break;
            case NAV_REQUEST_FAQ:
                setTitle("faq");
                mConfigFragment = ConfigFAQFragment.newInstance();
                break;
        }
        mConfigFragmentManager.beginTransaction()
                .add(R.id.config_container, mConfigFragment)
                .commit();
    }

    @Override
    public boolean onSupportNavigateUp() {
//        return super.onSupportNavigateUp();
        onBackPressed();
        return true;
    }
}
