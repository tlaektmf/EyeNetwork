package com.example.yangj.en_0617;

/**
 * Created by 심다슬 on 2017-06-19.
 */

public class EdataReadWrite {

    public String userID;//올라오는 데이터 "키"값이랑 똑같아야돼
     public String sendedText;//올라오는 데이터 키값이랑 똑같아야돼



             public EdataReadWrite() {
                 // Default constructor required for calls to DataSnapshot.getValue(Comment.class)
             }


             public EdataReadWrite(String uid, String text) {
                 this.userID = uid;
                 this.sendedText = text;
             }

    public String getEmail() {
        return userID;
    }

    public void setEmail(String email) {
        this.userID = email;
    }

    public String getText() {
        return sendedText;
    }

    public void setText(String text) {
        this.sendedText = text;
    }
}
