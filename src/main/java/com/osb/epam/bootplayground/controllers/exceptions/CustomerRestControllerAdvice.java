package com.osb.epam.bootplayground.controllers.exceptions;

import org.springframework.hateoas.VndErrors;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Optional;

@RestControllerAdvice
public class CustomerRestControllerAdvice {
    private final MediaType vndErrorMediaType = MediaType.parseMediaType("application/vnd.error+json");

    @ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<VndErrors> customerNotFoundHandler(CustomerNotFoundException e) {
        return this.error(e, HttpStatus.NOT_FOUND, e.getUserId().toString());
    }

    private <E extends Exception> ResponseEntity<VndErrors> error(E error, HttpStatus httpStatus, String lofRef) {
        String message = Optional.of(error.getMessage()).orElse(error.getClass().getSimpleName());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(this.vndErrorMediaType);

        return new ResponseEntity<>(new VndErrors(lofRef, message), headers, httpStatus);
    }
}
