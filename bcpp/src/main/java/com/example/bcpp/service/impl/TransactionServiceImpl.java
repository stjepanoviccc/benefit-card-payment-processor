package com.example.bcpp.service.impl;

import com.example.bcpp.dto.TransactionDTO;
import com.example.bcpp.exception.BadRequestException;
import com.example.bcpp.model.*;
import com.example.bcpp.model.enums.RoleType;
import com.example.bcpp.model.enums.TransactionStatus;
import com.example.bcpp.repository.TransactionRepository;
import com.example.bcpp.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

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
    public TransactionDTO create(Long userId, Long merchantId, String role) {
        User user = userService.getModel(userId);
        Card card = cardService.getModelByUserId(userId);
        Long companyId = user.getCompany().getId();

        Merchant merchant = merchantService.getModel(merchantId);

        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setAmount(merchant.getPrice());

        try {
            switch(RoleType.valueOf(role)) {
                case ROLE_Standard:
                    companyCategoryService.getModel(companyId, merchant.getCategory());
                    transactionDTO.setDate(LocalDateTime.now());
                    transactionDTO.setMerchant(merchant);
                    transactionDTO.setUser(user);
                    if (card.getTotalAmount() >= transactionDTO.getAmount()) {
                        transactionDTO.setStatus(TransactionStatus.Successful);
                        cardService.update(card.getId(), transactionDTO.getAmount(), "DECREASE", user.getId());
                    } else {
                        transactionDTO.setStatus(TransactionStatus.Unsuccessful);
                    }

                    break;
                case ROLE_Premium:
                    transactionDTO.setDate(LocalDateTime.now());
                    transactionDTO.setMerchant(merchant);
                    transactionDTO.setUser(user);
                    if (card.getTotalAmount() >= transactionDTO.getAmount()) {
                        transactionDTO.setStatus(TransactionStatus.Successful);
                        cardService.update(card.getId(), transactionDTO.getAmount(), "DECREASE", user.getId());
                    } else {
                        transactionDTO.setStatus(TransactionStatus.Unsuccessful);
                    }

                    break;
                case ROLE_Platinum:
                    CompanyMerchant companyMerchant = companyMerchantService.getModel(companyId, merchantId);
                    if(companyMerchant != null) {
                        Discount discount = discountService.getModel(companyMerchant.getId());
                        Double discountPercentage = discount.getDiscountPercentage();
                        Double discountedAmount = transactionDTO.getAmount() - (transactionDTO.getAmount() * discountPercentage / 100);
                        transactionDTO.setAmount(discountedAmount);
                        transactionDTO.setDate(LocalDateTime.now());
                        transactionDTO.setMerchant(merchant);
                        transactionDTO.setUser(user);
                        if (card.getTotalAmount() >= transactionDTO.getAmount()) {
                            transactionDTO.setStatus(TransactionStatus.Successful);
                            cardService.update(card.getId(), transactionDTO.getAmount(), "DECREASE", user.getId());
                        } else {
                            transactionDTO.setStatus(TransactionStatus.Unsuccessful);
                        }
                    } else {
                        transactionDTO.setDate(LocalDateTime.now());
                        transactionDTO.setMerchant(merchant);
                        transactionDTO.setUser(user);
                        if (card.getTotalAmount() >= transactionDTO.getAmount()) {
                            transactionDTO.setStatus(TransactionStatus.Successful);
                            cardService.update(card.getId(), transactionDTO.getAmount(), "DECREASE", user.getId());
                        } else {
                            transactionDTO.setStatus(TransactionStatus.Unsuccessful);
                        }
                    }

                    break;
            }

            return convertToDto(transactionRepository.save(transactionDTO.convertToModel()));

        } catch(Exception e) {
            throw new BadRequestException("Something wrong happened with transaction. Check UserType and please try again.");
        }

    }
}
