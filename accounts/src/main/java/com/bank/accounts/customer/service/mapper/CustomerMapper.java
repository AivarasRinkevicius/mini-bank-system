package com.bank.accounts.customer.service.mapper;

import com.bank.accounts.customer.dto.CustomerDto;
import com.bank.accounts.customer.model.Customer;
import org.springframework.stereotype.Service;

@Service
public class CustomerMapper {
    public Customer toCustomer(CustomerDto customerDto) {
        return Customer.builder()
                .name(customerDto.name())
                .lastName(customerDto.lastName())
                .phoneNumber(customerDto.phoneNumber())
                .email(customerDto.email())
                .address(customerDto.address())
                .customerType(customerDto.customerType())
                .build();
    }
}
