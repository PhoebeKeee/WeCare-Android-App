package com.example.victor.mertial_test.activities.familymember;

/**
 * Created by ipt810105 on 2015/10/22.
 */
public class member {
    String account;
    String name;
    int photoId;
    private String nickname;
    String pin;
    member(String account,String name, int photoId,String nickname,String pin) {
        this.account=account;
        this.name = name;
        this.photoId = photoId;
        this.setNickname(nickname);
        this.pin=pin;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
