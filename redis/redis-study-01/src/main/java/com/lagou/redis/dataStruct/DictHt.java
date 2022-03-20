package com.lagou.redis.dataStruct;

/**
 * Date: 2022/3/13
 * <p>
 * Description: 哈希表
 *
 * @author xiazhenyu
 */
public class DictHt {

    private DictEntry[] table;

    private long size;

    private long sizeMask;


    public DictEntry[] getTable() {
        return table;
    }

    public void setTable(DictEntry[] table) {
        this.table = table;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public long getSizeMask() {
        return sizeMask;
    }

    public void setSizeMask(long sizeMask) {
        this.sizeMask = sizeMask;
    }

    /**
     * 计算hash值
     *
     * @param key 键key
     * @return 哈希值
     */
    public static int hashFunction(String key) {
        return key.hashCode();
    }


    /**
     * 根据key定位到具体的位置
     * @param key
     * @return
     */
    public DictEntry getEntry(String key) {
        //定位索引的位置
        int position = (int) (hashFunction(key) & (size - 1));
        return table[position];
    }
}