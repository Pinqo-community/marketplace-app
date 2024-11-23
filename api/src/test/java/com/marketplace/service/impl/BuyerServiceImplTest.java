package com.marketplace.service.impl;

import com.marketplace.entity.Buyer;
import com.marketplace.repository.BuyerRepository;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BuyerServiceImplTest {
    @Mock
    private BuyerRepository buyerRepository;

    @InjectMocks
    private BuyerServiceImpl buyerService;

    @Nested
    class CreateBuyer {
        @Test
        void createBuyer_WithValidData_ShouldReturnSavedBuyer() {
            // Given
            String firstName = "John";
            String lastName = "Doe";

            Buyer buyer = mock(Buyer.class);

            when(buyerRepository.save(any(Buyer.class))).thenReturn(buyer);

            // When
            Buyer result = buyerService.createBuyer(firstName, lastName);

            // Then
            assertNotNull(result);
            verify(buyerRepository).save(any(Buyer.class));
        }
    }
}