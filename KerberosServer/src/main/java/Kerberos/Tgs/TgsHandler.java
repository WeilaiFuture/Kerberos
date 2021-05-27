package Kerberos.Tgs;

import Framework.SessionLayer.Handlers.SessionHandler;

public class TgsHandler extends SessionHandler {
    /*
    1 只有一个的入口函数
    2 在构造的时候需要定下来，构造函数要可以抛出异常
     */

    @Override
    public void receive(String channelName,Object msg) {

    }
}
