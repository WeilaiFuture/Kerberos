package Kerberos.AS;

import Framework.CommunicationLayer.CommunicationLayer;
import Framework.ServerBuilder;
import Framework.SessionLayer.Handlers.DefaultSessionHandler;
import Framework.SessionLayer.SessionLayer;

public class AS extends ServerBuilder {
    public static void main(String[] args) {

        try{
            initializer(new CommunicationLayer(10087),new SessionLayer(new ASHandler()));
            run();
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }
}
