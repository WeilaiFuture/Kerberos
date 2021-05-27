package Framework;

import Framework.CommunicationLayer.CommunicationLayer;
import Framework.SessionLayer.Handlers.DefaultSessionHandler;
import Framework.SessionLayer.SessionLayer;

public abstract class SeverBuilder {
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
        run();
    }
}
