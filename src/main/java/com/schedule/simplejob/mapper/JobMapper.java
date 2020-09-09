package com.schedule.simplejob.mapper;

import com.schedule.simplejob.curd.BaseMapper;
import com.schedule.simplejob.model.entity.Job;

import java.util.List;

public interface JobMapper extends BaseMapper<Job,String> {

    List<Job> selectParam();
}
