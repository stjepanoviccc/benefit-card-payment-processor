package com.example.bcpp.service.impl;

import com.example.bcpp.dto.CardDTO;
import com.example.bcpp.exception.BadRequestException;
import com.example.bcpp.model.Card;
import com.example.bcpp.model.User;
import com.example.bcpp.repository.CardRepository;
import com.example.bcpp.service.CardService;
import com.example.bcpp.utils.GenerateCardNumber;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.example.bcpp.dto.CardDTO.convertToDto;

@Service
@RequiredArgsConstructor
public class CardServiceImpl implements CardService {

    private final CardRepository cardRepository;

    @Override
    public List<CardDTO> findAll() {
        List<Card> cards = cardRepository.findAll();
        return cards.stream()
                .map(CardDTO::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public CardDTO create(User user) {
        String generatedCardNumber = GenerateCardNumber.generateCardNumber();
        cardRepository.findByCardNumber(generatedCardNumber)
                .ifPresent(dto -> {
                    throw new BadRequestException(String.format("Card with this number %s is already in database", generatedCardNumber));
                });;
        Card card = new Card();
        card.setCardNumber(generatedCardNumber);
        card.setUser(user);
        card.setTotalAmount(0.0);
        return convertToDto(cardRepository.save(card));
    }
}
