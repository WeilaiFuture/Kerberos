package StateMachine;

import java.util.EnumSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.listener.StateMachineListener;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.transition.Transition;

import static StateMachine.RegEventEnum.*;
import static StateMachine.RegStatusEnum.*;


@Configuration
@EnableStateMachineFactory(name="StateMachine") // 开启状态机配置
public class StateMachineConfig extends EnumStateMachineConfigurerAdapter<RegStatusEnum,RegEventEnum> {
    /**
     * 配置状态机状态
     */
    /**状态机ID*/
   // public static final String StateMachineId = "StateMachineId";
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Override
    public void configure(StateMachineStateConfigurer<RegStatusEnum,RegEventEnum> states) throws Exception {
        states.withStates()
                // 初始化状态机状态
                .initial(WAITINFO)
                // 指定状态机的所有状态
                .states(EnumSet.allOf(RegStatusEnum.class));
    }
    /* @Override
  *  public void configure(StateMachineConfigurationConfigurer<RegStatusEnum,RegEventEnum> config) throws Exception {
         config
                 .withConfiguration()
                 .machineId("mymachine");
     }
  */   /**
      * 配置状态机状态转换
      */
    @Override
    public void configure(StateMachineTransitionConfigurer<RegStatusEnum,RegEventEnum> transitions) throws Exception {
                // 1.
        transitions
                .withExternal().source(WAITINFO).target(HEADOVER).event(RECIVE).action(recive(Application.message))
                // 2.
                .and().withExternal().source(WAITINFO).target(EXIT).event(TIMEOUT).action(timeout())
                // 3.
                .and().withExternal().source(HEADOVER).target(HANDLER1001).event(HEAD1001).action(head1001())
                // 4.
                .and().withExternal().source(HEADOVER).target(HANDLER1002).event(HEAD1002).action(head1002())
                // 5.
                .and().withExternal().source(HEADOVER).target(HANDLER1003).event(HEAD1003).action(head1003())
                // 6.
                .and().withExternal().source(HEADOVER).target(HANDLER1006).event(HEAD1006).action(head1006())
                // 7.
                .and().withExternal().source(HEADOVER).target(HANDLER1007).event(HEAD1007).action(head1007())
                // 8.
                .and().withExternal().source(HEADOVER).target(EXIT).event(HEAD1008).action(head1008())
                // 9.
                .and().withExternal().source(HEADOVER).target(HANDLER1009).event(HEAD1009).action(head1009())
                // 10.
                .and().withExternal().source(HEADOVER).target(HANDLER1010).event(HEAD1010).action(head1010())
                // 11.
                .and().withExternal().source(HEADOVER).target(HANDLER2001).event(HEAD2001).action(head2001())
                // 12.
                .and().withExternal().source(HEADOVER).target(HANDLER2002).event(HEAD2002).action(head2002())
                // 13.
                .and().withExternal().source(HANDLER1001).target(WAITINFO).event(HANDLEROVER).action(handlerover())
                // 14.
                .and().withExternal().source(HANDLER1002).target(WAITINFO).event(HANDLEROVER).action(handlerover())
                // 15.
                .and().withExternal().source(HANDLER1003).target(WAITINFO).event(HANDLEROVER).action(handlerover())
                // 16.
                .and().withExternal().source(HANDLER1006).target(WAITINFO).event(HANDLEROVER).action(handlerover())
                // 17.
                .and().withExternal().source(HANDLER1007).target(WAITINFO).event(HANDLEROVER).action(handlerover())
                // 18.
                .and().withExternal().source(HANDLER1009).target(WAITINFO).event(HANDLEROVER).action(handlerover())
                // 19.
                .and().withExternal().source(HANDLER1010).target(WAITINFO).event(HANDLEROVER).action(handlerover())
                // 20.
                .and().withExternal().source(HANDLER2001).target(WAITINFO).event(HANDLEROVER).action(handlerover())
                // 21.
                .and().withExternal().source(HANDLER2002).target(WAITINFO).event(HANDLEROVER).action(handlerover());
    }
    
