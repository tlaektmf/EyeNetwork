package com.example.yangj.en_0617;

/**
 * Created by 심다슬 on 2017-06-19.
 */

public class EdataReadWrite {//데이터를 올린다

    public String latitude;//올라오는 데이터 "키"값이랑 똑같아야돼
     public String longitude;//올라오는 데이터 키값이랑 똑같아야돼



             public EdataReadWrite() {
                 // Default constructor required for calls to DataSnapshot.getValue(Comment.class)
             }


             public EdataReadWrite(String uid, String text) {
                 this.latitude = uid;
                 this.longitude = text;
             }

    public String getEmail() {
        return latitude;
    }

    public void setEmail(String email) {
        this.latitude = email;
    }

    public String getText() {
        return longitude;
    }

    public void setText(String text) {
        this.longitude = text;
    }
}
