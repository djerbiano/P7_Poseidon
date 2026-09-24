package com.nnk.springboot.services;

import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.dto.TradeDto;
import com.nnk.springboot.exceptions.ResourceNotFoundException;
import com.nnk.springboot.repositories.TradeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Tests unitaires pour {@link TradeService}.
 * Les dépendances (TradeRepository) sont simulées avec Mockito
 * pour tester la logique du service de manière isolée, sans base de données réelle.
 */
@ExtendWith(MockitoExtension.class)
public class TradeServiceTest {

    @Mock
    private TradeRepository tradeRepository;

    @InjectMocks
    private TradeService tradeService;

    private Trade trade;
    private TradeDto dto;

    @BeforeEach
    void setUp() {
        trade = new Trade("Trade Account", "Type");
        trade.setTradeId(1);
        trade.setBuyQuantity(10d);

        dto = new TradeDto();
        dto.setAccount("New Account");
        dto.setType("New Type");
        dto.setBuyQuantity(20d);
    }

    /**
     * Vérifie que findAll() retourne bien la liste fournie par le repository.
     */
    @Test
    void findAll_shouldReturnListOfTrades() {
        when(tradeRepository.findAll()).thenReturn(List.of(trade));

        List<Trade> result = tradeService.findAll();

        assertEquals(1, result.size());
        verify(tradeRepository, times(1)).findAll();
    }

    /**
     * Vérifie que findById() retourne le bon Trade quand l'id existe.
     */
    @Test
    void findById_shouldReturnTrade_whenIdExists() {
        when(tradeRepository.findById(1)).thenReturn(Optional.of(trade));

        Trade result = tradeService.findById(1);

        assertNotNull(result);
        assertEquals(1, result.getTradeId());
    }

    /**
     * Vérifie que findById() lève ResourceNotFoundException quand l'id n'existe pas.
     */
    @Test
    void findById_shouldThrowException_whenIdDoesNotExist() {
        when(tradeRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> tradeService.findById(99));
    }

    /**
     * Vérifie que create() enregistre un nouveau Trade contenant les champs
     * du formulaire, en ignorant tout identifiant fourni par l'utilisateur.
     */
    @Test
    void create_shouldSaveNewEntity_andIgnoreIdFromDto() {
        dto.setTradeId(42);

        tradeService.create(dto);

        verify(tradeRepository, times(1)).save(
                argThat((Trade saved) ->
                        saved.getTradeId() == null
                                && "New Account".equals(saved.getAccount())
                                && "New Type".equals(saved.getType())
                                && saved.getBuyQuantity().equals(20d)
                )
        );
    }

    /**
     * Vérifie que update() modifie uniquement les champs du formulaire et
     * conserve les autres colonnes de l'entité existante.
     */
    @Test
    void update_shouldOverwriteFormFields_andKeepOtherFields() {
        trade.setTrader("Trader Original");
        trade.setBook("Book Original");
        trade.setBuyPrice(99.5);
        when(tradeRepository.findById(1)).thenReturn(Optional.of(trade));

        tradeService.update(1, dto);

        verify(tradeRepository, times(1)).save(trade);
        assertEquals(1, trade.getTradeId());
        assertEquals("New Account", trade.getAccount());
        assertEquals("New Type", trade.getType());
        assertEquals(20d, trade.getBuyQuantity());
        assertEquals("Trader Original", trade.getTrader());
        assertEquals("Book Original", trade.getBook());
        assertEquals(99.5, trade.getBuyPrice());
    }

    /**
     * Vérifie que update() lève ResourceNotFoundException si l'id n'existe pas,
     * sans jamais appeler save().
     */
    @Test
    void update_shouldThrowException_whenIdDoesNotExist() {
        when(tradeRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> tradeService.update(99, dto));
        verify(tradeRepository, never()).save(any());
    }

    /**
     * Vérifie que deleteById() trouve l'entité puis la supprime via le repository.
     */
    @Test
    void deleteById_shouldCallRepositoryDelete_whenIdExists() {
        when(tradeRepository.findById(1)).thenReturn(Optional.of(trade));

        tradeService.deleteById(1);

        verify(tradeRepository, times(1)).delete(trade);
    }

    /**
     * Vérifie que deleteById() lève ResourceNotFoundException si l'id n'existe pas,
     * sans jamais appeler delete().
     */
    @Test
    void deleteById_shouldThrowException_whenIdDoesNotExist() {
        when(tradeRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> tradeService.deleteById(99));
        verify(tradeRepository, never()).delete(any());
    }
}