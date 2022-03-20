package com.lagou.redis;

/**
 * Date: 2022/3/13
 * <p>
 * Description: 编码产量
 *
 * @author xiazhenyu
 */
public enum EncodingEnum {


    REDIS_ENCODING_INT,
    REDIS_ENCODING_EMBSTR,
    REDIS_ENCODING_RAW,
    REDIS_ENCODING_HT,
    REDIS_ENCODING_LINKEDLIST,
    REDIS_ENCODING_ZIPLIST,
    REDIS_ENCODING_INTSET,
    REDIS_ENCODING_SKIPLIST;
}
