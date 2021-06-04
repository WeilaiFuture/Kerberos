package Kerberos.TGS;

import Framework.CommunicationLayer.CommunicationLayer;
import Framework.ServerBuilder;
import Framework.SessionLayer.SessionLayer;

public class TGS extends ServerBuilder {
    public static void main(String[] args){
        try{
            initializer(new CommunicationLayer(1122),new SessionLayer(new TGSHandler()));
            run();
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }
}
