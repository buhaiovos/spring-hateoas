package com.osb.epam.bootplayground.repositories;

import com.osb.epam.bootplayground.entities.Customer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends CrudRepository<Customer, Long> {
}
