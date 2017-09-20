package com.memorial.altar.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.memorial.altar.R;

/**
 * Created by yoon on 2017. 9. 16..
 */

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = SignUpActivity.class.getSimpleName();

    public static Intent newIntent(Context packageContext) {
        Intent intent = new Intent(packageContext, SignUpActivity.class);
        return intent;
    }

    private EditText mPhoneNumberEditText;
    private EditText mEmailEditText;
    private EditText mPasswordEditText;
    private EditText mConfirmPasswordEditText;
    private TextView mSignUpButtonTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mPhoneNumberEditText = (EditText) findViewById(R.id.sign_up_phone_number_edit_text);
        mEmailEditText = (EditText) findViewById(R.id.sign_up_email_edit_text);
        mPasswordEditText = (EditText) findViewById(R.id.sign_up_password_edit_text);
        mConfirmPasswordEditText = (EditText) findViewById(R.id.sign_up_confirm_password_edit_text);
        mSignUpButtonTextView = (TextView) findViewById(R.id.sign_up_button_text_view);
        mSignUpButtonTextView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sign_up_button_text_view:
                startActivity(HomeReadyActivity.newIntent(getApplicationContext()));
                finish();
                break;
        }
    }
}
