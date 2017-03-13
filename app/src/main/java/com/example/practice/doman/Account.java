package com.example.practice.doman;

/**
 * Created by AMOBBS on 2016/11/4.
 */

public class Account {

    public String account;//账号
    public String password;//密码
    public String nickname;//昵称
    public int state;       //状态
    public int headimg;//头像

    public int getHeadimg() {
        return headimg;
    }

    public void setHeadimg(int headimg) {
        this.headimg = headimg;
    }

    public Account(){}

    public Account(String account, String password, String nickname, int state) {
        this.account = account;
        this.password = password;
        this.nickname = nickname;
        this.state = state;
    }

    public Account(String account, String password, String nickname, int state, int headimg) {
        this.account = account;
        this.password = password;
        this.nickname = nickname;
        this.state = state;
        this.headimg = headimg;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "Account{" +
                "account='" + account + '\'' +
                ", password='" + password + '\'' +
                ", nickname='" + nickname + '\'' +
                ", state=" + state +
                ", headimg=" + headimg +
                '}';
    }
}
