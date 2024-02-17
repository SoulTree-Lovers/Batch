package com.example.batch.application.dormant;

import com.example.batch.batches.ItemProcessor;
import com.example.batch.customer.Customer;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class PreDormantBatchItemProcessor implements ItemProcessor<Customer, Customer> {
    @Override
    public Customer process(Customer customer) {
        final LocalDate targetDate = LocalDate.now()
                .minusYears(1)
                .plusDays(7);

        if (targetDate.equals(customer.getLoginAt().toLocalDate())) {
            return customer;
        } else {
            return null;
        }
    }
}
