package com.marketplace.service;

import com.marketplace.entity.Buyer;

public interface BuyerService {
    Buyer createBuyer(String firstname, String lastname);
}
