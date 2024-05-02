package com.example.bcpp.service;

import com.example.bcpp.dto.CompanyMerchantDTO;
import com.example.bcpp.exception.NotFoundException;
import com.example.bcpp.model.Company;
import com.example.bcpp.model.CompanyMerchant;
import com.example.bcpp.model.Merchant;
import com.example.bcpp.model.enums.MerchantCategory;
import com.example.bcpp.repository.CompanyMerchantRepository;
import com.example.bcpp.repository.CompanyRepository;
import com.example.bcpp.repository.MerchantRepository;
import com.example.bcpp.service.impl.CompanyMerchantServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.example.bcpp.dto.CompanyMerchantDTO.convertToDto;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CompanyMerchantServiceImplTest {

    private final CompanyMerchant companyMerchant1 = createCompanyMerchant(1L);
    private final CompanyMerchant companyMerchant2 = createCompanyMerchant(2L);

    private CompanyMerchant createCompanyMerchant(Long id) {
        return CompanyMerchant.builder()
                .id(id)
                .merchant(new Merchant(1L, "merchant", MerchantCategory.Traveling, 100.0, null))
                .company(new Company(1L, "company", null))
                .build();
    }

    @Mock
    CompanyRepository companyRepository;

    @Mock
    MerchantRepository merchantRepository;

    @Mock
    CompanyMerchantRepository companyMerchantRepository;

    @InjectMocks
    CompanyMerchantServiceImpl companyMerchantService;

    @Test
    void shouldGetCompanyMerchant_whenGetModel_ifCompanyMerchantExists() {
        // Given: Mocking getModel when CompanyMerchant exists.
        when(companyMerchantRepository.findByCompanyIdAndMerchantId(anyLong(), anyLong())).thenReturn(Optional.of(companyMerchant1));

        CompanyMerchantDTO companyMerchantDTO = convertToDto(companyMerchantService.getModel(companyMerchant1.getCompany().getId(), companyMerchant1.getMerchant().getId()));

        assertNotNull(companyMerchantDTO);
        assertEquals(companyMerchant1.getCompany().getId(), companyMerchantDTO.getCompany().getId());
        assertEquals(companyMerchant1.getMerchant().getId(), companyMerchantDTO.getMerchant().getId());

        verify(companyMerchantRepository).findByCompanyIdAndMerchantId(anyLong(), anyLong());
        verifyNoMoreInteractions(companyMerchantRepository);
    }

    @Test
    void shouldThrowNotFoundException_whenGetModel_ifCompanyMerchantDoesNotExist() {
        // Given: Mocking getModel when CompanyMerchant doesn't exist.
        when(companyMerchantRepository.findByCompanyIdAndMerchantId(anyLong(), anyLong())).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class,() -> companyMerchantService.getModel(
                companyMerchant2.getCompany().getId(), companyMerchant2.getMerchant().getId()
        ));

        assertEquals("CompanyMerchant associated with this company and category not found.", exception.getMessage());

        verify(companyMerchantRepository).findByCompanyIdAndMerchantId(companyMerchant2.getCompany().getId(), companyMerchant2.getMerchant().getId());
        verifyNoMoreInteractions(companyMerchantRepository);
    }
}
