package com.example.batch.application;

import com.example.batch.batches.ItemProcessor;
import com.example.batch.customer.Customer;
import com.example.batch.customer.enums.CustomerStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class DormantBatchItemProcessor implements ItemProcessor<Customer, Customer> {
    @Override
    public Customer process(Customer item) {
        // 2. 휴면 계정 대상을 추출 및 변환
        //     로그인 날짜     -      365일 전      -     오늘
        final boolean isDormantTarget = LocalDate.now() // 오늘 날짜로부터 365일 전 날짜가, 로그인한 이후라면 휴면 계정 (오늘 - 365 > 마지막 로그인 == 휴면)
                .minusDays(365)
                .isAfter(item.getLoginAt().toLocalDate());

        if (isDormantTarget) {
            item.setStatus(CustomerStatus.DORMANT);
            return item;
        } else {
            return null;
        }
    }
}
