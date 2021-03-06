package com.memorial.altar.view.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.memorial.altar.R;
import com.memorial.altar.model.Friend;
import com.memorial.altar.util.UserSharedPreferences;
import com.memorial.altar.view.activity.AltarActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import static com.memorial.altar.R.id.friend_group_name_text_view;
import static com.memorial.altar.common.Common.URL_HOST;

/**
 * Created by yoon on 2017. 8. 26..
 */

public class FriendListFragment extends Fragment implements View.OnClickListener {

    public static FriendListFragment newInstance() {

        Bundle args = new Bundle();

        FriendListFragment fragment = new FriendListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private NestedScrollView mFriendNestedScrollView;
    private RecyclerView mFriendRecyclerView;
    private ArrayList<Friend> mFriends;
    private FriendAdapter mFriendAdapter;
    private TextView mAddFriendGroupButtonTextView;
    private LinearLayout mFriendGroupLayout;
    private ArrayList<String> mFriendGroupNames;
    private ArrayList<AddedFriendAdapter> mAddedFriendAdapters;
    private ArrayList<ArrayList<Friend>> mAddedFriendGroups;
    private ArrayList<TextView> mAddedFriendGroupTextViews;
    private ArrayList<RecyclerView> mAddedFriendGroupRecyclerViews;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFriends = new ArrayList<>();
        mFriendGroupNames = new ArrayList<>();
        mAddedFriendAdapters = new ArrayList<>();
        mAddedFriendGroups = new ArrayList<>();
        mAddedFriendGroupTextViews = new ArrayList<>();
        mAddedFriendGroupRecyclerViews = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friend_list, container, false);
        mFriendNestedScrollView = view.findViewById(R.id.friend_nested_scroll_view);
        mFriendNestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                View view = v.getChildAt(v.getChildCount() - 1);
                int diff = (view.getBottom() - (v.getHeight() + v.getScrollY()));
                if (diff == 0) { // or diff <= 10
                    hideAddFriendGroupTextView();
                } else {
                    showAddFriendGroupTextView();
                }
            }
        });
        mFriendGroupLayout = view.findViewById(R.id.friend_group_layout);
        mFriendRecyclerView = view.findViewById(R.id.friend_recycler_view);
        mFriendRecyclerView.setHasFixedSize(true);
        mFriendRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(4, LinearLayoutManager.VERTICAL));
        mFriendAdapter = new FriendAdapter(mFriends);
        mFriendRecyclerView.setAdapter(mFriendAdapter);
        mFriendRecyclerView.setNestedScrollingEnabled(false);
        mFriends = getFriends();
        mFriendAdapter.setFriends(mFriends);
        mFriendAdapter.notifyDataSetChanged();

        mAddFriendGroupButtonTextView = view.findViewById(R.id.add_friend_group_button_text_view);
        mAddFriendGroupButtonTextView.setOnClickListener(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_friend_group_button_text_view:
                addFriendGroupShowDialog();
//                addFriendGroupScopeShowDialog();
                break;
        }
    }

    private class FriendAdapter extends RecyclerView.Adapter<FriendViewHolder> {

        private ArrayList<Friend> mFriends;

        public FriendAdapter(ArrayList<Friend> friends) {
            mFriends = friends;
        }

        public void setFriends(ArrayList<Friend> friends) {
            mFriends = friends;
        }

        @Override
        public FriendViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view = layoutInflater.inflate(R.layout.list_item_friend, parent, false);
            return new FriendViewHolder(view);
        }

        @Override
        public void onBindViewHolder(FriendViewHolder holder, int position) {
            holder.bindFriend(mFriends.get(position));
        }

        @Override
        public int getItemCount() {
            return mFriends.size();
        }
    }

    private class FriendViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, View.OnLongClickListener {

        private Friend mFriend;
        private ImageView mPhotoImageView;
        private TextView mObitDateTextView;
        private TextView mNameTextView;
        private TextView mAgeTextView;
        private TextView mCommentTextView;

        public FriendViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            mPhotoImageView = itemView.findViewById(R.id.list_item_friend_photo_image_view);
            mObitDateTextView = itemView.findViewById(R.id.list_item_friend_obit_date_text_view);
            mNameTextView = itemView.findViewById(R.id.list_item_friend_name_text_view);
            mAgeTextView = itemView.findViewById(R.id.list_item_friend_age_text_view);
            mCommentTextView = itemView.findViewById(R.id.list_item_friend_comment_text_view);
        }

        public void bindFriend(Friend friend) {
            mFriend = friend;
            if (mFriend.getObitDate() != null) {
                Glide.with(getActivity()).load(URL_HOST + mFriend.getImagePath()).into(mPhotoImageView);
//                mPhotoImageView.setImageResource(R.drawable.img_person);
                mObitDateTextView.setText(mFriend.getObitDate());
                mNameTextView.setText(mFriend.getName());
                mAgeTextView.setText(mFriend.getBirth());
                mCommentTextView.setText(R.string.cherish_comment);
                if (mFriend.isCommented()) {
                    mCommentTextView.setBackgroundResource(R.drawable.ic_commented_true);
                } else {
                    mCommentTextView.setBackgroundResource(R.drawable.ic_commented_false);
                }
            } else {
                mObitDateTextView.setText(null);
                mNameTextView.setText(null);
                mAgeTextView.setText(null);
                mCommentTextView.setText(null);
            }
        }

        @Override
        public void onClick(View view) {
            if (mFriend.getObitDate() != null) {
                startMemorialFriendActivity(mFriend.getId() + 1);
            }
        }

        @Override
        public boolean onLongClick(View view) {
            if (mFriend.getObitDate() != null) {
                assignFriendGroupShowDialog(mFriend, false);
            }
            return true;
        }
    }

