package com.bank.accounts.customer.controller;

import com.bank.accounts.customer.dto.CustomerDto;
import com.bank.accounts.customer.dto.CustomerSearchDto;
import com.bank.accounts.customer.dto.UpdateCustomerDto;
import com.bank.accounts.customer.model.Customer;
import com.bank.accounts.customer.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    @Autowired
    CustomerService customerService;

    @PostMapping
    public ResponseEntity<Customer> create(@RequestBody CustomerDto request) {
        Customer createdCustomer = customerService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCustomer);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Customer> updateCustomer(
            @PathVariable Long id,
            @RequestBody UpdateCustomerDto updateCustomerDto) {
        Customer updatedCustomer = customerService.update(id, updateCustomerDto);
        return ResponseEntity.ok(updatedCustomer);
    }

    @GetMapping("/search")
    public CustomerSearchDto searchCustomers(
            @RequestParam String searchTerm,
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "10") int rowNumber) {
        return customerService.searchCustomers(searchTerm, pageNumber, rowNumber);
    }
}
