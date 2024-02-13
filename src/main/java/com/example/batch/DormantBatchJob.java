package com.example.batch;

import com.example.batch.customer.CustomerRepository;
import org.springframework.stereotype.Component;

@Component
public class DormantBatchJob {

    private final CustomerRepository customerRepository;

    private final EmailProcvider emailProcvider;

    public DormantBatchJob(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
        this.emailProcvider = new EmailProcvider.Fake();
    }

    public void execute() {
        /**
         * 1. 유저를 조회한다.
         * 2. 휴면 계정 대상을 추출 및 변한한다.
         * 3. 휴면 계정으로 상태를 변경한다.
         * 4. 메일을 보낸다.
         */
    }
}
