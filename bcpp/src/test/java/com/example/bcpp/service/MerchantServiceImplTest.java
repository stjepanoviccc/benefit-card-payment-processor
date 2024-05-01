package com.example.bcpp.service;

import com.example.bcpp.dto.MerchantDTO;
import com.example.bcpp.exception.BadRequestException;
import com.example.bcpp.exception.NotFoundException;
import com.example.bcpp.model.Merchant;
import com.example.bcpp.model.enums.MerchantCategory;
import com.example.bcpp.repository.MerchantRepository;
import com.example.bcpp.service.impl.MerchantServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.example.bcpp.dto.MerchantDTO.convertToDto;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MerchantServiceImplTest {

    private final Merchant merchant = createMerchant(1L, "merchant1");

    private final Merchant merchant2 = createMerchant(2L, "merchant2");

    private Merchant createMerchant(Long id, String name) {
        return Merchant.builder()
                .id(id)
                .name(name)
                .category(MerchantCategory.Traveling)
                .price(100.0)
                .build();
    }

    @Mock
    private MerchantRepository merchantRepository;

    @InjectMocks
    private MerchantServiceImpl merchantService;

    @Test
    void shouldGetMerchant_whenGetModel_ifMerchantExists() {
        // Given: Mocking getModel when Merchant exists.
        when(merchantRepository.findById(merchant.getId())).thenReturn(Optional.of(merchant));

        MerchantDTO merchantDTO = convertToDto(merchantService.getModel(merchant.getId()));

        assertNotNull(merchantDTO);
        assertEquals(merchant.getName(), merchantDTO.getName());

        verify(merchantRepository).findById(merchant.getId());
        verifyNoMoreInteractions(merchantRepository);
    }

    @Test
    void shouldThrowNotFoundException_whenGetModel_ifMerchantDoesNotExist() {
        // Given: Mocking getModel when Merchant doesn't exist.
        when(merchantRepository.findById(merchant2.getId())).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class,() -> merchantService.getModel(merchant2.getId()));

        assertEquals(String.format("Merchant with id %s not found.", merchant2.getId()), exception.getMessage());

        verify(merchantRepository).findById(merchant2.getId());
        verifyNoMoreInteractions(merchantRepository);
    }

    @Test
    void shouldCreateMerchant_whenCreate_IfMerchantDoesNotExists() {
        // Given: Mocking when Merchant doesn't exist.
        MerchantDTO merchantDTO = convertToDto(merchant);

        when(merchantRepository.findByName(anyString())).thenReturn(Optional.empty());

        when(merchantRepository.save(any(Merchant.class))).thenReturn(merchant);

        // Verify is it created successful
        MerchantDTO createdMerchantDTO = merchantService.create(merchantDTO);
        assertEquals(merchant.getName(), createdMerchantDTO.getName());
        assertEquals(merchant.getPrice(), createdMerchantDTO.getPrice());
        assertEquals(merchant.getCategory(), createdMerchantDTO.getCategory());

        verify(merchantRepository).findByName(anyString());
        verify(merchantRepository).save(any(Merchant.class));
        verifyNoMoreInteractions(merchantRepository);
    }

    @Test
    void shouldThrowBadRequestException_whenCreate_ifMerchantExists() {
        // Given: Mocking existing Merchant
        MerchantDTO existingMerchantDTO = convertToDto(merchant2);

        when(merchantRepository.findByName(anyString())).thenReturn(Optional.of(existingMerchantDTO.convertToModel()));

        BadRequestException exception = assertThrows(BadRequestException.class, () -> merchantService.create(existingMerchantDTO));

        assertEquals(String.format("Merchant with this name: %s is already in database", merchant2.getName()), exception.getMessage());

        verify(merchantRepository).findByName(anyString());
        verify(merchantRepository, never()).save(any());
    }

}
