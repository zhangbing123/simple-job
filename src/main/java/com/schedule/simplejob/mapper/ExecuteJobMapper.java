package com.schedule.simplejob.mapper;

import com.schedule.simplejob.curd.BaseMapper;
import com.schedule.simplejob.model.dto.ExecuteJobDTO;
import com.schedule.simplejob.model.entity.ExecuteJob;

import java.util.List;

/**
 * @description:
 * @author: zhangbing
 * @create: 2020-09-07 10:38
 **/
public interface ExecuteJobMapper extends BaseMapper<ExecuteJob, Long> {

    List<ExecuteJobDTO> selectByParam();

}
