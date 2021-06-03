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

import javax.swing.*;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

import static Framework.SessionLayer.SessionLayer.bindChannelWithUserName;
import static Json.MyJson.OrderToString;
import static Json.MyJson.StringToOrder;
import static StateMachine.RegEventEnum.RECIVE;
import static UI.log.add;
import static UI.log.createTable;

//@RestController
@SpringBootApplication
public class Application implements CommandLineRunner {

    //实例化一个状态机工厂
    @Autowired
    @Qualifier("StateMachine")
    public StateMachineFactory<RegStatusEnum,RegEventEnum> stateMachineFactory;
   @Override
    public void run(String... args)  {
       String channelName=args[0];
       String info=args[1];
       //解析报文头部
       MyJson.Order order=StringToOrder(info);
       String head="HEAD"+order.getMsgType();
       //判断绑定
       if(!channelName.equals(order.getSrc())){
    //       bindChannelWithUserName(channelName,order.getSrc());
       }
       //实例化一个状态机
       StateMachine<RegStatusEnum,RegEventEnum> stateMachine=stateMachineFactory.getStateMachine();

       //状态机启动
       stateMachine.start();

       //RECIVE事件，传递message
       Message<RegEventEnum> message = MessageBuilder.withPayload(RECIVE).setHeader("order",info).build();
       stateMachine.sendEvent(message);


       //进行处理
       stateMachine.sendEvent(RegEventEnum.valueOf(head));

       //结束
       stateMachine.stop();
    }

    public static void main(String []args) throws InterruptedException {

        MyStruct struct=new MyStruct();
        struct.my_k=new MyStruct.My_k();
        struct.my_k.setKey("1");
        String json=MyJson.StructToString(struct);
        MyStruct struct1=MyJson.StringToStruct(json);
        MyJson.Order order=new MyJson.Order();
        order.setMsgType("1001");
        String c="1";
        String info=OrderToString(order);
        String []s={c,info};
        SpringApplication.run(Application.class,s);

 /*       JTable table = createTable();
        TimeUnit.SECONDS.sleep(1);
        LinkedList<String[]> list = new LinkedList<String[]>();
        for (int i = 0; i < 10; i++) {
            String[] s = new String[4];
            s[0] = i + "0";
            s[1] = i + "1";
            s[2] = i + "2";
            s[3] = i + "3";
            list.addFirst(s);
            add(table, list);
            TimeUnit.SECONDS.sleep(1);
        }
 */   }
}
