package Framework.CommunicationLayer.Handlers;

import Framework.SessionLayer.SessionLayer;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ReceiveHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        SessionLayer.receive(msg);
        //传递给下一个testinghandler，仅在测试时有用
        ctx.fireChannelRead(msg);
    }
}
