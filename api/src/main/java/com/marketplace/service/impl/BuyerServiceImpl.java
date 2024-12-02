package com.marketplace.service.impl;

import com.marketplace.entity.Buyer;
import com.marketplace.repository.BuyerRepository;
import com.marketplace.service.BuyerService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class BuyerServiceImpl implements BuyerService {
    private final BuyerRepository buyerRepository;

    @Override
    public Buyer createBuyer(String firstname, String lastname) {
        return buyerRepository.save(
                Buyer.builder()
                        .firstName(firstname)
                        .lastName(lastname)
                        .build()
        );
    }
}
