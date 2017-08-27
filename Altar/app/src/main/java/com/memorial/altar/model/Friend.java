package com.memorial.altar.model;

/**
 * Created by yoon on 2017. 8. 27..
 */

public class Friend {

    private String mImagePath;
    private String mName;
    private String mAge;
    private String mObitDate;
    private boolean mIsCommented;

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
