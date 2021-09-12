package com.lagou.sqlSession;

import com.lagou.pojo.Configuration;
import com.lagou.pojo.MappedStatement;

import java.util.List;

public interface Executor {

    public <E> List<E> query(Configuration configuration, MappedStatement mappedStatement, Object... params) throws Exception;


    /**
     * 新增、删除、修改通用方法
     */
    int update(Configuration configuration, MappedStatement mappedStatement, Object... param) throws Exception;


}
