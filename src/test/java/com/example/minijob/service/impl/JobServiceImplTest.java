package com.example.minijob.service.impl;

import com.example.minijob.model.Job;
import com.example.minijob.service.JobService;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Ignore
public class JobServiceImplTest {
    @Autowired
    private JobService jobService;

    @Test
    public void save() {
        Job job = new Job();
        job.setJobName("name1");
        jobService.save(job);
    }
}