    public Action<RegStatusEnum,RegEventEnum> recive(Message<RegEventEnum> message) {
        return new Action<RegStatusEnum,RegEventEnum>() {
            @Override
            public void execute(StateContext<RegStatusEnum,RegEventEnum> context) {
                // do something
             //   System.out.println("传递的参数：" + message.getHeaders().get("order"));
                logger.info("Switch state from WAITINFO to HEADOVER");
            }
        };
    }

    public Action<RegStatusEnum,RegEventEnum> handlerover() {
        return new Action<RegStatusEnum,RegEventEnum>() {
            @Override
            public void execute(StateContext<RegStatusEnum,RegEventEnum> context) {
                // do something
                logger.info("Switch state from WAITINFO to HEADOVER");
            }
        };
    }

    public Action<RegStatusEnum,RegEventEnum> timeout() {
        return new Action<RegStatusEnum,RegEventEnum>() {
            @Override
            public void execute(StateContext<RegStatusEnum,RegEventEnum> context) {
                // do something
                logger.info("Switch state from WAITINFO to EXIT");
            }
        };
    }

    public Action<RegStatusEnum,RegEventEnum> head1001() {
        return new Action<RegStatusEnum,RegEventEnum>() {
            @Override
            public void execute(StateContext<RegStatusEnum,RegEventEnum> context) {
                // do something
                logger.info("Switch state from HEADOVER to 1001");
            }
        };
    }

    public Action<RegStatusEnum,RegEventEnum> head1002() {
        return new Action<RegStatusEnum,RegEventEnum>() {
            @Override
            public void execute(StateContext<RegStatusEnum,RegEventEnum> context) {
                // do something
                logger.info("Switch state from HEADOVER to 1002");
            }
        };
    }

    public Action<RegStatusEnum,RegEventEnum> head1003() {
        return new Action<RegStatusEnum,RegEventEnum>() {
            @Override
            public void execute(StateContext<RegStatusEnum,RegEventEnum> context) {
                // do something
                logger.info("Switch state from HEADOVER to 1003");
            }
        };
    }

    public Action<RegStatusEnum,RegEventEnum> head1006() {
        return new Action<RegStatusEnum,RegEventEnum>() {
            @Override
            public void execute(StateContext<RegStatusEnum,RegEventEnum> context) {
                // do something
                logger.info("Switch state from HEADOVER to 1006");
            }
        };
    }

    public Action<RegStatusEnum,RegEventEnum> head1007() {
        return new Action<RegStatusEnum,RegEventEnum>() {
            @Override
            public void execute(StateContext<RegStatusEnum,RegEventEnum> context) {
                // do something
                logger.info("Switch state from HEADOVER to 1007");
            }
        };
    }

    public Action<RegStatusEnum,RegEventEnum> head1008() {
        return new Action<RegStatusEnum,RegEventEnum>() {
            @Override
            public void execute(StateContext<RegStatusEnum,RegEventEnum> context) {
                // do something
                logger.info("Switch state from HEADOVER to EXIT");
            }
        };
    }@Bean
    public Action<RegStatusEnum,RegEventEnum> head1009() {
        return new Action<RegStatusEnum,RegEventEnum>() {
            @Override
            public void execute(StateContext<RegStatusEnum,RegEventEnum> context) {
                // do something
                logger.info("Switch state from HEADOVER to 1009");
            }
        };
    }@Bean
    public Action<RegStatusEnum,RegEventEnum> head1010() {
        return new Action<RegStatusEnum,RegEventEnum>() {
            @Override
            public void execute(StateContext<RegStatusEnum,RegEventEnum> context) {
                // do something
                logger.info("Switch state from HEADOVER to 1010");
            }
        };
    }

    public Action<RegStatusEnum,RegEventEnum> head2001() {
        return new Action<RegStatusEnum,RegEventEnum>() {
            @Override
            public void execute(StateContext<RegStatusEnum,RegEventEnum> context) {
                // do something
                logger.info("Switch state from HEADOVER to 2001");
            }
        };
    }

    public Action<RegStatusEnum,RegEventEnum> head2002() {
        return new Action<RegStatusEnum,RegEventEnum>() {
            @Override
            public void execute(StateContext<RegStatusEnum,RegEventEnum> context) {
                // do something
                logger.info("Switch state from HEADOVER to 2002");
            }
        };
    }


}