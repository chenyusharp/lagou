package com.lagou.redis.dataStruct;

/**
 * Date: 2022/3/13
 * <p>
 * Description:
 *
 * @author xiazhenyu
 */
public class DictHt {

    private DictEntry[] table;

    private long size;

    private long sizeMask;

    private long used;

}