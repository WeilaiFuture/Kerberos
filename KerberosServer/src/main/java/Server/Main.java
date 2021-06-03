package Server;
import Framework.CommunicationLayer.CommunicationLayer;
import Framework.SessionLayer.SessionLayer;
import Framework.ServerBuilder;

public class Main extends ServerBuilder {
    public static void main(String[] args) throws Exception {
        initializer(new CommunicationLayer(1122),new SessionLayer(new ServerFunction()));
        run();
    }
}
