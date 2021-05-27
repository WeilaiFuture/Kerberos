package Server;

import Framework.SessionLayer.Handlers.SessionHandler;

public class ServerFunction extends SessionHandler {
    //继承方法集
    ServerFunction(){
        //调用函数，发送本地证书给AS
    }
    public boolean Integer(String message){
        /*
        判断消息头部，转到相应的处理函数；
        这里应使用状态机；
        原来为等待连接状态；
        调用这个函数进入到等待报文状态和报文处理状态；
        直到用户登出，转为等待连接状态。
         */

        return true;
    }

    @Override
    public void receive(String channelName,Object msg) {

    }
}
