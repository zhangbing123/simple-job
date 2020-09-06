package com.schedule.simplejob.curd;

import java.io.Serializable;
import java.util.List;

/**
 * @description: crud的基础支持
 * @author: zhangbing
 * @create: 2019-12-11 13:56
 **/
public interface BaseService<T, E extends Serializable> {

    List<T> select(T t);

    List<T> selectAll();

    List<T> selectByIds(String ids);

    int selectCount(T t);

    int deleteByPrimaryKey(E id);

    int insert(T record);

    int insertSelective(T record);

    int updateByPrimaryKeySelective(T record);

    int updateByPrimaryKey(T record);

    int deleteByPrimaryKey(Object o);

    int delete(T t);

    boolean existsWithPrimaryKey(Object o);

    T selectByPrimaryKey(Object o);

    T selectOne(T t);

    int deleteByIds(String s);
}
