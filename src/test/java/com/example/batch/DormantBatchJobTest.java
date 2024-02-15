package com.example.batch;

import com.example.batch.customer.Customer;
import com.example.batch.customer.CustomerRepository;
import com.example.batch.customer.batches.BatchStatus;
import com.example.batch.customer.batches.JobExecution;
import com.example.batch.customer.enums.CustomerStatus;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.UUID;


@SpringBootTest
class DormantBatchJobTest {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private DormantBatchJob dormantBatchJob;

    @BeforeEach
    public void setup() {
        customerRepository.deleteAll();
    }

    @Test
    @DisplayName("로그인 시간이 1년을 경과한 고객이 세 명이고, 1년 이내 로그인한 고객이 5명이면 3명의 고객이 휴면 전환 대상이다.")
    void test1() {
        // given
        // 365일 전에 로그인한 객체 생성 --> 휴면 계정
        saveCustomer(366L);
        saveCustomer(366L);
        saveCustomer(366L);

        // 364일 전에 로그인한 객체 생성
        saveCustomer(364L);
        saveCustomer(364L);
        saveCustomer(364L);
        saveCustomer(364L);
        saveCustomer(364L);

        // when
        final JobExecution result = dormantBatchJob.execute();

        // then
        final Long dormantCount = customerRepository.findAll()
                .stream()
                .filter(it -> it.getStatus() == CustomerStatus.DORMANT)
                .count();

        Assertions.assertThat(dormantCount).isEqualTo(3);
        Assertions.assertThat(result.getBatchStatus()).isEqualTo(BatchStatus.COMPLETED);
    }

    @Test
    @DisplayName("고객이 10명이 있지만, 모두 다 휴면전환 대상이면 휴면 전환 대상은 10명이다.")
    void test2() {
        // given
        // 1000일 전에 로그인한 객체 생성
        saveCustomer(1000L);
        saveCustomer(1000L);
        saveCustomer(1000L);
        saveCustomer(1000L);
        saveCustomer(1000L);
        saveCustomer(1000L);
        saveCustomer(1000L);
        saveCustomer(1000L);
        saveCustomer(1000L);
        saveCustomer(1000L);

        // when
        final JobExecution result = dormantBatchJob.execute();

        // then
        final Long dormantCount = customerRepository.findAll()
                .stream()
                .filter(it -> it.getStatus() == CustomerStatus.DORMANT)
                .count();

        Assertions.assertThat(dormantCount).isEqualTo(10);
        Assertions.assertThat(result.getBatchStatus()).isEqualTo(BatchStatus.COMPLETED);

    }

    @Test
    @DisplayName("고객이 없는 경우에도 배치 프로그램은 정상 작동 해야한다.")
    void test3() {
        // when
        final JobExecution result = dormantBatchJob.execute();

        // then
        final Long dormantCount = customerRepository.findAll()
                .stream()
                .filter(it -> it.getStatus() == CustomerStatus.DORMANT)
                .count();

        Assertions.assertThat(dormantCount).isEqualTo(0);
        Assertions.assertThat(result.getBatchStatus()).isEqualTo(BatchStatus.COMPLETED);

    }

    @Test
    @DisplayName("배치가 실패하면 BatchStatus는 FAILED를 반환해야 한다.")
    void test4() {
        // given
        final DormantBatchJob dormantBatchJob = new DormantBatchJob(null);

        // when
        final JobExecution result = dormantBatchJob.execute();

        // then
        Assertions.assertThat(result.getBatchStatus()).isEqualTo(BatchStatus.FAILED);

    }

    private void saveCustomer(Long loginMinusDays) {
        final String uuid = UUID.randomUUID().toString(); // 랜덤 아이디 생성
        final Customer customer = new Customer(uuid, uuid + "@naver.com"); // 랜덤 객체 생성
        customer.setLoginAt(LocalDateTime.now().minusDays(loginMinusDays));
        customerRepository.save(customer); // DB에 저장
    }


}