package com.memorial.altar.view.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.memorial.altar.R;

/**
 * Created by yoon on 2017. 9. 4..
 */

public class AltarCreateFragment extends Fragment
        implements View.OnClickListener, View.OnFocusChangeListener {

    private static final String TAG = AltarCreateFragment.class.getSimpleName();

    public static AltarCreateFragment newInstance() {

        Bundle args = new Bundle();

        AltarCreateFragment fragment = new AltarCreateFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private AlertDialog mDialog;
    private ImageView mAltarCreateUserImageView;
    private TextInputLayout mBirthTextInputLayout;
    private EditText mBirthEditText;
    private boolean mIsBirthValidateInfoConfirmed;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_altar_create, container, false);
        mAltarCreateUserImageView = view.findViewById(R.id.altar_create_user_image_view);
        mAltarCreateUserImageView.setOnClickListener(this);
        mBirthEditText = view.findViewById(R.id.altar_create_user_birth_edit_text);
        mBirthEditText.setKeyListener(null);
        mBirthEditText.setOnFocusChangeListener(this);
        mBirthEditText.setOnClickListener(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.altar_create_user_image_view:
                createUserImageShowDialog();
                break;

            // read storage run time permission / image handler
            case R.id.create_image_take_a_picture:
                if (mDialog.isShowing()) {
                    mDialog.dismiss();
                }

                break;
            case R.id.create_image_select_photo_in_album:
                if (mDialog.isShowing()) {
                    mDialog.dismiss();
                }

                break;
            case R.id.altar_create_user_birth_edit_text:
                createUserBirthInputShowDialog();
                break;
        }
    }

    @Override
    public void onFocusChange(View view, boolean b) {
        switch (view.getId()) {
            case R.id.altar_create_user_birth_edit_text:
                if (b && !mIsBirthValidateInfoConfirmed) {
                    validateUserBirthInfoShowDialog();
                }
                break;
        }
    }

    private void createUserImageShowDialog() {
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        View view = layoutInflater.inflate(R.layout.dialog_create_user_image, null);

        TextView cameraTextView = view.findViewById(R.id.create_image_take_a_picture);
        cameraTextView.setOnClickListener(this);
        TextView galleryTextView = view.findViewById(R.id.create_image_select_photo_in_album);
        galleryTextView.setOnClickListener(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setView(view);
        mDialog = builder.create();
        mDialog.show();
    }

    private void validateUserBirthInfoShowDialog() {
//        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
//        View view = layoutInflater.inflate(R.layout.dialog_create_user_image, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

//        builder.setView(view);
        builder.setTitle(getString(R.string.dialog_title_information));
        builder.setIcon(R.drawable.ic_error_outline_grey_300_24dp);
        builder.setMessage(getString(R.string.dialog_create_birth_validate_info_message));
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                createUserBirthInputShowDialog();
                mIsBirthValidateInfoConfirmed = true;
            }
        });
        mDialog = builder.create();
        mDialog.show();
    }

    private void createUserBirthInputShowDialog() {
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        View view = layoutInflater.inflate(R.layout.dialog_create_user_birth_input, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);

        mDialog = builder.create();
        mDialog.show();
    }

}
