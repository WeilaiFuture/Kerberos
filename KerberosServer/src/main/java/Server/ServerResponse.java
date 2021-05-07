package Server;

public class ServerResponse {
    /*
    包含所有回复消息
     */
    public boolean Certif(String message){
        /*
        head=0001;
        证书信息；
        将信息存入数据库；
        返回处理结果ACK；
         */
        return false;
    }
    public boolean friendList(String message){
        /*
        head=1004;
        回复好友界面；
         */
        return false;
    }
}
