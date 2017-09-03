package com.memorial.altar.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.memorial.altar.R;
import com.memorial.altar.model.User;

import java.util.ArrayList;

/**
 * Created by yoon on 2017. 9. 2..
 */

public class AltarFragment extends Fragment {

    private static final String TAG = AltarFragment.class.getSimpleName();

    private static final String ARG_ALTAR_USER_ID = "altar_user_id";

    public static AltarFragment newInstance(int altarUserId) {

        Bundle args = new Bundle();
        args.putInt(ARG_ALTAR_USER_ID, altarUserId);

        AltarFragment fragment = new AltarFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private int mAltarUserId;
    private User mAltarUser;

    private ImageView mAltarUserImageView;
    private TextView mAltarUserNameTextView;
    private TextView mAltarUserBirthTextView;
    private TextView mAltarUserGenderTextView;
    private RecyclerView mAltarUserGroupNameRecyclerView;
    private UserGroupNameAdapter mUserGroupNameAdapter;
    private TextView mAltarUserPublicLastWillMessageTextView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAltarUserId = getArguments().getInt(ARG_ALTAR_USER_ID);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_altar, container, false);
        mAltarUserImageView = view.findViewById(R.id.altar_user_image_view);
        mAltarUserNameTextView = view.findViewById(R.id.altar_user_name_text_view);
        mAltarUserBirthTextView = view.findViewById(R.id.altar_user_birth_text_view);
        mAltarUserGenderTextView = view.findViewById(R.id.altar_user_gender_text_view);
        mAltarUserGroupNameRecyclerView = view.findViewById(R.id.altar_user_group_name_recycler_view);
        mAltarUserGroupNameRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAltarUserGroupNameRecyclerView.setNestedScrollingEnabled(false);
        mAltarUserPublicLastWillMessageTextView = view.findViewById(R.id.altar_user_public_last_will_message_text_view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        requestAltarUser(mAltarUserId);
    }

    private void requestAltarUser(int altarUserId) {

        User user = new User();
        user.setId(altarUserId);
        user.setName("sample name");
        user.setGender("sample gender");
        user.setBirth("sample obit date");
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
        mUserGroupNameAdapter = new UserGroupNameAdapter(mAltarUser.getGroupNames());
        mAltarUserGroupNameRecyclerView.setAdapter(mUserGroupNameAdapter);
        updateUI();
    }

    private void updateUI() {
        if (mUserGroupNameAdapter != null) {
            mUserGroupNameAdapter.notifyDataSetChanged();
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
