package com.lagou.redis.aof;

import com.google.common.base.Stopwatch;
import com.google.gson.Gson;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import javax.annotation.Resource;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Service;


/**
 * Date: 2022/3/23
 * <p>
 * Description: 删除操作类
 *
 * @author xiazhenyu
 */
@Service
public class DService {


    /**
     * 速率
     */
    private Speed speed;

    /**
     * 计时器
     */
    private Stopwatch stopwatch;


    Map<Integer/*第几秒*/, Integer/*删除的数据量*/> index;


    /**
     * 每夜查询的条数
     */
    public static final int pageSize = 100;
    public static final String deleteTopic = "goodDelete";
    @Resource
    private ThreadPoolExecutor dynamicThreadPoolExecutor;

    public void start(List<Long> merchantIdList) {
        GoodsDeleteKafkaClient producer = new GoodsDeleteKafkaClient();
        //得到消息生产者
        KafkaProducer<String, String> kafkaProducer = producer.createProducer();

        GoodsService goodsService = new GoodsService() {
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
        Gson gson = new Gson();
        for (long merchantId : merchantIdList) {
            //查询总数量
            int total = goodsService.selectCountByMerchantId(merchantId);
            //计算总页数
            int pages = total % pageSize;
            int i = 1;
            List<Goods> goodsList;
            while (i <= pages) {
                goodsList = goodsService.selectGoodsListByMerchantId((i - 1) * pages, pages, merchantId);
                ProducerRecord<String, String> record = new ProducerRecord<>(deleteTopic, String.valueOf(i),
                        gson.toJson(goodsList));
                //发送商品数据
                kafkaProducer.send(record);
                i++;
            }
        }
        stopwatch.elapsed();
    }


    public boolean process() {
        //创建对应的消费者
        GoodsDeleteKafkaClient client = new GoodsDeleteKafkaClient();
        Properties properties = client.initConfig();
        KafkaConsumerThread consumerThread = new KafkaConsumerThread(properties, deleteTopic, dynamicThreadPoolExecutor);
        consumerThread.start();
        return true;
    }


    public static void main(String[] args) throws InterruptedException {
        Stopwatch stopwatch = Stopwatch.createStarted();
        Thread.sleep(5000);
        long timeSpend = stopwatch.elapsed(TimeUnit.MILLISECONDS);
        System.out.println(timeSpend);
        stopwatch.reset();
        stopwatch.start();
        Thread.sleep(2000);
        long timeSpend1 = stopwatch.elapsed(TimeUnit.MILLISECONDS);
        System.out.println(timeSpend1);
    }


}