//    private ArrayList<Friend> getFriends() {
//        ArrayList<Friend> friends = new ArrayList<>();
//        for (int i = 0; i < 20; i++) {
//            Friend friend = new Friend();
//            friend.setId(i);
//            friend.setBirth("향년 " + (i + 50) + "세");
//            friend.setName("name " + i);
//            if (i <= 9) {
//                friend.setObitDate("2017-" + "9-" + i);
//            }
//            friend.setCommented(i % 2 == 0);
//            friends.add(friend);
//        }
//        return friends;
//    }

    private ArrayList<Friend> getFriends() {
        String[] obituarySampleFriendNameArr = getResources().getStringArray(R.array.obituary_sample_friend_name);
        String[] obituarySampleFriendEmailArr = getResources().getStringArray(R.array.obituary_sample_friend_email);
        String[] obituarySampleImagePathArr = getResources().getStringArray(R.array.obituary_sample_friend_image_path);
        String[] obituarySampleFriendBirthArr = getResources().getStringArray(R.array.obituary_sample_friend_birth);
        ArrayList<Friend> friends = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            Friend friend = new Friend();
            friend.setId(i);
            friend.setCommented(i % 2 == 0);
            if (i <= 9) {
                friend.setName(obituarySampleFriendNameArr[i]);
                friend.setEmail(obituarySampleFriendEmailArr[i]);
                friend.setImagePath(obituarySampleImagePathArr[i]);
                friend.setBirth(obituarySampleFriendBirthArr[i]);
                friend.setObitDate("2017-" + "9-" + i);
            }
            friends.add(friend);
        }

        String userEmail = UserSharedPreferences.getStoredUserEmail(getActivity());
        if (userEmail != null) {
            int size = friends.size();
            for (int i = 0; i < size; i++) {
                if (friends.get(i).getEmail().equals(userEmail)) {
                    friends.remove(i);
                    break;
                }
            }
        }

        return friends;
    }


    private void showAddFriendGroupTextView() {
        mAddFriendGroupButtonTextView.animate().translationY(0)
                .setInterpolator(new DecelerateInterpolator(2));
    }

    private void hideAddFriendGroupTextView() {
        mAddFriendGroupButtonTextView.animate().translationY(mAddFriendGroupButtonTextView.getHeight())
                .setInterpolator(new AccelerateInterpolator(2));
    }

    private void addFriendGroupShowDialog() {
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        View view = layoutInflater.inflate(R.layout.dialog_add_friend_group, null);
        TextInputLayout addFriendGroupNameTextLayout = view.findViewById(R.id.add_group_name_text_input_layout);
        final TextInputEditText addFriendGroupNameEditText = view.findViewById(R.id.add_group_name_edit_text);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.friend_list_add_group));
        builder.setView(view);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (addFriendGroupNameEditText.getText().toString().length() > 0) {
                    addFriendGroupLayout(addFriendGroupNameEditText.getText().toString(), mFriendGroupLayout);
                } else {
                    Toast.makeText(getActivity(), R.string.check_the_input_window, Toast.LENGTH_SHORT).show();
                }
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

