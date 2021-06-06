package Framework.CommunicationLayer;

import Framework.CommunicationLayer.Handlers.ClientIdleStateTrigger;
import Framework.CommunicationLayer.Handlers.TestingHandler;
import Framework.CommunicationLayer.Handlers.ReceiveHandler;
import Framework.ServerConfig;
import Framework.SessionLayer.SessionLayer;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.AttributeKey;

public class ReceiveChannelInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        //将本Channel提交到SessionLayer进行注册，并且给这个Channel命名
        ch.attr(AttributeKey.valueOf("channelName")).set(SessionLayer.bindChannelWithTempName(ch));

        ChannelPipeline p = ch.pipeline();
        /*
         * 所有的逻辑操作都在这里完成，操作步骤如下：
         * 1-2 设置超时断开时间，配置时间触发器trigger
         * 3 用户初次登陆时写对照表，写入channel 用户对照表
         */
        p.addLast(new IdleStateHandler(ServerConfig.TIMEOUT,0,0));
        p.addLast(new ClientIdleStateTrigger());
        p.addLast(new ReceiveHandler());
        //p.addLast(new TestingHandler());
    }
}
