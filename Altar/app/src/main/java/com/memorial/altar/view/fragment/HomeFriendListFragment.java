package com.memorial.altar.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.memorial.altar.R;
import com.memorial.altar.model.Friend;

import java.util.ArrayList;

/**
 * Created by yoon on 2017. 8. 26..
 */

public class HomeFriendListFragment extends Fragment {

    public static HomeFriendListFragment newInstance() {

        Bundle args = new Bundle();

        HomeFriendListFragment fragment = new HomeFriendListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private RecyclerView mFriendRecyclerView;
    private ArrayList<Friend> mFriends;
    private FriendAdapter mFriendAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFriends = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_friend_list, container, false);
        mFriendRecyclerView = view.findViewById(R.id.friend_recycler_view);
        mFriendRecyclerView.setHasFixedSize(true);
        mFriendRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 4));
        mFriendAdapter = new FriendAdapter(mFriends);
        mFriendRecyclerView.setAdapter(mFriendAdapter);
        mFriends = getFriends();
        mFriendAdapter.setFriends(mFriends);
        mFriendAdapter.notifyDataSetChanged();
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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

    private class FriendViewHolder extends RecyclerView.ViewHolder {

        private Friend mFriend;
        private ImageView mPhotoImageView;
        private TextView mObitDateTextView;
        private TextView mNameTextView;
        private TextView mAgeTextView;

        public FriendViewHolder(View itemView) {
            super(itemView);
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
    }

    private ArrayList<Friend> getFriends() {
        ArrayList<Friend> friends = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            Friend friend = new Friend();
            friend.setAge("age :" + (i + 50) + "age");
            friend.setName("name " + i);
            friend.setObitDate("2017-" + "9-" + i);
            friends.add(friend);
        }
        return friends;
    }
}
