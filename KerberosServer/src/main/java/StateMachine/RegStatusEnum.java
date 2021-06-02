package StateMachine;

public enum RegStatusEnum {
    WAITINFO,//等待报文
    HEADOVER,//解析完成
    HANDLER1,//处理证书报文
    HANDLER2,//处理Kv报文
    HANDLER7,//处理client认证报文
    HANDLER1001,//处理注册报文
    HANDLER1002,//处理登录报文
    HANDLER1003,//处理请求好友界面报文
    HANDLER1006,//处理心跳报文
    HANDLER1007,//处理查找ID报文
    HANDLER1009,//处理查询个人信息报文
    HANDLER1010,//处理修改个人信息报文
    HANDLER2001,//处理私聊报文
    HANDLER2002,//处理群聊报文
    EXIT//退出
}