package com.example.minijob.service;

import com.example.minijob.model.Job;

public interface SchedulerJobService {
    /**
     * 执行全部未执行的job
     */
    void execute();

    /**
     * 执行指定job
     * @param job
     */
    void executeJob(Job job);

    /**
     * mock 批量执行任务
     * @param count 任务数量
     */
    void batchExecuteJob(int count);
}
