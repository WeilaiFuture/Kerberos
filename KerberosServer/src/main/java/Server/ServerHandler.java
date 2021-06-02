package Server;

import Json.MyJson;
import Json.MyStruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;

import static Framework.SessionLayer.SessionLayer.*;
import static Server.ServerDataBase.*;

public class ServerHandler {
    /*
        包含所有收到的报文
    */

    public static boolean Certif(String message){
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
        wCertif(mystruct.certificate);
        return false;
    }
    static public boolean Kv(String message){
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
    //注册功能属于web server
    public static boolean regeister(String message){
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
    public static boolean login(String message){
        /*
        head=1002;
        登录信息；
        返回登陆结果ACK；
         */
        //获取报文
        MyJson.Order order =MyJson.StringToOrder(message);
        //解密操作
        //获取消息
        MyStruct mystruct=MyJson.StringToStruct(order.getExtend());
        //存入数据库
        wLogin(order.getSrc(),mystruct.user.getStatus());
        //更改ACK
        order.setStatusReport(true);
        //发送
        order.setDst(order.getSrc());
        String sstruct=MyJson.StructToString(mystruct);
        //加密操作
        order.setExtend(sstruct);
        String sorder=MyJson.OrderToString(order);
        send(order.getDst(),sorder);
        return false;
    }
    static public boolean searchFriendList(String message){
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
        //查询数据库
        mystruct.friendlist.setFriends(rFriendList(order.getSrc()));
        //发送
        order.setDst(order.getSrc());
        order.setMsgType("1004");
        order.setDst(order.getSrc());
        String sstruct=MyJson.StructToString(mystruct);
        //加密操作
        order.setExtend(sstruct);
        String sorder=MyJson.OrderToString(order);
        send(order.getDst(),sorder);
        return false;
    }
    static public boolean hello(String message){
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
        //查询数据库，所有上线好友ID
        LinkedList<String>onLineFriends=rOnLineFriend(order.getSrc());
        //发送
        for(int i=0;i<onLineFriends.size();i++){
            onLineFriends.get(i);
            order.setDst(onLineFriends.get(i));
            String sstruct=MyJson.StructToString(mystruct);
            //加密操作
            order.setExtend(sstruct);
            String sorder=MyJson.OrderToString(order);
            send(order.getDst(),sorder);
        }
        return false;
    }
    static public boolean heart(String message){
        /*
        head=1006;
        心跳信息；
        什么都不用做；
         */
        //获取报文
        MyJson.Order order =MyJson.StringToOrder(message);
        //获取消息
        MyStruct mystruct=MyJson.StringToStruct(order.getExtend());
        return false;
    }
    static public boolean searchID(String message){
        /*
        head=1007;
        查找信息；
        返回；
         */
        //获取报文
        MyJson.Order order =MyJson.StringToOrder(message);
        //获取消息
        MyStruct mystruct=MyJson.StringToStruct(order.getExtend());
        //查询数据库，用户信息
        mystruct.user=rSearchID(mystruct.user.getUid());
        //发送
        order.setDst(order.getSrc());
        String sstruct=MyJson.StructToString(mystruct);
        //加密操作
        order.setExtend(sstruct);
        String sorder=MyJson.OrderToString(order);
        send(order.getDst(),sorder);
        return false;
    }
    static public boolean logout(String message){
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
        //查询数据库，所有上线好友ID
        String ID=order.getSrc();
        LinkedList<String>onLineFriends=rOnLineFriend(ID);
        //向好友发送下线提醒
        for(int i=0;i<onLineFriends.size();i++){
            onLineFriends.get(i);
            order.setDst(onLineFriends.get(i));
            String sstruct=MyJson.StructToString(mystruct);
            //加密操作
            order.setExtend(sstruct);
            String sorder=MyJson.OrderToString(order);
            send(order.getDst(),sorder);
        }
        //向netty发送登出信息
        logOut(order.getSrc());
        return false;
    }
    static public boolean information(String message){
        /*
        head=1009;
        个人信息；
         */
        //获取报文
        MyJson.Order order =MyJson.StringToOrder(message);
        //获取消息
        MyStruct mystruct=MyJson.StringToStruct(order.getExtend());
        //查询数据库，用户信息
        mystruct.user=rSearchID(mystruct.user.getUid());
        //返回个人信息
        order.setDst(order.getSrc());
        String sstruct=MyJson.StructToString(mystruct);
        //加密操作
        order.setExtend(sstruct);
        String sorder=MyJson.OrderToString(order);
        send(order.getDst(),sorder);
        return false;
    }
    static public boolean changeInfo(String message){
        /*
        head=1010;
        修改个人信息；
        返回ACK；
         */
        //获取报文
        MyJson.Order order =MyJson.StringToOrder(message);
        //获取消息
        MyStruct mystruct=MyJson.StringToStruct(order.getExtend());
        //更改数据库，个人信息
        //返回是否修改成功
        order.setStatusReport(wInfo(mystruct.user));
        order.setDst(order.getSrc());
        //发送
        String sstruct=MyJson.StructToString(mystruct);
        //加密操作
        order.setExtend(sstruct);
        String sorder=MyJson.OrderToString(order);
        send(order.getDst(),sorder);
        return false;
    }
    static public boolean privateChat(String message){
        /*
        head=2001；
        单聊信息；
         */
        //获取报文
        MyJson.Order order =MyJson.StringToOrder(message);
        //获取消息
        MyStruct mystruct=MyJson.StringToStruct(order.getExtend());
        /*
        101 文本消息
        102 图片消息
        103 语音消息
        104 表情消息
        105 动图消息
        9001 添加好友信息
        9006 同意加好友
        9007 拒绝加好友
         */
        //先转发后判断是否要进行数据库操作
        String IDA=order.getSrc();
        String IDB=order.getDst();
        switch (order.getContentType()){
            case "9001":
                //添加好友，先加入好友列表，等待回复是否同意。
                wAddF(order.getSrc(),mystruct.friend);
                break;
            case "9006":
                //同意添加好友
                wAddF(order.getSrc(),mystruct.friend);
                break;
            case "9007":
                //拒绝加好友，在好友列表中删除。
                wDeleteF(order.getDst(),order.getSrc());
                break;
        }
        //发送
        String sstruct=MyJson.StructToString(mystruct);
        //加密操作
        order.setExtend(sstruct);
        String sorder=MyJson.OrderToString(order);
        send(order.getDst(),sorder);
        return false;
    }
    static public boolean publicChat(String message){
        /*
        head=2002；
        群聊信息；
         */
        //获取报文
        MyJson.Order order =MyJson.StringToOrder(message);
        //获取消息
        MyStruct mystruct=MyJson.StringToStruct(order.getExtend());
        /*
        需要区分具体消息类型；
        9002 加入群聊信息
        9003 组建群聊信息
        9004 退出群聊信息
        9008同意加群
        9009拒绝加群
         */
        //查询群内好友
        LinkedList<String> GroupUsers=rGroupUser(order.getDst());
        //转发
        MyStruct.User user=rSearchID(order.getSrc());
        switch (order.getContentType()){
            case "9002":
                //加入群聊
                wAddG(user,mystruct.group.getGid());
                break;
            case "9003":
                //组建群聊
                wCreateG(mystruct.group);
                break;
            case "9004":
                //退出群聊
                wDeleteG(order.getSrc(),mystruct.group.getGid());
                break;
            case "9008":
                //同意加群
                break;
            case "9009":
                //拒绝加群
                wDeleteG(order.getSrc(),mystruct.group.getGid());
                break;
        }
        return false;
    }

}
