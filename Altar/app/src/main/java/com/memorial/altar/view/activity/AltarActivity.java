package com.memorial.altar.view.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

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

    private InputMethodManager mInputMethodManager;

    private int mFriendId;
    private Toolbar mAltarToolbar;
    private Fragment mAltarFragment;
    private FragmentManager mAltarFragmentManager;
    private TextView mAltarCommentReadButtonTextView;
    private TextView mAltarCommentCreateButtonTextView;
    private EditText mAltarCommentEditText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mInputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mFriendId = getIntent().getIntExtra(EXTRA_FRIEND_ID, -1);
        setContentView(R.layout.activity_altar);
        mAltarFragmentManager = getSupportFragmentManager();

        mAltarToolbar = (Toolbar) findViewById(R.id.altar_toolbar);
        setSupportActionBar(mAltarToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        requestFriend(mFriendId);
        mAltarCommentReadButtonTextView = (TextView) findViewById(R.id.altar_comment_read_button_text_view);
        mAltarCommentReadButtonTextView.setOnClickListener(this);
        mAltarCommentCreateButtonTextView = (TextView) findViewById(R.id.altar_comment_create_button_text_view);
        mAltarCommentCreateButtonTextView.setOnClickListener(this);
        mAltarCommentEditText = (EditText) findViewById(R.id.altar_comment_edit_text);
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
            case R.id.altar_comment_read_button_text_view:
//                Toast.makeText(getApplicationContext(), "show altar comment", Toast.LENGTH_SHORT).show();
                Fragment fragment = mAltarFragmentManager.findFragmentById(R.id.altar_container);
                ((AltarReadFragment) fragment).updateComment();
                hideCommentButtonTextView();
                break;
            case R.id.altar_comment_create_button_text_view:
                createCommentShowDialog();
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
        mAltarCommentReadButtonTextView.animate().translationY(mAltarCommentReadButtonTextView.getHeight())
                .setInterpolator(new AccelerateInterpolator(2));
        mAltarCommentReadButtonTextView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mAltarCommentReadButtonTextView.setVisibility(View.GONE);
            }
        }, 250);
    }

    private void createCommentShowDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(R.string.create_comment_dialog_message);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                confirmCommentShowDialog();
            }
        });
        builder.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void confirmCommentShowDialog() {
        mInputMethodManager.hideSoftInputFromWindow(mAltarCommentEditText.getWindowToken(), 0);
        mAltarCommentEditText.getText().clear();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(R.string.confirm_comment_dialog_message);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Fragment fragment = mAltarFragmentManager.findFragmentById(R.id.altar_container);
                ((AltarReadFragment)fragment).updateComment();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
