package com.example.batch.application;

import com.example.batch.batches.ItemReader;
import com.example.batch.customer.Customer;
import com.example.batch.customer.CustomerRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

@Component
public class DormantBatchItemReader implements ItemReader<Customer> {

    private final CustomerRepository customerRepository;

    private int pageNo = 0;

    public DormantBatchItemReader(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public Customer read() {
        // 1. 유저 조회
        final PageRequest pageRequest = PageRequest.of(pageNo, 1, Sort.by("id").ascending()); // 한 페이지씩 가져오기
        final Page<Customer> page = customerRepository.findAll(pageRequest);
        final Customer customer;

        if (page.isEmpty()) {
            pageNo = 0;
            return null; // 페이지에 데이터가 없다면 종료
        } else {
            pageNo++;
            return page.getContent().get(0); // 1개씩 가져오므로 0번째 데이터를 가져온다.
        }
    }
}
