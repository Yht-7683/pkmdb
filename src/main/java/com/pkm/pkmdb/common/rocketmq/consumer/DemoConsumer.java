package com.pkm.pkmdb.common.rocketmq.consumer;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 *  RocketMq消费者Demo
 * @author Howe Hsiang
 * @date 2020/02/03 9:20
 **/
@Service
public class DemoConsumer{

    private static final Logger logger = LoggerFactory.getLogger(DemoConsumer.class);

    public void sub(DefaultMQPushConsumer consumer){
        try {
            consumer.subscribe("zssp-bg", "*");
            consumer.registerMessageListener((MessageListenerConcurrently) (msgs, context) -> {
                for (MessageExt msg : msgs) {
                    logger.info("ROCKET MQ 收到消息成功，Topic:{}，消息内容:{}", msg.getTopic(), new String(msg.getBody()));
                }
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            });
        }catch (Exception e){
            logger.error("ROCKET MQ 收到消息失败，消息内容:"+e.getMessage());
        }
    }
}
