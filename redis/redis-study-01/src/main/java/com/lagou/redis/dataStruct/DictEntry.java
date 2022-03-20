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

        public Object getVal() {
            return val;
        }

        public void setVal(Object val) {
            this.val = val;
        }

        public long getType() {
            return type;
        }

        public void setType(long type) {
            this.type = type;
        }
    }


    public Object getKey() {
        return key;
    }

    public void setKey(Object key) {
        this.key = key;
    }

    public Union getUnion() {
        return union;
    }

    public void setUnion(Union union) {
        this.union = union;
    }

    public DictEntry getNext() {
        return next;
    }

    public void setNext(DictEntry next) {
        this.next = next;
    }
}