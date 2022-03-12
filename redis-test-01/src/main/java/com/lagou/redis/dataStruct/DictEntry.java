package com.lagou.redis.dataStruct;

/**
 * Date: 2022/3/13
 * <p>
 * Description:
 *
 * @author xiazhenyu
 */
public class DictEntry {


    private Object key;

    private Union union;

    private DictEntry next;

    public static class Union {

        private Object val;
        private long type;
    }
}