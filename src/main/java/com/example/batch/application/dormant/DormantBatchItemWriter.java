package com.example.batch.application.dormant;

import com.example.batch.EmailProvider;
import com.example.batch.batches.ItemWriter;
import com.example.batch.customer.Customer;
import com.example.batch.customer.CustomerRepository;
import org.springframework.stereotype.Component;

@Component
public class DormantBatchItemWriter implements ItemWriter<Customer> {
    private final CustomerRepository customerRepository;

    private final EmailProvider emailProvider;

    public DormantBatchItemWriter(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
        this.emailProvider = new EmailProvider.Fake();
    }

    @Override
    public void write(Customer item) {
        // 3. 휴면 계정으로 상태를 변경
        customerRepository.save(item);

        // 4. 메일 전송
        emailProvider.send(item.getEmail(), "휴면 전환 안내 메일입니다.", "내용");
    }
}
