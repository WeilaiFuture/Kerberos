package StateMachine;
import Server.ServerHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.statemachine.annotation.OnTransition;
import org.springframework.statemachine.annotation.WithStateMachine;

@Configuration
@WithStateMachine(id = "mymachine")
public class StateMachineEventConfig {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @OnTransition(source = "WAITINFO", target = "HEADOVER")
    public void recive(Message<RegEventEnum> message) {
        logger.info("Switch state from W to O");
        logger.info("传递的参数：" + message.getHeaders().get("order"));
    }

    @OnTransition(source = "WAITINFO", target = "EXIT")
    public void timeout(Message<RegEventEnum> message) {

        logger.info("Switch state from W to E");
    }
    //证书
    @OnTransition(source = "HEADOVER", target = "HANDLER1")
    public void head1(Message<RegEventEnum> message) {
        ServerHandler.Certif(message.getHeaders().get("order").toString());
        logger.info("Switch state from O to 1");
    }
    //Kv
    @OnTransition(source = "HEADOVER", target = "HANDLER2")
    public void head2(Message<RegEventEnum> message) {
        ServerHandler.Kv(message.getHeaders().get("order").toString());
        logger.info("Switch state from O to 2");
    }
    //Kcv
    @OnTransition(source = "HEADOVER", target = "HANDLER7")
    public void head7(Message<RegEventEnum> message) {
        ServerHandler.Kcv(message.getHeaders().get("order").toString());
        logger.info("Switch state from O to 7");
    }
    //登录
    @OnTransition(source = "HEADOVER", target = "HANDLER1002")
    public void head1002(Message<RegEventEnum> message) throws Exception {
        ServerHandler.login(message.getHeaders().get("order").toString());
        logger.info("Switch state from O to 1002");
    }

    //请求好友界面
    @OnTransition(source = "HEADOVER", target = "HANDLER1003")
    public void head1003(Message<RegEventEnum> message) throws Exception {
        ServerHandler.searchFriendList(message.getHeaders().get("order").toString());
        logger.info("Switch state from O to 2");
    }

    //问好
    @OnTransition(source = "HEADOVER", target = "HANDLER1005")
    public void head1001(Message<RegEventEnum> message) throws Exception {
        ServerHandler.hello(message.getHeaders().get("order").toString());
        logger.info("Switch state from O to 1005");
    }

    //心跳
    @OnTransition(source = "HEADOVER", target = "HANDLER1006")
    public void head1006(Message<RegEventEnum> message) {
        logger.info("Switch state from O to 6");
    }

    //查找ID
    @OnTransition(source = "HEADOVER", target = "HANDLER1007")
    public void head1007(Message<RegEventEnum> message) throws Exception {
        ServerHandler.searchID(message.getHeaders().get("order").toString());
        logger.info("Switch state from O to 7");
    }
    //登出
    @OnTransition(source = "HEADOVER", target = "EXIT")
    public void head1008(Message<RegEventEnum> message) throws Exception {
        ServerHandler.logout(message.getHeaders().get("order").toString());
        logger.info("Switch state from O to E");
    }

    //个人信息
    @OnTransition(source = "HEADOVER", target = "HANDLER1009")
    public void head1009(Message<RegEventEnum> message) throws Exception {
        ServerHandler.information(message.getHeaders().get("order").toString());
        logger.info("Switch state from O to 9");
    }

    //修改信息
    @OnTransition(source = "HEADOVER", target = "HANDLER1010")
    public void head1010(Message<RegEventEnum> message) throws Exception {
        ServerHandler.changeInfo(message.getHeaders().get("order").toString());
        logger.info("Switch state from O to 10");
    }

    //单聊信息
    @OnTransition(source = "HEADOVER", target = "HANDLER2001")
    public void head2001(Message<RegEventEnum> message) throws Exception {
        ServerHandler.privateChat(message.getHeaders().get("order").toString());
        logger.info("Switch state from O to 21");
    }

    //群聊信息
    @OnTransition(source = "HEADOVER", target = "HNDLER2002")
    public void head2002(Message<RegEventEnum> message) throws Exception {
        ServerHandler.publicChat(message.getHeaders().get("order").toString());
        logger.info("Switch state from O to 22");
    }

}