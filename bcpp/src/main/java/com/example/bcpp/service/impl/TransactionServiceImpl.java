package com.example.bcpp.service.impl;

import com.example.bcpp.dto.TransactionDTO;
import com.example.bcpp.exception.NotFoundException;
import com.example.bcpp.model.*;
import com.example.bcpp.model.enums.CardUpdateStatus;
import com.example.bcpp.model.enums.RoleType;
import com.example.bcpp.model.enums.TransactionStatus;
import com.example.bcpp.model.enums.UserType;
import com.example.bcpp.repository.TransactionRepository;
import com.example.bcpp.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.bcpp.dto.TransactionDTO.convertToDto;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final UserService userService;
    private final CompanyMerchantService companyMerchantService;
    private final CompanyCategoryService companyCategoryService;
    private final MerchantService merchantService;
    private final CardService cardService;
    private final DiscountService discountService;

    @Override
    public List<TransactionDTO> findAll() {
        List<Transaction> transactions = transactionRepository.findAll();
        return transactions.stream()
                .map(TransactionDTO::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public TransactionDTO create(Long userId, Long merchantId, String role) {
        User user = userService.getModel(userId);
        Card card = cardService.getModelByUserId(userId);
        Long companyId = user.getCompany().getId();

        Merchant merchant = merchantService.getModel(merchantId);

        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setAmount(merchant.getPrice());

        switch(RoleType.valueOf(role)) {
            case ROLE_Standard:
                    companyCategoryService.getModel(companyId, merchant.getCategory());
                    processTransaction(transactionDTO, user, card, merchant);

                    break;
            case ROLE_Premium:
                    processTransaction(transactionDTO, user, card, merchant);

                    break;
            case ROLE_Platinum:
                    CompanyMerchant companyMerchant = companyMerchantService.getModel(companyId, merchantId);
                    if(companyMerchant != null) {
                        processPlatinumTransaction(transactionDTO, user, card, merchant, companyMerchant);
                    } else {
                        processTransaction(transactionDTO, user, card, merchant);
                    }

                    break;
        }

        return convertToDto(transactionRepository.save(transactionDTO.convertToModel()));
    }

    // helping methods
    private void processTransaction(TransactionDTO transactionDTO, User user, Card card, Merchant merchant) {
        transactionDTO.setDate(LocalDateTime.now());
        transactionDTO.setMerchant(merchant);
        transactionDTO.setUser(user);
        if (card.getTotalAmount() >= transactionDTO.getAmount()) {
            transactionDTO.setStatus(TransactionStatus.Successful);
            cardService.update(card.getId(), transactionDTO.getAmount(), "DECREASE");
        } else {
            transactionDTO.setStatus(TransactionStatus.Unsuccessful);
        }
    }

    private void processPlatinumTransaction(TransactionDTO transactionDTO, User user, Card card, Merchant merchant, CompanyMerchant companyMerchant) {
        Discount discount = discountService.getModel(companyMerchant.getId());
        Double discountPercentage = discount.getDiscountPercentage();
        Double discountedAmount = calculateDiscountedAmount(transactionDTO.getAmount(), discountPercentage);
        transactionDTO.setAmount(discountedAmount);
        processTransaction(transactionDTO, user, card, merchant);
    }

    private Double calculateDiscountedAmount(Double originalAmount, Double discountPercentage) {
        return originalAmount - (originalAmount * discountPercentage / 100);
    }
}
