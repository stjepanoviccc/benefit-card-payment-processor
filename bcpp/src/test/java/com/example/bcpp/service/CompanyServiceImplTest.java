package com.example.bcpp.service;

import com.example.bcpp.dto.CompanyDTO;
import com.example.bcpp.dto.MerchantDTO;
import com.example.bcpp.exception.BadRequestException;
import com.example.bcpp.exception.NotFoundException;
import com.example.bcpp.model.Company;
import com.example.bcpp.model.Merchant;
import com.example.bcpp.model.enums.MerchantCategory;
import com.example.bcpp.repository.CompanyRepository;
import com.example.bcpp.repository.MerchantRepository;
import com.example.bcpp.service.impl.CompanyServiceImpl;
import com.example.bcpp.service.impl.MerchantServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.example.bcpp.dto.CompanyDTO.convertToDto;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CompanyServiceImplTest {

    private final Company company = createCompany(1L, "company1");

    private final Company company2 = createCompany(2L, "company2");

    private Company createCompany(Long id, String name) {
        return Company.builder()
                .id(id)
                .name(name)
                .build();
    }

    @Mock
    private CompanyRepository companyRepository;

    @InjectMocks
    private CompanyServiceImpl companyService;

    @Test
    void shouldGetCompany_whenGetModel_ifCompanyExists() {
        // Given: Mocking getModel when Company exists.
        when(companyRepository.findById(company.getId())).thenReturn(Optional.of(company));

        CompanyDTO companyDTO = convertToDto(companyService.getModel(company.getId()));

        assertNotNull(companyDTO);
        assertEquals(company.getName(), companyDTO.getName());

        verify(companyRepository).findById(company.getId());
        verifyNoMoreInteractions(companyRepository);
    }

    @Test
    void shouldThrowNotFoundException_whenGetModel_ifCompanyDoesNotExist() {
        // Given: Mocking getModel when Company doesn't exist.
        when(companyRepository.findById(company2.getId())).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class,() -> companyService.getModel(company2.getId()));

        assertEquals(String.format("Company with id %s not found.", company2.getId()), exception.getMessage());

        verify(companyRepository).findById(company2.getId());
        verifyNoMoreInteractions(companyRepository);
    }

    @Test
    void shouldCreateCompany_whenCreate_IfCompanyDoesNotExists() {
        // Given: Mocking when Company doesn't exist.
        CompanyDTO companyDTO = convertToDto(company);

        when(companyRepository.findByName(anyString())).thenReturn(Optional.empty());

        when(companyRepository.save(any(Company.class))).thenReturn(company);

        // Verify is it created successful
        CompanyDTO createdCompanyDTO = companyService.create(companyDTO);
        assertEquals(company.getName(), createdCompanyDTO.getName());

        verify(companyRepository).findByName(anyString());
        verify(companyRepository).save(any(Company.class));
        verifyNoMoreInteractions(companyRepository);
    }

    @Test
    void shouldThrowBadRequestException_whenCreate_ifCompanyExists() {
        // Given: Mocking existing Company
        CompanyDTO existingCompanyDTO = convertToDto(company2);

        when(companyRepository.findByName(anyString())).thenReturn(Optional.of(existingCompanyDTO.convertToModel()));

        BadRequestException exception = assertThrows(BadRequestException.class, () -> companyService.create(existingCompanyDTO));

        assertEquals(String.format("Company with this name: %s is already in database", company2.getName()), exception.getMessage());

        verify(companyRepository).findByName(anyString());
        verify(companyRepository, never()).save(any());
    }

}
