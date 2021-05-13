package StateMachine;

import java.util.EnumSet;

import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;

import static StateMachine.RegEventEnum.*;
import static StateMachine.RegStatusEnum.*;


@Configuration
@EnableStateMachine // 开启状态机配置
public class StateMachineConfig extends EnumStateMachineConfigurerAdapter<RegStatusEnum, RegEventEnum>{
    /**
     * 配置状态机状态
     */
    @Override
    public void configure(StateMachineStateConfigurer<RegStatusEnum, RegEventEnum> states) throws Exception {
        states.withStates()
                // 初始化状态机状态
                .initial(WAITINFO)
                // 指定状态机的所有状态
                .states(EnumSet.allOf(RegStatusEnum.class));
    }

    /**
     * 配置状态机状态转换
     */
    @Override
    public void configure(StateMachineTransitionConfigurer<RegStatusEnum, RegEventEnum> transitions) throws Exception {
                // 1.
        transitions.withExternal()
                .source(WAITINFO)
                .target(HEADOVER)
                .event(RECIVE)
                // 2. beginToLogin CONNECTED -> LOGINING
                .source(WAITINFO)
                .target(EXIT)
                .event(TIMEOUT)
                // 2. beginToLogin CONNECTED -> LOGINING
                .and().withExternal()
                .source(HEADOVER)
                .target(HANDLER1001)
                .event(HEAD1001)
                // 3. login failure LOGINING -> UNCONNECTED
                .and().withExternal()
                .source(HEADOVER)
                .target(HANDLER1002)
                .event(HEAD1002)
                // 4. login success LOGINING -> LOGIN_INTO_SYSTEM
                .and().withExternal()
                .source(HEADOVER)
                .target(HANDLER1003)
                .event(HEAD1003)
                // 5. logout LOGIN_INTO_SYSTEM -> UNCONNECTED
                .and().withExternal()
                .source(HEADOVER)
                .target(HANDLER1006)
                .event(HEAD1006)
                // 5. logout LOGIN_INTO_SYSTEM -> UNCONNECTED
                .and().withExternal()
                .source(HEADOVER)
                .target(HANDLER1007)
                .event(HEAD1007)
                // 5. logout LOGIN_INTO_SYSTEM -> UNCONNECTED
                .and().withExternal()
                .source(HEADOVER)
                .target(EXIT)
                .event(HEAD1008)
                // 5. logout LOGIN_INTO_SYSTEM -> UNCONNECTED
                .and().withExternal()
                .source(HEADOVER)
                .target(HANDLER1009)
                .event(HEAD1009)
                // 5. logout LOGIN_INTO_SYSTEM -> UNCONNECTED
                .and().withExternal()
                .source(HEADOVER)
                .target(HANDLER1010)
                .event(HEAD1010)
                // 5. logout LOGIN_INTO_SYSTEM -> UNCONNECTED
                .and().withExternal()
                .source(HEADOVER)
                .target(HANDLER2001)
                .event(HEAD2001)
                // 5. logout LOGIN_INTO_SYSTEM -> UNCONNECTED
                .and().withExternal()
                .source(HEADOVER)
                .target(HANDLER2002)
                .event(HEAD2002)
                // 5. logout LOGIN_INTO_SYSTEM -> UNCONNECTED
                .and().withExternal()
                .source(HANDLER1001)
                .target(WAITINFO)
                .event(HANDLEROVER)
                // 5. logout LOGIN_INTO_SYSTEM -> UNCONNECTED
                .and().withExternal()
                .source(HANDLER1002)
                .target(WAITINFO)
                .event(HANDLEROVER)
                // 5. logout LOGIN_INTO_SYSTEM -> UNCONNECTED
                .and().withExternal()
                .source(HANDLER1003)
                .target(WAITINFO)
                .event(HANDLEROVER)
                // 5. logout LOGIN_INTO_SYSTEM -> UNCONNECTED
                .and().withExternal()
                .source(HANDLER1006)
                .target(WAITINFO)
                .event(HANDLEROVER)
                // 5. logout LOGIN_INTO_SYSTEM -> UNCONNECTED
                .and().withExternal()
                .source(HANDLER1007)
                .target(WAITINFO)
                .event(HANDLEROVER)
                // 5. logout LOGIN_INTO_SYSTEM -> UNCONNECTED
                .and().withExternal()
                .source(HANDLER1009)
                .target(WAITINFO)
                .event(HANDLEROVER)
                // 5. logout LOGIN_INTO_SYSTEM -> UNCONNECTED
                .and().withExternal()
                .source(HANDLER1010)
                .target(WAITINFO)
                .event(HANDLEROVER)
                // 5. logout LOGIN_INTO_SYSTEM -> UNCONNECTED
                .and().withExternal()
                .source(HANDLER2001)
                .target(WAITINFO)
                .event(HANDLEROVER)
                // 5. logout LOGIN_INTO_SYSTEM -> UNCONNECTED
                .and().withExternal()
                .source(HANDLER2002)
                .target(WAITINFO)
                .event(HANDLEROVER);
    }
}