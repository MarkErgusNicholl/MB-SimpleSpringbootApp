package com.markergus.mainserver.service;

import com.markergus.mainserver.dto.CustomerCreationDto;
import com.markergus.mainserver.dto.CustomerUpdateDto;
import com.markergus.mainserver.dto.LinkDeviceDto;
import com.markergus.mainserver.entity.Customer;
import com.markergus.mainserver.entity.Device;
import com.markergus.mainserver.exceptions.DuplicateEntryException;
import com.markergus.mainserver.exceptions.NotFoundException;
import com.markergus.mainserver.repository.CustomerRepository;
import com.markergus.mainserver.repository.DeviceRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
public class CustomerServiceTests {

    private final String EXISTING_USER_ID = "12345";
    private final String NON_EXISTING_USER_ID = "67890";

    private final String EXISTING_DEVICE_ID = "asdfg";
    private final String NON_EXISTING_DEVICE_ID = "riruwr";

    private final String LINKED_DEVICE_ID = "LINKED";

    @MockBean
    private CustomerRepository customerRepository;

    @MockBean
    private DeviceRepository deviceRepository;

    @Autowired
    private CustomerService customerService;

    @BeforeEach
    public void setup() {
        Mockito.when(customerRepository.existsByUserId(EXISTING_USER_ID)).thenReturn(true);
        Mockito.when(customerRepository.existsByUserId(NON_EXISTING_USER_ID)).thenReturn(false);
        Mockito.when(deviceRepository.existsByDeviceId(EXISTING_DEVICE_ID)).thenReturn(true);
        Mockito.when(deviceRepository.existsByDeviceId(NON_EXISTING_DEVICE_ID)).thenReturn(false);
    }

    @Test
    public void createCustomer_UserIdAlreadyExists_ShouldThrowDuplicateEntryException() {
        CustomerCreationDto customerCreationDto = new CustomerCreationDto();
        customerCreationDto.setUserId(EXISTING_USER_ID);
        Assertions.assertThrows(DuplicateEntryException.class, () -> customerService.createCustomer(customerCreationDto));
    }

    @Test
    public void updateCustomer_UserIdNotFound_ShouldThrowNotFoundExeption() {
        CustomerUpdateDto customerUpdateDto = new CustomerUpdateDto();
        Assertions.assertThrows(NotFoundException.class, () -> customerService.updateCustomer(NON_EXISTING_USER_ID, customerUpdateDto));
    }

    @Test
    public void linkDevice_UserIdNotFound_ShouldThrowNotFoundException() {
        LinkDeviceDto linkDeviceDto = new LinkDeviceDto();
        linkDeviceDto.setUserId(NON_EXISTING_USER_ID);
        linkDeviceDto.setDeviceId(NON_EXISTING_DEVICE_ID);

        Assertions.assertThrows(NotFoundException.class, () -> customerService.linkDevice(linkDeviceDto));
    }

    @Test
    public void linkDevice_CustomerHasLinkedDevice_ShouldThrowDuplicateEntryException() {
        Mockito.when(customerRepository.findByUserId(EXISTING_USER_ID)).thenReturn(createDefaultCustomer(LINKED_DEVICE_ID));

        LinkDeviceDto linkDeviceDto = new LinkDeviceDto();
        linkDeviceDto.setUserId(EXISTING_USER_ID);
        linkDeviceDto.setDeviceId(LINKED_DEVICE_ID);

        Assertions.assertThrows(DuplicateEntryException.class, () -> customerService.linkDevice(linkDeviceDto));
    }

    private Customer createDefaultCustomer(String linkedDeviceId) {
        Device device = null;
        if (linkedDeviceId != null) {
            device = new Device();
            device.setDeviceId(linkedDeviceId);
        }
        Customer customer = new Customer();
        customer.setUserId(EXISTING_USER_ID);
        customer.setDevice(device);
        return customer;
    }

}
