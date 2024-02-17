package com.example.batch.application.dormant;

import com.example.batch.customer.Customer;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class PreDormantBatchItemProcessorTest {

    private PreDormantBatchItemProcessor preDormantBatchItemProcessor;

    @BeforeEach
    void setup() {
        this.preDormantBatchItemProcessor = new PreDormantBatchItemProcessor();
    }
    @Test
    @DisplayName("로그인 날짜가 오늘로부터 358일 전(휴면 전환 일주일 전)이면 customer를 반환해야한다.")
    void test1() {
        // given
        final Customer customer1 = new Customer("c1", "c1@naver.com");
        // 마지막으로 로그인한 시점을: 현재 날짜 - 1년 + 7일로 설정
        customer1.setLoginAt(LocalDateTime.now().minusYears(1).plusDays(7));

        // when
        final Customer result = preDormantBatchItemProcessor.process(customer1);

        // then
        Assertions.assertThat(result).isEqualTo(customer1);
        Assertions.assertThat(result).isNotNull();

    }

    @Test
    @DisplayName("로그인 날짜가 오늘로부터 358일 전(휴면 전환 일주일 전)이 아니면 null을 반환해야한다.")
    void test2() {
        // given
        final Customer customer1 = new Customer("c1", "c1@naver.com");

        // when
        final Customer result = preDormantBatchItemProcessor.process(customer1);

        // then
        Assertions.assertThat(result).isNull();

    }



}