package Framework;

import Framework.CommunicationLayer.CommunicationLayer;
import Framework.SessionLayer.Handlers.DefaultSessionHandler;
import Framework.SessionLayer.SessionLayer;
import Server.ServerFunction;

public abstract class ServerBuilder {
    protected static CommunicationLayer communicationLayer;
    protected static SessionLayer sessionLayer;

    public static void initializer(CommunicationLayer myCommunicationLayer,SessionLayer mySessionLayer){
        communicationLayer = myCommunicationLayer;
        sessionLayer = mySessionLayer;
    }
    public static void run()throws Exception{
        communicationLayer.run();
    }
    public static void main(String[] args)throws Exception {

        initializer(new CommunicationLayer(1122),new SessionLayer(new DefaultSessionHandler()));
        //initializer(new CommunicationLayer(1122),new SessionLayer(new ServerFunction()));
        run();
    }
}