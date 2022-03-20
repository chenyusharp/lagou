package com.lagou.redis.dataStruct;

import com.lagou.redis.RedisObject;

/**
 * Date: 2022/3/13
 * <p>
 * Description:
 *
 * @author xiazhenyu
 */
public class ZskipListNode {


    private ZskipListNode backWard;

    private double score;

    private RedisObject robj;

    private ZskipListLevel[] level;


}