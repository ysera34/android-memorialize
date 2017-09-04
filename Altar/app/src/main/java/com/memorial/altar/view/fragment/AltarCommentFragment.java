package com.memorial.altar.view.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.memorial.altar.R;
import com.memorial.altar.model.Comment;

import java.util.ArrayList;

/**
 * Created by yoon on 2017. 9. 3..
 */

public class AltarCommentFragment extends Fragment {

    private static final String TAG = AltarCommentFragment.class.getSimpleName();

    public static AltarCommentFragment newInstance() {

        Bundle args = new Bundle();

        AltarCommentFragment fragment = new AltarCommentFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private RecyclerView mCommentRecyclerView;
    private CommentAdapter mCommentAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_altar_comment, container, false);
        mCommentRecyclerView = view.findViewById(R.id.altar_comment_recycler_view);
        mCommentRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mCommentRecyclerView.setNestedScrollingEnabled(false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        requestComments();
    }

    private void requestComments() {

        ArrayList<Comment> comments = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Comment comment = new Comment();
            comment.setWriter("sample writer");
            comment.setDate("sample date");
            comment.setMessage(
                    "sample message sample message " + i + "\n" +
                    "sample message sample message sample message \n" +
                    "sample message sample message sample message sample message");
            comments.add(comment);
        }

        mCommentAdapter = new CommentAdapter(comments);
        mCommentRecyclerView.setAdapter(mCommentAdapter);
        updateUI();
    }

    public void updateUI() {
        if (mCommentAdapter != null) {
            mCommentAdapter.notifyDataSetChanged();
        }
    }

    private class CommentAdapter extends RecyclerView.Adapter<CommentViewHolder> {

        private ArrayList<Comment> mComments;

        public CommentAdapter(ArrayList<Comment> comments) {
            mComments = comments;
        }

        @Override
        public CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view = layoutInflater.inflate(R.layout.list_item_altar_comment, parent, false);
            return new CommentViewHolder(view);
        }

        @Override
        public void onBindViewHolder(CommentViewHolder holder, int position) {
            holder.bindComment(mComments.get(position));
        }

        @Override
        public int getItemCount() {
            return mComments.size();
        }
    }

    private class CommentViewHolder extends RecyclerView.ViewHolder
            implements View.OnLongClickListener {

        private Comment mComment;

        private TextView mCommentWriterTextView;
        private TextView mCommentDateTextView;
        private TextView mCommentMessageTextView;

        public CommentViewHolder(View itemView) {
            super(itemView);
            itemView.setOnLongClickListener(this);
            mCommentWriterTextView = itemView.findViewById(R.id.list_item_altar_comment_writer_text_view);
            mCommentDateTextView = itemView.findViewById(R.id.list_item_altar_comment_date_text_view);
            mCommentMessageTextView = itemView.findViewById(R.id.list_item_altar_comment_message_text_view);
        }

        public void bindComment(Comment comment) {
            mComment = comment;
            mCommentWriterTextView.setText(String.valueOf(mComment.getWriter()));
            mCommentDateTextView.setText(String.valueOf(mComment.getDate()));
            mCommentMessageTextView.setText(String.valueOf(mComment.getMessage()));
        }

        @Override
        public boolean onLongClick(View view) {
            // if comment writer == user
            deleteCommentShowDialog();
            return true;
        }
    }

    private void deleteCommentShowDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setMessage(R.string.delete_comment_dialog_message);
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
