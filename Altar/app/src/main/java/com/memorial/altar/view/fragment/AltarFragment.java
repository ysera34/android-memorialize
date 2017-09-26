package com.memorial.altar.view.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.memorial.altar.R;
import com.memorial.altar.model.User;
import com.memorial.altar.view.activity.HomeUpdateActivity;

import java.util.ArrayList;

import static com.memorial.altar.common.Common.URL_HOST;

/**
 * Created by yoon on 2017. 8. 26..
 */

public class AltarFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = AltarFragment.class.getSimpleName();

    private static final String ARG_ALTAR_USER_ID = "altar_user_id";
    private static final String ARG_ALTAR_USER = "altar_user";


    public static AltarFragment newInstance() {

        Bundle args = new Bundle();

        AltarFragment fragment = new AltarFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static AltarFragment newInstance(int altarUserId) {

        Bundle args = new Bundle();
        args.putInt(ARG_ALTAR_USER_ID, altarUserId);

        AltarFragment fragment = new AltarFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static AltarFragment newInstance(User user) {

        Bundle args = new Bundle();
        args.putSerializable(ARG_ALTAR_USER, user);

        AltarFragment fragment = new AltarFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private int mAltarUserId;
    private User mAltarUser;

    private NestedScrollView mAltarReadNestedScrollView;
    private ImageView mAltarUserImageView;
    private TextView mAltarUserNameTextView;
    private TextView mAltarUserBirthTextView;
    private TextView mAltarUserGenderTextView;
    private RecyclerView mAltarUserGroupNameRecyclerView;
    private UserGroupNameAdapter mUserGroupNameAdapter;
    private TextView mAltarUserPublicLastWillMessageTextView;

    private FragmentManager mCommentFragmentManager;

    private TextView mAltarUpdateButtonTextView;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAltarUserId = getArguments().getInt(ARG_ALTAR_USER_ID);
        mAltarUser = (User) getArguments().getSerializable(ARG_ALTAR_USER);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_altar, container, false);
        mAltarReadNestedScrollView = view.findViewById(R.id.altar_read_nested_scroll_view);
        mAltarUserImageView = view.findViewById(R.id.altar_read_user_image_view);
        mAltarUserNameTextView = view.findViewById(R.id.altar_user_name_text_view);
        mAltarUserBirthTextView = view.findViewById(R.id.altar_user_birth_text_view);
        mAltarUserGenderTextView = view.findViewById(R.id.altar_user_gender_text_view);
        mAltarUserGroupNameRecyclerView = view.findViewById(R.id.altar_user_group_name_recycler_view);
        mAltarUserGroupNameRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAltarUserGroupNameRecyclerView.setNestedScrollingEnabled(false);
        mAltarUserPublicLastWillMessageTextView = view.findViewById(R.id.altar_user_public_last_will_message_text_view);
        mAltarUpdateButtonTextView = view.findViewById(R.id.altar_update_button_text_view);
        mAltarUpdateButtonTextView.setOnClickListener(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (mAltarUser != null) {
            mUserGroupNameAdapter = new UserGroupNameAdapter(mAltarUser.getGroupNames());
            mAltarUserGroupNameRecyclerView.setAdapter(mUserGroupNameAdapter);
            updateUI();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.altar_update_button_text_view:
                startActivity(HomeUpdateActivity.newIntent(getActivity(), mAltarUser));
                break;
        }
    }

    private void updateUI() {
        if (mUserGroupNameAdapter != null) {
            mUserGroupNameAdapter.notifyDataSetChanged();
        }
        if (mAltarUser.getImagePath().contains("storage")) {
            Bitmap bitmap = BitmapFactory.decodeFile(mAltarUser.getImagePath());
            mAltarUserImageView.setImageBitmap(bitmap);
        } else {
            Glide.with(getActivity()).load(URL_HOST + mAltarUser.getImagePath()).into(mAltarUserImageView);
        }

        mAltarUserNameTextView.setText(String.valueOf(mAltarUser.getName()));
        mAltarUserBirthTextView.setText(String.valueOf(mAltarUser.getBirth()));
        mAltarUserGenderTextView.setText(String.valueOf(mAltarUser.getGender()));
        mAltarUserPublicLastWillMessageTextView.setText(String.valueOf(mAltarUser.getPublicLastWillMessage()));
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
}
