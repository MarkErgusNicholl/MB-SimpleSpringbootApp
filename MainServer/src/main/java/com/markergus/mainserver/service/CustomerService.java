package com.markergus.mainserver.service;

import com.markergus.mainserver.dto.CustomerCreationDto;
import com.markergus.mainserver.dto.CustomerDto;
import com.markergus.mainserver.dto.CustomerUpdateDto;
import com.markergus.mainserver.dto.LinkDeviceDto;
import com.markergus.mainserver.entity.Customer;
import com.markergus.mainserver.entity.Device;
import com.markergus.mainserver.exceptions.DuplicateEntryException;
import com.markergus.mainserver.exceptions.NotFoundException;
import com.markergus.mainserver.repository.CustomerRepository;
import com.markergus.mainserver.repository.DeviceRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final DeviceRepository deviceRepository;

    private final ModelMapper modelMapper;

    @Autowired
    public CustomerService(CustomerRepository customerRepository, DeviceRepository deviceRepository, ModelMapper modelMapper) {
        this.customerRepository = customerRepository;
        this.deviceRepository = deviceRepository;
        this.modelMapper = modelMapper;
    }

    public CustomerDto createUser(CustomerCreationDto customerCreationDto) throws DuplicateEntryException {
        if (customerRepository.existsByUserId(customerCreationDto.getUserId())) {
            throw new DuplicateEntryException();
        }
        Customer customer = new Customer();
        customer.setUserId(customerCreationDto.getUserId());
        customer.setFullName(customerCreationDto.getFullName());
        Customer result = customerRepository.save(customer);
        return modelMapper.map(result, CustomerDto.class);
    }

    public CustomerDto updateUser(String userId, CustomerUpdateDto customerUpdateDto) throws NotFoundException {
        if (!customerRepository.existsByUserId(userId)) {
            throw new NotFoundException();
        }
        Customer customer = customerRepository.findByUserId(userId);
        customer.setFullName(customerUpdateDto.getFullName());
        Customer updatedCustomer = customerRepository.save(customer);
        return modelMapper.map(updatedCustomer, CustomerDto.class);
    }

    /**
     * Transactional annotation especially important here, to keep whole function ACID
     */
    @Transactional(rollbackFor = DuplicateEntryException.class)
    public CustomerDto linkDevice(LinkDeviceDto linkDeviceDto) throws NotFoundException, DuplicateEntryException {
        if (!customerRepository.existsByUserId(linkDeviceDto.getUserId())) {
            throw new NotFoundException();
        }
        Customer customer = customerRepository.findByUserId(linkDeviceDto.getUserId());
        if (customer.getDevice() != null) {
            throw new DuplicateEntryException();
        }
        Device device = new Device();
        device.setDeviceId(linkDeviceDto.getDeviceId());
        deviceRepository.save(device);

        customer.setDevice(device);
        return modelMapper.map(customer, CustomerDto.class);
    }

    public Page<CustomerDto> getCustomers(int page) {
        return customerRepository.findAll(Pageable.ofSize(10).withPage(page)).map(customer -> modelMapper.map(customer, CustomerDto.class));
    }

}
