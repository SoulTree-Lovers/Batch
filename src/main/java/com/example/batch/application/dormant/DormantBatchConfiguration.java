package com.example.batch.application.dormant;

import com.example.batch.batches.Job;
import com.example.batch.batches.Step;
import com.example.batch.batches.StepJobBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DormantBatchConfiguration {

    @Bean
    public Job dormantBatchJob (
            Step preDormantBatchStep,
            Step dormantBatchStep,
            DormantBatchJobExecutionListener JobExecutionListener
    ) {
        return new StepJobBuilder()
                .start(preDormantBatchStep)
                .next(dormantBatchStep)
                .build();
    }

    @Bean
    public Step preDormantBatchStep(
            AllCustomerItemReader itemReader,
            PreDormantBatchItemProcessor itemProcessor,
            PreDormantBatchItemWriter itemWriter
    ) {
        return Step.builder()
                .itemReader(itemReader)
                .itemProcessor(itemProcessor)
                .itemWriter(itemWriter)
                .build();
    }

    @Bean
    public Step dormantBatchStep(
            AllCustomerItemReader itemReader,
            DormantBatchItemProcessor itemProcessor,
            DormantBatchItemWriter itemWriter
    ) {
        return Step.builder()
                .itemReader(itemReader)
                .itemProcessor(itemProcessor)
                .itemWriter(itemWriter)
                .build();
    }


    // TODO: 휴면 전환 예정 1주일 전인 사람에게 이메일 발송

}
