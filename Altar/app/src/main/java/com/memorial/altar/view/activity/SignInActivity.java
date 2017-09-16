package com.memorial.altar.view.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.memorial.altar.R;

/**
 * Created by yoon on 2017. 9. 16..
 */

public class SignInActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = SignInActivity.class.getSimpleName();

    public static Intent newIntent(Context packageContext) {
        Intent intent = new Intent(packageContext, SignInActivity.class);
        return intent;
    }

    private EditText mEmailEditText;
    private EditText mPasswordEditText;
    private TextView mForgetUserInfoTextView;
    private TextView mSignInButtonTextView;
    private TextView mSignUpButtonTextView;
    private AlertDialog mDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        mEmailEditText = (EditText) findViewById(R.id.sign_in_email_edit_text);
        mPasswordEditText = (EditText) findViewById(R.id.sign_in_password_edit_text);
        mForgetUserInfoTextView = (TextView) findViewById(R.id.forget_user_info_text_view);
        mForgetUserInfoTextView.setOnClickListener(this);
        mSignInButtonTextView = (TextView) findViewById(R.id.sign_in_button_text_view);
        mSignInButtonTextView.setOnClickListener(this);
        mSignUpButtonTextView = (TextView) findViewById(R.id.sign_up_button_text_view);
        mSignUpButtonTextView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.forget_user_info_text_view:
                forgetUserInfoShowDialog();
                break;
            case R.id.find_forgotten_email_text_view:
                if (mDialog.isShowing()) {
                    mDialog.dismiss();
                    findEmailShowDialog();
                }
                break;
            case R.id.find_forgotten_password_text_view:
                if (mDialog.isShowing()) {
                    mDialog.dismiss();
                    findPasswordShowDialog();
                }
                break;

            case R.id.sign_in_button_text_view:
                break;
            case R.id.sign_up_button_text_view:
                startActivity(SignUpActivity.newIntent(getApplicationContext()));
                break;
        }
    }

    private void forgetUserInfoShowDialog() {
        LayoutInflater layoutInflater = LayoutInflater.from(SignInActivity.this);
        View view = layoutInflater.inflate(R.layout.dialog_forget_user_info, null);

        TextView findForgottenEmailTextView = view.findViewById(R.id.find_forgotten_email_text_view);
        findForgottenEmailTextView.setOnClickListener(this);
        TextView findForgottenPasswordTextView = view.findViewById(R.id.find_forgotten_password_text_view);
        findForgottenPasswordTextView.setOnClickListener(this);

        AlertDialog.Builder builder = new AlertDialog.Builder(SignInActivity.this);
        builder.setView(view);
        builder.setTitle(R.string.dialog_title_forget_user_info);
        mDialog = builder.create();
        mDialog.show();
    }

    private void findEmailShowDialog() {
        LayoutInflater layoutInflater = LayoutInflater.from(SignInActivity.this);
        View view = layoutInflater.inflate(R.layout.dialog_find_email, null);

        TextInputEditText findEmailTextInputEditText = view.findViewById(R.id.find_email_text_input_edit_text);

        AlertDialog.Builder builder = new AlertDialog.Builder(SignInActivity.this);
        builder.setTitle(R.string.dialog_title_find_email);
        builder.setMessage(R.string.dialog_message_find_email);
        builder.setView(view);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.setNegativeButton(android.R.string.no, null);
        mDialog = builder.create();
        mDialog.show();
    }

    private void findPasswordShowDialog() {
        LayoutInflater layoutInflater = LayoutInflater.from(SignInActivity.this);
        View view = layoutInflater.inflate(R.layout.dialog_find_password, null);

        TextInputEditText findPasswordTextInputEditText = view.findViewById(R.id.find_password_text_input_edit_text);

        AlertDialog.Builder builder = new AlertDialog.Builder(SignInActivity.this);
        builder.setTitle(R.string.dialog_title_find_password);
        builder.setMessage(R.string.dialog_message_find_password);
        builder.setView(view);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.setNegativeButton(android.R.string.no, null);
        mDialog = builder.create();
        mDialog.show();
    }
}
