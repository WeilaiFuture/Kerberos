package Server;

import Json.MyStruct;

import java.util.LinkedList;

 public class ServerDataBase {
    /*
    包含所有数据库操作的封装函数；
     */
    static public boolean connectData() {
        /*
        连接数据库；
         */
        return false;
    }

    static public boolean wCertif(MyStruct struct) {
        /*
        向数据库存入证书；
         */
        return false;
    }

    static public boolean wRegister(MyStruct struct) {
        /*
        向数据库存入注册信息；
         */
        return false;
    }
    static public boolean wLogin(String ID,String status){
        /*
        向数据库写入登陆状态；
         */
        return false;
    }
    static public LinkedList rFriendList(String ID){
        /*
        查询好友列表；
         */
        return new LinkedList();
    }
    static public boolean rSearchID(String ID){
        /*
        查询用户；
         */
        return false;
    }
    static public boolean rInfo(String ID){
        /*
        查询个人信息；
         */
        return false;
    }
    static public boolean wAddF(String IDA,String IDB){
        /*
        修改好友列表，添加好友；
         */
        return false;
    }
    static public boolean wAddG(String IDA,String IDG){
        /*
        修改群列表，添加群；
         */
        return false;
    }
    static public boolean wDeleteF(String IDA,String IDB){
        /*
        修改好友列表，删除好友；
         */
        return false;
    }
    static public boolean wDeleteG(String IDA,String IDG){
        /*
        修改群列表，退出群聊；
         */
        return false;
    }
    static public String wCreateG(String message){
        /*
        组建群聊；
         */
        String IDG="";//群账号
        return IDG;
    }
}