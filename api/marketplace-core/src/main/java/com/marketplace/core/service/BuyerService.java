package com.marketplace.core.service;

import com.marketplace.core.entity.Buyer;

public interface BuyerService {
    Buyer createBuyer(String firstname, String lastname);
}
