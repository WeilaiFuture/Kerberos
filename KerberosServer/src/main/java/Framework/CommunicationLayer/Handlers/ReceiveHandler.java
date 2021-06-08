package Framework.CommunicationLayer.Handlers;

import Framework.SessionLayer.SessionLayer;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.AttributeKey;

public class ReceiveHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println(ctx.toString());
        ctx.channel().attr(AttributeKey.valueOf("channelName")).get();
        SessionLayer.receive((String) ctx.channel().attr(AttributeKey.valueOf("channelName")).get(),msg);
        //传递给下一个testinghandler，仅在测试时有用
        ctx.fireChannelRead(msg);
    }
}
