package com.marketplace.repository;

import com.marketplace.entity.InvalidRefreshToken;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;

public interface InvalidRefreshTokenRepository extends JpaRepository<InvalidRefreshToken, Long> {
    boolean existsByToken(String token);

    @Query("DELETE FROM InvalidRefreshToken t WHERE t.expiryDate < :date")
    @Modifying
    @Transactional
    void deleteAllExpiredBefore(@Param("date") Date date);
}
