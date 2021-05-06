package Server;

public class ServerDataBase {
    /*
    包含所有数据库操作的封装函数；
     */
    public boolean connectData() {
        /*
        连接数据库；
         */
        return false;
    }

    public boolean wCertif(String message) {
        /*
        向数据库存入证书；
         */
        return false;
    }

    public boolean wRegister(String message) {
        /*
        向数据库存入注册信息；
         */
        return false;
    }
    public boolean wLogin(String message){
        /*
        向数据库写入登陆状态；
         */
        return false;
    }
    public boolean rFriendList(String message){
        /*
        查询好友列表；
         */
        return false;
    }
    public boolean rSearchID(String message){
        /*
        查询用户；
         */
        return false;
    }
    public boolean rInfo(String message){
        /*
        查询个人信息；
         */
        return false;
    }
    public boolean wAddF(String message){
        /*
        修改好友列表，添加好友；
         */
        return false;
    }
    public boolean wAddG(String message){
        /*
        修改群列表，添加群；
         */
        return false;
    }
    public boolean wDeleteF(String message){
        /*
        修改好友列表，删除好友；
         */
        return false;
    }
    public boolean wDeleteG(String message){
        /*
        修改群列表，退出群聊；
         */
        return false;
    }
    public boolean wCreateG(String message){
        /*
        组建群聊；
         */
        return false;
    }
}