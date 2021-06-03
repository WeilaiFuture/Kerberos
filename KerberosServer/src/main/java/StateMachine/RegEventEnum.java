package StateMachine;

public enum RegEventEnum {
    RECIVE,//收到报文
    TIMEOUT,//超时未收到心跳包
    HEAD1,//证书
    HEAD2,//Kv
    HEAD7,//Kcv
    HEAD1008,//登出
    HEAD1001,//注册
    HEAD1002,//登录
    HEAD1003,//请求好友界面
    HEAD1006,//心跳信息
    HEAD1007,//查找信息
    HEAD1009,//查询个人信息
    HEAD1010,//修改个人信息
    HEAD2001,//私聊信息
    HEAD2002//群聊信息
}