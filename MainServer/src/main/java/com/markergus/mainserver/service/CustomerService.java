package com.markergus.mainserver.service;

import com.markergus.mainserver.dto.*;
import com.markergus.mainserver.entity.Customer;
import com.markergus.mainserver.entity.Device;
import com.markergus.mainserver.exceptions.DuplicateEntryException;
import com.markergus.mainserver.exceptions.NoDeviceLinkedException;
import com.markergus.mainserver.exceptions.NotFoundException;
import com.markergus.mainserver.repository.CustomerRepository;
import com.markergus.mainserver.repository.DeviceRepository;
import com.markergus.mainserver.util.client.GenTokenClient;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final DeviceRepository deviceRepository;
    private final ModelMapper modelMapper;

    private final GenTokenClient genTokenClient;

    @Autowired
    public CustomerService(CustomerRepository customerRepository, DeviceRepository deviceRepository, GenTokenClient genTokenClient, ModelMapper modelMapper) {
        this.customerRepository = customerRepository;
        this.deviceRepository = deviceRepository;
        this.genTokenClient = genTokenClient;
        this.modelMapper = modelMapper;
    }

    public CustomerDto createCustomer(CustomerCreationDto customerCreationDto) throws DuplicateEntryException {
        if (customerRepository.existsByUserId(customerCreationDto.getUserId())) {
            throw new DuplicateEntryException();
        }
        Customer customer = new Customer();
        customer.setUserId(customerCreationDto.getUserId());
        customer.setFullName(customerCreationDto.getFullName());
        Customer result = customerRepository.save(customer);
        return modelMapper.map(result, CustomerDto.class);
    }

    public CustomerDto updateCustomer(String userId, CustomerUpdateDto customerUpdateDto) throws NotFoundException {
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
    @Transactional(rollbackFor = Exception.class)
    public CustomerDto linkDevice(LinkDeviceDto linkDeviceDto) throws NotFoundException, DuplicateEntryException {
        if (!customerRepository.existsByUserId(linkDeviceDto.getUserId())) {
            throw new NotFoundException();
        }
        if (deviceRepository.existsByDeviceId(linkDeviceDto.getDeviceId())) {
            throw new DuplicateEntryException();
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

    public TokenResponseDto generateToken(CustomerIdDto customerIdDto) throws NotFoundException, NoDeviceLinkedException {
        if (!customerRepository.existsByUserId(customerIdDto.getUserId())) {
            throw new NotFoundException();
        }
        Customer customer = customerRepository.findByUserId(customerIdDto.getUserId());
        if (customer.getDevice() == null) {
            throw new NoDeviceLinkedException();
        }

        GenTokenRequestDto genTokenRequestDto = new GenTokenRequestDto();
        genTokenRequestDto.setUserId(customer.getUserId());
        genTokenRequestDto.setDeviceId(customer.getDevice().getDeviceId());
        Mono<String> result = genTokenClient.submitGenTokenRequest(genTokenRequestDto);
        TokenResponseDto tokenResponseDto = new TokenResponseDto();
        tokenResponseDto.setToken(result.block());
        return tokenResponseDto;
    }

}
