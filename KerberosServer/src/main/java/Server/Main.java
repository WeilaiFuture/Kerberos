package Server;
import Framework.CommunicationLayer.CommunicationLayer;
import Framework.SessionLayer.SessionLayer;
import Framework.SeverBuilder;

public class Main extends SeverBuilder {
    public static void main(String[] args) throws Exception {
        initializer(new CommunicationLayer(1122),new SessionLayer(new ServerFunction()));
        run();
    }
}
