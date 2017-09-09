package com.memorial.altar.view.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.memorial.altar.R;
import com.memorial.altar.util.ImageHandler;

import java.util.Calendar;

import static android.app.Activity.RESULT_OK;
import static com.memorial.altar.view.fragment.PermissionHeadlessFragment.CONTACT_PERMISSION_REQUEST;
import static com.memorial.altar.view.fragment.PermissionHeadlessFragment.STORAGE_PERMISSION_REQUEST;

/**
 * Created by yoon on 2017. 9. 4..
 */

public class AltarCreateFragment extends Fragment
        implements View.OnClickListener {

    private static final String TAG = AltarCreateFragment.class.getSimpleName();

    public static final int ALTAR_CREATE_STORAGE_PERMISSION_REQUEST = 1001;
    public static final int ALTAR_CREATE_CONTACT_PERMISSION_REQUEST = 1002;

    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 1100;
    private static final int GALLERY_IMAGE_REQUEST_CODE = 2100;

    public static AltarCreateFragment newInstance() {

        Bundle args = new Bundle();

        AltarCreateFragment fragment = new AltarCreateFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private ImageHandler mImageHandler;
    private AlertDialog mDialog;
    private ImageView mAltarCreateUserImageView;
    private TextView mNameTextView;
    private TextView mBirthTextView;
    private TextView mContactFriendButtonTextView;
    private boolean mIsBirthValidateInfoConfirmed;
    private boolean mIsBirthNumberValidated;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mImageHandler = new ImageHandler(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_altar_create, container, false);
        mAltarCreateUserImageView = view.findViewById(R.id.altar_create_user_image_view);
        mAltarCreateUserImageView.setOnClickListener(this);
        mNameTextView = view.findViewById(R.id.altar_create_user_name_text_view);
        mNameTextView.setOnClickListener(this);
        mBirthTextView = view.findViewById(R.id.altar_create_user_birth_text_view);
        mBirthTextView.setOnClickListener(this);
        mContactFriendButtonTextView = view.findViewById(R.id.contact_friend_button_text_view);
        mContactFriendButtonTextView.setOnClickListener(this);
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
                getActivity().getSupportFragmentManager().beginTransaction()
                        .add(PermissionHeadlessFragment.newInstance(
                                ALTAR_CREATE_STORAGE_PERMISSION_REQUEST,
                                STORAGE_PERMISSION_REQUEST),
                                PermissionHeadlessFragment.TAG)
                        .commit();
                break;
            case R.id.create_image_take_a_picture:
                if (mDialog.isShowing()) {
                    mDialog.dismiss();
                }
                if (mImageHandler.hasCamera()) {
                    startActivityForResult(mImageHandler.dispatchTakePictureIntent(), CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
                } else {
                    Toast.makeText(getActivity(), R.string.camera_not_available, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.create_image_select_photo_in_album:
                if (mDialog.isShowing()) {
                    mDialog.dismiss();
                }
                startActivityForResult(mImageHandler.pickGalleryPictureIntent(), GALLERY_IMAGE_REQUEST_CODE);
                break;
            case R.id.altar_create_user_name_text_view:
                createUserNameInputShowDialog();
                break;
            case R.id.altar_create_user_birth_text_view:
                validateUserBirthInfoShowDialog();
                break;
            case R.id.contact_friend_button_text_view:
                getActivity().getSupportFragmentManager().beginTransaction()
                        .add(PermissionHeadlessFragment.newInstance(
                                ALTAR_CREATE_CONTACT_PERMISSION_REQUEST,
                                CONTACT_PERMISSION_REQUEST),
                                PermissionHeadlessFragment.TAG)
                        .commit();
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CAMERA_CAPTURE_IMAGE_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    mImageHandler.handleCameraImage(mAltarCreateUserImageView);
                }
                break;
            case GALLERY_IMAGE_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    mImageHandler.handleGalleryImage(data, mAltarCreateUserImageView);
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

    private void createUserNameInputShowDialog() {
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        View view = layoutInflater.inflate(R.layout.dialog_create_user_name, null);

        final TextInputEditText userNameEditText = view.findViewById(R.id.create_user_name_edit_text);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.altar_user_name));
        builder.setView(view);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                checkInputTextAndSetText(userNameEditText, mNameTextView);
            }
        });
        builder.setNegativeButton(android.R.string.no, null);

        mDialog = builder.create();
        mDialog.show();
    }

    private void validateUserBirthInfoShowDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.dialog_title_information));
        builder.setIcon(R.drawable.ic_error_outline_grey_300_24dp);
        builder.setMessage(getString(R.string.dialog_create_birth_validate_info_message));
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                createUserBirthInputShowDialog();
            }
        });
        mDialog = builder.create();
        mDialog.show();
    }

    private boolean validationBirthFirstNumber(EditText editText) {
        String firstNumberStr = editText.getText().toString();
        if (firstNumberStr.length() != 6) {
            Log.i(TAG, "validationBirthFirstNumber: length false");
            return false;
        }
        int year = Integer.valueOf(firstNumberStr.substring(0, 2));
        int month = Integer.valueOf(firstNumberStr.substring(2, 4));
        int day = Integer.valueOf(firstNumberStr.substring(4));
        Calendar now = Calendar.getInstance();
        int nowYear = now.get(Calendar.YEAR);
        year = year + 1900;
        Log.i(TAG, "validationBirthFirstNumber: year: " + year);
        Log.i(TAG, "validationBirthFirstNumber: month: " + month);
        Log.i(TAG, "validationBirthFirstNumber: day: " + day);
        Log.i(TAG, "validationBirthFirstNumber: nowYear: " + nowYear);

        if (year < 1900 || year > nowYear) {
            Log.i(TAG, "validationBirthFirstNumber: nowYear");
            return false;
        }

        if (day < 1 || day > 31) {
//            ("Day must be between 1 and 31.");
            Log.i(TAG, "validationBirthFirstNumber: day");
            return false;
        }

        if (month < 1 || month > 12) {
//            ("Month must be between 1 and 12.");
            Log.i(TAG, "validationBirthFirstNumber: month");
            return false;
        }

        if ((month == 4 || month == 6 || month == 9 || month == 11) && day == 31) {
            Log.i(TAG, "validationBirthFirstNumber: month 46911");
//            ("Month "+month+" doesn't have 31 days!");
            return false;
        }

        if (month == 2) { // check for february 29th
            boolean isLeap = (year % 4 == 0 && (year % 100 != 0 || year % 400 == 0));
            if (day > 29 || (day == 29 && !isLeap)) {
//                ("February " + year + " doesn't have " + day + " days!");
                Log.i(TAG, "validationBirthFirstNumber: leap");
                return false;
            }
        }
        return true;
    }

    private boolean validationBirthSecondNumber(EditText editText) {
        String secondNumberStr = editText.getText().toString();
        if (secondNumberStr.length() != 7) {
            Log.i(TAG, "validationBirthSecondNumber: length false");
            return false;
        }
        int gender = Integer.valueOf(secondNumberStr.substring(0, 1));
        if (gender < 1 || gender > 4) {
            Log.i(TAG, "validationBirthSecondNumber:  false");
            return false;
        }
        Log.i(TAG, "validationBirthSecondNumber: true");
        return true;
    }

    private void createUserBirthInputShowDialog() {
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        View view = layoutInflater.inflate(R.layout.dialog_create_user_birth_input, null);
        final EditText firstNumberEditText = view.findViewById(R.id.first_half_number_input_edit_Text);
        final EditText secondNumberEditText = view.findViewById(R.id.second_half_number_input_edit_Text);
        final Button repetitionButton = view.findViewById(R.id.dialog_confirm_repetition_button);
        final TextView numberValidationTextView = view.findViewById(R.id.number_validation_text_view);
        repetitionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mIsBirthNumberValidated) {
                    if (checkInputTextAndSetText(firstNumberEditText, null)
                            && validationBirthFirstNumber(firstNumberEditText)
                            && checkInputTextAndSetText(secondNumberEditText, null)
                            && validationBirthSecondNumber(secondNumberEditText)) {
                        numberValidationTextView.setText(R.string.birth_number_validation_true);
                        numberValidationTextView.setTextColor(getResources().getColor(R.color.colorGray700));
                        repetitionButton.setText(android.R.string.ok);
                        mIsBirthNumberValidated = true;
                    } else {
                        numberValidationTextView.setText(R.string.birth_number_validation_false);
                        numberValidationTextView.setTextColor(getResources().getColor(R.color.colorAccent));
                    }
                } else {
                    checkInputTextAndSetText(firstNumberEditText, mBirthTextView);
                    if (mDialog.isShowing()) {
                        mDialog.dismiss();
                    }
                    mIsBirthNumberValidated = false;
                }
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);

        mDialog = builder.create();
        mDialog.show();
    }

    public void onPermissionCallback(int requestCode, int requestPermissionId, boolean isGranted) {
        switch (requestCode) {
            case ALTAR_CREATE_STORAGE_PERMISSION_REQUEST:
                if (requestPermissionId == STORAGE_PERMISSION_REQUEST) {
                    if (isGranted) {
                        createUserImageShowDialog();
                    }
                }
                break;
            case ALTAR_CREATE_CONTACT_PERMISSION_REQUEST:
                if (requestPermissionId == CONTACT_PERMISSION_REQUEST) {
                    if (isGranted) {
                        BottomSheetDialogFragment altarContactFragment =
                                AltarContactFragment.newInstance();
                        altarContactFragment.show(getChildFragmentManager(), "altar_contact");
                    }
                }
                break;
        }
    }

    public void onAltarContactDialogDismissed(String contactName) {
        String contactStr = getString(R.string.altar_user_contact_people) + " : " + contactName;
        mContactFriendButtonTextView.setText(contactStr);
    }

    private boolean checkInputTextAndSetText(EditText inputEditText, TextView setTargetTextView) {
        if (inputEditText.getText().toString().length() > 0) {
            if (setTargetTextView != null) {
                setTargetTextView.setText(inputEditText.getText().toString());
            }
            return true;
        } else {
            Toast.makeText(getActivity(), R.string.check_the_input_window, Toast.LENGTH_SHORT).show();
            return false;
        }
    }
}
