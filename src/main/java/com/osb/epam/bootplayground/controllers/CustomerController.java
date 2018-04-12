package com.osb.epam.bootplayground.controllers;

import com.osb.epam.bootplayground.controllers.exceptions.CustomerNotFoundException;
import com.osb.epam.bootplayground.entities.Customer;
import com.osb.epam.bootplayground.repositories.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping(value = "/v2", produces = "application/hal+json")
public class CustomerController {
    private final CustomerRepository customerRepository;
    private final CustomerResourceAssembler resourceAssembler;

    @Autowired
    public CustomerController(CustomerRepository customerRepository,
                              CustomerResourceAssembler resourceAssembler) {
        this.customerRepository = customerRepository;
        this.resourceAssembler = resourceAssembler;
    }

    @GetMapping
    ResponseEntity<Resources<Object>> root() {
        Resources<Object> objects = new Resources<>(Collections.emptyList());
        URI uri = MvcUriComponentsBuilder
                .fromMethodCall(MvcUriComponentsBuilder.on(getClass()).getCustomers())
                .build().toUri();
        Link link = new Link(uri.toString(), "customers");
        objects.add(link);
        return ResponseEntity.ok(objects);
    }

    @GetMapping("/customers")
    public ResponseEntity<Resources<Resource<Customer>>> getCustomers() {
        Iterable<Customer> allCustomers = customerRepository.findAll();

        List<Resource<Customer>> customerResourceList = StreamSupport.stream(allCustomers.spliterator(), false)
                .map(resourceAssembler::toResource)
                .collect(Collectors.toList());

        Resources<Resource<Customer>> resources = new Resources<>(customerResourceList);
        URI self = ServletUriComponentsBuilder.fromCurrentRequest().build().toUri();
        resources.add(new Link(self.toString(), "self"));

        return ResponseEntity.ok(resources);
    }

    @GetMapping("/customer/{id}")
    public ResponseEntity<Resource<Customer>> getCustomer(@PathVariable("id") Long id) {
        return customerRepository.findById(id)
                .map(customer -> ResponseEntity.ok(resourceAssembler.toResource(customer)))
                .orElseThrow(() -> new CustomerNotFoundException(id));
    }
}
