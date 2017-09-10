package com.memorial.altar.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by yoon on 2017. 9. 10..
 */

public class LastWill implements Serializable {

    private int mId;
    private ArrayList<Contact> mContacts;
    private String mSendTo;
    private String mMessage;
    private boolean mIsUpdate;

    public LastWill() {
        mContacts = new ArrayList<>();
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public ArrayList<Contact> getContacts() {
        return mContacts;
    }

    public void setContacts(ArrayList<Contact> contacts) {
        mContacts = contacts;
    }

    public String getSendTo() {
        return mSendTo;
    }

    public void setSendTo(String sendTo) {
        mSendTo = sendTo;
    }

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage = message;
    }

    public boolean isUpdate() {
        return mIsUpdate;
    }

    public void setUpdate(boolean update) {
        mIsUpdate = update;
    }
}
