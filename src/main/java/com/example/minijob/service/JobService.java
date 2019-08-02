package com.example.minijob.service;

import com.example.minijob.model.Job;

import java.util.List;

public interface JobService {
    long save(Job job);

    void delete(Long id);

    Job update(Job job);

    List<Job> findAll();
    List<Job> findAliveJobs();

    Job findById(Long id);

    Job updateStatusById(Long id, int status);
}
