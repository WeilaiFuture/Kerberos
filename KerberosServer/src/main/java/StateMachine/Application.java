package StateMachine;

import Json.MyJson;
import Json.MyStruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;

import static StateMachine.RegEventEnum.RECIVE;

//@RestController
@SpringBootApplication
public class Application implements CommandLineRunner {

    public static Message<RegEventEnum> message;

    @Autowired
    @Qualifier("StateMachine")
    public StateMachineFactory<RegStatusEnum,RegEventEnum> stateMachineFactory;
 //   @Autowired
   // @Qualifier("StateMachine")
 //   public StateMachine<RegStatusEnum,RegEventEnum> stateMachine;
   @Override
    public void run(String... args)  {
       StateMachine<RegStatusEnum,RegEventEnum> stateMachine=stateMachineFactory.getStateMachine("myMachine");
       System.out.println(stateMachine.getId());
       stateMachine.start();

       String s="123";
       message = MessageBuilder.withPayload(RECIVE).setHeader("order",s).build();
       stateMachine.sendEvent(message);
    //   stateMachine.sendEvent(RegEventEnum.valueOf("RECIVE"));
       stateMachine.sendEvent(RegEventEnum.valueOf("HEAD1001"));
       stateMachine.sendEvent(RegEventEnum.valueOf("HANDLEROVER"));

       StateMachine<RegStatusEnum,RegEventEnum> stateMachine1=stateMachineFactory.getStateMachine("myMachine1");
       System.out.println(stateMachine1.getId());
       stateMachine1.start();
       stateMachine1.sendEvent(RegEventEnum.valueOf("RECIVE"));

       stateMachine.sendEvent(RegEventEnum.valueOf("RECIVE"));

       stateMachine1.stop();
    }

    public static void main(String []args) {
       SpringApplication.run(Application.class,args);

        MyStruct struct=new MyStruct();
        struct.my_k=new MyStruct.My_k();
        struct.my_k.setKey("1");
        String json=MyJson.StructToString(struct);
        MyStruct struct1=MyJson.StringToStruct(json);

    }
}
