package com.example.bcpp.service;

import com.example.bcpp.dto.CardDTO;
import com.example.bcpp.model.Card;
import com.example.bcpp.model.User;

public interface CardService {
    Card getModel(Long id);
    Card getModelByUserId(Long userId);
    CardDTO create(User user);
    CardDTO update(Long cardId, Double amount, String cardUpdateStatus, Long userId);
}
