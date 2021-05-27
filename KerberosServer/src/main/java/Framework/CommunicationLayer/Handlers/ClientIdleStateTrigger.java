package Framework.CommunicationLayer.Handlers;

import Framework.SessionLayer.SessionLayer;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.AttributeKey;

public class ClientIdleStateTrigger extends ChannelInboundHandlerAdapter {
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleState state = ((IdleStateEvent) evt).state();
            if (state == IdleState.READER_IDLE) {
                // 在规定时间内没有收到客户端的上行数据, 主动断开连接
                System.out.println(ctx.channel().attr(AttributeKey.valueOf("channelName")).get() + "超时断开连接");
                SessionLayer.unbindChannel((String) ctx.channel().attr(AttributeKey.valueOf("channelName")).get());

                //发送超时信息，断开连接
                /********这里定义超时时，应该向client发送的信息*******/

                String response = "";
                ChannelFuture future = ctx.writeAndFlush(response);

                //使用Listener以保证线程安全
                future.addListener(ChannelFutureListener.CLOSE);
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }
}
