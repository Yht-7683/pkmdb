package com.pkm.pkmdb.common.rocketmq.consumer;

import org.apache.rocketmq.acl.common.AclClientRPCHook;
import org.apache.rocketmq.acl.common.SessionCredentials;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.rebalance.AllocateMessageQueueAveragely;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * RocketMQ消费者基类
 * @author Howe Hsiang
 * @date 2020/03/02 9:35
 **/
public class BaseRocketMqConsumer {

    DefaultMQPushConsumer consumer;

    String consumerGroup;

    String namesrvAddr;

    String secretKey;

    String accessKey;

    @Autowired
    private DemoConsumer demoConsumer;

    public void init() throws Exception{
        AclClientRPCHook acl = new AclClientRPCHook(new SessionCredentials(getAccessKey(), getSecretKey()));
        consumer = new DefaultMQPushConsumer(getConsumerGroup(), acl, new AllocateMessageQueueAveragely());
        consumer.setNamesrvAddr(getNamesrvAddr());
        consumer.setConsumeMessageBatchMaxSize(1);
        demoConsumer.sub(consumer);
        consumer.start();
    }

    public void destroy(){
        if(consumer != null){
            consumer.shutdown();
        }
    }

    public String getConsumerGroup() {
        return consumerGroup;
    }

    public void setConsumerGroup(String consumerGroup) {
        this.consumerGroup = consumerGroup;
    }

    public String getNamesrvAddr() {
        return namesrvAddr;
    }

    public void setNamesrvAddr(String namesrvAddr) {
        this.namesrvAddr = namesrvAddr;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public DefaultMQPushConsumer getConsumer() {
        return consumer;
    }

    public void setConsumer(DefaultMQPushConsumer consumer) {
        this.consumer = consumer;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }
}




















