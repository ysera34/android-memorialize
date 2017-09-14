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
import com.memorial.altar.model.User;
import com.memorial.altar.view.fragment.AltarPreviewFragment;
import com.memorial.altar.view.fragment.AltarReadFragment;

import static com.memorial.altar.common.Common.ALTAR_PREVIEW_RESULT;

/**
 * Created by yoon on 2017. 9. 2..
 */

public class AltarActivity extends AppCompatActivity {

    private static final String TAG = AltarActivity.class.getSimpleName();

    private static final String EXTRA_FRIEND_ID = "com.memorial.altar.friend_id";
    private static final String EXTRA_ALTAR_USER = "com.memorial.altar.user";

    public static Intent newIntent(Context packageContext, int friendId) {
        Intent intent = new Intent(packageContext, AltarActivity.class);
        intent.putExtra(EXTRA_FRIEND_ID, friendId);
        return intent;
    }

    public static Intent newIntent(Context packageContext, User user) {
        Intent intent = new Intent(packageContext, AltarActivity.class);
        intent.putExtra(EXTRA_ALTAR_USER, user);
        return intent;
    }

    private int mFriendId;
    private User mUser;
    private Toolbar mAltarToolbar;
    private Fragment mAltarFragment;
    private FragmentManager mAltarFragmentManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFriendId = getIntent().getIntExtra(EXTRA_FRIEND_ID, -1);
        mUser = (User) getIntent().getSerializableExtra(EXTRA_ALTAR_USER);
        setContentView(R.layout.activity_altar);
        mAltarFragmentManager = getSupportFragmentManager();
        mAltarToolbar = (Toolbar) findViewById(R.id.altar_toolbar);
        setSupportActionBar(mAltarToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        requestFriend();

    }

    private void requestFriend() {

        if (mUser == null) {
            setTitle(getString(R.string.friend_altar));
            mAltarFragment = AltarReadFragment.newInstance(mFriendId);
            mAltarFragmentManager.beginTransaction()
                    .add(R.id.altar_container, mAltarFragment)
                    .commit();
        } else {
            setTitle(getString(R.string.altar_create_preview));
            mAltarFragment = AltarPreviewFragment.newInstance(mUser);
            mAltarFragmentManager.beginTransaction()
                    .add(R.id.altar_container, mAltarFragment)
                    .commit();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
//        return super.onSupportNavigateUp();
        onBackPressed();
        return true;
    }

    public void previewConfirm() {
        Intent intent = new Intent();
        intent.putExtra(ALTAR_PREVIEW_RESULT, mUser);
        setResult(RESULT_OK, intent);
        finish();
    }

}
