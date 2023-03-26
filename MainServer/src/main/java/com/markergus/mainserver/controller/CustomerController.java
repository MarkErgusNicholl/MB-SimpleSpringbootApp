package com.markergus.mainserver.controller;

import com.markergus.mainserver.dto.CustomerCreationDto;
import com.markergus.mainserver.dto.CustomerDto;
import com.markergus.mainserver.dto.CustomerUpdateDto;
import com.markergus.mainserver.dto.LinkDeviceDto;
import com.markergus.mainserver.exceptions.DuplicateEntryException;
import com.markergus.mainserver.exceptions.NotFoundException;
import com.markergus.mainserver.service.CustomerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

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
            return customerService.createUser(customerCreationDto);
        } catch (DuplicateEntryException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Duplicate User Id found in customer table", e);
        }
    }

    @PutMapping(value = "/update/{userId}")
    public CustomerDto updateUser(@PathVariable String userId, @RequestBody CustomerUpdateDto customerUpdateDto) {
        try {
            return customerService.updateUser(userId, customerUpdateDto);
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
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format("Existing device already linked with customer"), e);
        }
    }

    @GetMapping(value = "/list", params = {"page"})
    public List<CustomerDto> getCustomers(@RequestParam("page") int page) {
        return customerService.getCustomers(page).getContent();
    }
}
