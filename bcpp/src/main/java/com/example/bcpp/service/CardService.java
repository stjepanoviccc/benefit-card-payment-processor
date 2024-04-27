package com.example.bcpp.service;

import com.example.bcpp.dto.CardDTO;
import com.example.bcpp.model.User;

import java.util.List;

public interface CardService {

    List<CardDTO> findAll();
    CardDTO create(User user);
}
