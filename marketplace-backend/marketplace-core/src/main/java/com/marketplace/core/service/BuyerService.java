package com.marketplace.core.service;

import com.marketplace.core.entity.Buyer;

/**
 * Service interface for managing buyer operations.
 */
public interface BuyerService {
    /**
     * Creates a new buyer.
     *
     * @param firstname buyer's first name
     * @param lastname buyer's last name
     * @return created buyer entity
     */
    Buyer createBuyer(String firstname, String lastname);
}
