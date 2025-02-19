package com.bank.accounts.customer.service;

import com.bank.accounts.account.service.AccountService;
import com.bank.accounts.address.model.Address;
import com.bank.accounts.address.repository.AddressRepository;
import com.bank.accounts.customer.dto.CustomerDto;
import com.bank.accounts.customer.dto.CustomerSearchDto;
import com.bank.accounts.customer.dto.UpdateCustomerDto;
import com.bank.accounts.customer.model.Customer;
import com.bank.accounts.customer.repository.CustomerRepository;
import com.bank.accounts.customer.service.mapper.CustomerMapper;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import static com.bank.accounts.common.Utils.updateIfNotNull;
import static java.lang.String.format;

@Service
public class CustomerService {
    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    AddressRepository addressRepository;

    @Autowired
    AccountService accountService;

    @Autowired
    CustomerMapper customerMapper;

    @Transactional
    public Customer create(CustomerDto customerDto) {
        validate(customerDto);

        String username = customerDto.username();
        Customer customer = customerMapper.toCustomer(customerDto);
        customer.setCreatedBy(username);

        for (Address address : customerDto.address()) {
            address.setCreatedBy(username);
            addressRepository.save(address);
        }

        Customer createdCustomer = customerRepository.save(customer);

        accountService.assignCustomer(customer, customerDto.accountId(), customerDto.username());

        return createdCustomer;
    }

    @Transactional
    public Customer update(Long id, UpdateCustomerDto updateCustomerDto) {
        Customer existingCustomer = customerRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(format("Customer with id: %s not found", id)));
        updateIfNotNull(updateCustomerDto.name(), existingCustomer::setName);
        updateIfNotNull(updateCustomerDto.lastName(), existingCustomer::setLastName);
        updateIfNotNull(updateCustomerDto.phoneNumber(), existingCustomer::setPhoneNumber);
        updateIfNotNull(updateCustomerDto.email(), existingCustomer::setEmail);
        updateIfNotNull(updateCustomerDto.customerType(), existingCustomer::setCustomerType);

        if (updateCustomerDto.address() != null && !updateCustomerDto.address().isEmpty()) {
            existingCustomer.updateAddress(updateCustomerDto.address());
        }

        existingCustomer.setLastModifiedBy(updateCustomerDto.username());

        return customerRepository.save(existingCustomer);
    }

    public CustomerSearchDto searchCustomers(String searchTerm, int pageNumber, int rowNumber) {
        Pageable pageable = PageRequest.of(pageNumber, rowNumber);

        Page<Customer> customerPage = customerRepository.findCustomers(searchTerm, pageable);

        return CustomerSearchDto.builder()
                .customers(customerPage.getContent())
                .totalCount(customerPage.getTotalElements())
                .build();
    }

    private void validate(CustomerDto customerDto) {
        boolean customerExists = customerRepository.existsByNameAndLastNameAndPhoneNumberAndEmail(
                customerDto.name(), customerDto.lastName(), customerDto.phoneNumber(), customerDto.email());

        if (customerExists) {
            throw new EntityExistsException(
                    format("Can't create. Customer: %s %s, already exists.", customerDto.name(),
                            customerDto.lastName()));
        }
    }
}
