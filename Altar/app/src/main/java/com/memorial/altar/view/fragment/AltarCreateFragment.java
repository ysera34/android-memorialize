package com.memorial.altar.view.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bignerdranch.expandablerecyclerview.ChildViewHolder;
import com.bignerdranch.expandablerecyclerview.ExpandableRecyclerAdapter;
import com.bignerdranch.expandablerecyclerview.ParentViewHolder;
import com.memorial.altar.R;
import com.memorial.altar.model.GroupChild;
import com.memorial.altar.model.GroupParent;
import com.memorial.altar.util.ImageHandler;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

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
    private ImageView mUserImageView;
    private TextView mNameTextView;
    private TextView mBirthTextView;
    private RecyclerView mGroupRecyclerView;
    private GroupAdapter mGroupAdapter;
    private ArrayList<GroupParent> mGroupParents;
    private TextView mContactFriendButtonTextView;
    private TextView mLastWillTextView;

    private boolean mIsBirthValidateInfoConfirmed;
    private boolean mIsBirthNumberValidated;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mImageHandler = new ImageHandler(getActivity());
        mGroupParents = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_altar_create, container, false);
        mUserImageView = view.findViewById(R.id.altar_create_user_image_view);
        mUserImageView.setOnClickListener(this);
        mNameTextView = view.findViewById(R.id.altar_create_user_name_text_view);
        mNameTextView.setOnClickListener(this);
        mBirthTextView = view.findViewById(R.id.altar_create_user_birth_text_view);
        mBirthTextView.setOnClickListener(this);
        mGroupRecyclerView = view.findViewById(R.id.altar_create_user_group_recycler_view);
        mGroupRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mGroupRecyclerView.setNestedScrollingEnabled(false);
        mGroupAdapter = new GroupAdapter(mGroupParents);
        mGroupRecyclerView.setAdapter(mGroupAdapter);
        mContactFriendButtonTextView = view.findViewById(R.id.contact_friend_button_text_view);
        mContactFriendButtonTextView.setOnClickListener(this);
        mLastWillTextView = view.findViewById(R.id.altar_create_user_last_will_text_view);
        mLastWillTextView.setOnClickListener(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mGroupParents = getGroupParents();
        mGroupAdapter.setParentList(mGroupParents, true);
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
            case R.id.altar_create_user_last_will_text_view:
                createLastWillTypeButtonShowDialog();
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CAMERA_CAPTURE_IMAGE_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    mImageHandler.handleCameraImage(mUserImageView);
                }
                break;
            case GALLERY_IMAGE_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    mImageHandler.handleGalleryImage(data, mUserImageView);
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

    private void createSchoolInputShowDialog() {
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        View view = layoutInflater.inflate(R.layout.dialog_create_user_school, null);

        final TextInputEditText userSchoolEditText = view.findViewById(R.id.create_user_school_edit_text);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.altar_create_user_school));
        builder.setView(view);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (userSchoolEditText.getText().toString().length() > 0) {
                    GroupChild groupChild = new GroupChild();
                    groupChild.setName(userSchoolEditText.getText().toString());
                    mGroupParents.get(0).getChildList().add(groupChild);
                    mGroupAdapter.setParentList(mGroupParents, true);
                }
            }
        });
        builder.setNegativeButton(android.R.string.no, null);

        mDialog = builder.create();
        mDialog.show();
    }

    private void createCompanyInputShowDialog() {
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        View view = layoutInflater.inflate(R.layout.dialog_create_user_company, null);

        final TextInputEditText userCompanyEditText = view.findViewById(R.id.create_user_company_edit_text);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.altar_create_user_company));
        builder.setView(view);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (userCompanyEditText.getText().toString().length() > 0) {
                    GroupChild groupChild = new GroupChild();
                    groupChild.setName(userCompanyEditText.getText().toString());
                    mGroupParents.get(1).getChildList().add(groupChild);
                    mGroupAdapter.setParentList(mGroupParents, true);
                }
            }
        });
        builder.setNegativeButton(android.R.string.no, null);

        mDialog = builder.create();
        mDialog.show();
    }

    private void createSocietyInputShowDialog() {
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        View view = layoutInflater.inflate(R.layout.dialog_create_user_society, null);

        final TextInputEditText userSocialEditText = view.findViewById(R.id.create_user_society_edit_text);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.altar_create_user_society));
        builder.setView(view);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (userSocialEditText.getText().toString().length() > 0) {
                    GroupChild groupChild = new GroupChild();
                    groupChild.setName(userSocialEditText.getText().toString());
                    mGroupParents.get(2).getChildList().add(groupChild);
                    mGroupAdapter.setParentList(mGroupParents, true);
                }
            }
        });
        builder.setNegativeButton(android.R.string.no, null);

        mDialog = builder.create();
        mDialog.show();
    }

    private void updateChildNameShowDialog(final GroupChild groupChild) {
        int parentIndex = -1;
        if (findParent(groupChild) != null) {
            parentIndex = mGroupParents.indexOf(findParent(groupChild));
        }
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        View view;
        final TextInputEditText textInputEditText;
        String title;
        switch (parentIndex) {
            case 0:
                view = layoutInflater.inflate(R.layout.dialog_create_user_school, null);
                textInputEditText = view.findViewById(R.id.create_user_school_edit_text);
                title = getString(R.string.altar_create_user_school);
                break;
            case 1:
                view = layoutInflater.inflate(R.layout.dialog_create_user_company, null);
                textInputEditText = view.findViewById(R.id.create_user_company_edit_text);
                title = getString(R.string.altar_create_user_company);
                break;
            case 2:
                view = layoutInflater.inflate(R.layout.dialog_create_user_society, null);
                textInputEditText = view.findViewById(R.id.create_user_society_edit_text);
                title = getString(R.string.altar_create_user_society);
                break;
            default:
                return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title);
        builder.setView(view);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (textInputEditText.getText().toString().length() > 0) {
                    groupChild.setName(textInputEditText.getText().toString());
                    mGroupAdapter.setParentList(mGroupParents, true);
                }
            }
        });
        builder.setNegativeButton(android.R.string.no, null);
        AlertDialog dialog = builder.create();
        dialog.show();
        textInputEditText.postDelayed(new Runnable() {
            @Override
            public void run() {
                textInputEditText.setText(String.valueOf(groupChild.getName()));
                textInputEditText.requestFocusFromTouch();
                InputMethodManager lManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                lManager.showSoftInput(textInputEditText, 0);
                textInputEditText.setSelection(groupChild.getName().length());
            }
        }, 300);
    }

    private void removeChildNameShowDialog(final GroupChild groupChild) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("삭제하시겠습니까?");
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (findParent(groupChild) != null) {
                    findParent(groupChild).getChildList().remove(groupChild);
                    mGroupAdapter.setParentList(mGroupParents, true);
                }
            }
        });
        builder.setNegativeButton(android.R.string.no, null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private GroupParent findParent(GroupChild groupChild) {
        /*Loop :*/
        for (GroupParent parent : mGroupParents) {
            for (int i = 0; i < parent.getChildList().size(); i++) {
                if (parent.getChildList().contains(groupChild)) {
//                    break Loop;
                    return parent;
                }
            }
        }
        return null;
    }

    private void createLastWillTypeButtonShowDialog() {
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        View view = layoutInflater.inflate(R.layout.dialog_create_user_last_will, null);
        final RadioButton publicRadioButton = view.findViewById(R.id.last_will_public_radio_button);
        final RadioButton privateRadioButton = view.findViewById(R.id.last_will_private_radio_button);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.altar_create_user_last_will));
        builder.setView(view);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (publicRadioButton.isChecked()) {
                    BottomSheetDialogFragment altarPublicLastWillFragment =
                            AltarPublicLastWillFragment.newInstance(mLastWillTextView.getText().toString());
                    altarPublicLastWillFragment.show(getChildFragmentManager(), "altar_public_last_will");
//                    getChildFragmentManager().beginTransaction()
//                            .add(altarPublicLastWillFragment, "altar_public_last_will")
//                            .commitAllowingStateLoss();
                } else if (privateRadioButton.isChecked()) {

                }
            }
        });
        builder.setNegativeButton(android.R.string.no, null);

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
                    if (isGranted /*&& isResumed()*/) {
                        BottomSheetDialogFragment altarContactFragment =
                                AltarContactFragment.newInstance();
//                        altarContactFragment.show(getChildFragmentManager(), "altar_contact");
                        getChildFragmentManager().beginTransaction()
                                .add(altarContactFragment, "altar_contact")
                                .commitAllowingStateLoss();
                    }
                }
                break;
        }
    }

    public void onAltarContactDialogDismissed(String contactName) {
        String contactStr = getString(R.string.altar_user_contact_people) + " : " + contactName;
        mContactFriendButtonTextView.setText(contactStr);
    }

    public void onAltarPublicLastWillDialogDismissed(String publicLastWillMessage) {
        mLastWillTextView.setText(String.valueOf(publicLastWillMessage));
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

    private ArrayList<GroupParent> getGroupParents() {

        ArrayList<GroupParent> groupParents = new ArrayList<>();
        String[] groupNames = getResources().getStringArray(R.array.group_names_array);

        for (int i = 0; i < groupNames.length; i++) {
            GroupParent groupParent = new GroupParent();
            groupParent.setId(i);
            groupParent.setName(groupNames[i]);

//            if (i == 0) {
//                for (int j = 0; j < 3; j++) {
//                    GroupChild groupChild = new GroupChild();
//                    groupChild.setName("삼현초");
//                    groupParent.getChildList().add(groupChild);
//                }
//            }
            groupParents.add(groupParent);
        }
        return groupParents;
    }

    private class GroupAdapter
            extends ExpandableRecyclerAdapter<GroupParent, GroupChild, GroupParentViewHolder, GroupChildViewHolder> {

        private LayoutInflater mLayoutInflater;

        public GroupAdapter(@NonNull List<GroupParent> parentList) {
            super(parentList);
            mLayoutInflater = LayoutInflater.from(getActivity());
        }

        @NonNull
        @Override
        public GroupParentViewHolder onCreateParentViewHolder(@NonNull ViewGroup parentViewGroup, int viewType) {
            View view = mLayoutInflater.inflate(R.layout.list_item_group_parent, parentViewGroup, false);
            return new GroupParentViewHolder(view);
        }

        @NonNull
        @Override
        public GroupChildViewHolder onCreateChildViewHolder(@NonNull ViewGroup childViewGroup, int viewType) {
            View view = mLayoutInflater.inflate(R.layout.list_item_group_child, childViewGroup, false);
            return new GroupChildViewHolder(view);
        }

        @Override
        public void onBindParentViewHolder(@NonNull GroupParentViewHolder parentViewHolder, int parentPosition, @NonNull GroupParent parent) {
            parentViewHolder.bindGroupParent(parent);
        }

        @Override
        public void onBindChildViewHolder(@NonNull GroupChildViewHolder childViewHolder, int parentPosition, int childPosition, @NonNull GroupChild child) {
            childViewHolder.bindGroupChild(child);
        }
    }

    private class GroupParentViewHolder extends ParentViewHolder {

        private static final float INITIAL_POSITION = 0.0f;
        private static final float ROTATED_POSITION = 180.0f;

        private GroupParent mGroupParent;
        private StringBuilder mGroupChildNamesBuilder;

        private TextView mGroupNameTextView;
        private ImageView mAddImageView;
        private ImageView mArrowDownImageView;

        public GroupParentViewHolder(@NonNull View itemView) {
            super(itemView);
            mGroupChildNamesBuilder = new StringBuilder();
            mGroupNameTextView = itemView.findViewById(R.id.list_item_group_parent_name_text_view);
            mAddImageView = itemView.findViewById(R.id.list_item_group_parent_add_image_view);
            mAddImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    switch (mGroupParents.indexOf(mGroupParent)) {
                        case 0:
                            createSchoolInputShowDialog();
                            break;
                        case 1:
                            createCompanyInputShowDialog();
                            break;
                        case 2:
                            createSocietyInputShowDialog();
                            break;
                    }
                }
            });
            mArrowDownImageView = itemView.findViewById(R.id.list_item_group_parent_arrow_image_view);
            mArrowDownImageView.setVisibility(View.GONE);
        }

        public void bindGroupParent(GroupParent groupParent) {
            mGroupParent = groupParent;
            mGroupNameTextView.setHint(String.valueOf(mGroupParent.getName()));

            mGroupChildNamesBuilder.setLength(0);
            for (int i = 0; i < mGroupParent.getChildList().size(); i++) {
                if (i != 0) {
                    mGroupChildNamesBuilder.append(", ");
                }
                mGroupChildNamesBuilder.append(mGroupParent.getChildList().get(i).getName());
            }
            if (mGroupChildNamesBuilder.length() > 0 && !isExpanded()) {
                mGroupNameTextView.setText(mGroupChildNamesBuilder.toString());
            }
        }

        @Override
        public void setExpanded(boolean expanded) {
            super.setExpanded(expanded);
            if (expanded) {
                mArrowDownImageView.setRotation(ROTATED_POSITION);
                mGroupNameTextView.setText(null);
            } else {
                mArrowDownImageView.setRotation(INITIAL_POSITION);
                mGroupNameTextView.setText(mGroupChildNamesBuilder.toString());
            }
        }

        @Override
        public void onExpansionToggled(boolean expanded) {
            super.onExpansionToggled(expanded);
            RotateAnimation rotateAnimation;
            if (expanded) { // rotate clockwise
                rotateAnimation = new RotateAnimation(ROTATED_POSITION,
                        INITIAL_POSITION,
                        RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                        RotateAnimation.RELATIVE_TO_SELF, 0.5f);
            } else { // rotate counterclockwise
                rotateAnimation = new RotateAnimation(-1 * ROTATED_POSITION,
                        INITIAL_POSITION,
                        RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                        RotateAnimation.RELATIVE_TO_SELF, 0.5f);
            }

            rotateAnimation.setDuration(200);
            rotateAnimation.setFillAfter(true);
            mArrowDownImageView.startAnimation(rotateAnimation);
        }
    }

    private class GroupChildViewHolder extends ChildViewHolder
            implements View.OnClickListener, View.OnLongClickListener  {

        private GroupChild mGroupChild;

        private TextView mGroupNameTextView;

        public GroupChildViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            mGroupNameTextView = itemView.findViewById(R.id.list_item_group_child_name_text_view);

        }

        public void bindGroupChild(GroupChild groupChild) {
            mGroupChild = groupChild;
            mGroupNameTextView.setText(String.valueOf(mGroupChild.getName()));
        }

        @Override
        public void onClick(View view) {
            updateChildNameShowDialog(mGroupChild);
        }

        @Override
        public boolean onLongClick(View view) {
            removeChildNameShowDialog(mGroupChild);
            return true;
        }
    }
}
