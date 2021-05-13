package StateMachine;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.annotation.OnTransition;
import org.springframework.statemachine.annotation.WithStateMachine;

@Configuration
@WithStateMachine
public class StateMachineEventConfig {

    @OnTransition(source = "UNCONNECTED", target = "CONNECTED")
    public void connect() {
        System.out.println("Switch state from UNCONNECTED to CONNECTED: connect");
    }

    @OnTransition(source = "CONNECTED", target = "LOGINING")
    public void beginToLogin() {
        System.out.println("Switch state from CONNECTED to LOGINING: beginToLogin");
    }

    @OnTransition(source = "LOGINING", target = "LOGIN_INTO_SYSTEM")
    public void loginSuccess() {
        System.out.println("Switch state from LOGINING to LOGIN_INTO_SYSTEM: loginSuccess");
    }

    @OnTransition(source = "LOGINING", target = "UNCONNECTED")
    public void loginFailure() {
        System.out.println("Switch state from LOGINING to UNCONNECTED: loginFailure");
    }

    @OnTransition(source = "LOGIN_INTO_SYSTEM", target = "UNCONNECTED")
    public void logout()
    {
        System.out.println("Switch state from LOGIN_INTO_SYSTEM to UNCONNECTED: logout");
    }
}