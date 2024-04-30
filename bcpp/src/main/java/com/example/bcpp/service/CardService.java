package com.example.bcpp.service;

import com.example.bcpp.dto.CardDTO;
import com.example.bcpp.model.Card;
import com.example.bcpp.model.User;

import java.util.List;

public interface CardService {
    Card getModel(Long id);
    Card getModelByUserId(Long userId);
    List<CardDTO> findAll();
    CardDTO create(User user);
    CardDTO update(Long cardId, Double amount, String cardUpdateStatus, Long userId);
}
