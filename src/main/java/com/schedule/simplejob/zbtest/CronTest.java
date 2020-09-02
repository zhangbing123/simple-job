package com.schedule.simplejob.zbtest;

import org.springframework.scheduling.support.CronSequenceGenerator;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @description:cron表达式解析
 * @author: zhangbing
 * @create: 2020-09-02 17:22
 **/
public class CronTest {

    public static void main(String[] args) {
        //每个月1号的凌晨1点执行
        CronSequenceGenerator cronSequenceGenerator = new CronSequenceGenerator("*/1 * * * * ?");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<String> list = new ArrayList<>(20);
        Date nextTimePoint = new Date();
        for (int i = 0; i < 20; i++) {
            // 计算下次时间点的开始时间
            nextTimePoint = cronSequenceGenerator.next(nextTimePoint);
            list.add(sdf.format(nextTimePoint));
        }
        for (String string : list) {
            System.out.println(string);
        }
    }
}
