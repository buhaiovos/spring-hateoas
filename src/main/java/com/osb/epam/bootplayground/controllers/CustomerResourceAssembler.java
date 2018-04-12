package com.osb.epam.bootplayground.controllers;

import com.osb.epam.bootplayground.entities.Customer;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.net.URI;

@Component
public class CustomerResourceAssembler implements ResourceAssembler<Customer, Resource<Customer>> {
    @Override
    public Resource<Customer> toResource(Customer customer) {
        Resource<Customer> customerResource = new Resource<>(customer);

        URI selfUri = MvcUriComponentsBuilder.fromMethodCall(
                MvcUriComponentsBuilder
                        .on(CustomerController.class)
                        .getCustomer(customer.getId())
        ).buildAndExpand().toUri();

        customerResource.add(new Link(selfUri.toString(), "self"));

        return customerResource;
    }
}
