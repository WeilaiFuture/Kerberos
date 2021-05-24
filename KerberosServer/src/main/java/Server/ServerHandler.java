package Server;

import Json.MyJson;
import Json.MyStruct;

import static Server.ServerDataBase.*;

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

        //获取报文
        MyJson.Order order =MyJson.StringToOrder(message);
        //获取消息
        MyStruct mystruct=MyJson.StringToStruct(order.getExtend());
        //存入数据库
        wCertif(mystruct);
        return false;
    }
    public boolean Kv(String message){
        /*
        head=0002;
        Kv；
        将信息存入数据库；
         */
        //获取报文
        MyJson.Order order =MyJson.StringToOrder(message);
        //获取消息
        MyStruct mystruct=MyJson.StringToStruct(order.getExtend());
        //存入数据库
        //缺少一个数据库函数
        return false;
    }
    public boolean regeister(String message){
        /*
        head=1001;
        注册信息；
        将信息存入数据库；
        返回处理结果ACK；
         */
        //获取报文
        MyJson.Order order =MyJson.StringToOrder(message);
        //获取消息
        MyStruct mystruct=MyJson.StringToStruct(order.getExtend());
        //存入数据库
        wRegister(mystruct);
        return false;
    }
    public boolean login(String message){
        /*
        head=1002;
        登录信息；
        返回登陆结果ACK；
         */
        //获取报文
        MyJson.Order order =MyJson.StringToOrder(message);
        //获取消息
        MyStruct mystruct=MyJson.StringToStruct(order.getExtend());
        //存入数据库
        wRegister(mystruct);
        return false;
    }
    public boolean searchFriendList(String message){
        /*
        head=1003;
        请求好友界面；
        查询数据库；
        返回好友列表，转1004；
         */
        //获取报文
        MyJson.Order order =MyJson.StringToOrder(message);
        //获取消息
        MyStruct mystruct=MyJson.StringToStruct(order.getExtend());
        return false;
    }
    public boolean hello(String message){
        /*
        head=1005;
        问好信息；
        修改数据库；
        向好友转发；
         */
        //获取报文
        MyJson.Order order =MyJson.StringToOrder(message);
        //获取消息
        MyStruct mystruct=MyJson.StringToStruct(order.getExtend());
        return false;
    }
    public boolean heart(String message){
        /*
        head=1006;
        心跳信息；
         */
        //获取报文
        MyJson.Order order =MyJson.StringToOrder(message);
        //获取消息
        MyStruct mystruct=MyJson.StringToStruct(order.getExtend());
        return false;
    }
    public boolean searchID(String message){
        /*
        head=1007;
        查找信息；
        返回；
         */
        //获取报文
        MyJson.Order order =MyJson.StringToOrder(message);
        //获取消息
        MyStruct mystruct=MyJson.StringToStruct(order.getExtend());
        return false;
    }
    public boolean logout(String message){
        /*
        head=1008;
        登出信息；
        修改数据库
        向好友转发；
         */
        //获取报文
        MyJson.Order order =MyJson.StringToOrder(message);
        //获取消息
        MyStruct mystruct=MyJson.StringToStruct(order.getExtend());
        return false;
    }
    public boolean information(String message){
        /*
        head=1009;
        个人信息；
         */
        //获取报文
        MyJson.Order order =MyJson.StringToOrder(message);
        //获取消息
        MyStruct mystruct=MyJson.StringToStruct(order.getExtend());
        return false;
    }
    public boolean changeInfo(String message){
        /*
        head=1010;
        修改个人信息；
        返回ACK；
         */
        //获取报文
        MyJson.Order order =MyJson.StringToOrder(message);
        //获取消息
        MyStruct mystruct=MyJson.StringToStruct(order.getExtend());
        return false;
    }
    public boolean privateChat(String message){
        /*
        head=2001；
        单聊信息；
         */
        //获取报文
        MyJson.Order order =MyJson.StringToOrder(message);
        //获取消息
        MyStruct mystruct=MyJson.StringToStruct(order.getExtend());
        return false;
    }
    public boolean publicChat(String message){
        /*
        head=2002；
        群聊信息；
         */
        //获取报文
        MyJson.Order order =MyJson.StringToOrder(message);
        //获取消息
        MyStruct mystruct=MyJson.StringToStruct(order.getExtend());
        return false;
    }

}
