package com.schedule.simplejob;

import com.schedule.simplejob.model.entity.Job;
import com.schedule.simplejob.service.JobService;
import com.schedule.simplejob.timer.SimpleJob;
import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.net.InetAddress;
import java.util.List;

/**
 * 项目启动后  初始化一些东西
 */
@Slf4j
@Component
public class InitApplicationRunner implements CommandLineRunner {

    @Autowired
    private JobService jobService;

    @Value("${server.port}")
    private String port;

    @Autowired
    private ZooKeeper zooKeeper;

    @Autowired
    private SimpleJob simpleJob;

    @Override
    public void run(String... args) throws Exception {

        selectMaster();
    }

    private void selectMaster() {
        if (tryToBeMaster()) {
            //成为master节点
            simpleJob.setRole("MASTER");
            simpleJob.start();
            log.info("the server start successfully,and the role is master");
            reRegister();
        } else {
            //成为salve节点  该节点不对外提供服务
            simpleJob.setRole("SLAVE");
            watcherMaster(simpleJob);
            log.info("the server start successfully,and the role is salve");
        }


    }

    private void watcherMaster(SimpleJob simpleJob) {
        Stat stat = new Stat();
        try {
            zooKeeper.getData("/master", watchedEvent -> {
                if (watchedEvent.getType() == Watcher.Event.EventType.NodeDeleted) {
                    log.info("主节点挂了，重新选举主节点");
                    //master节点挂掉了  重新选举
                    if (tryToBeMaster()) {
                        log.info("选举成功 成为主节点!!!!");
                        simpleJob.setRole("MASTER");
                        simpleJob.start();//启动
                        reRegister();//初始化
                    } else {
                        watcherMaster(simpleJob);
                        log.info("选举失败");
                    }
                }
            }, stat);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean tryToBeMaster() {
        String path = null;
        try {
            InetAddress addr = InetAddress.getLocalHost();
            path = zooKeeper.create("/master", (new String(addr.getAddress()) + ":" + port).getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
            System.out.println(path);
        } catch (Exception e) {

        }
        return !StringUtils.isEmpty(path);
    }

    public void reRegister() {
        log.info("开始进行数据初始化...");

        /**
         * 系统启动之初 将持久化的任务重新注册到任务执行器中
         */

        Job param = Job.builder().deleted(0).status("RUNNING").build();

        //查询启用的周期任务
        List<Job> jobs = jobService.select(param);

        int count = 0;

        if (!CollectionUtils.isEmpty(jobs)) {

            for (Job job : jobs) {

                if (jobService.reRegister(job))
                    count++;
            }
        }
        log.info("初始化完成，重新注册" + count + "个任务!!!!");

    }
}
