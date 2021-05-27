package Framework.Testing;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EchoClientHandler2 extends ChannelInboundHandlerAdapter{
    private Logger logger = LoggerFactory.getLogger(getClass());
    private final ByteBuf firstMessage;

    public EchoClientHandler2(){
        byte[] req = "Hello from client".getBytes();
        firstMessage = Unpooled.buffer(req.length);
        firstMessage.writeBytes(req);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        logger.info("client channel active");
        // Send the message to Server
        logger.info("client send req...");
        ctx.writeAndFlush(firstMessage);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) { // (2)
        System.out.println("From Handler2!" + msg);
        /*String res = (String) msg;
        try {
            System.out.println(res);
        }catch (Exception e){
            e.printStackTrace();
        }*/
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) { // (4)
        // Close the connection when an exception is raised.
        logger.error("client caught exception", cause);
        ctx.close();
    }
}

