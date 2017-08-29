package com.memorial.altar.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.memorial.altar.R;

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mConfigCode = getIntent().getIntExtra(EXTRA_CONFIG_CODE, -1);
        setContentView(R.layout.activity_config);

        mConfigToolbar = (Toolbar) findViewById(R.id.config_toolbar);
        setSupportActionBar(mConfigToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        handleConfigCode(mConfigCode);
    }

    private void handleConfigCode(int configCode) {
        switch (configCode) {
            case 0:
                setTitle("notifications");
                break;
            case 1:

                break;
            case 2:
                setTitle("settings");
                break;
            case 3:
                setTitle("obituary");
                break;
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
//        return super.onSupportNavigateUp();
        onBackPressed();
        return true;
    }
}
