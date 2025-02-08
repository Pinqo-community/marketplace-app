package com.marketplace.core.service.impl;

import com.marketplace.core.entity.Buyer;
import com.marketplace.core.repository.BuyerRepository;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BuyerServiceImplTest {

    @Mock
    private BuyerRepository buyerRepository;

    @InjectMocks
    private BuyerServiceImpl buyerService;

    @Nested
    class CreateBuyer {
        @Test
        void whenValidData_thenReturnSavedBuyer() {
            // Given
            String firstName = "John";
            String lastName = "Doe";

            Buyer savedBuyer = Buyer.builder()
                    .id(1L)
                    .firstName(firstName)
                    .lastName(lastName)
                    .build();

            when(buyerRepository.save(any(Buyer.class))).thenReturn(savedBuyer);

            // When
            Buyer result = buyerService.createBuyer(firstName, lastName);

            // Then
            assertThat(result)
                    .isNotNull()
                    .satisfies(buyer -> {
                        assertThat(buyer.getId()).isEqualTo(1L);
                        assertThat(buyer.getFirstName()).isEqualTo(firstName);
                        assertThat(buyer.getLastName()).isEqualTo(lastName);
                    });

            verify(buyerRepository).save(argThat(buyer ->
                    buyer.getFirstName().equals(firstName) &&
                            buyer.getLastName().equals(lastName)
            ));
        }

        @Test
        void whenNullValues_thenSaveWithNullValues() {
            // Given
            Buyer savedBuyer = Buyer.builder()
                    .id(1L)
                    .firstName(null)
                    .lastName(null)
                    .build();

            when(buyerRepository.save(any(Buyer.class))).thenReturn(savedBuyer);

            // When
            Buyer result = buyerService.createBuyer(null, null);

            // Then
            assertThat(result)
                    .isNotNull()
                    .satisfies(buyer -> {
                        assertThat(buyer.getId()).isEqualTo(1L);
                        assertThat(buyer.getFirstName()).isNull();
                        assertThat(buyer.getLastName()).isNull();
                    });

            verify(buyerRepository).save(argThat(buyer ->
                    buyer.getFirstName() == null &&
                            buyer.getLastName() == null
            ));
        }
    }
}