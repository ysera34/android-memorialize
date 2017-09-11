package com.memorial.altar.model;

import com.bignerdranch.expandablerecyclerview.model.Parent;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yoon on 2017. 9. 9..
 */

public class GroupParent implements Parent<GroupChild>, Serializable {

    private int mId;
    private String mName;
    private boolean mInitiallyExpanded;

    private ArrayList<GroupChild> mGroupChildren;

    public GroupParent() {
        mGroupChildren = new ArrayList<>();
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public ArrayList<GroupChild> getGroupChildren() {
        return mGroupChildren;
    }

    public void setGroupChildren(ArrayList<GroupChild> groupChildren) {
        mGroupChildren = groupChildren;
    }

    @Override
    public List<GroupChild> getChildList() {
        return mGroupChildren;
    }

    public void setInitiallyExpanded(boolean initiallyExpanded) {
        mInitiallyExpanded = initiallyExpanded;
    }

    @Override
    public boolean isInitiallyExpanded() {
        return mInitiallyExpanded;
    }
}
