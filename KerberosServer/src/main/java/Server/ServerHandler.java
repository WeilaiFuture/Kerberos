package Server;

import Json.MyJson;
import Json.MyStruct;
import SecurityUtils.DESHandler;
import SecurityUtils.RSAHandler;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.util.LinkedList;
import java.util.Map;

import static Framework.SessionLayer.SessionLayer.*;
import static Json.MyJson.*;
import static Server.ServerDataBase.*;
import static Server.ServerSecurity.createCertif;

public class ServerHandler {
    /*
        包含所有收到的报文处理
    */
    private static String Key; //Kv
    private static MyStruct.Certificate certificate;
    private static RSAPrivateKey sk;
    private static RSAPublicKey pk;

    public ServerHandler() throws Exception {
        connectData();//连接数据库
        Map<String, String> kmap= RSAHandler.createKeys(1024);
        //生成公钥
        String pk1=kmap.get("publicKey");
        pk=RSAHandler.getPublicKey(pk1);
        //生成私钥
        String sk1=kmap.get("privateKey");
        sk=RSAHandler.getPrivateKey(sk1);
        //生成证书
        String ID="SERVER";
        certificate=createCertif(ID,pk1);
        MyStruct myStruct=new MyStruct();
        myStruct.certificate=certificate;
        MyJson.Order order=new MyJson.Order();
        order.setExtend(StructToString(myStruct));
        order.setDst("AS");
        order.setSrc("SERVER");
        order.setMsgType("0001");
        String message=OrderToString(order);
        //发送证书
        System.out.println("发送证书"+message);
     /*   String m=(String) sendByAddress("192.168.43.130",10087,message);
        if(m!=null){
            System.out.println("Kv"+m);
            Kv(m);
        }
        else {
            System.out.println("没有收到Kv");
        }

      */
       Key="12345678";//测试使用
    }
    public static boolean Kcv(String message){
        /*
        head=7;
        Kcv；
        存入数据库
         */
        //获取报文
        MyJson.Order order =MyJson.StringToOrder(message);
        //获取消息
        //解密
        MyStruct mystruct=MyJson.StringToStruct(order.getExtend());
        MyStruct.Ticket ticket=JSON.parseObject(DESHandler.DecryptDES(mystruct.message5.getT(),Key), MyStruct.Ticket.class);
        String kcv=ticket.getKey();
        MyStruct.Authenticator authenticator=JSON.parseObject(DESHandler.DecryptDES(mystruct.message5.getAc(),kcv), MyStruct.Authenticator.class);
        //存入数据库
        wKcv(order.getSrc(),kcv);
        //发送messgae6
        mystruct.message6.setTs(DESHandler.DecryptDES(authenticator.getTs()+1,kcv));//使用Kcv加密
        //发送
        String src=order.getSrc();
        order.setSrc(order.getDst());
        order.setDst(src);
        String sstruct= StructToString(mystruct);
        order.setExtend(sstruct);
        String sorder=MyJson.OrderToString(order);
        send(order.getDst(),sorder);
        System.out.println("发送:"+sorder);
        return false;
    }
    public static boolean Certif(String message){
        /*
        head=0001;
        证书信息；
        存入数据库
        发送证书
         */
        //获取报文
        MyJson.Order order =MyJson.StringToOrder(message);
        //获取消息
        MyStruct mystruct=MyJson.StringToStruct(order.getExtend());
        //存入数据库
        wCertif(mystruct.certificate);
        //发送
     //   MyStruct mystruct1=new MyStruct();
    //    mystruct1.certificate=certificate;
        mystruct.certificate=certificate;
        String src=order.getSrc();
        order.setSrc(order.getDst());
        order.setDst(src);
        String sstruct= StructToString(mystruct);
        order.setExtend(sstruct);
        String sorder=MyJson.OrderToString(order);

        send(order.getDst(),sorder);
        System.out.println("发送:"+sorder);
        return false;
    }
    static public boolean Kv(String message){
        /*
        head=0002;
        Kv；
        将信息保存；
         */
        //获取报文
        MyJson.Order order =MyJson.StringToOrder(message);
        //获取消息
        MyStruct mystruct=MyJson.StringToStruct(order.getExtend());
        Key=RSAHandler.privateDecrypt(mystruct.my_k.getKey(),sk);//保存Kv,需要使用私钥解密
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
        String kcv=rKcv(order.getSrc());
        order.setExtend(DESHandler.DecryptDES(order.getExtend(),kcv));
        //获取消息
        MyStruct mystruct=MyJson.StringToStruct(order.getExtend());
        if(!rLogin(order.getSrc(),mystruct.user.getPsswd())){
            order.setStatusReport(false);
        }
        else{
            //存入数据库
            wLogin(order.getSrc(),1);
            mystruct.user=rSearchID(order.getSrc());
            //更改ACK
            order.setStatusReport(true);
        }
        //发送
        String src=order.getSrc();
        order.setSrc(order.getDst());
        order.setDst(src);
        String sstruct= StructToString(mystruct);
        //加密操作
        kcv=rKcv(order.getDst());
        sstruct=DESHandler.EncryptDES(sstruct,kcv);
        order.setExtend(sstruct);
        String sorder=MyJson.OrderToString(order);
        send(order.getDst(),sorder);
        System.out.println("发送:"+sorder);
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
        //解密操作
        String kcv=rKcv(order.getSrc());
        order.setExtend(DESHandler.DecryptDES(order.getExtend(),kcv));
        //获取消息
        MyStruct mystruct=MyJson.StringToStruct(order.getExtend());
        //查询数据库
        mystruct.friendlist=rFriendList(order.getSrc());
        //发送
        String src=order.getSrc();
        order.setSrc(order.getDst());
        order.setMsgType("1004");
        order.setDst(src);
        String sstruct= StructToString(mystruct);
        //加密操作
        kcv=rKcv(order.getDst());
        sstruct=DESHandler.EncryptDES(sstruct,kcv);
        order.setExtend(sstruct);
        String sorder=MyJson.OrderToString(order);
        send(order.getDst(),sorder);
        System.out.println("发送:"+sorder);
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
        //解密操作
        String kcv=rKcv(order.getSrc());
        order.setExtend(DESHandler.DecryptDES(order.getExtend(),kcv));
        //获取消息
        MyStruct mystruct=MyJson.StringToStruct(order.getExtend());
        //查询数据库，所有上线好友ID
        LinkedList<String>onLineFriends=rOnLineFriend(order.getSrc());
        //发送
        for(int i=0;i<onLineFriends.size();i++){
            onLineFriends.get(i);
            order.setDst(onLineFriends.get(i));
            String sstruct= StructToString(mystruct);
            //加密操作
            kcv=rKcv(order.getDst());
            sstruct=DESHandler.EncryptDES(sstruct,kcv);
            order.setExtend(sstruct);
            String sorder=MyJson.OrderToString(order);
            send(order.getDst(),sorder);
            System.out.println("发送:"+sorder);
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
        //解密操作
        String kcv=rKcv(order.getSrc());
        order.setExtend(DESHandler.DecryptDES(order.getExtend(),kcv));
        //获取消息
        MyStruct mystruct=MyJson.StringToStruct(order.getExtend());
        //查询数据库，用户信息
        mystruct.user=rSearchID(mystruct.user.getUid());
        //发送
        String src=order.getSrc();
        order.setSrc(order.getDst());
        order.setDst(src);
        String sstruct= StructToString(mystruct);
        //加密操作
        kcv=rKcv(order.getDst());
        sstruct=DESHandler.EncryptDES(sstruct,kcv);
        order.setExtend(sstruct);
        String sorder=MyJson.OrderToString(order);
        send(order.getDst(),sorder);
        System.out.println("发送:"+sorder);
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
        //解密操作
        String kcv=rKcv(order.getSrc());
        order.setExtend(DESHandler.DecryptDES(order.getExtend(),kcv));
        //获取消息
        MyStruct mystruct=MyJson.StringToStruct(order.getExtend());
        //查询数据库，所有上线好友ID
        String ID=order.getSrc();
        LinkedList<String>onLineFriends=rOnLineFriend(ID);
        //向好友发送下线提醒
        for(int i=0;i<onLineFriends.size();i++){
            order.setDst(onLineFriends.get(i));
            String sstruct= StructToString(mystruct);
            //加密操作
            kcv=rKcv(order.getDst());
            sstruct=DESHandler.EncryptDES(sstruct,kcv);
            order.setExtend(sstruct);
            String sorder=MyJson.OrderToString(order);
            send(order.getDst(),sorder);
            System.out.println("发送:"+sorder);
        }
        wLogin(ID,0);
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
        //解密操作
        String kcv=rKcv(order.getSrc());
        order.setExtend(DESHandler.DecryptDES(order.getExtend(),kcv));
        //获取消息
        MyStruct mystruct=MyJson.StringToStruct(order.getExtend());
        //查询数据库，用户信息
        mystruct.user=rSearchID(mystruct.user.getUid());
        //返回个人信息
        String src=order.getSrc();
        order.setSrc(order.getDst());
        order.setDst(src);
        String sstruct= StructToString(mystruct);
        //加密操作
        kcv=rKcv(order.getDst());
        sstruct=DESHandler.EncryptDES(sstruct,kcv);
        order.setExtend(sstruct);
        String sorder=MyJson.OrderToString(order);
        send(order.getDst(),sorder);
        System.out.println("发送:"+sorder);
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
        //解密操作
        String kcv=rKcv(order.getSrc());
        order.setExtend(DESHandler.DecryptDES(order.getExtend(),kcv));
        //获取消息
        MyStruct mystruct=MyJson.StringToStruct(order.getExtend());
        //更改数据库，个人信息
        //返回是否修改成功
        order.setStatusReport(wInfo(mystruct.user));
        String src=order.getSrc();
        order.setSrc(order.getDst());
        order.setDst(src);
        //发送
        String sstruct= StructToString(mystruct);
        //加密操作
        kcv=rKcv(order.getDst());
        sstruct=DESHandler.EncryptDES(sstruct,kcv);
        order.setExtend(sstruct);
        String sorder=MyJson.OrderToString(order);
        send(order.getDst(),sorder);
        System.out.println("发送:"+sorder);
        return false;
    }
    static public boolean privateChat(String message){
        /*
        head=2001；
        单聊信息；
         */
        //获取报文
        MyJson.Order order =MyJson.StringToOrder(message);
        //解密操作
        String kcv=rKcv(order.getSrc());
        order.setExtend(DESHandler.DecryptDES(order.getExtend(),kcv));
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
        String sstruct= StructToString(mystruct);
        //加密操作
        kcv=rKcv(order.getDst());
        sstruct=DESHandler.EncryptDES(sstruct,kcv);
        order.setExtend(sstruct);
        String sorder=MyJson.OrderToString(order);
        send(order.getDst(),sorder);
        System.out.println("发送:"+sorder);
        return false;
    }
    static public boolean publicChat(String message){
        /*
        head=2002；
        群聊信息；
         */
        //获取报文
        MyJson.Order order =MyJson.StringToOrder(message);
        //解密操作
        String kcv=rKcv(order.getSrc());
        order.setExtend(DESHandler.DecryptDES(order.getExtend(),kcv));
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
        String sstruct="";
        String sorder="";
        switch (order.getContentType()){
            case "9002":
                //加入群聊
                //转发给群主
                order.setContentType("9002");
                order.setDst(mystruct.group.getLeader());
                //发送
                sstruct= StructToString(mystruct);
                //加密操作
                kcv=rKcv(order.getDst());
                sstruct=DESHandler.EncryptDES(sstruct,kcv);
                order.setExtend(sstruct);
                sorder=MyJson.OrderToString(order);
                send(order.getDst(),sorder);
                System.out.println("发送:"+sorder);
                break;
            case "9003":
                //组建群聊
                wCreateG(mystruct.group);
                //转发邀请
                for(int i=0;i<mystruct.group.getList().size();i++){
                    mystruct.group.getList().get(i);
                    order.setDst(mystruct.group.getList().get(i));//这个地方有问题
                    sstruct= StructToString(mystruct);
                    //加密操作
                    kcv=rKcv(order.getDst());
                    sstruct=DESHandler.EncryptDES(sstruct,kcv);
                    order.setExtend(sstruct);
                    sorder=MyJson.OrderToString(order);
                    send(order.getDst(),sorder);
                    System.out.println("发送:"+sorder);
                }
                break;
            case "9004":
                //退出群聊
                wDeleteG(order.getSrc(),mystruct.group.getGid());
                //转发给群主
                order.setContentType("9004");
                order.setDst(mystruct.group.getLeader());
                //发送
                sstruct= StructToString(mystruct);
                //加密操作
                kcv=rKcv(order.getDst());
                sstruct=DESHandler.EncryptDES(sstruct,kcv);
                order.setExtend(sstruct);
                sorder=MyJson.OrderToString(order);
                send(order.getDst(),sorder);
                System.out.println("发送:"+sorder);
                break;
            case "9008":
                //同意加群
                wAddG(user,mystruct.group.getGid());
                //转发给群主
                order.setContentType("9008");
                order.setDst(mystruct.group.getLeader());
                //发送
                sstruct= StructToString(mystruct);
                //加密操作
                kcv=rKcv(order.getDst());
                sstruct=DESHandler.EncryptDES(sstruct,kcv);
                order.setExtend(sstruct);
                sorder=MyJson.OrderToString(order);
                send(order.getDst(),sorder);
                System.out.println("发送:"+sorder);
                break;
            case "9009":
                //拒绝加群
                //转发给群主
                order.setContentType("9009");
                order.setDst(mystruct.group.getLeader());
                //发送
                sstruct= StructToString(mystruct);
                //加密操作
                kcv=rKcv(order.getDst());
                sstruct=DESHandler.EncryptDES(sstruct,kcv);
                order.setExtend(sstruct);
                sorder=MyJson.OrderToString(order);
                send(order.getDst(),sorder);
                System.out.println("发送:"+sorder);
                break;
        }
        return false;
    }

}
