package Framework.Testing;

import Framework.CommunicationLayer.CommunicationLayer;

public class Sever {

    public static void main(String[] args)throws Exception {

        new CommunicationLayer(12345).run();
    }
}
