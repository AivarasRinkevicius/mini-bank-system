package com.bank.accounts.customer.dto;

import com.bank.accounts.customer.model.Customer;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class CustomerSearchDto {
    long totalCount;
    List<Customer> customers;
}
