package com.pkm.pkmdb.common.rocketmq.producer;

import org.apache.rocketmq.common.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *  RocketMq生产者
 * @author Howe Hsiang
 * @date 2020/02/03 9:20
 **/
public class RocketMqProducer {

    private static final Logger logger = LoggerFactory.getLogger(RocketMqProducer.class);

    @Autowired
    private RocketMqBaseProducer rocketMqBaseProducer;

    /**
     * 发送消息
     * @param topic
     * @param tags
     * @param body
     * @return
     */
    public boolean sendMessage(String topic, String tags, byte[] body) {
        try {
            rocketMqBaseProducer.getProducer().send(new Message(topic, tags, body));
            logger.info("ROCKET MQ 发送消息成功，Topic:{}，消息内容:{}为："+ topic, new String(body));
        } catch (Exception e) {
            logger.error("ROCKET MQ 发送消息失败，消息内容:"+e.getMessage());
            return false;
        }
        return true;
    }
}