//    private void addFriendGroupScopeShowDialog() {
//        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
//        View view = layoutInflater.inflate(R.layout.dialog_add_friend_group_scope, null);
//        final Spinner groupScopeSpinner = view.findViewById(R.id.add_group_scope_spinner);
//        final String[] groupScopeArray = getResources().getStringArray(R.array.add_friend_group_scope);
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
//                android.R.layout.simple_dropdown_item_1line, groupScopeArray);
//        groupScopeSpinner.setAdapter(adapter);
//        TextInputLayout addFriendGroupNameTextLayout = view.findViewById(R.id.add_group_name_text_input_layout);
//        final TextInputEditText addFriendGroupNameEditText = view.findViewById(R.id.add_group_name_edit_text);
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//        builder.setTitle(getString(R.string.friend_list_add_group));
//        builder.setView(view);
//        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
////                Toast.makeText(getActivity(), "scope : " + groupScopeArray[groupScopeSpinner.getSelectedItemPosition()] +
////                " group name : " + addFriendGroupNameEditText.getText().toString(), Toast.LENGTH_SHORT).show();
//                if (addFriendGroupNameEditText.getText().toString().length() > 0) {
//                    addFriendGroupLayout(groupScopeArray[groupScopeSpinner.getSelectedItemPosition()] + " | "
//                            + addFriendGroupNameEditText.getText().toString(), mFriendGroupLayout);
//                } else {
//                    Toast.makeText(getActivity(), "Please check the input window.", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//        builder.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//
//            }
//        });
//        AlertDialog dialog = builder.create();
//        dialog.show();
//    }

    private void addFriendGroupLayout(final String groupName, LinearLayout parentLayout) {
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        View view = layoutInflater.inflate(R.layout.layout_friend_group_name_text_view, null);
        TextView groupNameTextView = view.findViewById(friend_group_name_text_view);
        groupNameTextView.setText(String.valueOf(groupName));
        mAddedFriendGroupTextViews.add(groupNameTextView);
        mFriendGroupNames.add(groupName);
        RecyclerView groupRecyclerView = (RecyclerView) layoutInflater.inflate(R.layout.layout_friend_group_recycler_view, null);
        groupRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        mAddedFriendGroups.add(new ArrayList<Friend>());
        mAddedFriendAdapters.add(new AddedFriendAdapter(mAddedFriendGroups.get(mAddedFriendGroups.size() - 1)));
        groupRecyclerView.setAdapter(mAddedFriendAdapters.get(mAddedFriendAdapters.size() - 1));
        mAddedFriendGroupRecyclerViews.add(groupRecyclerView);
        parentLayout.addView(view, 2 * mAddedFriendGroups.size() - 2);
        parentLayout.addView(groupRecyclerView, 2 * mAddedFriendGroups.size() - 1);
        Toast.makeText(getActivity(), "\"" + groupName + "\"" + " is added.", Toast.LENGTH_SHORT).show();
        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                removeFriendGroupShowDialog(groupName);
                return false;
            }
        });
    }

    private void removeFriendGroupShowDialog(final String groupName) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.friend_list_remove_group));
        builder.setMessage(getString(R.string.friend_list_remove_group_message, groupName));
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                for (String str : mFriendGroupNames) {
                    if (str.equals(groupName)) {
                        int groupIndex = mFriendGroupNames.indexOf(str);
                        removeFriendGroupLayout(groupIndex);
                        break;
                    }
                }
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

    private void removeFriendGroupLayout(int groupPosition) {
        mFriendGroupNames.remove(groupPosition);
        mAddedFriendGroupTextViews.remove(groupPosition);
        mAddedFriendAdapters.remove(groupPosition);
        mAddedFriendGroupRecyclerViews.remove(groupPosition);
        ArrayList<Friend> friends = mAddedFriendGroups.get(groupPosition);
        for (Friend friend : friends) {
            friend.setGroupId(-1);
            mFriends.add(friend);
        }
        mAddedFriendGroups.remove(groupPosition);
        mFriendGroupLayout.removeViewsInLayout(2 * groupPosition, 2);
        mFriendGroupLayout.requestLayout();
        notifyAdapters();
    }

    private void assignFriendGroupShowDialog(final Friend friend, final boolean isAdded) {
        if (mFriendGroupNames.size() == 0) {
            Toast.makeText(getActivity(), R.string.promote_add_group, Toast.LENGTH_SHORT).show();
            return;
        }
        if (isAdded) {
            mFriendGroupNames.add(getString(R.string.friend_group_title));
        }
        CharSequence[] cs = mFriendGroupNames.toArray(new CharSequence[mFriendGroupNames.size()]);
        int checkedItem = friend.getGroupId();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.friend_list_assign_group));
        builder.setCancelable(false);

        builder.setSingleChoiceItems(cs, checkedItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                Toast.makeText(getActivity(), "position : " + i, Toast.LENGTH_SHORT).show();
                ListView listView = ((AlertDialog) dialogInterface).getListView();
                listView.setTag(String.valueOf(i));
            }
        });
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                ListView listView = ((AlertDialog) dialogInterface).getListView();
                String tag = (String) listView.getTag();
                if (tag == null) {
                    Toast.makeText(getActivity(), "No group selected.", Toast.LENGTH_SHORT).show();
                } else {
                    int selectedPosition = Integer.valueOf((String) listView.getTag());
                    if (selectedPosition > -1) {
                        Toast.makeText(getActivity(), "selected position : " + selectedPosition, Toast.LENGTH_SHORT).show();
                        if (isAdded && selectedPosition == mFriendGroupNames.size() - 1) {
                            selectedPosition = -1;
                        }
                        moveFriend(friend, selectedPosition);
                    }
                }
                if (isAdded) {
                    mFriendGroupNames.remove(getString(R.string.friend_group_title));
                }
