package com.example.bcpp.repository;

import com.example.bcpp.model.Card;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CardRepository extends JpaRepository<Card, Long> {
    Optional<Card> findByUserId(Long userId);
    Optional<Card> findByCardNumber(String cardNumber);
}
