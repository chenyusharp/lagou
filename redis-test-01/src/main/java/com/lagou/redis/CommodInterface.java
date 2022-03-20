package com.lagou.redis;

import static com.lagou.redis.EncodingEnum.REDIS_ENCODING_HT;
import static com.lagou.redis.TypeEnum.REDIS_HASH;
import static com.lagou.redis.TypeEnum.REDIS_LIST;
import static com.lagou.redis.TypeEnum.REDIS_STRING;

import com.lagou.redis.dataStruct.Dict;
import com.lagou.redis.dataStruct.DictEntry;
import com.lagou.redis.dataStruct.DictEntry.Union;
import com.lagou.redis.dataStruct.DictHt;
import com.lagou.redis.dataStruct.ListNode;
import com.lagou.redis.dataStruct.RedisList;
import com.lagou.redis.dataStruct.SdsHdr;
import com.lagou.redis.dataStruct.ZipList;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.regex.Pattern;

/**
 * Date: 2022/3/13
 * <p>
 * Description:
 *
 * @author xiazhenyu
 */
public class CommodInterface {


    private static final Pattern INTEGER_PATTERN = Pattern.compile("^-?[0-9]*$");


    //字典
    public static final Dict dict = new Dict();
    //哈希表
    public static DictHt[] dictHts = new DictHt[2];

    //初始化操作
    {
        dictHts[0] = new DictHt();
        dictHts[1] = new DictHt();
        dict.setHt(dictHts);
    }


    public static void main(String[] args) {

        String number1 = "-23232";
        String number2 = "00-";
        String number3 = "43439";

        System.out.println(INTEGER_PATTERN.matcher(number1).find());
        System.out.println(INTEGER_PATTERN.matcher(number2).find());
        System.out.println(INTEGER_PATTERN.matcher(number3).find());
    }

    /**
     * 对应于redis中的set 命令
     *
     * @param value 数据库值
     * @return 键值对中的值对象
     */
    public RedisObject set(Object... value) {
        RedisObject redisObject = new RedisObject();
        if (value.length == 0) {
            if (INTEGER_PATTERN.matcher(String.valueOf(value[0])).find() &&
                    compareToMaxInt(String.valueOf(value[0]))) {
                redisObject.setType(REDIS_STRING);
                redisObject.setEncoding(EncodingEnum.REDIS_ENCODING_INT);
                redisObject.setPtr(value[0]);
            } else if ((String.valueOf(value[0]).getBytes(StandardCharsets.UTF_8)).length > 39) {
                redisObject = buildSdsHdr(value[0]);
            } else {
                //TODO embstr的连续的内存怎么表示？
            }

        }
        return redisObject;
    }


    private static RedisObject buildSdsHdr(Object val) {
        RedisObject redisObject = new RedisObject();
        redisObject.setType(REDIS_STRING);
        redisObject.setEncoding(EncodingEnum.REDIS_ENCODING_RAW);
        SdsHdr sdsHdr = new SdsHdr();
        sdsHdr.setFree(0);
        sdsHdr.setLen((String.valueOf(val).getBytes(StandardCharsets.UTF_8)).length);
        sdsHdr.setBuf(String.valueOf(val).toCharArray());
        redisObject.setPtr(sdsHdr);
        return redisObject;
    }


    /**
     * 对应于redis中的push命令
     *
     * @param values 数据库值
     * @return redisObject
     */
    public RedisObject push(Object... values) {
        RedisObject redisObject = new RedisObject();
        if (Arrays.stream(values).anyMatch(this::matchByteSizeLessThan64) && values.length < 512) {
            //使用压缩列表
            redisObject.setType(REDIS_LIST);
            redisObject.setEncoding(EncodingEnum.REDIS_ENCODING_ZIPLIST);
            ZipList zipList = new ZipList();
            ZlEntry[] array = new ZlEntry[values.length];
            long length = 0;
            int totalLength = 0;
            for (int i = 0; i < values.length; i++) {
                ZlEntry zlEntry = new ZlEntry();
                zlEntry.setPreviousEntryLength(length);
                zlEntry.setContent(values[i]);
                zlEntry.setEncoding(getEncodingForZipList(values[i]));
                length = (String.valueOf(values[i]).getBytes(StandardCharsets.UTF_8)).length;
                totalLength += length;
                array[i] = zlEntry;
            }
            zipList.setZlBytes(4 + 4 + 2 + totalLength + 1); //总字节数
            zipList.setZlTail(4 + 2 + totalLength); //表尾结点距离起始结点的字节数
            zipList.setZlLen(values.length);//总的结点数
            zipList.setEntry(array);
            redisObject.setPtr(zipList);
        } else {
            //使用linkedList
            redisObject.setType(REDIS_LIST);
            redisObject.setEncoding(EncodingEnum.REDIS_ENCODING_LINKEDLIST);
            RedisList redisList = convertArray2RedisList(values);
            redisObject.setPtr(redisList);

        }
        return redisObject;
    }


