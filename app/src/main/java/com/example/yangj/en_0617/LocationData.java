package com.example.yangj.en_0617;

/**
 * Created by USER on 2017-06-18.
 */

public class LocationData {
    private String userName;
    private String message;

    public LocationData(){}
    public LocationData(String userName, String message){
        this.userName=userName;
        this.message=message;
    }
    public String getUserName(){
        return userName;
    }
    public void setUserName(String userName){
        this.userName=userName;
    }
    public String getMessage(){
        return message;
    }
    public void setMessage(String message){
        this.message=message;
    }
}
