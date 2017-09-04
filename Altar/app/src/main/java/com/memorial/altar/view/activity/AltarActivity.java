package com.memorial.altar.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.TextView;
import android.widget.Toast;

import com.memorial.altar.R;
import com.memorial.altar.view.fragment.AltarReadFragment;

/**
 * Created by yoon on 2017. 9. 2..
 */

public class AltarActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = AltarActivity.class.getSimpleName();

    private static final String EXTRA_FRIEND_ID = "com.memorial.altar.friend_id";

    public static Intent newIntent(Context packageContext, int friendId) {
        Intent intent = new Intent(packageContext, AltarActivity.class);
        intent.putExtra(EXTRA_FRIEND_ID, friendId);
        return intent;
    }

    private int mFriendId;
    private Toolbar mAltarToolbar;
    private Fragment mAltarFragment;
    private FragmentManager mAltarFragmentManager;
    private TextView mAltarCommentButtonTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFriendId = getIntent().getIntExtra(EXTRA_FRIEND_ID, -1);
        setContentView(R.layout.activity_altar);
        mAltarFragmentManager = getSupportFragmentManager();

        mAltarToolbar = (Toolbar) findViewById(R.id.altar_toolbar);
        setSupportActionBar(mAltarToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        requestFriend(mFriendId);
        mAltarCommentButtonTextView = (TextView) findViewById(R.id.altar_comment_button_text_view);
        mAltarCommentButtonTextView.setOnClickListener(this);
    }

    private void requestFriend(int friendId) {
        setTitle(getString(R.string.friend_altar));
        mAltarFragment = AltarReadFragment.newInstance(friendId);
        mAltarFragmentManager.beginTransaction()
                .add(R.id.altar_container, mAltarFragment)
                .commit();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.altar_comment_button_text_view:
                Toast.makeText(getApplicationContext(), "show altar comment", Toast.LENGTH_SHORT).show();
                Fragment fragment = mAltarFragmentManager.findFragmentById(R.id.altar_container);
                ((AltarReadFragment)fragment).updateComment();
                hideCommentButtonTextView();


                break;
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
//        return super.onSupportNavigateUp();
        onBackPressed();
        return true;
    }

    private void hideCommentButtonTextView() {
        mAltarCommentButtonTextView.animate().translationY(mAltarCommentButtonTextView.getHeight())
                .setInterpolator(new AccelerateInterpolator(2));
    }
}
