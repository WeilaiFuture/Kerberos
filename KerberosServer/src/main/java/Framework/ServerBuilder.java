package Framework;

import Framework.CommunicationLayer.CommunicationLayer;
import Framework.SessionLayer.Handlers.DefaultSessionHandler;
import Framework.SessionLayer.SessionLayer;
import Server.ServerFunction;

public abstract class ServerBuilder {
    public static CommunicationLayer communicationLayer;
    protected static SessionLayer sessionLayer;

    public static void initializer(CommunicationLayer myCommunicationLayer,SessionLayer mySessionLayer){
        communicationLayer = myCommunicationLayer;
        sessionLayer = mySessionLayer;
    }
    public static void run()throws Exception{
        communicationLayer.runReceive();
        //communicationLayer.runSend("192.168.43.130",10087);
    }
    public static void main(String[] args)throws Exception {

        initializer(new CommunicationLayer(1122),new SessionLayer(new DefaultSessionHandler()));
        run();
    }
}
