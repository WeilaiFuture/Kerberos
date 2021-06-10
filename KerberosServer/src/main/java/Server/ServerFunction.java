package Server;


import Framework.SessionLayer.Handlers.SessionHandler;
import Framework.SessionLayer.SessionLayer;
import Json.MyJson;
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
import static Server.ServerDataBase.con;
import static Server.ServerDataBase.connectData;
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
    JTable table = createTable();

    LinkedList<String[]> list = new LinkedList<String[]>();
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
        String []s=new String[5];
        list.addFirst(s);
        s[0]=order.getSrc();//源
        s[1]=order.getDst();//目的
        s[2]=order.getMsgType();//报文类型
        s[3]=order.getExtend();//密文
        s[4]=order.getExtend();//明文
        add(table, list);
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
                serverHandler.login(info);
                break;
            case "1003":
                serverHandler.searchFriendList(info);
                break;
            case "1005":
                serverHandler.hello(info);
                logIn(order.getSrc());
                break;
            case "1006":
                serverHandler.heart(info);
                break;
            case "1007":
                serverHandler.searchID(info);
                break;
            case "1008":
                serverHandler.logout(info);
                logOut(order.getSrc());
                break;
            case "1009":
                serverHandler.information(info);
                break;
            case "1010":
                serverHandler.changeInfo(info);
                break;
            case "2001":
                serverHandler.privateChat(info);
                break;
            case "2002":
                serverHandler.publicChat(info);
                break;
            default:
                break;
        }
    }
}
