package StateMachine;

import Json.MyJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.statemachine.StateMachine;

//@RestController
@SpringBootApplication
public class Application implements CommandLineRunner {

    @Autowired
    public StateMachine<RegStatusEnum, RegEventEnum> stateMachine;

   @Override
    public void run(String... args)  {
       stateMachine.start();
       stateMachine.sendEvent(RegEventEnum.RECIVE);
       stateMachine.sendEvent(RegEventEnum.HEAD1001);
       stateMachine.sendEvent(RegEventEnum.HANDLEROVER);
       stateMachine.sendEvent(RegEventEnum.RECIVE);
    }

    public static void main(String []args) throws Exception {
       SpringApplication.run(Application.class,args);
    }
}
