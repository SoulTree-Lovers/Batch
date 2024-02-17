package com.example.batch.application.dormant;

import com.example.batch.EmailProvider;
import com.example.batch.batches.JobExecution;
import com.example.batch.batches.JobExecutionListener;
import org.springframework.stereotype.Component;

@Component
public class DormantBatchJobExecutionListener implements JobExecutionListener {

    private final EmailProvider emailProvider;

    public DormantBatchJobExecutionListener() {
        this.emailProvider = new EmailProvider.Fake();
    }


    @Override
    public void beforeJob(JobExecution jobExecution) {
        // no - op
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        emailProvider.send( // 비즈니스 로직
                "admin@naver.com",
                "배치 완료 알림",
                "DormantBatchJob이 실행되었습니다. status:" + jobExecution.getBatchStatus()
        );
    }
}
