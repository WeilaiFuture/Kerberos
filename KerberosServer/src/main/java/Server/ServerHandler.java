package Server;

public class ServerHandler {
    /*
        包含所有收到的报文
    */
    public boolean Certif(String message){
        /*
        head=0001;
        证书信息；
        存入数据库
         */
        return false;
    }
    public boolean Kv(String message){
        /*
        head=0002;
        Kv；
        将信息存入数据库；
         */
        return false;
    }
    public boolean regeister(String message){
        /*
        head=1001;
        注册信息；
        将信息存入数据库；
        返回处理结果ACK；
         */
        return false;
    }
    public boolean login(String message){
        /*
        head=1002;
        登录信息；
        返回登陆结果ACK；
         */
        return false;
    }
    public boolean searchFriendList(String message){
        /*
        head=1003;
        请求好友界面；
        查询数据库；
        返回好友列表，转1004；
         */
        return false;
    }
    public boolean hello(String message){
        /*
        head=1005;
        问好信息；
        修改数据库；
        向好友转发；
         */
        return false;
    }
    public boolean heart(String message){
        /*
        head=1006;
        心跳信息；
         */
        return false;
    }
    public boolean searchID(String message){
        /*
        head=1007;
        查找信息；
        返回；
         */
        return false;
    }
    public boolean logout(String message){
        /*
        head=1008;
        登出信息；
        修改数据库
        向好友转发；
         */
        return false;
    }
    public boolean information(String message){
        /*
        head=1009;
        个人信息；
         */
        return false;
    }
    public boolean changeInfo(String message){
        /*
        head=1010;
        修改个人信息；
        返回ACK；
         */
        return false;
    }
    public boolean privateChat(String message){
        /*
        head=2001；
        单聊信息；
         */
        return false;
    }
    public boolean publicChat(String message){
        /*
        head=2002；
        群聊信息；
         */
        return false;
    }

}
