package com.bank.accounts.customer.repository;

import com.bank.accounts.customer.model.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends CrudRepository<Customer, Long> {

    boolean existsByNameAndLastNameAndPhoneNumberAndEmail(
            String name, String lastName, String phoneNumber, String email);

    @Query("SELECT customer FROM Customer customer " +
            "LEFT JOIN customer.address address " +
            "WHERE LOWER(customer.name) LIKE LOWER(concat('%', :searchTerm, '%')) " +
            "OR LOWER(customer.lastName) LIKE LOWER(concat('%', :searchTerm, '%')) " +
            "OR LOWER(customer.phoneNumber) LIKE LOWER(concat('%', :searchTerm, '%')) " +
            "OR LOWER(customer.email) LIKE LOWER(concat('%', :searchTerm, '%'))" +
            "OR LOWER(address.country) LIKE LOWER(concat('%', :searchTerm, '%'))" +
            "OR LOWER(address.city) LIKE LOWER(concat('%', :searchTerm, '%'))" +
            "OR LOWER(address.street) LIKE LOWER(concat('%', :searchTerm, '%'))")
    Page<Customer> findCustomers(@Param("searchTerm") String searchTerm, Pageable pageable);
}
