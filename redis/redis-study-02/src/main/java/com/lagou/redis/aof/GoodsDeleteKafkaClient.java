package com.lagou.redis.aof;

import java.util.Properties;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.KafkaProducer;

/**
 * Date: 2022/3/23
 * <p>
 * Description:
 *
 * @author xiazhenyu
 */
public class GoodsDeleteKafkaClient {

    public static final String BROKER_LIST = "";
    public static final String GROUP_ID = "group-delete";

    public KafkaProducer<String, String> createProducer() {
        Properties properties = new Properties();
        properties.put("bootstrap.servers", BROKER_LIST);
        properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        return new KafkaProducer<>(properties);
    }


    public Properties initConfig() {
        Properties properties = new Properties();
        properties.put("bootstrap.server", BROKER_LIST);
        properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, GROUP_ID);
        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
        return properties;
    }


}