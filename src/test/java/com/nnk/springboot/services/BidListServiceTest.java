package com.nnk.springboot.services;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.exceptions.ResourceNotFoundException;
import com.nnk.springboot.repositories.BidListRepository;
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
 * Tests unitaires pour {@link BidListService}.
 * Les dépendances (BidListRepository) sont simulées avec Mockito
 * pour tester la logique du service de manière isolée, sans base de données réelle.
 */
@ExtendWith(MockitoExtension.class)
public class BidListServiceTest {

    @Mock
    private BidListRepository bidListRepository;

    @InjectMocks
    private BidListService bidListService;

    private BidList bidList;

    @BeforeEach
    void setUp() {
        bidList = new BidList("Account Test", "Type Test", 10d);
        bidList.setBidListId(1);
    }

    /**
     * Vérifie que findAll() retourne bien la liste fournie par le repository.
     */
    @Test
    void findAll_shouldReturnListOfBidLists() {
        when(bidListRepository.findAll()).thenReturn(List.of(bidList));

        List<BidList> result = bidListService.findAll();

        assertEquals(1, result.size());
        verify(bidListRepository, times(1)).findAll();
    }

    /**
     * Vérifie que findById() retourne le bon BidList quand l'id existe.
     */
    @Test
    void findById_shouldReturnBidList_whenIdExists() {
        when(bidListRepository.findById(1)).thenReturn(Optional.of(bidList));

        BidList result = bidListService.findById(1);

        assertNotNull(result);
        assertEquals(1, result.getBidListId());
    }

    /**
     * Vérifie que findById() lève ResourceNotFoundException quand l'id n'existe pas.
     */
    @Test
    void findById_shouldThrowException_whenIdDoesNotExist() {
        when(bidListRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> bidListService.findById(99));
    }

    /**
     * Vérifie que save() délègue bien au repository et retourne l'objet sauvegardé.
     */
    @Test
    void save_shouldCallRepositoryAndReturnSavedBidList() {
        when(bidListRepository.save(any(BidList.class))).thenReturn(bidList);

        BidList result = bidListService.save(bidList);

        assertNotNull(result);
        verify(bidListRepository, times(1)).save(bidList);
    }

    /**
     * Vérifie que deleteById() trouve l'entité puis la supprime via le repository.
     */
    @Test
    void deleteById_shouldCallRepositoryDelete_whenIdExists() {
        when(bidListRepository.findById(1)).thenReturn(Optional.of(bidList));

        bidListService.deleteById(1);

        verify(bidListRepository, times(1)).delete(bidList);
    }

    /**
     * Vérifie que deleteById() lève ResourceNotFoundException si l'id n'existe pas,
     * sans jamais appeler delete().
     */
    @Test
    void deleteById_shouldThrowException_whenIdDoesNotExist() {
        when(bidListRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> bidListService.deleteById(99));
        verify(bidListRepository, never()).delete(any());
    }
}
