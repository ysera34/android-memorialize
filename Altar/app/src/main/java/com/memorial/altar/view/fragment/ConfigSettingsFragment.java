package com.memorial.altar.view.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.memorial.altar.R;

/**
 * Created by yoon on 2017. 8. 29..
 */

public class ConfigSettingsFragment extends Fragment implements View.OnClickListener {

    public static ConfigSettingsFragment newInstance() {

        Bundle args = new Bundle();

        ConfigSettingsFragment fragment = new ConfigSettingsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private TextView mSendEmailTextView;
    private LinearLayout mAuthEmailLayout;
    private TextView mChangePasswordTextView;
    private TextView mSignOutTextView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_config_settings, container, false);
        mSendEmailTextView = view.findViewById(R.id.config_settings_send_email_text_view);
        mSendEmailTextView.setOnClickListener(this);
        mAuthEmailLayout = view.findViewById(R.id.config_settings_auth_email_layout);
        mChangePasswordTextView = view.findViewById(R.id.config_settings_change_password_text_view);
        mChangePasswordTextView.setOnClickListener(this);
        mSignOutTextView = view.findViewById(R.id.config_settings_sign_out_text_view);
        mSignOutTextView.setOnClickListener(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.config_settings_send_email_text_view:
                sendEmailShowDialog();
                break;
            case R.id.config_settings_change_password_text_view:
                changePasswordShowDialog();
                break;
            case R.id.config_settings_sign_out_text_view:
                signOutShowDialog();
                break;
        }
    }

    private void sendEmailShowDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(getString(R.string.dialog_send_email_message));
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mSendEmailTextView.setVisibility(View.GONE);
                mAuthEmailLayout.setVisibility(View.VISIBLE);
            }
        });
//        builder.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//
//            }
//        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void changePasswordShowDialog() {
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        View view = layoutInflater.inflate(R.layout.dialog_change_password, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);
        builder.setTitle(getString(R.string.dialog_change_password_title));
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
//        builder.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//
//            }
//        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void signOutShowDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(getString(R.string.dialog_sign_out_message));
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

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
}