//                else {
//                    Toast.makeText(getActivity(), "It is a group that already belongs.", Toast.LENGTH_SHORT).show();
//                }
            }
        });
        builder.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (isAdded) {
                    mFriendGroupNames.remove(getString(R.string.friend_group_title));
                }
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void moveFriend(Friend friend, int selectedGroupIndex) {
        int groupId = friend.getGroupId();
        if (groupId == -1) {
            mFriends.remove(friend);
            friend.setGroupId(selectedGroupIndex);
            mAddedFriendGroups.get(selectedGroupIndex).add(friend);
        } else {
            mAddedFriendGroups.get(groupId).remove(friend);
            friend.setGroupId(selectedGroupIndex);

            if (selectedGroupIndex == -1) {
                mFriends.add(friend);
            } else {
                mAddedFriendGroups.get(selectedGroupIndex).add(friend);
            }
        }
        notifyAdapters();
    }

    private void notifyAdapters() {
        for (int i = 0; i < mAddedFriendGroups.size(); i++) {
            mAddedFriendAdapters.get(i).notifyDataSetChanged();
        }
        Collections.sort(mFriends, new Comparator<Friend>() {
            @Override
            public int compare(Friend friend1, Friend friend2) {
                if (friend1.getId() > friend2.getId()) {
                    return 1;
                } else if (friend1.getId() < friend2.getId()) {
                    return -1;
                } else {
                    return 0;
                }
            }
        });
        mFriendAdapter.notifyDataSetChanged();
    }

    private class AddedFriendAdapter extends RecyclerView.Adapter<AddedFriendViewHolder> {

        private ArrayList<Friend> mAddedFriends;

        public AddedFriendAdapter(ArrayList<Friend> addedFriends) {
            mAddedFriends = addedFriends;
        }

        public void setAddedFriends(ArrayList<Friend> addedFriends) {
            mAddedFriends = addedFriends;
        }

        @Override
        public AddedFriendViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view = layoutInflater.inflate(R.layout.list_item_added_friend, parent, false);
            return new AddedFriendViewHolder(view);
        }

        @Override
        public void onBindViewHolder(AddedFriendViewHolder holder, int position) {
            holder.bindAddedFriend(mAddedFriends.get(position));
        }

        @Override
        public int getItemCount() {
            return mAddedFriends.size();
        }
    }

    private class AddedFriendViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, View.OnLongClickListener {

        private Friend mAddedFriend;
        private ImageView mPhotoImageView;
        private TextView mObitDateTextView;
        private TextView mNameTextView;
        private TextView mAgeTextView;
        private TextView mCommentTextView;

        public AddedFriendViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            mPhotoImageView = itemView.findViewById(R.id.list_item_friend_photo_image_view);
            mObitDateTextView = itemView.findViewById(R.id.list_item_friend_obit_date_text_view);
            mNameTextView = itemView.findViewById(R.id.list_item_friend_name_text_view);
            mAgeTextView = itemView.findViewById(R.id.list_item_friend_age_text_view);
            mCommentTextView = itemView.findViewById(R.id.list_item_friend_comment_text_view);
        }

        public void bindAddedFriend(Friend addedFriend) {
            mAddedFriend = addedFriend;
            if (mAddedFriend.getObitDate() != null) {
                Glide.with(getActivity()).load(URL_HOST + mAddedFriend.getImagePath()).into(mPhotoImageView);
//                mPhotoImageView.setImageResource(R.drawable.img_person);
                mObitDateTextView.setText(mAddedFriend.getObitDate());
                mNameTextView.setText(mAddedFriend.getName());
                mAgeTextView.setText(mAddedFriend.getBirth());
                mCommentTextView.setText(R.string.cherish_comment);
                if (mAddedFriend.isCommented()) {
                    mCommentTextView.setBackgroundResource(R.drawable.ic_commented_true);
                } else {
                    mCommentTextView.setBackgroundResource(R.drawable.ic_commented_false);
                }
            } else {
                mObitDateTextView.setText(null);
                mNameTextView.setText(null);
                mAgeTextView.setText(null);
                mCommentTextView.setText(null);
            }
        }

        @Override
        public void onClick(View view) {
            startMemorialFriendActivity(mAddedFriend.getId() + 1);
        }

        @Override
        public boolean onLongClick(View view) {
            assignFriendGroupShowDialog(mAddedFriend, true);
            return true;
        }
    }

    private void startMemorialFriendActivity(int friendId) {
        startActivity(AltarActivity.newIntent(getActivity(), friendId));
    }

//    private final static Comparator mFriendAscComparator = new Comparator() {
//
////        private final Collator mCollator = Collator.getInstance();
//        @Override
//        public int compare(Object o, Object t1) {
//
//        }
//    }
}
