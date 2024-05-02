package com.example.bcpp.service;

import com.example.bcpp.dto.CardDTO;
import com.example.bcpp.exception.BadRequestException;
import com.example.bcpp.exception.NotFoundException;
import com.example.bcpp.model.Card;
import com.example.bcpp.model.Company;
import com.example.bcpp.model.User;
import com.example.bcpp.model.enums.UserType;
import com.example.bcpp.repository.CardRepository;
import com.example.bcpp.service.impl.CardServiceImpl;
import com.example.bcpp.utils.GenerateCardNumber;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.example.bcpp.dto.CardDTO.convertToDto;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CardServiceImplTest {

    private final Card card1 = createCard(1L);
    private final Card card2 = createCard(2L);

    private Card createCard(Long id) {
        return Card.builder()
                .id(id)
                .totalAmount(0.0)
                .user(new User(1L, "example@gmail.com", "password", UserType.Standard, new Company(1L, "company", null)))
                .cardNumber(GenerateCardNumber.generateCardNumber())
                .build();
    }

    @Mock
    CardRepository cardRepository;

    @InjectMocks
    CardServiceImpl cardService;

    @Test
    void shouldGetCard_whenGetModel_ifCardExists() {
        // Given: Mocking getModel when Card exists.
        when(cardRepository.findById(card1.getId())).thenReturn(Optional.of(card1));

        CardDTO cardDTO = convertToDto(cardService.getModel(card1.getId()));

        assertNotNull(cardDTO);
        assertEquals(card1.getUser().getId(), cardDTO.getUser().getId());

        verify(cardRepository).findById(anyLong());
        verifyNoMoreInteractions(cardRepository);
    }

    @Test
    void shouldThrowNotFoundException_whenGetModel_ifCardDoesNotExist() {
        // Given: Mocking getModel when Card doesn't exist.
        when(cardRepository.findById(card2.getId())).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class,() -> cardService.getModel(card2.getId()));

        assertEquals(String.format("Card with id %s not found.", card2.getId()), exception.getMessage());

        verify(cardRepository).findById(card2.getId());
        verifyNoMoreInteractions(cardRepository);
    }

    @Test
    void shouldGetCard_whenGetModelByUserId_ifCardExists() {
        // Given: Mocking getModelByUserId when Card exists.
        when(cardRepository.findByUserId(card1.getUser().getId())).thenReturn(Optional.of(card1));

        CardDTO cardDTO = convertToDto(cardService.getModelByUserId(card1.getId()));

        assertNotNull(cardDTO);
        assertEquals(card1.getUser().getId(), cardDTO.getUser().getId());

        verify(cardRepository).findByUserId(anyLong());
        verifyNoMoreInteractions(cardRepository);
    }

    @Test
    void shouldThrowNotFoundException_whenGetModelByUserId_ifCardDoesNotExist() {
        // Given: Mocking getModelByUserId when Card doesn't exist.
        when(cardRepository.findByUserId(card2.getUser().getId())).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class,() -> cardService.getModelByUserId(card2.getUser().getId()));

        assertEquals(String.format("Card with user id %s not found.", card2.getUser().getId()), exception.getMessage());

        verify(cardRepository).findByUserId(card2.getUser().getId());
        verifyNoMoreInteractions(cardRepository);
    }

    @Test
    void shouldCreateCard_whenCreate_IfCardDoesNotExists() {
        // Given: Mocking when Card doesn't exist.
        CardDTO cardDTO = convertToDto(card1);

        when(cardRepository.findByCardNumber(anyString())).thenReturn(Optional.empty());

        when(cardRepository.save(any(Card.class))).thenReturn(card1);

        // Verify is it created successful
        CardDTO createdCardDTO = cardService.create(cardDTO.getUser());
        assertEquals(card1.getId(), createdCardDTO.getId());
        assertEquals(card1.getUser().getId(), createdCardDTO.getUser().getId());
        assertEquals(card1.getCardNumber(), createdCardDTO.getCardNumber());

        verify(cardRepository).findByCardNumber(anyString());
        verify(cardRepository).save(any(Card.class));
        verifyNoMoreInteractions(cardRepository);
    }

    @Test
    void shouldThrowBadRequestException_whenCreate_ifCardExists() {
        // Given: Mocking existing Card
        CardDTO existingCardDTO = convertToDto(card2);
        String randomCardNumber = anyString();

        when(cardRepository.findByCardNumber(randomCardNumber)).thenReturn(Optional.of(existingCardDTO.convertToModel()));

        BadRequestException exception = assertThrows(BadRequestException.class, () -> cardService.create(existingCardDTO.getUser()));

        assertEquals("Card with this number is already in database", exception.getMessage());

        verify(cardRepository).findByCardNumber(anyString());
        verify(cardRepository, never()).save(any());
    }

    @Test
    void shouldUpdateCard_whenUpdate_IfEnoughFunds() {
        // Given: Mocking Card update when enough funds.
        CardDTO cardDTO = convertToDto(card1);
        Double amount = cardDTO.getTotalAmount() - 10;

        when(cardRepository.findById(anyLong())).thenReturn(Optional.of(card1));

        when(cardRepository.save(any(Card.class))).thenReturn(card1);

        CardDTO updatedCardDTO = cardService.update(card1.getId(), amount, "DECREASE", card1.getUser().getId());

        assertNotNull(updatedCardDTO);
        assertEquals(cardDTO.getId(), updatedCardDTO.getId());
        assertEquals(cardDTO.getUser().getId(), updatedCardDTO.getUser().getId());

        verify(cardRepository).findById(anyLong());
        verify(cardRepository).save(any(Card.class));
        verifyNoMoreInteractions(cardRepository);
    }

    // NOTE: This test is not throwing me BadRequestException
    @Test
    void shouldThrowBadRequestException_whenUpdate_IfNotEnoughFunds() {
        // Given: Mocking Card update when not having enough funds.

        when(cardRepository.findById(anyLong())).thenReturn(Optional.of(card2));

        BadRequestException exception = assertThrows(BadRequestException.class,
                () -> cardService.update(card2.getId(), card2.getTotalAmount() + 10, "DECREASE", card2.getUser().getId()));

        assertEquals("You don't have enough funds.", exception.getMessage());

        verify(cardRepository).findById(anyLong());
        verifyNoMoreInteractions(cardRepository);
    }

    // NOTE: This test is not throwing me BadRequestException
    @Test
    void shouldThrowBadRequestException_whenUpdate_IfCardUpdateStatusIsNotIncreaseOrDecrease() {
        // Given: Mocking Card update when card update status is not INCREASE/DECREASE

        Double amount = 10.0;
        String cardUpdateStatus = "INVALID";

        when(cardRepository.findById(anyLong())).thenReturn(Optional.of(card2));

        BadRequestException exception = assertThrows(BadRequestException.class,
                () -> cardService.update(card2.getId(), amount, cardUpdateStatus, card2.getUser().getId()));

        assertEquals("CardUpdateStatus is neither Increase or Decrease.", exception.getMessage());

        verify(cardRepository).findById(anyLong());
        verifyNoMoreInteractions(cardRepository);
    }
}
