package Framework;

import Framework.CommunicationLayer.CommunicationLayer;

public class Main {

    public static void main(String[] args)throws Exception {

        new CommunicationLayer(12345).run();
    }
}
