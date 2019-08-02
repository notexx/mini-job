package com.example.minijob.controller;

import com.example.minijob.model.Job;
import com.example.minijob.service.JobService;
import com.example.minijob.service.SchedulerJobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/job")
public class JobController {

    @Autowired
    private JobService jobService;

    @Autowired
    private SchedulerJobService schedulerJobService;

    @GetMapping("/list")
    public List<Job> list() {
        List<Job> all = jobService.findAll();
        return all;
    }

    @PostMapping("")
    public void create(@RequestBody Job job) {
        if (job.getStatus() == 0) {
            jobService.save(job);
            return;
        }
        if (job.getStatus() == 1) {
            schedulerJobService.executeJob(job);
            return;
        }

    }

    @GetMapping("/{id}")
    public Job get(@PathVariable Long id) {
        return jobService.findById(id);
    }

    @PutMapping("/{id}")
    public Job update(@PathVariable Long id, @RequestBody Job job) {
        jobService.update(job);
        return jobService.findById(id);
    }
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        jobService.delete(id);
    }

    @PutMapping("/execute/{id}")
    public Job executeJob(@PathVariable Long id) {
        Job job = jobService.findById(id);
        schedulerJobService.executeJob(job);
        return jobService.findById(id);
    }
    @PutMapping("/execute/batch/{count}")
    public void executeBatchJob(@PathVariable int count) {
        schedulerJobService.batchExecuteJob(count);
    }


}