    private boolean matchByteSizeLessThan64(Object obj) {
        if ((String.valueOf(obj).getBytes(StandardCharsets.UTF_8)).length < 64) {
            return true;
        } else {
            return false;
        }
    }


    /**
     * 对应于redis中的hset命令
     *
     * @param values 参数
     * @return redisObject
     */
    private boolean hSet(String key, Object[]... values) {
        //首先判断key是否存在
        DictHt[] dictHts = dict.getHt();
        DictHt dictHt = dictHts[0];
        int position = (int) (DictHt.hashFunction(key) & (dictHt.getSize() - 1));
        if (dictHt.getTable()[position] != null) {
            //覆盖
            Dict dict = (Dict) dictHt.getTable()[position].getUnion().getVal();
            DictHt dictHt1 = dict.getHt()[0];
            DictEntry[] dictEntries = dictHt1.getTable();
            //暂不考虑hash冲突
            DictEntry[] dictEntries1 = new DictEntry[dictEntries.length + values.length];


        } else {
            //新增
            Dict dict = new Dict();
            dict.setReHashIndex(0);
            DictHt[] dictHtArray = new DictHt[]{new DictHt(), new DictHt()};

            DictEntry[] dictEntries = new DictEntry[values.length];
            extracted(dictHtArray[0], dictEntries, values);
            dict.setHt(dictHtArray);
            RedisObject redisObject = new RedisObject();
            redisObject.setEncoding(REDIS_ENCODING_HT);
            redisObject.setType(REDIS_HASH);
            redisObject.setPtr(dict);

            DictEntry dictEntry = dictHt.getTable()[position];
            dictEntry.setKey(key);
            dictEntry.setUnion(new Union() {{
                setVal(dict);
            }});
        }
        return true;
    }

    private void extracted(DictHt dictHt1, DictEntry[] dictEntries, Object[][] values) {
        for (Object[] objects : values) {
            String pairKey = (String) objects[0];
            Object pairValue = objects[1];
            //位置
            int currPos = DictHt.hashFunction(pairKey) & (dictEntries.length - 1);
            DictEntry dictEntry = new DictEntry();
            dictEntry.setKey(pairKey);
            dictEntry.setUnion(new Union() {
                {
                    setVal(pairValue);
                }
            });
            dictEntries[currPos] = dictEntry;
        }
        dictHt1.setSize(values.length);
        dictHt1.setSizeMask(values.length - 1);
        dictHt1.setTable(dictEntries);
    }


    private static RedisList convertArray2RedisList(Object... values) {
        RedisList redisList = new RedisList();
        ListNode head = new ListNode();
        head.setValue(values[0]);
        head.setPrev(null);
        ListNode other = head;
        for (int i = 1; i < values.length; i++) {
            ListNode temp = new ListNode();
            temp.setPrev(other);
            temp.setValue(values[i]);
            other.setNext(temp);
            other = temp;
            if (values.length - 1 == i) {
                redisList.setTail(temp);
            }
        }
        redisList.setLen(values.length);
        return redisList;
    }

    /**
     * 根据content属性的值去定对应的encoding的值
     *
     * @param value content中存放的值
     * @return encoding
     */
    private Object getEncodingForZipList(Object value) {
        return null;
    }


    /**
     * 判断是不是比Integer的最大值大
     */
    private boolean compareToMaxInt(String val) {
        int cp = new BigDecimal(val).compareTo(new BigDecimal(Integer.MAX_VALUE));
        return cp <= 0;
    }
}