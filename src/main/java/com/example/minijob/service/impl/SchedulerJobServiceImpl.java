package com.example.minijob.service.impl;

import com.example.minijob.model.Job;
import com.example.minijob.service.JobService;
import com.example.minijob.service.SchedulerJobService;
import com.example.minijob.util.*;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;


@Service
public class SchedulerJobServiceImpl implements SchedulerJobService {
    private static final Logger logger = LoggerFactory.getLogger(SchedulerJobServiceImpl.class);
    @Autowired
    private JobService jobService;
    private static Random random = new Random();
    ThreadFactory nameThreadFactory = new ThreadFactoryBuilder().setNameFormat("job-pool-%d").build();
    /**
     * 线程池
     */
    ThreadPoolExecutor executor = new ThreadPoolExecutor(4, 6, 1, TimeUnit.SECONDS, new ArrayBlockingQueue<>(100), nameThreadFactory);

    private static AtomicInteger taskIdx = new AtomicInteger();
    // 抽象调度算法
    private static AbstractLoadBalance abstractLoadBalance = null;

    public SchedulerJobServiceImpl() {
        // 默认调用随机权重轮询算法
        this(LoadBalanceType.RANDOM_LOAD_BALANCE);
    }

    public SchedulerJobServiceImpl(LoadBalanceType type) {
        switch (type) {
            case RANDOM_LOAD_BALANCE:
                logger.info("使用算法【随机权重轮询算法】");
                abstractLoadBalance = new RandomLoadBalance();
                return;
            case LOAD_BALANCE:
                logger.info("使用算法【权重轮询算法】");
                abstractLoadBalance = new LoadBalance();
                return;
            case CONSISTENT_HASH_LOAD_BALANCE:
                logger.info("使用算法【一致性hash算法】");
                abstractLoadBalance = new ConsistentHashLoadBalance();
                return;
            default:
        }

    }


    @Override
    public void execute() {
        /**
         * 全部 未执行 任务
         */
        List<Job> aliveJobs = getAliveJobs();
        for (Job job : aliveJobs) {
            executeJob(job);
        }
    }

    @Override
    public void executeJob(Job job) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                if (job == null || job.getStatus() == 1) {
                    return;
                }
                long startTime = System.currentTimeMillis();
                LocalDateTime localDateTime = LocalDateTime.now();
                String format = localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                // 创建一个模拟任务执行消耗时间
                int finalMockTime = mockTime();
                String currentChannel = "";
                // 根据算法获取需要执行的通道
                if (abstractLoadBalance instanceof ConsistentHashLoadBalance) {
                    currentChannel = abstractLoadBalance.getExecuteChannel(job.getJobType());
                }
                String name = Thread.currentThread().getName();
                if (StringUtils.isEmpty(job.getId())) {
                    job.setStatus(0);
                    Long id = jobService.save(job);
                    job.setId(id);
                }
                try {
                    // 模拟业务执行时间
                    Thread.sleep(finalMockTime);
                    long endTime = System.currentTimeMillis();
                    double date = (endTime - startTime) / 1000.0;
                    String data = String.format("执行通道：%s, 线程模拟服务器：%s, 执行任务名称：%s, 执行时间：%s秒", currentChannel, name, job.getJobName(), date);
                    job.setStatus(1);
                    job.setJobTime(format);
                    job.setJobData(data);
                    job.setExecuteChannel(currentChannel);
                    job.setConsumerTime(String.valueOf(date));
                    jobService.update(job);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    @Override
    public void batchExecuteJob(int count) {
        if (count <= 0) {
            return;
        }
        Job job = null;
        for (int i = 0; i < count; i++) {
            job = new Job();
            job.setJobName("job-name-batch-" + taskIdx.getAndIncrement());
            job.setStatus(0);
            if (i % 4 == 0) {
                job.setJobType("类型一");
            } else if (i % 4 == 1) {
                job.setJobType("类型二");
            } else if (i % 4 == 2) {
                job.setJobType("类型三");
            } else if (i % 4 == 3) {
                job.setJobType("类型四");
            } else {
                job.setJobType("类型二");
            }
            executeJob(job);
        }


    }

    /**
     * 获取所有需要待执行的任务
     *
     * @return
     */
    private List<Job> getAliveJobs() {
        return jobService.findAliveJobs();
    }

    /**
     * 生成模拟业务执行时间
     * @return
     */
    private static int mockTime() {
        int mockTime = random.nextInt(500);
        if (mockTime < 100) {
            mockTime += 100;
        }
        return mockTime;
    }
}
