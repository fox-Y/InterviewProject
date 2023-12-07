package com.example.demo.service;

import com.example.demo.model.Customer;
import org.springframework.stereotype.Service;

@Service
public class SsnValidator implements Validator{

    @Override
    public boolean validate(Customer customer) {

        // Assume if there's some exception rise in SsnValidator server
        //throw new RuntimeException("SSN Internal RuntimeException");

        // Validate customer.ssn by calling external service
        return true;
    }
}
