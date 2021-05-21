package StateMachine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.annotation.OnTransition;
import org.springframework.statemachine.annotation.WithStateMachine;

@Configuration
@WithStateMachine
public class StateMachineEventConfig {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @OnTransition(source = "WAITINFO", target = "HEADOVER")
    public void recive() {
        logger.info("Switch state from W to O");
    }

    @OnTransition(source = "WAITINFO", target = "EXIT")
    public void timeout() {
        logger.info("Switch state from W to E");
    }

    @OnTransition(source = "HEADOVER", target = "HANDLER1001")
    public void head1001() {
        logger.info("Switch state from O to 1001");
    }

    @OnTransition(source = "HEADOVER", target = "HANDLER1002")
    public void head1002() {
        logger.info("Switch state from O to 1002");
    }

    @OnTransition(source = "HEADOVER", target = "HANDLER1003")
    public void head1003() {
        logger.info("Switch state from O to 2");
    }

    @OnTransition(source = "HEADOVER", target = "HANDLER1006")
    public void head1006() {
        logger.info("Switch state from O to 6");
    }

    @OnTransition(source = "HEADOVER", target = "HANDLER1007")
    public void head1007() {
        logger.info("Switch state from O to 7");
    }

    @OnTransition(source = "HEADOVER", target = "EXIT")
    public void head1008() {
        logger.info("Switch state from O to E");
    }

    @OnTransition(source = "HEADOVER", target = "HANDLER1009")
    public void head1009() {
        logger.info("Switch state from O to 9");
    }

    @OnTransition(source = "HEADOVER", target = "HANDLER1010")
    public void head1010() {
        logger.info("Switch state from O to 10");
    }

    @OnTransition(source = "HEADOVER", target = "HANDLER2001")
    public void head2001() {
        logger.info("Switch state from O to 21");
    }

    @OnTransition(source = "HEADOVER", target = "HNDLER2002")
    public void head2002() {
        logger.info("Switch state from O to 22");
    }

    @OnTransition(source = "HANDLER1001", target = "WAITINFO")
    public void over1001() {
        logger.info("Switch state from 1001 to W");
    }

    @OnTransition(source = "HANDLER1002", target = "WAITINFO")
    public void over1002() {
        logger.info("Switch state from 2 to W");
    }

    @OnTransition(source = "HANDLER1003", target = "WAITINFO")
    public void over1003() {
        logger.info("Switch state from 3 to W");
    }

    @OnTransition(source = "HANDLER1006", target = "WAITINFO")
    public void over1006() {
        logger.info("Switch state from 6 to W");
    }

    @OnTransition(source = "HANDLER1007", target = "WAITINFO")
    public void over1007() {
        logger.info("Switch state from 7 to W");
    }

    @OnTransition(source = "HANDLER1009", target = "WAITINFO")
    public void over1009() {
        logger.info("Switch state from 9 to W");
    }

    @OnTransition(source = "HANDLER1010", target = "WAITINFO")
    public void over1010() {
        logger.info("Switch state from 10 to W");
    }

    @OnTransition(source = "HANDLER2001", target = "WAITINFO")
    public void over2001() {
        logger.info("Switch state from 21 to W");
    }

    @OnTransition(source = "HANDLER2002", target = "WAITINFO")
    public void over2002() {
        logger.info("Switch state from 22 to W");
    }

}