package com.memorial.altar.view.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.memorial.altar.R;
import com.memorial.altar.model.Friend;

import java.util.ArrayList;

/**
 * Created by yoon on 2017. 8. 26..
 */

public class HomeObituaryFragment extends Fragment {

    public static HomeObituaryFragment newInstance() {

        Bundle args = new Bundle();

        HomeObituaryFragment fragment = new HomeObituaryFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private RecyclerView mSearchedFriendRecyclerView;
    private SearchedFriendAdapter mSearchedFriendAdapter;
    private ArrayList<Friend> mSearchedFriends;
    private EditText mObituarySearchEditText;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        mSearchedFriends = new ArrayList<>();
        mSearchedFriends = getSearchedFriends();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_obituary, container, false);
        mObituarySearchEditText = view.findViewById(R.id.obituary_search_edit_text);
        mObituarySearchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                mSearchedFriendAdapter.onFilterViewHolder(editable.toString());
            }
        });
        mSearchedFriendRecyclerView = view.findViewById(R.id.searched_friend_recycler_view);
        mSearchedFriendRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(4, LinearLayoutManager.VERTICAL));
        updateUI();
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        updateUI();
    }

    private void updateUI() {
        if (mSearchedFriendAdapter == null) {
            mSearchedFriendAdapter = new SearchedFriendAdapter(mSearchedFriends);
            mSearchedFriendRecyclerView.setAdapter(mSearchedFriendAdapter);
        } else {
            mSearchedFriendAdapter.notifyDataSetChanged();
        }
    }

    private class SearchedFriendAdapter extends RecyclerView.Adapter<SearchedFriendViewHolder> {

        private ArrayList<Friend> mSearchedFriends;

        public SearchedFriendAdapter(ArrayList<Friend> searchedFriends) {
            mSearchedFriends = searchedFriends;
        }

        public void setSearchedFriends(ArrayList<Friend> searchedFriends) {
            mSearchedFriends = searchedFriends;
        }

        @Override
        public SearchedFriendViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view = layoutInflater.inflate(R.layout.list_item_searched_friend, parent, false);
            return new SearchedFriendViewHolder(view);
        }

        @Override
        public void onBindViewHolder(SearchedFriendViewHolder holder, int position) {
            holder.bindSearchedFriend(mSearchedFriends.get(position));
        }

        @Override
        public int getItemCount() {
            return mSearchedFriends.size();
        }

        public void onFilterViewHolder(String text) {
            ArrayList<Friend> tempFriends = new ArrayList<>();
            for(Friend f: mSearchedFriends){
                //or use .equal(text) with you want equal match
                //use .toLowerCase() for better matches
                if(f.getName().contains(text)){
                    tempFriends.add(f);
                }
            }
            setSearchedFriends(tempFriends);
            notifyDataSetChanged();
        }
    }

    private class SearchedFriendViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        private Friend mSearchedFriend;

        private ImageView mPhotoImageView;
        private TextView mBirthTextView;
        private TextView mNameTextView;

        public SearchedFriendViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            mPhotoImageView = itemView.findViewById(R.id.list_item_searched_friend_photo_image_view);
            mBirthTextView = itemView.findViewById(R.id.list_item_searched_friend_birth_text_view);
            mNameTextView = itemView.findViewById(R.id.list_item_searched_friend_name_text_view);
        }

        public void bindSearchedFriend(Friend searchedFriend) {
            mSearchedFriend = searchedFriend;
            mNameTextView.setText(String.valueOf(mSearchedFriend.getName()));
        }

        @Override
        public void onClick(View view) {
            obituaryShowDialog();
        }
    }

    private void obituaryShowDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setIcon(R.drawable.ic_obituary_submit);
        builder.setMessage(getString(R.string.obituary_submit));
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                obituaryAdviceMessageShowDialog();
            }
        });
        builder.setNegativeButton(android.R.string.no, null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void obituaryAdviceMessageShowDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setIcon(R.drawable.ic_error_outline_grey_300_24dp);
        builder.setTitle(R.string.dialog_title_information);
        builder.setMessage(getString(R.string.obituary_advice_message));
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                obituarySubmitShowDialog();
            }
        });
        builder.setNegativeButton(android.R.string.no, null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void obituarySubmitShowDialog() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_submit_obituary, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);
        builder.setTitle(R.string.obituary_submit_title);
        builder.setMessage(getString(R.string.obituary_advice_message));
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // after validation
                obituaryConfirmMessageShowDialog();
            }
        });
        builder.setNegativeButton(android.R.string.no, null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void obituaryConfirmMessageShowDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setIcon(R.drawable.ic_error_outline_grey_300_24dp);
        builder.setTitle(R.string.dialog_title_information);
        builder.setMessage(getString(R.string.obituary_confirm_message));
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.setNegativeButton(android.R.string.no, null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private ArrayList<Friend> getSearchedFriends() {
        ArrayList<Friend> friends = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Friend friend = new Friend();
            friend.setName("name : " + i);
            friends.add(friend);
        }
        return friends;
    }
}
