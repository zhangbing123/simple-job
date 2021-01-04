package com.schedule.simplejob.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.schedule.simplejob.config.SpringApplicationContextUtil;
import com.schedule.simplejob.curd.BaseMapper;
import com.schedule.simplejob.curd.BaseServiceImpl;
import com.schedule.simplejob.mapper.ExecuteJobMapper;
import com.schedule.simplejob.model.dto.ExecuteJobDTO;
import com.schedule.simplejob.model.entity.ExecuteJob;
import com.schedule.simplejob.model.entity.Job;
import com.schedule.simplejob.model.req.QueryReq;
import com.schedule.simplejob.service.ExecuteJobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * @description:统计任务执行情况
 * @author: zhangbing
 * @create: 2020-09-07 10:36
 **/
@Transactional
@Service
public class ExecuteJobServiceImpl extends BaseServiceImpl<ExecuteJob, Long> implements ExecuteJobService {

    @Autowired
    private ExecuteJobMapper executeJobMapper;
    @Autowired
    private JobServiceImpl jobService;


    @Override
    public BaseMapper<ExecuteJob, Long> getMapper() {
        return executeJobMapper;
    }

    @Override
    public PageInfo<ExecuteJobDTO> list(QueryReq queryReq) {
        PageHelper.startPage(queryReq.getPage(), queryReq.getLimit());
        List<ExecuteJobDTO> executeJobs = executeJobMapper.selectByParam();
        if (CollectionUtils.isEmpty(executeJobs)) {
            return new PageInfo<>();
        }

        HashMap<String, Job> jobHashMap = new HashMap<>();

        for (ExecuteJobDTO executeJob : executeJobs) {

            Job job = jobHashMap.containsKey(executeJob.getJobId()) ? jobHashMap.get(executeJob.getJobId())
                    : jobService.selectByPrimaryKey(executeJob.getJobId());

            if (job != null) {
                jobHashMap.put(job.getId(), job);
            }
            executeJob.setName(job != null ? job.getName() : null);
        }


        return new PageInfo<>(executeJobs);
    }

    @Override
    public void saveSuccessful(long time, String jobId) {
        Job job = jobService.selectByPrimaryKey(jobId);
        if (job == null) return;
        Date date = new Date();
        ExecuteJob executeJob = ExecuteJob.builder()
                .status("SUCESSFUL")
                .jobId(jobId)
                .createTime(date)
                .modifyTime(date)
                .excuteDate(time)
                .build();

        super.insert(executeJob);

    }

    @Override
    public void saveFail(long time, String jobId, String e) {

        Job job = jobService.selectByPrimaryKey(jobId);
        if (job == null) return;
        Date date = new Date();
        ExecuteJob executeJob = ExecuteJob.builder()
                .status("FAIL")
                .jobId(jobId)
                .createTime(date)
                .modifyTime(date)
                .exception(e)
                .excuteDate(time)
                .build();

        super.insert(executeJob);

    }

    public static ExecuteJobService getInstance() {
        return (ExecuteJobServiceImpl) SpringApplicationContextUtil.getBean("executeJobServiceImpl");
    }
}
