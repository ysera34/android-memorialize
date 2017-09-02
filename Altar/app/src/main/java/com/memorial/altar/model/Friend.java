package com.memorial.altar.model;

/**
 * Created by yoon on 2017. 8. 27..
 */

public class Friend {

    private int mGroupId;
    private String mImagePath;
    private String mName;
    private String mAge;
    private String mObitDate;
    private boolean mIsCommented;

    public Friend() {
        setGroupId(-1);
    }

    public int getGroupId() {
        return mGroupId;
    }

    public void setGroupId(int groupId) {
        mGroupId = groupId;
    }

    public String getImagePath() {
        return mImagePath;
    }

    public void setImagePath(String imagePath) {
        mImagePath = imagePath;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getAge() {
        return mAge;
    }

    public void setAge(String age) {
        mAge = age;
    }

    public String getObitDate() {
        return mObitDate;
    }

    public void setObitDate(String obitDate) {
        mObitDate = obitDate;
    }

    public boolean isCommented() {
        return mIsCommented;
    }

    public void setCommented(boolean commented) {
        mIsCommented = commented;
    }
}
