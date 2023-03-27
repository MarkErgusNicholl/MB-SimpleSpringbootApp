package com.markergus.mainserver.repository;

import com.markergus.mainserver.entity.Customer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface CustomerRepository extends PagingAndSortingRepository<Customer, Long>, CrudRepository<Customer, Long> {
    public boolean existsByUserId(String userId);

    public Customer findByUserId(String userId);

}
