package com.marketplace.service.impl;

import com.marketplace.entity.Buyer;
import com.marketplace.repository.BuyerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BuyerServiceImplTest {
    @Mock
    private BuyerRepository buyerRepository;

    @InjectMocks
    private BuyerServiceImpl buyerService;

    @Test
    void createBuyer_WithValidData_ShouldReturnSavedBuyer() {
        String firstName = "John";
        String lastName = "Doe";

        Buyer expectedBuyer = Buyer.builder()
                .firstName(firstName)
                .lastName(lastName)
                .build();

        when(buyerRepository.save(any(Buyer.class))).thenReturn(expectedBuyer);

        Buyer result = buyerService.createBuyer(firstName, lastName);

        assertNotNull(result);
        assertEquals(firstName, result.getFirstName());
        assertEquals(lastName, result.getLastName());
        verify(buyerRepository).save(any(Buyer.class));
    }
}