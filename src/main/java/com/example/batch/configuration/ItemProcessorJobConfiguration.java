package com.example.batch.configuration;

import com.example.batch.model.User;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.support.CompositeItemProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Arrays;
import java.util.List;

//@Configuration
public class ItemProcessorJobConfiguration {

    @Bean
    public Job job(
            JobRepository jobRepository,
            Step step
    ) {
        return new JobBuilder("itemReaderJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(step)
                .build();
    }

    @Bean
    public Step step(
            JobRepository jobRepository,
            PlatformTransactionManager platformTransactionManager,
            ItemReader<User> jpaCursorItemReader,
            ItemWriter<User> jpaItemWriter
    ) {
        // 여러 프로세스가 담긴 리스트 선언
        final List<ItemProcessor<User, User>> processors = Arrays.asList(
                processor1(),
                processor2(),
                processor3(),
                processor4()
        );

        return new StepBuilder("step", jobRepository)
                .<User, User>chunk(2, platformTransactionManager)
                .reader(jpaCursorItemReader)
                .processor(new CompositeItemProcessor<>(processors))
                .writer(jpaItemWriter)
                .build();
    }

    private ItemProcessor<User, String> customProcessor() {
        return user -> "%s의 나이는 %s입니다. 사는 곳은 %s, 전화번호는 %s".formatted(
                user.getName(), user.getAge(), user.getRegion(), user.getPhone()
        );
    }

    private ItemProcessor<User, User> processor1() {
        return user -> {
            user.setName(user.getName() + user.getName());
            return user;
        };
    }

    private ItemProcessor<User, User> processor2() {
        return user -> {
            user.setAge(user.getAge() + user.getAge());
            return user;
        };
    }

    private ItemProcessor<User, User> processor3() {
        return user -> {
            user.setRegion(user.getRegion() + user.getRegion());
            return user;
        };
    }

    private ItemProcessor<User, User> processor4() {
        return user -> {
            user.setPhone(user.getPhone() + user.getPhone());
            return user;
        };
    }

    @Bean
    public FlatFileItemReader<User> flatFileItemReader() {
        return new FlatFileItemReaderBuilder<User>()
                .name("flatFileItemReader")
                .resource(new ClassPathResource("users.txt"))
                .linesToSkip(2)
                .delimited().delimiter(",")
                .names("name", "age", "region", "phone")
                .targetType(User.class)
                .build();
    }

    @Bean
    public JpaItemWriter<User> jpaItemWriter(
            EntityManagerFactory entityManagerFactory
    ) {
        return new JpaItemWriterBuilder<User>()
                .entityManagerFactory(entityManagerFactory)
                .build();

    }
}
