package com.osb.epam.bootplayground.controllers.exceptions;

public class CustomerNotFoundException extends RuntimeException {

    public final Long id;

    public CustomerNotFoundException(Long id) {
        super("Customer with id = <" + id + "> is not found");
        this.id = id;
    }

    public Long getUserId() {
        return this.id;
    }
}
