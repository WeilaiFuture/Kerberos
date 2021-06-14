package Framework.SessionLayer;

import Framework.ServerBuilder;
import Framework.SessionLayer.Exceptions.SessionLayerNotInitialized;
import Framework.SessionLayer.Handlers.SessionHandler;
import Framework.Testing.EchoClientHandler;
import Framework.Testing.EchoClientHandler2;
import Json.MyJson;
import SecurityUtils.DESHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;
import org.apache.commons.lang3.RandomStringUtils;

import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import static Json.MyJson.StringToOrder;
import static Server.ServerDataBase.rKcv;
import static UI.log.add;
import static UI.log.createTable;

/*
 * 本类要存储用户登录状态，屏蔽channel的相关细节，因此要么用单例，要么就是：
 * 静态变量 + 静态方法，这里选择静态变量 + 静态方法模式
 *
 * 本类有两个方法，一个成员变量：
 * 方法：receive
 * netty inbound handler 的延伸，在inbound handler中被调用，会调用
 * 以SessionHandler为基类的后续处理类，SessionHandler会派生出两种类，
 * 一种是直接处理然后调用send方法的简单SessionHandler，一种是会使用
 * Kafka的复杂SessionHandler。
 * 方法：send
 * netty outbound handler 的触发方法，在SessionHandler中被调用
 */
public class SessionLayer {
    static private SessionHandler sessionHandler = null;
    static public ConcurrentHashMap<String, Channel> channelMap = new ConcurrentHashMap<>();
    //static public ConcurrentHashMap<String, List<Object>> offlineMsgQueue = new ConcurrentHashMap<>();
    public SessionLayer(SessionHandler sessionHandler){
        this.sessionHandler = sessionHandler;
    }
    private static OfflineMsgQueue offlineMsgQueue = new OfflineMsgQueue();
    //static private ByteBuf msgBuf;


    static public void checkInitStatus(){
        if(sessionHandler == null) {
            throw new SessionLayerNotInitialized();
        }
    }

    static public Object sendByAddress(String host,int port,Object msg){
        /*byte[] response = ((String)msg).getBytes();
        ByteBuf msgBuf = Unpooled.buffer(response.length);
        msgBuf.writeBytes(response);
        ServerBuilder.communicationLayer.runSend(host,port);
        msgBuf.clear();*/
        try {
            // 和服务器创建连接
            Socket socket = new Socket(host,port);

            // 要发送给服务器的信息
            OutputStream os = socket.getOutputStream();
            PrintWriter pw = new PrintWriter(os);
            pw.write((String) msg);
            pw.flush();

            socket.shutdownOutput();

            // 从服务器接收的信息
            InputStream is = socket.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            String receive =br.readLine();

            /*br.readLine();
            while((temp = br.readLine())!=null){
                receive += temp;
            }*/
            br.close();
            is.close();
            os.close();
            pw.close();
            socket.close();
            return receive;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    static public void send(String userName,Object msg){
        /*
         * 三种情况：
         * 1 用户在线，直接发送
         * 2 用户不在线，在离线队列中有用户（不是第一个离线信息）
         * 3 用户不在线，离线队列中没有用户（第一条离线信息）
         */
//        try {
//            TimeUnit.SECONDS.sleep(1);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        checkInitStatus();
        if(channelMap.get(userName) != null){
            byte[] response = ((String)msg).getBytes();
            ByteBuf msgBuf = Unpooled.buffer(response.length);
            msgBuf.writeBytes(response);
            System.out.println("向"+ userName + "发送信息");
            channelMap.get(userName).writeAndFlush(msgBuf);
        }
        else{
            //获取报文
            MyJson.Order order =MyJson.StringToOrder((String)msg);
            //解密操作
            String kcv=rKcv(order.getDst());
            System.out.println("#####存入离线消息解密前"+order.getExtend());
            order.setExtend(DESHandler.DecryptDES(order.getExtend(),kcv));
            System.out.println("#####离线消息解密后"+order.getExtend());
            //获取消息
            msg=MyJson.OrderToString(order);
            offlineMsgQueue.write(userName,msg);
            System.out.println("############存入离线队列"+(String)msg);
        }
    }
    static public void receive(String channelName,Object msg){
        /*
         * 针对已经连接服务器但是没有登录的用户，会先生成一个临时ID，登录后临时ID改为用户ID
         * 临时ID改用户ID的过程在login中完成
         */
        checkInitStatus();
        ByteBuf buf = (ByteBuf)msg;
        byte[] data = new byte[buf.readableBytes()];
        buf.readBytes(data);
        try {
            String receive = new String(data, "utf-8");
            sessionHandler.receive(channelName,receive);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    static public void logIn(String userName)throws Exception{
        checkInitStatus();
        /*
         * 检查绑定结果
         */
 /*
        Channel channel =  channelMap.get(tempName);

        if(channel == null){
            throw new Exception();
        }
       bindChannelWithUserName(tempName,userName);
 */
        /*
         * 绑定完成后，立刻检查有没有离线队列
         */

        List<Object> offlineMsgList = offlineMsgQueue.read(userName);
        if(offlineMsgList != null){
            for (Object msg:offlineMsgList) {
                send(userName,msg);
                System.out.println("#####发送离线消息"+msg);
            }
        }
    }
    static public void logOut(String userName)throws Exception{
        checkInitStatus();
        Channel channel = channelMap.get(userName);
        if(channel == null){
            throw new Exception();
        }
        channel.close();
        channelMap.remove(userName);
    }

/*
 * 以下函数不应该在SessionLayer以上的层级中被调用
 */
    static public String bindChannelWithTempName(Channel channel){
        /*
         * 本函数仅在InBoundHandler中被调用，并且仅在一个channel被建立时被调用
         * 用于将channelname和channel存在map中形成映射关系
         */
        //给channel随机生成一个channelName并查重
        checkInitStatus();
        String channelName;
        do{
            channelName = RandomStringUtils.randomAlphanumeric(10);
        }while (channelMap.get(channel) != null);


        channelMap.put(channelName,channel);
        return channelName;
    }

    static public void unbindChannel(String channelName){
        /*
         * 本函数仅在channel被释放时被调用，用于将channelname与channel的映射关系删除
         */
        checkInitStatus();
        channelMap.remove(channelName);
    }
    static public void bindChannelWithUserName(String tempName,String userName){
        /*
         * 当用户在业务逻辑层面完成登录时，应该调用本函数将用户对应的channelname与
         * username绑定，之后就可以通过
         */
        checkInitStatus();
        Channel channel =  channelMap.get(tempName);
        channelMap.remove(tempName);
        channel.attr(AttributeKey.valueOf("channelName")).set(userName);
        channelMap.put(userName,channel);
    }
}
