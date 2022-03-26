package com.lagou.redis.aof;

import java.util.Map;

/**
 * Date: 2022/3/23
 * <p>
 * Description: 统计信息
 *
 * @author xiazhenyu
 */
public class Index {


    private Map<Integer, Integer> index;

    public Map<Integer, Integer> getIndex() {
        return index;
    }

    public void setIndex(Map<Integer, Integer> index) {
        this.index = index;
    }
}