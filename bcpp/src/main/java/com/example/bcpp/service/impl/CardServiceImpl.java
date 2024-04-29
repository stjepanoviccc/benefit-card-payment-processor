package com.example.bcpp.service.impl;

import com.example.bcpp.dto.CardDTO;
import com.example.bcpp.exception.BadRequestException;
import com.example.bcpp.exception.NotFoundException;
import com.example.bcpp.model.Card;
import com.example.bcpp.model.User;
import com.example.bcpp.model.enums.CardUpdateStatus;
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
    public Card getModel(Long id) {
        return cardRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Card with id %s not found.", id)));
    }

    @Override
    public Card getModelByUserId(Long userId) {
        Card card = cardRepository.findByUserId(userId);
        if (card == null) {
            throw new NotFoundException(String.format("Card with user id %s not found.", userId));
        }
        return card;
    }

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

    @Override
    public CardDTO update(Long cardId, Double amount, String cardUpdateStatus) {
        Card card = getModel(cardId);
        Double newTotalAmount = card.getTotalAmount();
        if(cardUpdateStatus.equals("INCREASE")) {
            newTotalAmount += amount;
        } else if(cardUpdateStatus.equals("DECREASE")) {
            newTotalAmount -= amount;
            if(newTotalAmount < 0) {
                throw new BadRequestException("You don't have enough funds.");
            }
        } else {
            throw new BadRequestException("CardUpdateStatus is neither Increase or Decrease.");
        }

        card.setTotalAmount(newTotalAmount);
        return convertToDto(cardRepository.save(card));
    }
}
