package com.lagou.redis.dataStruct;

import com.lagou.redis.ZlEntry;

/**
 * Date: 2022/3/13
 * <p>
 * Description:
 *
 * @author xiazhenyu
 */
public class ZipList {


    private int zlBytes;

    private int zlTail;

    private int zlLen;

    private ZlEntry[] entry;

    private int zlEnd = 255;


    public int getZlBytes() {
        return zlBytes;
    }

    public void setZlBytes(int zlBytes) {
        this.zlBytes = zlBytes;
    }

    public int getZlTail() {
        return zlTail;
    }

    public void setZlTail(int zlTail) {
        this.zlTail = zlTail;
    }

    public int getZlLen() {
        return zlLen;
    }

    public void setZlLen(int zlLen) {
        this.zlLen = zlLen;
    }

    public ZlEntry[] getEntry() {
        return entry;
    }

    public void setEntry(ZlEntry[] entry) {
        this.entry = entry;
    }

    public int getZlEnd() {
        return zlEnd;
    }

    public void setZlEnd(int zlEnd) {
        this.zlEnd = zlEnd;
    }
}