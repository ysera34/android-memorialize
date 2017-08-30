package com.memorial.altar.view.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ChildViewHolder;
import com.bignerdranch.expandablerecyclerview.ExpandableRecyclerAdapter;
import com.bignerdranch.expandablerecyclerview.ParentViewHolder;
import com.memorial.altar.R;
import com.memorial.altar.model.FAQChild;
import com.memorial.altar.model.FAQParent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yoon on 2017. 8. 29..
 */

public class ConfigFAQFragment extends Fragment {

    private static final String TAG = ConfigFAQFragment.class.getSimpleName();

    public static ConfigFAQFragment newInstance() {

        Bundle args = new Bundle();

        ConfigFAQFragment fragment = new ConfigFAQFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private FAQAdapter mFAQAdapter;
    private RecyclerView mFAQRecyclerView;
    private ArrayList<FAQParent> mFAQParents;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFAQParents = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_config_faq, container, false);
        mFAQRecyclerView = view.findViewById(R.id.config_faq_recycler_view);
        mFAQRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        updateUI();
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mFAQParents = getFAQParents();
        updateUI();
    }

    private void updateUI() {
        if (mFAQAdapter == null) {
            mFAQAdapter = new FAQAdapter(mFAQParents);
            mFAQRecyclerView.setAdapter(mFAQAdapter);
        } else {
            mFAQAdapter.setParentList(mFAQParents, true);
        }
    }

    private class FAQAdapter
            extends ExpandableRecyclerAdapter<FAQParent, FAQChild, FAQParentViewHolder, FAQChildViewHolder> {

        private LayoutInflater mLayoutInflater;

        public FAQAdapter(@NonNull List<FAQParent> parentList) {
            super(parentList);
            mLayoutInflater = LayoutInflater.from(getActivity());
        }

        @NonNull
        @Override
        public FAQParentViewHolder onCreateParentViewHolder(@NonNull ViewGroup parentViewGroup, int viewType) {
            View view = mLayoutInflater.inflate(R.layout.list_item_faq_parent, parentViewGroup, false);
            return new FAQParentViewHolder(view);
        }

        @NonNull
        @Override
        public FAQChildViewHolder onCreateChildViewHolder(@NonNull ViewGroup childViewGroup, int viewType) {
            View view = mLayoutInflater.inflate(R.layout.list_item_faq_child, childViewGroup, false);
            return new FAQChildViewHolder(view);
        }

        @Override
        public void onBindParentViewHolder(@NonNull FAQParentViewHolder parentViewHolder,
                                           int parentPosition, @NonNull FAQParent parent) {
            parentViewHolder.bindFAQParent(parent);
        }

        @Override
        public void onBindChildViewHolder(@NonNull FAQChildViewHolder childViewHolder,
                                          int parentPosition, int childPosition, @NonNull FAQChild child) {
            childViewHolder.bindFAQChild(child);
        }
    }

    private class FAQParentViewHolder extends ParentViewHolder {

        private static final float INITIAL_POSITION = 0.0f;
        private static final float ROTATED_POSITION = 180.0f;

        private FAQParent mFAQParent;

        private TextView mSequenceTextView;
        private TextView mQuestionTextView;
        private ImageView mArrowDownImageView;

        public FAQParentViewHolder(@NonNull View itemView) {
            super(itemView);
            mSequenceTextView = itemView.findViewById(R.id.list_item_faq_parent_sequence_text_view);
            mQuestionTextView = itemView.findViewById(R.id.list_item_faq_parent_question_text_view);
            mArrowDownImageView = itemView.findViewById(R.id.list_item_faq_parent_arrow_image_view);
        }

        public void bindFAQParent(FAQParent faqParent) {
            mFAQParent = faqParent;
            int questionNumber = mFAQParents.indexOf(mFAQParent) + 1;
            String numberStr;
            if (questionNumber < 10) {
                numberStr = "0" + String.valueOf(questionNumber);
            } else {
                numberStr = String.valueOf(questionNumber);
            }
            mSequenceTextView.setText(String.valueOf(numberStr));
            mQuestionTextView.setText(Html.fromHtml(String.valueOf(mFAQParent.getQuestion())));
        }

        @Override
        public void setExpanded(boolean expanded) {
            super.setExpanded(expanded);
            if (expanded) {
                mArrowDownImageView.setRotation(ROTATED_POSITION);
            } else {
                mArrowDownImageView.setRotation(INITIAL_POSITION);
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

    private class FAQChildViewHolder extends ChildViewHolder {

        private FAQChild mFAQChild;

        private TextView mAnswerTextView;

        public FAQChildViewHolder(@NonNull View itemView) {
            super(itemView);
            mAnswerTextView = itemView.findViewById(R.id.list_item_faq_child_answer_text_view);
        }

        public void bindFAQChild(FAQChild faqChild) {
            mFAQChild = faqChild;
            mAnswerTextView.setText(Html.fromHtml(String.valueOf(mFAQChild.getAnswer())));
        }
    }

    private ArrayList<FAQParent> getFAQParents() {

        ArrayList<FAQParent> faqParents = new ArrayList<>();
        String[] faqQuestions = getResources().getStringArray(R.array.faq_sample_questions_array);

        for (int i = 0; i < 5; i++) {
            FAQParent faqParent = new FAQParent();
            faqParent.setId(i);
            faqParent.setQuestion(faqQuestions[i]);

            FAQChild faqChild = new FAQChild();
            faqChild.setAnswer(getString(R.string.faq_sample_answer));
            faqParent.getChildList().add(faqChild);
            faqParents.add(faqParent);
        }
        return faqParents;
    }
}
