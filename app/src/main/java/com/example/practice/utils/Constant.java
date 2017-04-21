package com.example.practice.utils;

/**
 * Created by AMOBBS on 2016/11/16.
 */

public class Constant {

    /**
     * 记录需要修改的账号的昵称
     */
    public static final String MODIFY_NICKNAME = "modify_nickname";

    /**
     * 记录当前登录的账号
     */
    public static final String LOGIN_ACCOUNT = "login_account";

    /**
     * 记录当前账号的登录密码
     */
    public static final String LOGIN_PASSWORD = "login_password";

    /**
     * 记录当前账号的登录的头像
     */
    public static final String LOGIN_HEADIMAGE = "login_headimage";
    /**
     * 记录当前登录账号的昵称
     */
    public static final String LOGIN_NICKNAME = "login_nickname";
    /**
     * 记录当前登录的性别
     */
    public static final String LOGIN_SEX = "login_sex";

    /**
     * 记录当前账号的生日
     */
    public static final String LOGIN_BIRTHDAY = "login_birthday";

    /**
     * 记录当前登录账号的个性签名
     */
    public static final String LOGIN_SIGN = "login_sign";
    /**
     * 记录聊天时好友的昵称
     */
    public static final String CHAT_NICKNAME = "chat_nickname";

    /**
     * 记录聊天是发送的录音文件路径
     */
    public static final String RECORD_FILE = "record_file";
    /**
     * 记录聊天时拍照的路径
     */
    public static final String CARMERA_PATH = "carmera_path";
    /**
     * 记录聊天时本地图片的路径
     */
    public static final String PIC_PATH = "pic_path";
    /**
     * 服务器端的IP地址
     */
    public static final String SERVER_IP_ADDRESS = "192.168.0.110";

    /**
     * 服务器的端口地址
     */
    public static final int SERVER_PORT_ADDRESS = 8899;
    public static final String TEXT_STRING = "text_string";

    /**
     * 聊天输入的信息
     */
    public static final String SEND_MESSAGE = "send_message";

    public static final int CMD_LOGIN = 1;          //登录
    public static final int CMD_REGISTER = 2;       //注册
    public static final int CMD_SENDMSG = 3;        //发送消息
    public static final int CMD_GETCHATINFO = 4;    //获取聊天记录
    public static final int CMD_NOTIFY_NAME = 5;    //修改个人信息
    public static final int CMD_GETFRIEND_INFO = 6; //获取好友信息
    public static final int CMD_EXIT = 7;//退出登录
    public static final int CMD_SESSIONRECORD=8; //获取会话记录
    public static final int CMD_CHANGE_PASS=9;//修改密码

    //消息类型
    public static final int CHAT = 0;          //聊天
    public static final int RECORD = 1;          //录音
    public static final int PHOTO = 2;          //图片
    public static final int CAMERA = 3;          //拍照
    public static final int PICTURE = 4;         //图片
}