package com.memorial.altar.view.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.memorial.altar.R;
import com.memorial.altar.model.GroupChild;
import com.memorial.altar.model.User;
import com.memorial.altar.view.activity.AltarActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yoon on 2017. 9. 2..
 */

public class AltarPreviewFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = AltarPreviewFragment.class.getSimpleName();

    private static final String ARG_ALTAR_USER = "altar_user";

    public static AltarPreviewFragment newInstance(User user) {

        Bundle args = new Bundle();
        args.putSerializable(ARG_ALTAR_USER, user);

        AltarPreviewFragment fragment = new AltarPreviewFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private User mAltarUser;

    private NestedScrollView mAltarPreviewNestedScrollView;
    private ImageView mAltarPreviewUserImageView;
    private TextView mAltarPreviewUserNameTextView;
    private TextView mAltarPreviewUserBirthTextView;
    private TextView mAltarPreviewUserGenderTextView;
    private RecyclerView mAltarPreviewUserGroupNameRecyclerView;
    private UserGroupNameAdapter mUserGroupNameAdapter;
    private TextView mAltarPreviewUserPublicLastWillMessageTextView;

    private TextView mAltarPreviewConfirmButtonTextView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAltarUser = (User) getArguments().getSerializable(ARG_ALTAR_USER);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_altar_preview, container, false);
        mAltarPreviewNestedScrollView = view.findViewById(R.id.altar_preview_nested_scroll_view);
        mAltarPreviewUserImageView = view.findViewById(R.id.altar_preview_user_image_view);
        mAltarPreviewUserNameTextView = view.findViewById(R.id.altar_preview_user_name_text_view);
        mAltarPreviewUserBirthTextView = view.findViewById(R.id.altar_preview_user_birth_text_view);
        mAltarPreviewUserGenderTextView = view.findViewById(R.id.altar_preview_user_gender_text_view);
        mAltarPreviewUserGroupNameRecyclerView = view.findViewById(R.id.altar_preview_user_group_name_recycler_view);
        mAltarPreviewUserGroupNameRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAltarPreviewUserGroupNameRecyclerView.setNestedScrollingEnabled(false);
        mAltarPreviewUserPublicLastWillMessageTextView = view.findViewById(R.id.altar_preview_user_public_last_will_message_text_view);
        mAltarPreviewConfirmButtonTextView = view.findViewById(R.id.altar_preview_confirm_button_text_view);
        mAltarPreviewConfirmButtonTextView.setOnClickListener(this);
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
            case R.id.altar_preview_confirm_button_text_view:
                previewConfirm();
                break;
        }
    }

    private void requestAltarUser() {

        ArrayList<String> groupNames = new ArrayList<>();
        for (int i = 0; i < mAltarUser.getGroupParents().size(); i++) {
            List<GroupChild> groupChildren = mAltarUser.getGroupParents().get(i).getChildList();
            for (int j = 0; j < groupChildren.size(); j++) {
                groupNames.add(groupChildren.get(j).getName());
            }
        }
        mAltarUser.setGroupNames(groupNames);

        mUserGroupNameAdapter = new UserGroupNameAdapter(mAltarUser.getGroupNames());
        mAltarPreviewUserGroupNameRecyclerView.setAdapter(mUserGroupNameAdapter);
        updateUI();
    }

    private void updateUI() {
        if (mUserGroupNameAdapter != null) {
            mUserGroupNameAdapter.notifyDataSetChanged();
        }

        Bitmap bitmap = BitmapFactory.decodeFile(mAltarUser.getImagePath());
        mAltarPreviewUserImageView.setImageBitmap(bitmap);
        mAltarPreviewUserNameTextView.setText(String.valueOf(mAltarUser.getName()));
        mAltarPreviewUserBirthTextView.setText(String.valueOf(mAltarUser.getBirth()));
        mAltarPreviewUserGenderTextView.setText(String.valueOf(mAltarUser.getGender()));
        mAltarPreviewUserPublicLastWillMessageTextView.setText(String.valueOf(mAltarUser.getPublicLastWillMessage()));
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

    private void previewConfirm() {
        ((AltarActivity) getActivity()).previewConfirm();
    }
}
