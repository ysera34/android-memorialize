package com.memorial.altar.model;

/**
 * Created by yoon on 2017. 9. 3..
 */

public class Comment {

    private int mId;
    private String mWriter;
    private String mDate;
    private String mMessage;

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getWriter() {
        return mWriter;
    }

    public void setWriter(String writer) {
        mWriter = writer;
    }

    public String getDate() {
        return mDate;
    }

    public void setDate(String date) {
        mDate = date;
    }

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage = message;
    }
}
