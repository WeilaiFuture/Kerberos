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

import static Json.MyJson.OrderToString;
import static Json.MyJson.StringToOrder;
import static StateMachine.RegEventEnum.RECIVE;

//@RestController
@SpringBootApplication
public class Application implements CommandLineRunner {

    //实例化一个状态机工厂
    @Autowired
    @Qualifier("StateMachine")
    public StateMachineFactory<RegStatusEnum,RegEventEnum> stateMachineFactory;
   @Override
    public void run(String... args)  {
       String info=args[0];
       //实例化一个状态机
       StateMachine<RegStatusEnum,RegEventEnum> stateMachine=stateMachineFactory.getStateMachine();

       //状态机启动
       stateMachine.start();

       //RECIVE事件，传递message
       Message<RegEventEnum> message = MessageBuilder.withPayload(RECIVE).setHeader("order",info).build();
       stateMachine.sendEvent(message);

       //解析报文头部
       MyJson.Order order=StringToOrder(info);
       String head="HEAD"+order.getMsgType();

       //进行处理
       stateMachine.sendEvent(RegEventEnum.valueOf(head));

       //结束
       stateMachine.stop();
    }

    public static void main(String []args) {

        MyStruct struct=new MyStruct();
        struct.my_k=new MyStruct.My_k();
        struct.my_k.setKey("1");
        String json=MyJson.StructToString(struct);
        MyStruct struct1=MyJson.StringToStruct(json);
        MyJson.Order order=new MyJson.Order();
        order.setMsgType("1001");
        String info=OrderToString(order);
        SpringApplication.run(Application.class,info);
    }
}
