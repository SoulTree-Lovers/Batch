package com.example.batch;

import com.example.batch.customer.Customer;
import com.example.batch.customer.CustomerRepository;
import com.example.batch.customer.enums.CustomerStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class DormantBatchJob {

    private final CustomerRepository customerRepository;

    private final EmailProvider emailProvider;

    public DormantBatchJob(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
        this.emailProvider = new EmailProvider.Fake();
    }

    public void execute() {
        /**
         * 1. 유저를 조회한다.
         * 2. 휴면 계정 대상을 추출 및 변환한다.
         * 3. 휴면 계정으로 상태를 변경한다.
         * 4. 메일을 보낸다.
         */

        int pageNo = 0;

        while (true) {
            // 1. 유저 조회
            final PageRequest pageRequest = PageRequest.of(pageNo, 1, Sort.by("id").ascending()); // 한 페이지씩 가져오기
            final Page<Customer> page = customerRepository.findAll(pageRequest);
            final Customer customer;

            if (page.isEmpty()) {
                break; // 페이지에 데이터가 없다면 종료
            } else {
                pageNo++;
                customer = page.getContent().get(0); // 1개씩 가져오므로 0번째 데이터를 가져온다.
            }

            // 2. 휴면 계정 대상을 추출 및 변환
            //     로그인 날짜     -      365일 전      -     오늘
            final boolean isDormantTarget = LocalDate.now() // 오늘 날짜로부터 365일 전 날짜가, 로그인한 이후라면 휴면 계정 (오늘 - 365 > 마지막 로그인 == 휴면)
                    .minusDays(365)
                    .isAfter(customer.getLoginAt().toLocalDate());

            if (isDormantTarget) {
                customer.setStatus(CustomerStatus.DORMANT);
            } else {
                continue;
            }

            // 3. 휴면 계정으로 상태를 변경
            customerRepository.save(customer);

            // 4. 메일 전송
            emailProvider.send(customer.getEmail(), "휴면 전환 안내 메일입니다.", "내용");
        }
    }
}
