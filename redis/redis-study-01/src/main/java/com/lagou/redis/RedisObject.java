package com.lagou.redis;

/**
 * Date: 2022/3/13
 * <p>
 * Description: redis对象类
 *
 * @author xiazhenyu
 */

public class RedisObject {


    private TypeEnum type;


    private EncodingEnum encoding;

    private Object ptr;


    public TypeEnum getType() {
        return type;
    }

    public void setType(TypeEnum type) {
        this.type = type;
    }

    public EncodingEnum getEncoding() {
        return encoding;
    }

    public void setEncoding(EncodingEnum encoding) {
        this.encoding = encoding;
    }

    public Object getPtr() {
        return ptr;
    }

    public void setPtr(Object ptr) {
        this.ptr = ptr;
    }
}