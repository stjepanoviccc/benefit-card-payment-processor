package com.example.bcpp.service;

import com.example.bcpp.dto.CardDTO;
import com.example.bcpp.model.Card;
import com.example.bcpp.model.User;
import com.example.bcpp.model.enums.CardUpdateStatus;

import java.util.List;

public interface CardService {
    Card getModel(Long id);
    List<CardDTO> findAll();
    CardDTO create(User user);
    CardDTO update(Long cardId, Double amount, String cardUpdateStatus);
}
