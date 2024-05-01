package com.example.bcpp.service;

import com.example.bcpp.dto.DiscountDTO;
import com.example.bcpp.exception.NotFoundException;
import com.example.bcpp.model.Company;
import com.example.bcpp.model.CompanyMerchant;
import com.example.bcpp.model.Discount;
import com.example.bcpp.model.Merchant;
import com.example.bcpp.model.enums.MerchantCategory;
import com.example.bcpp.repository.CompanyMerchantRepository;
import com.example.bcpp.repository.DiscountRepository;
import com.example.bcpp.service.impl.DiscountServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.example.bcpp.dto.DiscountDTO.convertToDto;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DiscountServiceImplTest {

    private final Company company1 = new Company(1L, "company1", null);
    private final Company company2 = new Company(2L, "company2", null);
    private final Merchant merchant1 = new Merchant(1L, "merchant1", MerchantCategory.Traveling, 100.0, null);
    private final Merchant merchant2 = new Merchant(2L, "merchant2", MerchantCategory.FoodAndDrinks, 70.0, null);
    private final CompanyMerchant companyMerchant1 = new CompanyMerchant(1L, company1, merchant1);
    private final CompanyMerchant companyMerchant2 = new CompanyMerchant(1L, company2, merchant2);
    private final Discount discount1 = createDiscount(1L, companyMerchant1, 20.0);
    private final Discount discount2 = createDiscount(2L, companyMerchant2, 30.0);

    private Discount createDiscount(Long id, CompanyMerchant companyMerchant, Double discountPercentage) {
        return Discount.builder()
                .id(id)
                .companyMerchant(companyMerchant)
                .discountPercentage(discountPercentage)
                .build();
    }


    @Mock
    DiscountRepository discountRepository;

    @Mock
    CompanyMerchantRepository companyMerchantRepository;

    @InjectMocks
    DiscountServiceImpl discountService;

    @Test
    void shouldGetDiscount_whenGetModel_ifDiscountExists() {
        // Given: Mocking getModel when Discount exists.
        when(discountRepository.findByCompanyMerchantId(discount1.getId())).thenReturn(Optional.of(discount1));

        DiscountDTO discountDTO = convertToDto(discountService.getModel(discount1.getCompanyMerchant().getId()));

        assertNotNull(discountDTO);
        assertEquals(discount1.getCompanyMerchant().getCompany().getId(), discountDTO.getCompanyMerchant().getCompany().getId());
        assertEquals(discount1.getCompanyMerchant().getMerchant().getId(), discountDTO.getCompanyMerchant().getMerchant().getId());

        verify(discountRepository).findByCompanyMerchantId(anyLong());
        verifyNoMoreInteractions(discountRepository);
    }

    @Test
    void shouldThrowNotFoundException_whenGetModel_ifDiscountDoesNotExist() {
        // Given: Mocking getModel when Discount doesn't exist.
        when(discountRepository.findByCompanyMerchantId(discount2.getCompanyMerchant().getId())).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class,() -> discountService.getModel(discount2.getCompanyMerchant().getId()));

        assertEquals("Discount with that CompanyMerchantId is not found.", exception.getMessage());

        verify(discountRepository).findByCompanyMerchantId(discount2.getCompanyMerchant().getId());
        verifyNoMoreInteractions(discountRepository);
    }

}
