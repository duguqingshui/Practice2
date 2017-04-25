package com.example.practice.doman;

import java.util.Date;

/**
 * Created by AMOBBS on 2016/11/24.
 */

public class Messages {

    public int cmd;
    public Account sender;
    public Account receicer;
    public String content;
    public Date time;
    public int type;//发送类型
    public int sex;//性别
    public String birthday;//出生日期
    public String sign;//个性签名

    public int getSex() {
        return sex;
    }

    public Messages(int cmd, Account sender, Account receicer, String content, Date time, int type) {
        this.cmd = cmd;
        this.sender = sender;
        this.receicer = receicer;
        this.content = content;
        this.time = time;
        this.type = type;
    }

    public void setSex(int sex) {
        this.sex = sex;

    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public static final int SEND_TYPE_TXT=0;
    //表示发送录音
    public static final int SEND_TYPE_RECORD=1;
    //表示发送图片
    public static final int SEND_TYPE_PICTURE=2;

    public int getCmd() {
        return cmd;
    }

    public void setCmd(int cmd) {
        this.cmd = cmd;
    }

    public Account getSender() {
        return sender;
    }

    public void setSender(Account sender) {
        this.sender = sender;
    }

    public Account getReceicer() {
        return receicer;
    }

    public void setReceicer(Account receicer) {
        this.receicer = receicer;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Messages(int cmd, Account sender, Account receicer, String content, Date time, int type, int sex, String birthday, String sign) {
        this.cmd = cmd;
        this.sender = sender;
        this.receicer = receicer;
        this.content = content;
        this.time = time;
        this.type = type;
        this.sex = sex;
        this.birthday = birthday;
        this.sign = sign;
    }

    @Override
    public String toString() {
        return "Message{" +
                "cmd=" + cmd +
                ", sender=" + sender +
                ", receicer=" + receicer +
                ", content='" + content + '\'' +
                ", time=" + time +
                ", type=" + type +
                ", sex=" + sex +
                ", birthday='" + birthday + '\'' +
                ", sign='" + sign + '\'' +
                '}';
    }
}
