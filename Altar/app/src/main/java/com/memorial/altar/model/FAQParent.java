package com.memorial.altar.model;

import com.bignerdranch.expandablerecyclerview.model.Parent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yoon on 2017. 8. 29..
 */

public class FAQParent implements Parent<FAQChild> {

    private int mId;
    private String mQuestion;
    private String mCreateDate;
    private String mModifyDate;
    private boolean mInitiallyExpanded;

    private ArrayList<FAQChild> mFAQChildren;

    public FAQParent() {
        mFAQChildren = new ArrayList<>();
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getQuestion() {
        return mQuestion;
    }

    public void setQuestion(String question) {
        mQuestion = question;
    }

    public String getCreateDate() {
        return mCreateDate;
    }

    public void setCreateDate(String createDate) {
        mCreateDate = createDate;
    }

    public String getModifyDate() {
        return mModifyDate;
    }

    public void setModifyDate(String modifyDate) {
        mModifyDate = modifyDate;
    }

    public ArrayList<FAQChild> getFAQChildren() {
        return mFAQChildren;
    }

    public void setFAQChildren(ArrayList<FAQChild> FAQChildren) {
        mFAQChildren = FAQChildren;
    }

    @Override
    public List<FAQChild> getChildList() {
        return mFAQChildren;
    }

    public void setInitiallyExpanded(boolean initiallyExpanded) {
        mInitiallyExpanded = initiallyExpanded;
    }

    @Override
    public boolean isInitiallyExpanded() {
        return mInitiallyExpanded;
    }
}
