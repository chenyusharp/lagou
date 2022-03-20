package com.lagou.redis.dataStruct;

/**
 * Date: 2022/3/13
 * <p>
 * Description:
 *
 * @author xiazhenyu
 */
public class SdsHdr {

    private int len;
    private int free;
    private char[] buf;


    public int getLen() {
        return len;
    }

    public void setLen(int len) {
        this.len = len;
    }

    public int getFree() {
        return free;
    }

    public void setFree(int free) {
        this.free = free;
    }

    public char[] getBuf() {
        return buf;
    }

    public void setBuf(char[] buf) {
        this.buf = buf;
    }
}