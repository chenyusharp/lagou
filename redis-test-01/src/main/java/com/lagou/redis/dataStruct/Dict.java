package com.lagou.redis.dataStruct;

/**
 * Date: 2022/3/13
 * <p>
 * Description:
 *
 * @author xiazhenyu
 */
public class Dict {


    private Object privData;


    private DictHt[] ht;

    private long reHashIndex;

    private int iterators;


}