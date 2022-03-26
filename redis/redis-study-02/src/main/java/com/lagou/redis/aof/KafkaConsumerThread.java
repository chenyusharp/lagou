package com.lagou.redis.aof;

import static java.util.stream.Collectors.toList;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ThreadPoolExecutor;
import lombok.Data;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.springframework.util.ConcurrentReferenceHashMap;

/**
 * Date: 2022/3/26
 * <p>
 * Description:
 *
 * @author xiazhenyu
 */
@Data
public class KafkaConsumerThread extends Thread {

    private KafkaConsumer<String, String> deleteConsumer;
    private int threadNumber;
    private final GoodsService goodsService;
    private ThreadPoolExecutor dynamicThreadPoolExecutor;

    public KafkaConsumerThread(Properties properties, String topic, ThreadPoolExecutor dynamicThreadPoolExecutor) {
        deleteConsumer = new KafkaConsumer<>(properties);
        deleteConsumer.subscribe(Lists.newArrayList(topic));
        this.threadNumber = deleteConsumer.partitionsFor(topic).size();
        this.dynamicThreadPoolExecutor = dynamicThreadPoolExecutor;
        //动态线程池

        //引入service类
        goodsService = new GoodsService() {
            @Override
            public int selectCountByMerchantId(long merchantId) {
                return 0;
            }

            @Override
            public List<Goods> selectGoodsListByMerchantId(int start, int pageSize, long merchantId) {
                return null;
            }

            @Override
            public int batchDeletedByIds(List<Long> goodsIdList) {
                return 0;
            }
        };
    }

    static final Map<TopicPartition, OffsetAndMetadata> offsets = new ConcurrentReferenceHashMap<>();


    @Override
    public void run() {
        try {
            while (true) {
                //动态配置
                ConsumerRecords<String, String> records = deleteConsumer.poll(Duration.ofMillis(100));
                if (!records.isEmpty()) {
                    Set<TopicPartition> topicPartitions = records.partitions();
                    topicPartitions.forEach(tp -> {
                        List<ConsumerRecord<String, String>> recordList = records.records(tp);
                        recordList.forEach(
                                record -> dynamicThreadPoolExecutor.submit(new ExecuteHandler(record, tp, goodsService)));
                    });
                    synchronized (offsets) {
                        if (!offsets.isEmpty()) {
                            deleteConsumer.commitSync(offsets);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            deleteConsumer.close();
        }
    }


    public static class ExecuteHandler extends Thread {

        public final ConsumerRecord<String, String> consumerRecord;
        private final TopicPartition tp;
        private final GoodsService goodsService;

        private Gson gson = new Gson();

        public ExecuteHandler(ConsumerRecord<String, String> consumerRecord, TopicPartition tp,
                GoodsService goodsService) {
            this.consumerRecord = consumerRecord;
            this.tp = tp;
            this.goodsService = goodsService;
        }

        @Override
        public void run() {
            long lastConsumedOffset = consumerRecord.offset();
            List<Goods> goodsList = gson.fromJson(consumerRecord.value(), new TypeToken<List<Goods>>() {
            }.getType());
            goodsService.batchDeletedByIds(goodsList.stream().map(Goods::getId).collect(toList()));
            synchronized (offsets) {
                if (!offsets.containsKey(tp)) {
                    offsets.put(tp, new OffsetAndMetadata(lastConsumedOffset + 1));
                } else {
                    long position = offsets.get(tp).offset();
                    if (position < lastConsumedOffset + 1) {
                        offsets.put(tp, new OffsetAndMetadata(lastConsumedOffset + 1));
                    }
                }
            }
        }
    }
}