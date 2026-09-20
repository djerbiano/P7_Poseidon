package com.nnk.springboot.services;

import com.nnk.springboot.domain.Trade;
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

    @BeforeEach
    void setUp() {
        trade = new Trade("Trade Account", "Type");
        trade.setTradeId(1);
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
     * Vérifie que save() délègue bien au repository et retourne l'objet sauvegardé.
     */
    @Test
    void save_shouldCallRepositoryAndReturnSavedTrade() {
        when(tradeRepository.save(any(Trade.class))).thenReturn(trade);

        Trade result = tradeService.save(trade);

        assertNotNull(result);
        verify(tradeRepository, times(1)).save(trade);
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
