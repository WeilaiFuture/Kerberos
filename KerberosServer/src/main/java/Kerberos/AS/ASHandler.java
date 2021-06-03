package Kerberos.AS;

import Framework.SessionLayer.Handlers.SessionHandler;
import Json.MyJson;
import SecurityUtils.DESHandler;

public class ASHandler extends SessionHandler {
    /*
    1 只有一个的入口函数
    2 在构造的时候需要定下来，构造函数要可以抛出异常
     */

    @Override
    public void receive(String channelName,Object msg) {
        /*
         * AS收到两种报文，一种是证书报文
         * 另一种是认证报文
         */
        MyJson.Order order=MyJson.StringToOrder((String) msg);
        order.getMsgType();
        String res = DESHandler.DecryptDES(order.getExtend(),"12345678");
    }
}
