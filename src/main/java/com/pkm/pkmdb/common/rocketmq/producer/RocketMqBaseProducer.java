package com.pkm.pkmdb.common.rocketmq.producer;

import org.apache.rocketmq.acl.common.AclClientRPCHook;
import org.apache.rocketmq.acl.common.SessionCredentials;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;

/**
 * RocketMQ生产者基类。
 * @author Howe Hsiang
 * @date 2020/03/02 9:35
 **/
public class RocketMqBaseProducer {

    DefaultMQProducer producer;

    String producerGroup;

    String namesrvAddr;

    String secretKey;

    String accessKey;


    public void init() throws MQClientException {
        AclClientRPCHook acl = new AclClientRPCHook(new SessionCredentials(getAccessKey(), getSecretKey()));
        producer = new DefaultMQProducer(getProducerGroup(), acl, true, null);
        producer.setNamesrvAddr(namesrvAddr);
        producer.start();
    }

    public void destroy() {
        producer.shutdown();
    }

    public DefaultMQProducer getProducer() {
        return producer;
    }

    public String getProducerGroup() {
        return producerGroup;
    }

    public void setProducerGroup(String producerGroup) {
        this.producerGroup = producerGroup;
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

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }
}
