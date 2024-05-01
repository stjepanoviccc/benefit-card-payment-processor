package com.example.bcpp.service;

import com.example.bcpp.dto.CompanyCategoryDTO;
import com.example.bcpp.exception.NotFoundException;
import com.example.bcpp.model.Company;
import com.example.bcpp.model.CompanyCategory;
import com.example.bcpp.model.enums.MerchantCategory;
import com.example.bcpp.repository.CompanyCategoryRepository;
import com.example.bcpp.repository.CompanyRepository;
import com.example.bcpp.service.impl.CompanyCategoryServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.example.bcpp.dto.CompanyCategoryDTO.convertToDto;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CompanyCategoryServiceImplTest {

    private final CompanyCategory companyCategory1 = createCompanyCategory(1L);
    private final CompanyCategory companyCategory2 = createCompanyCategory(2L);

    private CompanyCategory createCompanyCategory(Long id) {
        return CompanyCategory.builder()
                .id(id)
                .merchantCategory(MerchantCategory.Traveling)
                .company(new Company(1L, "company1", null))
                .build();
    }

    @Mock
    CompanyRepository companyRepository;

    @Mock
    CompanyCategoryRepository companyCategoryRepository;

    @InjectMocks
    CompanyCategoryServiceImpl companyCategoryService;

    @Test
    void shouldGetCompanyCategory_whenGetModel_ifCompanyCategoryExists() {
        // Given: Mocking getModel when CompanyCategory exists.
        when(companyCategoryRepository.findByCompanyIdAndMerchantCategory(anyLong(), any())).thenReturn(Optional.of(companyCategory1));

        CompanyCategoryDTO companyCategoryDTO = convertToDto(companyCategoryService.getModel(companyCategory1.getCompany().getId(), companyCategory1.getMerchantCategory()));

        assertNotNull(companyCategoryDTO);
        assertEquals(companyCategory1.getCompany().getId(), companyCategoryDTO.getCompany().getId());
        assertEquals(companyCategory1.getMerchantCategory().getId(), companyCategoryDTO.getMerchantCategory().getId());

        verify(companyCategoryRepository).findByCompanyIdAndMerchantCategory(anyLong(), any());
        verifyNoMoreInteractions(companyCategoryRepository);
    }

    @Test
    void shouldThrowNotFoundException_whenGetModel_ifCompanyCategoryDoesNotExist() {
        // Given: Mocking getModel when CompanyCategory doesn't exist.
        when(companyCategoryRepository.findByCompanyIdAndMerchantCategory(anyLong(), any())).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class,() -> companyCategoryService.getModel(
                companyCategory2.getCompany().getId(), companyCategory2.getMerchantCategory()
        ));

        assertEquals("CompanyCategory associated with this company and category not found.", exception.getMessage());

        verify(companyCategoryRepository).findByCompanyIdAndMerchantCategory(companyCategory2.getCompany().getId(), companyCategory2.getMerchantCategory());
        verifyNoMoreInteractions(companyCategoryRepository);
    }
}
