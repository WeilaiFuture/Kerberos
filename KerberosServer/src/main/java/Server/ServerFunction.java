package Server;


import Framework.SessionLayer.Handlers.SessionHandler;
import Framework.SessionLayer.SessionLayer;
import Json.MyJson;
import SecurityUtils.RSAHandler;
import StateMachine.Application;
import StateMachine.RegEventEnum;
import StateMachine.RegStatusEnum;
import org.springframework.boot.SpringApplication;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

import javax.swing.*;

import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

import static Framework.SessionLayer.SessionLayer.*;
import static Json.MyJson.StringToOrder;
import static SecurityUtils.RSAHandler.verifySign;
import static Server.ServerDataBase.*;
import static StateMachine.RegEventEnum.RECIVE;
import static UI.log.add;
import static UI.log.createTable;

public class ServerFunction extends SessionHandler {
     ServerHandler serverHandler;
    {
        try {
            serverHandler = new ServerHandler();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
 //   JTable table = createTable();

 //   LinkedList<String[]> list = new LinkedList<String[]>();
    String message="";
    //继承方法集
    @Override
    public void receive(String channelName,Object msg) throws Exception {
        /*
        判断消息头部，转到相应的处理函数；
        这里应使用状态机；
        原来为等待连接状态；
        调用这个函数进入到等待报文状态和报文处理状态；
        直到用户登出，转为等待连接状态。
         */
        String info=(String) msg;
        if((info).substring((info).length()-1).equals("}")){
            info=message+info;
            message="";
        }
        else{
            message=info;
            return;
        }
        //解析报文头部
        System.out.println("收到"+info);
        MyJson.Order order=StringToOrder(info);
        String head="HEAD"+order.getMsgType();
        //判断绑定
        if(!channelName.equals(order.getSrc())){
            System.out.println("channelName:"+channelName+" uname:"+order.getSrc());
            bindChannelWithUserName(channelName,order.getSrc());
        }
        //UI表格
//        String []s=new String[5];
//        list.addFirst(s);
//        s[0]=order.getSrc();//源
//        s[1]=order.getDst();//目的
//        s[2]=order.getMsgType();//报文类型
//        s[3]=order.getExtend();//密文
//        s[4]=order.getExtend();//明文
//        add(table, list);
        String type=order.getMsgType();

        switch (order.getMsgType()){
            case "0001":
                serverHandler.Certif(info);
                break;
            case "0002":
                serverHandler.Kv(info);
                break;
            case "0007":
                serverHandler.Kcv(info);
                break;
            case "1002":
                System.out.println(rPK(order.getSrc()));
                System.out.println(order.getSign());
                System.out.println(order.getExtend());
                if(verifySign(RSAHandler.getPublicKey(rPK(order.getSrc())),order.getSign(),order.getExtend())){
                    System.out.println("1002#####签名验证成功");
                    serverHandler.login(info);
                }
                else System.out.println("1002####签名验证失败");
                break;
            case "1003":
                if(verifySign(RSAHandler.getPublicKey(rPK(order.getSrc())),order.getSign(),order.getExtend())){
                    System.out.println("1003#####签名验证成功");
                    serverHandler.searchFriendList(info);
                }
                else System.out.println("1003####签名验证失败");
                break;
            case "1005":
                if(verifySign(RSAHandler.getPublicKey(rPK(order.getSrc())),order.getSign(),order.getExtend())){
                    System.out.println("1005#####签名验证成功");
                    serverHandler.hello(info);
                }
                else System.out.println("1005####签名验证失败");
                logIn(order.getSrc());
                break;
            case "1006":
                if(verifySign(RSAHandler.getPublicKey(rPK(order.getSrc())),order.getSign(),order.getExtend())){
                    System.out.println("1006#####签名验证成功");
                    serverHandler.heart(info);
                }
                else System.out.println("1006####签名验证失败");
                break;
            case "1007":
                if(verifySign(RSAHandler.getPublicKey(rPK(order.getSrc())),order.getSign(),order.getExtend())){
                    System.out.println("1007#####签名验证成功");
                    serverHandler.searchID(info);
                }
                else System.out.println("1007####签名验证失败");
                break;
            case "1008":
                if(verifySign(RSAHandler.getPublicKey(rPK(order.getSrc())),order.getSign(),order.getExtend())){
                    System.out.println("1008#####签名验证成功");
                    serverHandler.logout(info);
                }
                else System.out.println("1008####签名验证失败");
                logOut(order.getSrc());
                break;
            case "1009":
                if(verifySign(RSAHandler.getPublicKey(rPK(order.getSrc())),order.getSign(),order.getExtend())){
                    System.out.println("1009#####签名验证成功");
                    serverHandler.information(info);
                }
                else System.out.println("1009####签名验证失败");
                break;
            case "1010":
                if(verifySign(RSAHandler.getPublicKey(rPK(order.getSrc())),order.getSign(),order.getExtend())){
                    System.out.println("1010#####签名验证成功");
                    serverHandler.changeInfo(info);
                }
                else System.out.println("1010####签名验证失败");
                break;
            case "2001":
                if(verifySign(RSAHandler.getPublicKey(rPK(order.getSrc())),order.getSign(),order.getExtend())){
                    System.out.println("2001#####签名验证成功");
                    serverHandler.privateChat(info);
                }
                else System.out.println("2001####签名验证失败");
                break;
            case "2002":
                if(verifySign(RSAHandler.getPublicKey(rPK(order.getSrc())),order.getSign(),order.getExtend())){
                    System.out.println("2002#####签名验证成功");
                    serverHandler.publicChat(info);
                }
                else System.out.println("2002####签名验证失败");
                break;
            default:
                break;
        }
    }
}
