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
     @Override
    public void configure(StateMachineConfigurationConfigurer<RegStatusEnum,RegEventEnum> config) throws Exception {
         config
                 .withConfiguration()
                 .machineId("mymachine");
     }
     /**
      * 配置状态机状态转换
      */
    @Override
    public void configure(StateMachineTransitionConfigurer<RegStatusEnum,RegEventEnum> transitions) throws Exception {

        transitions
                // 1.
                .withExternal().source(WAITINFO).target(HEADOVER).event(RECIVE)
                // 2.
                .and().withExternal().source(WAITINFO).target(EXIT).event(TIMEOUT)
                // 3.
                .and().withExternal().source(HEADOVER).target(HANDLER1001).event(HEAD1001)
                // 4.
                .and().withExternal().source(HEADOVER).target(HANDLER1002).event(HEAD1002)
                // 5.
                .and().withExternal().source(HEADOVER).target(HANDLER1003).event(HEAD1003)
                // 6.
                .and().withExternal().source(HEADOVER).target(HANDLER1006).event(HEAD1006)
                // 7.
                .and().withExternal().source(HEADOVER).target(HANDLER1007).event(HEAD1007)
                // 8.
                .and().withExternal().source(HEADOVER).target(EXIT).event(HEAD1008)
                // 9.
                .and().withExternal().source(HEADOVER).target(HANDLER1009).event(HEAD1009)
                // 10.
                .and().withExternal().source(HEADOVER).target(HANDLER1010).event(HEAD1010)
                // 11.
                .and().withExternal().source(HEADOVER).target(HANDLER2001).event(HEAD2001)
                // 12.
                .and().withExternal().source(HEADOVER).target(HANDLER2002).event(HEAD2002)
                // 13.
                .and().withExternal().source(HEADOVER).target(HANDLER1).event(HEAD1)
                // 13.
                .and().withExternal().source(HEADOVER).target(HANDLER2).event(HEAD2)
                // 14.
                .and().withExternal().source(HEADOVER).target(HANDLER7).event(HEAD7);
    }
}