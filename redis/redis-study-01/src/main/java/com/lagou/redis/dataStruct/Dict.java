package com.lagou.redis.dataStruct;

/**
 * Date: 2022/3/13
 * <p>
 * Description: 字典结构
 *
 * @author xiazhenyu
 */
public class Dict {


    private Object privData;

    //有两项，ht[0]、ht[1]。
    //字典只会使用ht[0]哈希表，ht[1]是在rehash时使用的。
    private DictHt[] ht;

    private long reHashIndex;

    private int iterators;

    public Object getPrivData() {
        return privData;
    }

    public void setPrivData(Object privData) {
        this.privData = privData;
    }

    public DictHt[] getHt() {
        return ht;
    }

    public void setHt(DictHt[] ht) {
        this.ht = ht;
    }

    public long getReHashIndex() {
        return reHashIndex;
    }

    public void setReHashIndex(long reHashIndex) {
        this.reHashIndex = reHashIndex;
    }

    public int getIterators() {
        return iterators;
    }

    public void setIterators(int iterators) {
        this.iterators = iterators;
    }


}