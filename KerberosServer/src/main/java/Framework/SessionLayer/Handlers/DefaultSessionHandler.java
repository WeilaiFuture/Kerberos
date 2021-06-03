package Framework.SessionLayer.Handlers;

import Json.MyJson;
import SecurityUtils.DESHandler;
import io.netty.buffer.ByteBuf;

public class DefaultSessionHandler extends SessionHandler{
    @Override
    public void receive(String channelName,Object msg) {
        System.out.println("Server: " + (String)msg);
        //String res = DESHandler.DecryptDES(request,"12345678");
        MyJson.Order order=MyJson.StringToOrder((String) msg);
        String res = DESHandler.DecryptDES(order.getExtend(),"12345678");
        System.out.println(res);
    }
}
