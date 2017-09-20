package com.memorial.altar.view.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.memorial.altar.R;
import com.memorial.altar.model.User;
import com.memorial.altar.util.UserSharedPreferences;
import com.memorial.altar.view.activity.BillingStarActivity;

import java.util.ArrayList;

import static com.memorial.altar.common.Common.URL_HOST;

/**
 * Created by yoon on 2017. 9. 2..
 */

public class AltarReadFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = AltarReadFragment.class.getSimpleName();

    private static final String ARG_ALTAR_USER_ID = "altar_user_id";
    private static final String ARG_ALTAR_USER = "altar_user";

    public static AltarReadFragment newInstance(int altarUserId) {

        Bundle args = new Bundle();
        args.putInt(ARG_ALTAR_USER_ID, altarUserId);

        AltarReadFragment fragment = new AltarReadFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static AltarReadFragment newInstance(User user) {

        Bundle args = new Bundle();
        args.putSerializable(ARG_ALTAR_USER, user);

        AltarReadFragment fragment = new AltarReadFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private int mAltarUserId;
    private User mAltarUser;

    private NestedScrollView mAltarReadNestedScrollView;
    private ImageView mAltarUserImageView;
    private TextView mAltarReadContributionButtonTextView;
    private TextView mAltarReadStarTextView;
    private TextView mAltarUserNameTextView;
    private TextView mAltarUserBirthTextView;
    private TextView mAltarUserGenderTextView;
    private RecyclerView mAltarUserGroupNameRecyclerView;
    private UserGroupNameAdapter mUserGroupNameAdapter;
    private TextView mAltarUserPublicLastWillMessageTextView;

    private FragmentManager mCommentFragmentManager;

    private AlertDialog mDialog;
    private TextInputEditText mStarEditText;
    private InputMethodManager mInputMethodManager;
    private TextView mAltarCommentReadButtonTextView;
    private TextView mAltarCommentCreateButtonTextView;
    private EditText mAltarCommentEditText;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAltarUserId = getArguments().getInt(ARG_ALTAR_USER_ID);
        mAltarUser = (User) getArguments().getSerializable(ARG_ALTAR_USER);
        mInputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_altar_read, container, false);
        mAltarReadNestedScrollView = view.findViewById(R.id.altar_read_nested_scroll_view);
        mAltarUserImageView = view.findViewById(R.id.altar_read_user_image_view);
        mAltarReadContributionButtonTextView = view.findViewById(R.id.altar_read_contribution_button_text_view);
        mAltarReadContributionButtonTextView.setOnClickListener(this);
        mAltarReadStarTextView = view.findViewById(R.id.altar_read_star_text_view);
        mAltarUserNameTextView = view.findViewById(R.id.altar_user_name_text_view);
        mAltarUserBirthTextView = view.findViewById(R.id.altar_user_birth_text_view);
        mAltarUserGenderTextView = view.findViewById(R.id.altar_user_gender_text_view);
        mAltarUserGroupNameRecyclerView = view.findViewById(R.id.altar_user_group_name_recycler_view);
        mAltarUserGroupNameRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAltarUserGroupNameRecyclerView.setNestedScrollingEnabled(false);
        mAltarUserPublicLastWillMessageTextView = view.findViewById(R.id.altar_user_public_last_will_message_text_view);

        mAltarCommentReadButtonTextView = view.findViewById(R.id.altar_comment_read_button_text_view);
        mAltarCommentReadButtonTextView.setOnClickListener(this);
        mAltarCommentCreateButtonTextView = view.findViewById(R.id.altar_comment_create_button_text_view);
        mAltarCommentCreateButtonTextView.setOnClickListener(this);
        mAltarCommentEditText = view.findViewById(R.id.altar_comment_edit_text);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        requestAltarUser();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.altar_read_contribution_button_text_view:
                contributionShowDialog();
                break;
            case R.id.altar_comment_read_button_text_view:
//                Toast.makeText(getApplicationContext(), "show altar comment", Toast.LENGTH_SHORT).show();
//                Fragment fragment = mAltarFragmentManager.findFragmentById(R.id.altar_container);
//                ((AltarReadFragment) fragment).
                updateComment();
                hideCommentButtonTextView();
                break;
            case R.id.altar_comment_create_button_text_view:
                createCommentShowDialog();
                break;
            case R.id.contribution_star_button:
                if (mStarEditText.getText().length() > 0) {
                    int star = Integer.valueOf(mStarEditText.getText().toString());
                    if (UserSharedPreferences.getStoredStar(getActivity()) >= star) {
                        if (mDialog.isShowing()) {
                            mDialog.dismiss();
                        }
                        contributionConfirmShowDialog();
                        int star2 = Integer.valueOf(mAltarReadStarTextView.getText().toString());
                        mAltarReadStarTextView.setText(String.valueOf(star + star2));
                    } else {
                        Toast.makeText(getActivity(), R.string.not_enough_star_message, Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.billing_star_button:
                if (mDialog.isShowing()) {
                    mDialog.dismiss();
                }
                startActivity(BillingStarActivity.newIntent(getActivity()));
                break;
        }
    }

    private void requestAltarUser() {

        if (mAltarUser == null) {
            User user = new User();
            user.setId(mAltarUserId);
            user.setName("sample name");
            user.setGender("sample gender");
            user.setBirth("sample birth date");
            ArrayList<String> groupNames = new ArrayList<>();
            for (int i = 0; i < 10; i++) {
                String groupName = "sample group name" + i;
                groupNames.add(groupName);
            }
            user.setGroupNames(groupNames);
            user.setPublicLastWillMessage(
                    "sample last will message\nsample last will message last will message\n" +
                            "sample last will message\nsample last will message last will message\n" +
                            "sample last will message\nsample last will message last will message\n" +
                            "sample last will message\nsample last will message last will message");
            mAltarUser = user;
        } else {

//            ArrayList<String> groupNames = new ArrayList<>();
//            for (int i = 0; i < mAltarUser.getGroupParents().size(); i++) {
//                List<GroupChild> groupChildren = mAltarUser.getGroupParents().get(i).getChildList();
//                for (int j = 0; j < groupChildren.size(); j++) {
//                    groupNames.add(groupChildren.get(j).getName());
//                }
//            }
//            mAltarUser.setGroupNames(groupNames);
        }
        mUserGroupNameAdapter = new UserGroupNameAdapter(mAltarUser.getGroupNames());
        mAltarUserGroupNameRecyclerView.setAdapter(mUserGroupNameAdapter);
        updateUI();
    }

    private void updateUI() {
        if (mUserGroupNameAdapter != null) {
            mUserGroupNameAdapter.notifyDataSetChanged();
        }
        Glide.with(getActivity()).load(URL_HOST + mAltarUser.getImagePath()).into(mAltarUserImageView);
        mAltarUserNameTextView.setText(String.valueOf(mAltarUser.getName()));
        mAltarUserBirthTextView.setText(String.valueOf(mAltarUser.getBirth()));
        mAltarUserGenderTextView.setText(String.valueOf(mAltarUser.getGender()));
        mAltarUserPublicLastWillMessageTextView.setText(String.valueOf(mAltarUser.getPublicLastWillMessage()));
    }

    public void updateComment() {
        if (mCommentFragmentManager == null) {
            mCommentFragmentManager = getChildFragmentManager();
            mCommentFragmentManager.beginTransaction()
                    .add(R.id.altar_comment_container, AltarCommentFragment.newInstance())
                    .commit();
            mAltarReadNestedScrollView.postDelayed(new Runnable() {
                @Override
                public void run() {
//                    mAltarReadNestedScrollView.smoothScrollTo(
//                            0, (int) mAltarUserPublicLastWillMessageTextView.getY());
                    mAltarReadNestedScrollView.fullScroll(View.FOCUS_UP);
                }
            }, 250);
        } else {
            Fragment fragment = mCommentFragmentManager.findFragmentById(R.id.altar_comment_container);
            ((AltarCommentFragment) fragment).updateUI();
            mAltarReadNestedScrollView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mAltarReadNestedScrollView.fullScroll(View.FOCUS_DOWN);
                }
            }, 750);
        }
    }

    private class UserGroupNameAdapter extends RecyclerView.Adapter<UserGroupNameViewHolder> {

        private ArrayList<String> mAltarUserGroupNames;

        public UserGroupNameAdapter(ArrayList<String> altarUserGroupNames) {
            mAltarUserGroupNames = altarUserGroupNames;
        }

        @Override
        public UserGroupNameViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view = layoutInflater.inflate(R.layout.list_item_altar_user_group_name, parent, false);
            return new UserGroupNameViewHolder(view);
        }

        @Override
        public void onBindViewHolder(UserGroupNameViewHolder holder, int position) {
            holder.bindUserGroup(mAltarUserGroupNames.get(position));
        }

        @Override
        public int getItemCount() {
            return mAltarUserGroupNames.size();
        }
    }

    private class UserGroupNameViewHolder extends RecyclerView.ViewHolder {

        private String mAltarUserGroupName;

        private TextView mAltarUserGroupNameTextView;

        public UserGroupNameViewHolder(View itemView) {
            super(itemView);
            mAltarUserGroupNameTextView =
                    itemView.findViewById(R.id.list_item_altar_user_group_name_text_view);
        }

        public void bindUserGroup(String altarUserGroup) {
            mAltarUserGroupName = altarUserGroup;
            mAltarUserGroupNameTextView.setText(String.valueOf(mAltarUserGroupName));
        }
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

    private void contributionShowDialog() {
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        View view = layoutInflater.inflate(R.layout.dialog_altar_contribution, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        mStarEditText = view.findViewById(R.id.contribution_star_edit_text);
        Button contributionStarButton = view.findViewById(R.id.contribution_star_button);
        contributionStarButton.setOnClickListener(this);
        Button billingStarButton = view.findViewById(R.id.billing_star_button);
        billingStarButton.setOnClickListener(this);

        builder.setMessage(getString(R.string.dialog_message_contribution,
                String.valueOf(UserSharedPreferences.getStoredStar(getActivity()))));
        builder.setView(view);

        mDialog = builder.create();
        mDialog.show();
    }

    private void contributionConfirmShowDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setMessage(R.string.dialog_message_contribution_confirm);
        builder.setPositiveButton(android.R.string.ok, null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void createCommentShowDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

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
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setMessage(R.string.confirm_comment_dialog_message);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
//                Fragment fragment = mAltarFragmentManager.findFragmentById(R.id.altar_container);
//                ((AltarReadFragment)fragment).
                updateComment();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
