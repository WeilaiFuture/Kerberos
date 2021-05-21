package StateMachine;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.statemachine.StateMachine;

//@RestController
@SpringBootApplication
public class Application implements CommandLineRunner {
 //   @GetMapping(value = "/testStateMachine")
    public static void main(String []args){
        SpringApplication.run(Application.class,args);
    }
    @Autowired
    private  StateMachine<RegStatusEnum, RegEventEnum> stateMachine;
    @Override
    public void run(String... args) throws Exception {
        stateMachine.start();
        stateMachine.sendEvent(RegEventEnum.RECIVE);
        stateMachine.sendEvent(RegEventEnum.HEAD1001);
        stateMachine.sendEvent(RegEventEnum.HANDLEROVER);
        stateMachine.sendEvent(RegEventEnum.RECIVE);
        stateMachine.sendEvent(RegEventEnum.HEAD1008);
    }

}
