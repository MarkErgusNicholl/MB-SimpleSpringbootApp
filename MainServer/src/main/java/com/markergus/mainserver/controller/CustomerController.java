package com.markergus.mainserver.controller;

import com.markergus.mainserver.dto.*;
import com.markergus.mainserver.exceptions.DuplicateEntryException;
import com.markergus.mainserver.exceptions.NoDeviceLinkedException;
import com.markergus.mainserver.exceptions.NotFoundException;
import com.markergus.mainserver.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * Entry point for core customer functions
 */
@RestController
@RequestMapping("api/v1/customer")
public class CustomerController {

    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping(value = "/create")
    public CustomerDto createUser(@RequestBody CustomerCreationDto customerCreationDto) {
        try {
            return customerService.createCustomer(customerCreationDto);
        } catch (DuplicateEntryException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Duplicate User Id found in customer table", e);
        }
    }

    @PutMapping(value = "/update/{userId}")
    public CustomerDto updateUser(@PathVariable String userId, @RequestBody CustomerUpdateDto customerUpdateDto) {
        try {
            return customerService.updateCustomer(userId, customerUpdateDto);
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format("No Customer with user id: %s found", userId), e);
        }
    }

    @PostMapping(value = "/linkDevice")
    public CustomerDto linkDevice(@RequestBody LinkDeviceDto linkDeviceDto) {
        try {
            return customerService.linkDevice(linkDeviceDto);
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format("No Customer with user id: %s found", linkDeviceDto.getUserId()), e);
        } catch (DuplicateEntryException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Existing device already linked with a customer", e);
        }
    }

    @GetMapping(value = "/list", params = {"page"})
    public List<CustomerDto> getCustomers(@RequestParam("page") int page) {
        return customerService.getCustomers(page).getContent();
    }

    @PostMapping(value = "/genToken")
    public TokenResponseDto generateToken(@RequestBody CustomerIdDto customerIdDto) {
        System.out.println(customerIdDto.getUserId());
        try {
            return customerService.generateToken(customerIdDto);
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Customer not found", e);
        } catch (NoDeviceLinkedException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No device linked to customer", e);
        }
    }
}
