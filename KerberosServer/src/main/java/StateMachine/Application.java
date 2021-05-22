package StateMachine;

import Json.MyJson;
import Json.MyStruct;
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

    public static void main(String []args) {
       SpringApplication.run(Application.class,args);

        MyStruct struct=new MyStruct();
        struct.my_k=new MyStruct.My_k();
        struct.my_k.setKey("1");
        String json=MyJson.StructToString(struct);
        MyStruct struct1=MyJson.StringToStruct(json);

    }
}
