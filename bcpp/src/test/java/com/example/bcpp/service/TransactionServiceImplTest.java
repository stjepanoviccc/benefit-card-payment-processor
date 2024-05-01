package com.example.bcpp.service;

import com.example.bcpp.dto.CardDTO;
import com.example.bcpp.dto.TransactionDTO;
import com.example.bcpp.exception.BadRequestException;
import com.example.bcpp.model.*;
import com.example.bcpp.model.enums.MerchantCategory;
import com.example.bcpp.model.enums.UserType;
import com.example.bcpp.repository.TransactionRepository;
import com.example.bcpp.service.impl.TransactionServiceImpl;
import com.example.bcpp.utils.GenerateCardNumber;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceImplTest {
    private final Company company = new Company(1L, "company", null);
    private final User user1 = new User(1L, "standard_user@gmail.com", "password", UserType.Standard, company);
    private final User user2 = new User(1L, "premium_user@gmail.com", "password", UserType.Premium, company);
    private final User user3 = new User(1L, "platinum_user@gmail.com", "password", UserType.Platinum, company);
    private final Card card1 = new Card(1L, user1, GenerateCardNumber.generateCardNumber(), 100.0);
    private final Card card2 = new Card(1L, user1, GenerateCardNumber.generateCardNumber(), 100.0);
    private final Card card3 = new Card(1L, user1, GenerateCardNumber.generateCardNumber(), 100.0);
    private final Merchant merchant = new Merchant(1L, "merchant", MerchantCategory.Traveling, 20.0, null);
    private final Transaction transaction1 = new Transaction(1L, user1, merchant.getPrice(), LocalDateTime.now(), null, merchant);
    private final Transaction transaction2 = new Transaction(1L, user1, merchant.getPrice(), LocalDateTime.now(), null, merchant);
    private final Transaction transaction3 = new Transaction(1L, user1, merchant.getPrice(), LocalDateTime.now(), null, merchant);

    private Transaction createTransaction(Transaction transaction) {
        return Transaction.builder()
                .id(transaction.getId())
                .user(transaction.getUser())
                .amount(transaction.getAmount())
                .date(transaction.getDate())
                .status(transaction.getStatus())
                .merchant(transaction.getMerchant())
                .build();
    }

    @Mock
    UserService userService;
    @Mock
    CompanyMerchantService companyMerchantService;
    @Mock
    CompanyCategoryService companyCategoryService;
    @Mock
    MerchantService merchantService;
    @Mock
    DiscountService discountService;
    @Mock
    TransactionRepository transactionRepository;
    @Mock
    CardService cardService;
    @InjectMocks
    TransactionServiceImpl transactionService;

    @Test
    void shouldCreateTransactionForStandardUser_whenCreate() {
        // Given: Mocking create Transaction for Standard User.
        when(userService.getModel(anyLong())).thenReturn(user1);
        when(cardService.getModelByUserId(anyLong())).thenReturn(card1);
        when(merchantService.getModel(anyLong())).thenReturn(merchant);
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction1);
        when(cardService.update(anyLong(), anyDouble(), anyString(), anyLong())).thenReturn(CardDTO.convertToDto(card1));

        TransactionDTO transactionDTO = transactionService.create(transaction1.getUser().getId(), transaction1.getMerchant().getId(), "ROLE_Standard");

        assertNotNull(transactionDTO);
        assertEquals(transaction1.getId(), transactionDTO.getId());
        verify(userService).getModel(user1.getId());
        verify(cardService).getModelByUserId(user1.getId());
        verify(merchantService).getModel(merchant.getId());
        verify(transactionRepository).save(any(Transaction.class));
        verifyNoMoreInteractions(userService, cardService, merchantService, transactionRepository);
    }

    @Test
    void shouldCreateTransactionForPremiumUser_whenCreate() {
        // Given: Mocking create Transaction for Premium User.
        when(userService.getModel(anyLong())).thenReturn(user2);
        when(cardService.getModelByUserId(anyLong())).thenReturn(card2);
        when(merchantService.getModel(anyLong())).thenReturn(merchant);
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction2);
        when(cardService.update(anyLong(), anyDouble(), anyString(), anyLong())).thenReturn(CardDTO.convertToDto(card2));

        TransactionDTO transactionDTO = transactionService.create(transaction2.getUser().getId(), transaction2.getMerchant().getId(), "ROLE_Premium");

        assertNotNull(transactionDTO);
        assertEquals(transaction2.getId(), transactionDTO.getId());
        verify(userService).getModel(user2.getId());
        verify(cardService).getModelByUserId(user2.getId());
        verify(merchantService).getModel(merchant.getId());
        verify(transactionRepository).save(any(Transaction.class));
        verifyNoMoreInteractions(userService, cardService, merchantService, transactionRepository);
    }

    @Test
    void shouldCreateTransactionForPlatinumUser_whenCreate() {
        // Given: Mocking create Transaction for Platinum User.
        when(userService.getModel(anyLong())).thenReturn(user3);
        when(cardService.getModelByUserId(anyLong())).thenReturn(card3);
        when(merchantService.getModel(anyLong())).thenReturn(merchant);
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction3);
        when(cardService.update(anyLong(), anyDouble(), anyString(), anyLong())).thenReturn(CardDTO.convertToDto(card3));

        TransactionDTO transactionDTO = transactionService.create(transaction3.getUser().getId(), transaction3.getMerchant().getId(), "ROLE_Platinum");

        assertNotNull(transactionDTO);
        assertEquals(transaction3.getId(), transactionDTO.getId());
        verify(userService).getModel(user3.getId());
        verify(cardService).getModelByUserId(user3.getId());
        verify(merchantService).getModel(merchant.getId());
        verify(transactionRepository).save(any(Transaction.class));
        verifyNoMoreInteractions(userService, cardService, merchantService, transactionRepository);
    }

    // NOTE: This test is not throwing me BadRequestException
    @Test
    void shouldThrowBadRequestException_whenCreate_IfRoleTypeDoesNotExist() {
        // Given: Mocking create Transaction for ROLE_Undefined User.
        when(userService.getModel(anyLong())).thenReturn(user3);
        when(cardService.getModelByUserId(anyLong())).thenReturn(card3);
        when(merchantService.getModel(anyLong())).thenReturn(merchant);

        BadRequestException exception = assertThrows(BadRequestException.class, () ->
                transactionService.create(transaction3.getUser().getId(), transaction3.getMerchant().getId(), anyString()));

        assertEquals("Something wrong happened with transaction. Check UserType and please try again.", exception.getMessage());

        verify(userService).getModel(user3.getId());
        verify(cardService).getModelByUserId(user3.getId());
        verify(merchantService).getModel(merchant.getId());
        verify(transactionRepository, never()).save(any());
        verifyNoMoreInteractions(userService, cardService, merchantService, transactionRepository);
    }
}
