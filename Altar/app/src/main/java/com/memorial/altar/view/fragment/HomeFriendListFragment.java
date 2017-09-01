package com.memorial.altar.view.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.memorial.altar.R;
import com.memorial.altar.model.Friend;
import com.memorial.altar.util.OnRecyclerViewScrollListener;

import java.util.ArrayList;

import static android.support.v7.widget.ListViewCompat.NO_POSITION;

/**
 * Created by yoon on 2017. 8. 26..
 */

public class HomeFriendListFragment extends Fragment implements View.OnClickListener {

    public static HomeFriendListFragment newInstance() {

        Bundle args = new Bundle();

        HomeFriendListFragment fragment = new HomeFriendListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private RecyclerView mFriendRecyclerView;
    private ArrayList<Friend> mFriends;
    private FriendAdapter mFriendAdapter;
    private TextView mAddFriendGroupButtonTextView;
    private LinearLayout mFriendGroupLayout;
    private ArrayList<String> mFriendGroupNames;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFriends = new ArrayList<>();
        mFriendGroupNames = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_friend_list, container, false);
        mFriendGroupLayout = view.findViewById(R.id.friend_group_layout);
        mFriendRecyclerView = view.findViewById(R.id.friend_recycler_view);
        mFriendRecyclerView.setHasFixedSize(true);
        mFriendRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(4, LinearLayoutManager.VERTICAL));
        mFriendAdapter = new FriendAdapter(mFriends);
        mFriendRecyclerView.setAdapter(mFriendAdapter);
        mFriendRecyclerView.setNestedScrollingEnabled(false);
        mFriendRecyclerView.addOnScrollListener(new OnRecyclerViewScrollListener() {
            @Override
            public void onShowView() {
                showAddFriendGroupTextView();
            }

            @Override
            public void onHideView() {
                hideAddFriendGroupTextView();
            }
        });
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

    private class FriendViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {

        private Friend mFriend;
        private ImageView mPhotoImageView;
        private TextView mObitDateTextView;
        private TextView mNameTextView;
        private TextView mAgeTextView;

        public FriendViewHolder(View itemView) {
            super(itemView);
            itemView.setOnLongClickListener(this);
            mPhotoImageView = itemView.findViewById(R.id.list_item_friend_photo_image_view);
            mObitDateTextView = itemView.findViewById(R.id.list_item_friend_obit_date_text_view);
            mNameTextView = itemView.findViewById(R.id.list_item_friend_name_text_view);
            mAgeTextView = itemView.findViewById(R.id.list_item_friend_age_text_view);
        }

        public void bindFriend(Friend friend) {
            mFriend = friend;
            mObitDateTextView.setText(mFriend.getObitDate());
            mNameTextView.setText(mFriend.getName());
            mAgeTextView.setText(mFriend.getAge());
        }

        @Override
        public boolean onLongClick(View view) {
            assignFriendGroupShowDialog();
            return false;
        }
    }

    private ArrayList<Friend> getFriends() {
        ArrayList<Friend> friends = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            Friend friend = new Friend();
            friend.setAge("age :" + (i + 50) + "age");
            friend.setName("name " + i);
            friend.setObitDate("2017-" + "9-" + i);
            friends.add(friend);
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
        final Spinner groupScopeSpinner = view.findViewById(R.id.add_group_scope_spinner);
        final String[] groupScopeArray = getResources().getStringArray(R.array.add_friend_group_scope);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_dropdown_item_1line, groupScopeArray);
        groupScopeSpinner.setAdapter(adapter);
        TextInputLayout addFriendGroupNameTextLayout = view.findViewById(R.id.add_group_name_text_input_layout);
        final EditText addFriendGroupNameEditText = view.findViewById(R.id.add_group_name_edit_text);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.friend_list_add_group));
        builder.setView(view);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
//                Toast.makeText(getActivity(), "scope : " + groupScopeArray[groupScopeSpinner.getSelectedItemPosition()] +
//                " group name : " + addFriendGroupNameEditText.getText().toString(), Toast.LENGTH_SHORT).show();
                addFriendGroupLayout(groupScopeArray[groupScopeSpinner.getSelectedItemPosition()] + " | "
                        + addFriendGroupNameEditText.getText().toString(), mFriendGroupLayout);
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

    private void addFriendGroupLayout(String groupName, LinearLayout parentLayout) {
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        TextView groupNameTextView = (TextView) layoutInflater.inflate(R.layout.layout_friend_group_name_text_view, null);
        groupNameTextView.setText(String.valueOf(groupName));
        mFriendGroupNames.add((groupName));
        parentLayout.addView(groupNameTextView, 0);
        RecyclerView groupRecyclerView = (RecyclerView) layoutInflater.inflate(R.layout.layout_friend_group_recycler_view, null);
        parentLayout.addView(groupRecyclerView, 1);
        Toast.makeText(getActivity(), "\"" + groupName + "\"" + " is added.", Toast.LENGTH_SHORT).show();
    }

    private void assignFriendGroupShowDialog() {
        if (mFriendGroupNames.size() == 0) {
            Toast.makeText(getActivity(), R.string.promote_add_group, Toast.LENGTH_SHORT).show();
            return;
        }
        CharSequence[] cs = mFriendGroupNames.toArray(new CharSequence[mFriendGroupNames.size()]);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.friend_list_assign_group));
        builder.setSingleChoiceItems(cs, NO_POSITION, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(getActivity(), "position : " + i, Toast.LENGTH_SHORT).show();
            }
        });
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
