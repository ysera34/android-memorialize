package com.memorial.altar.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by yoon on 2017. 9. 3..
 */

public class User implements Serializable {

    private int mId;
    private String mImagePath;
    private String mName;
    private String mBirth;
    private String mEmail;
    private String mGender;
    private ArrayList<String> mGroupNames;
    private ArrayList<GroupParent> mGroupParents;
    private String mPublicLastWillMessage;
    private String mBankInfo;

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
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

    public String getBirth() {
        return mBirth;
    }

    public void setBirth(String birth) {
        mBirth = birth;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        mEmail = email;
    }

    public String getGender() {
        return mGender;
    }

    public void setGender(String gender) {
        mGender = gender;
    }

    public ArrayList<String> getGroupNames() {
        return mGroupNames;
    }

    public void setGroupNames(ArrayList<String> groupNames) {
        mGroupNames = groupNames;
    }

    public ArrayList<GroupParent> getGroupParents() {
        return mGroupParents;
    }

    public void setGroupParents(ArrayList<GroupParent> groupParents) {
        mGroupParents = groupParents;
    }

    public String getPublicLastWillMessage() {
        return mPublicLastWillMessage;
    }

    public void setPublicLastWillMessage(String publicLastWillMessage) {
        mPublicLastWillMessage = publicLastWillMessage;
    }

    public String getBankInfo() {
        return mBankInfo;
    }

    public void setBankInfo(String bankInfo) {
        mBankInfo = bankInfo;
    }

    @Override
    public String toString() {
        return "User{" +
                "mId=" + mId +
                ", mImagePath='" + mImagePath + '\'' +
                ", mName='" + mName + '\'' +
                ", mBirth='" + mBirth + '\'' +
                ", mGender='" + mGender + '\'' +
                ", mGroupNames=" + mGroupNames +
                ", mGroupParents=" + mGroupParents +
                ", mPublicLastWillMessage='" + mPublicLastWillMessage + '\'' +
                '}';
    }
}
