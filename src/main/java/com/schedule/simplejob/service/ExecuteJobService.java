package com.schedule.simplejob.service;

import com.github.pagehelper.PageInfo;
import com.schedule.simplejob.curd.BaseService;
import com.schedule.simplejob.model.dto.ExecuteJobDTO;
import com.schedule.simplejob.model.entity.ExecuteJob;
import com.schedule.simplejob.model.req.QueryReq;

/**
 * @description:
 * @author: zhangbing
 * @create: 2020-09-07 10:35
 **/
public interface ExecuteJobService extends BaseService<ExecuteJob, Long> {

    PageInfo<ExecuteJobDTO> list(QueryReq queryReq);

    void saveSuccessful(long time, String jobId);

    void saveFail(long time, String jobId, String e);


}
