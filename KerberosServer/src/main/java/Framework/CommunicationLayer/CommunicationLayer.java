package Framework.CommunicationLayer;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class CommunicationLayer {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private int port;

    public CommunicationLayer(int port) {
        this.port = port;
    }

    public void runReceive() throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup(); // (1)
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap(); // (2)
            /*
             * 在此处进行超时配置等一系列配置，可能后续还需要修改这部分
             */
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class) // (3)
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    //.option(ChannelOption.SO_TIMEOUT,10)
                    .childHandler(new ReceiveChannelInitializer());

            // Bind and start to accept incoming connections.
            ChannelFuture f = b.bind(port).sync(); // (7)

            logger.info("server bind port:{}", port);

            // Wait until the server socket is closed.
            f.channel().closeFuture().sync();
        } finally {
            // Shut down all event loops to terminate all threads.
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public void runSend(String host,int port){
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new SendChannelInitializer());
            // Start the client.
            ChannelFuture f = b.connect(host, port).sync();

            //logger.info("client connect to host:{}, port:{}", host, port);

            // Wait until the connection is closed.
            f.channel().closeFuture().sync();
        } catch (Exception e){
            e.printStackTrace();
        }
        finally {
            // Shut down the event loop to terminate all threads.
            group.shutdownGracefully();
        }
    }
}
