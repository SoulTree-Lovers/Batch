package com.example.batch.batches;

import lombok.Builder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

public class Job {

    private final Tasklet tasklet;

    private final JobExecutionListener jobExecutionListener;

    public Job(Tasklet tasklet) {
        this(tasklet, null);
    }

    public Job(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
        this.emailProvider = new EmailProvider.Fake();
    }

    public JobExecution execute() {
        /**
         * 1. 유저를 조회한다.
         * 2. 휴면 계정 대상을 추출 및 변환한다.
         * 3. 휴면 계정으로 상태를 변경한다.
         * 4. 메일을 보낸다.
         */

        final JobExecution jobExecution = new JobExecution();
        jobExecution.setBatchStatus(BatchStatus.STARTING);
        jobExecution.setStartTime(LocalDateTime.now());

        jobExecutionListener.beforeJob(jobExecution);

        try { // 비즈니스 로직
            tasklet.execute();
            jobExecution.setBatchStatus(BatchStatus.COMPLETED);
        } catch (Exception e) {
            jobExecution.setBatchStatus(BatchStatus.FAILED);
        }

        jobExecution.setEndTime(LocalDateTime.now());

        jobExecutionListener.afterJob(jobExecution);
        return jobExecution;
    }
}
