package com.lagou.redis;

/**
 * Date: 2022/3/13
 * <p>
 * Description:
 *
 * @author xiazhenyu
 */
public class ZlEntry {


    private long previousEntryLength;


    private Object encoding;

    private Object content;


    public long getPreviousEntryLength() {
        return previousEntryLength;
    }

    public void setPreviousEntryLength(long previousEntryLength) {
        this.previousEntryLength = previousEntryLength;
    }

    public Object getEncoding() {
        return encoding;
    }

    public void setEncoding(Object encoding) {
        this.encoding = encoding;
    }

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }
}