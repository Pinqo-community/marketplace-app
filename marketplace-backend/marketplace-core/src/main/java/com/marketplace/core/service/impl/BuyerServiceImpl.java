package com.marketplace.core.service.impl;

import com.marketplace.core.entity.Buyer;
import com.marketplace.core.repository.BuyerRepository;
import com.marketplace.core.service.BuyerService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class BuyerServiceImpl implements BuyerService {
    private final BuyerRepository buyerRepository;

    @Override
    public Buyer createBuyer(String firstname, String lastname) {
        log.debug("Enter createBuyer(firstname = {}, lastname = {})", firstname, lastname);

        Buyer buyer = buyerRepository.save(
                Buyer.builder()
                        .firstName(firstname)
                        .lastName(lastname)
                        .build()
        );

        log.debug("Leave createBuyer() - return {}", buyer);

        return buyer;
    }
}
