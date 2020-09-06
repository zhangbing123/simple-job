package com.schedule.simplejob.curd;

import tk.mybatis.mapper.common.IdsMapper;
import tk.mybatis.mapper.common.Mapper;

import java.io.Serializable;

/**
 * @description: crud的基础支持
 * @author: zhangbing
 * @create: 2019-12-11 13:54
 **/
public interface BaseMapper<T, E extends Serializable> extends Mapper<T>, IdsMapper<T> {
}
