package com.memorial.altar.model;

import java.io.Serializable;

/**
 * Created by yoon on 2017. 9. 6..
 */

public class Contact implements Serializable {

    private String mName;
    private String mPhoneNumber;
    private boolean mIsSelected;

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getPhoneNumber() {
        return mPhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        mPhoneNumber = phoneNumber;
    }

    public boolean isSelected() {
        return mIsSelected;
    }

    public void setSelected(boolean selected) {
        mIsSelected = selected;
    }
}
