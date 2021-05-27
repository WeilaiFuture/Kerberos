package Framework.SessionLayer;

import Framework.SessionLayer.Handlers.SessionHandler;
import io.netty.channel.Channel;
import io.netty.util.AttributeKey;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

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
    static private SessionHandler sessionHandler;
    static public ConcurrentHashMap<String, Channel> channelMap = new ConcurrentHashMap<>();
    //static public ConcurrentHashMap<String, List<Object>> offlineMsgQueue = new ConcurrentHashMap<>();
    public SessionLayer(SessionHandler sessionHandler){
        this.sessionHandler = sessionHandler;
    }
    private static OfflineMsgQueue offlineMsgQueue = new OfflineMsgQueue();
    static public void send(String userName,Object msg){
        /*
         * 三种情况：
         * 1 用户在线，直接发送
         * 2 用户不在线，在离线队列中有用户（不是第一个离线信息）
         * 3 用户不在线，离线队列中没有用户（第一条离线信息）
         */
        if(channelMap.get(userName) != null){
            channelMap.get(userName).write(msg);
        }
        else{
            offlineMsgQueue.write(userName,msg);
        }
    }
    static public void receive(Object msg){
        /*
         * 针对已经连接服务器但是没有登录的用户，会先生成一个临时ID，登录后临时ID改为用户ID
         * 临时ID改用户ID的过程在login中完成
         */
        sessionHandler.receive(msg);
    }
    static public void logOut(String userName){
        Channel channel = channelMap.get(userName);
        channel.close();
        channelMap.remove(userName);
    }
    static public String bindChannelWithTempName(Channel channel){
        /*
         * 本函数仅在InBoundHandler中被调用，并且仅在一个channel被建立时被调用
         * 用于将channelname和channel存在map中形成映射关系
         */
        //给channel随机生成一个channelName并查重
        String channelName = RandomStringUtils.randomAlphanumeric(10);
        channelMap.put(channelName,channel);
        return channelName;
    }

    static public void unbindChannel(String channelName){
        /*
         * 本函数仅在channel被释放时被调用，用于将channelname与channel的映射关系删除
         */
        channelMap.remove(channelName);
    }
    static public void bindUserNameWithUserName(String tempName,String userName){
        /*
         * 当用户在业务逻辑层面完成登录时，应该调用本函数将用户对应的channelname与
         * username绑定，之后就可以通过
         */
        Channel channel =  channelMap.get(tempName);
        channelMap.remove(tempName);
        channel.attr(AttributeKey.valueOf("channelName")).set(tempName);
        channelMap.put(userName,channel);
        /*
         * 绑定完成后，立刻检查有没有离线队列
         */
        List<Object> offlineMsgList = offlineMsgQueue.read(userName);
        if(offlineMsgList != null){
            for (Object msg:offlineMsgList) {
                channel.writeAndFlush(msg);
            }
        }
    }
}
