package StateMachine;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.persist.StateMachinePersister;

import java.util.Map;
import java.util.UUID;

//@Service
public class StateMachineService {

   // @Autowired
    private StateMachinePersister<RegStatusEnum, RegEventEnum, Integer> stateMachinePersist;
   // @Autowired
    private StateMachineFactory<RegStatusEnum, RegEventEnum> stateMachineFactory;

    public void execute(Integer businessId, RegEventEnum event, Map<String, Object> context) {
        // 利用随机ID创建状态机，创建时没有与具体定义状态机绑定
        StateMachine<RegStatusEnum, RegEventEnum> stateMachine = stateMachineFactory.getStateMachine("StateMachineId");
        stateMachine.start();
        try {
            // 在BizStateMachinePersist的restore过程中，绑定turnstileStateMachine状态机相关事件监听
            stateMachinePersist.restore(stateMachine, businessId);
            // 本处写法较为繁琐，实际为注入Map<String, Object> context内容到message中
            MessageBuilder<RegEventEnum> messageBuilder = MessageBuilder
                    .withPayload(event)
                    .setHeader("BusinessId", businessId);
            if (context != null) {
                context.forEach((key, value) -> messageBuilder.setHeader(key, value));
            }
            Message<RegEventEnum> message = messageBuilder.build();

            // 发送事件，返回是否执行成功
            boolean success = stateMachine.sendEvent(message);
            if (success) {
                stateMachinePersist.persist(stateMachine, businessId);
            } else {
                System.out.println("状态机处理未执行成功，请处理，ID：" + businessId + "，当前context：" + context);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            stateMachine.stop();
        }
    }
}