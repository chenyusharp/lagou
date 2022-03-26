package com.lagou.redis.aof;

/**
 * Date: 2022/3/23
 * <p>
 * Description:  商品信息
 *
 * @author xiazhenyu
 */
public class Goods {


    private Long id;
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}