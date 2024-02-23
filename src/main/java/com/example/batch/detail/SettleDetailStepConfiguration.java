package com.example.batch.detail;

import com.example.batch.domain.ApiOrder;
import com.example.batch.domain.SettleDetail;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.listener.ExecutionContextPromotionListener;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class SettleDetailStepConfiguration {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager platformTransactionManager;



    // 첫 번째 스탭: 파일을 고객 + 서비스 단위로 집계해서 ExecutionContext에 넣는다.
    @Bean
    public Step preSettleDetailStep(
            FlatFileItemReader<ApiOrder> preSettleDetailReader,
            PreSettleDetailWrtier preSettleDetailWrtier,
            ExecutionContextPromotionListener executionContextPromotionListener
    ) {
        return new StepBuilder("preSettleDetailStep", jobRepository)
                .<ApiOrder, Key>chunk(5000, platformTransactionManager)
                .reader(preSettleDetailReader)
                .processor(new PreSettleDetailProcessor())
                .writer(preSettleDetailWrtier)
                .listener(executionContextPromotionListener)
                .build();
    }

    @Bean
    @StepScope
    // 저장되어 있던 파일 읽어오기
    public FlatFileItemReader<ApiOrder> preSettleDetailReader(
            @Value("#{jobParameters['targetDate']}") String targetDate
    ) {

        final String fileName = targetDate + "_api_orders.csv";

        return new FlatFileItemReaderBuilder<ApiOrder>()
                .name("preSettleDetailReader")
                .resource(new ClassPathResource("/data/" + fileName))
                .linesToSkip(1)
                .delimited().delimiter(",")
                .names("id", "customerId", "url", "state", "createdAt")
                .targetType(ApiOrder.class)
                .build();

    }


    // 두 번째 스탭: 집계된 ExecutionContext 데이터를 통해 DB에 Write한다.
    @Bean
    public Step settleDetailStep(
            SettleDetailReader settleDetailReader,
            SettleDetailProcessor settleDetailProcessor,
            JpaItemWriter<SettleDetail> settleDetailJpaItemWriter
    ) {
        return new StepBuilder("settleDetailStep", jobRepository)
                .<KeyAndCount, SettleDetail>chunk(1000, platformTransactionManager)
                .reader(settleDetailReader)
                .processor(settleDetailProcessor)
                .writer(settleDetailJpaItemWriter)
                .build();
    }

    // Step1에서 ExecutionContext에 넣은 snapshot은
    // Step2가 바로 가져올 수 없으므로,
    // listener를 통해 Step 레벨에서 Job 레벨로 snapshot을 올려줘야 함.
    // 이러면 Step2에서 jobExecution을 통해 snapshot을 가져올 수 있음 !

    @Bean
    public ExecutionContextPromotionListener promotionListener() {
        ExecutionContextPromotionListener listener = new ExecutionContextPromotionListener();
        listener.setKeys(new String[]{"snapshots"});
        return listener;
    }


    @Bean
    public JpaItemWriter<SettleDetail> settleDetailJpaItemWriter(
            EntityManagerFactory entityManagerFactory
    ) {
        return new JpaItemWriterBuilder<SettleDetail>()
                .entityManagerFactory(entityManagerFactory)
                .build();
    }
}
