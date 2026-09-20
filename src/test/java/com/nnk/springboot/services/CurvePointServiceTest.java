package com.nnk.springboot.services;

import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.exceptions.ResourceNotFoundException;
import com.nnk.springboot.repositories.CurvePointRepository;
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
 * Tests unitaires pour {@link CurvePointService}.
 * Les dépendances (CurvePointRepository) sont simulées avec Mockito
 * pour tester la logique du service de manière isolée, sans base de données réelle.
 */
@ExtendWith(MockitoExtension.class)
public class CurvePointServiceTest {

    @Mock
    private CurvePointRepository curvePointRepository;

    @InjectMocks
    private CurvePointService curvePointService;

    private CurvePoint curvePoint;

    @BeforeEach
    void setUp() {
        curvePoint = new CurvePoint(10, 5.0, 20.0);
        curvePoint.setId(1);
    }

    /**
     * Vérifie que findAll() retourne bien la liste fournie par le repository.
     */
    @Test
    void findAll_shouldReturnListOfCurvePoints() {
        when(curvePointRepository.findAll()).thenReturn(List.of(curvePoint));

        List<CurvePoint> result = curvePointService.findAll();

        assertEquals(1, result.size());
        assertEquals(curvePoint, result.get(0));
        verify(curvePointRepository, times(1)).findAll();
    }

    /**
     * Vérifie que findById() retourne le bon CurvePoint quand l'id existe.
     */
    @Test
    void findById_shouldReturnCurvePoint_whenIdExists() {
        when(curvePointRepository.findById(1)).thenReturn(Optional.of(curvePoint));

        CurvePoint result = curvePointService.findById(1);

        assertNotNull(result);
        assertEquals(1, result.getId());
    }

    /**
     * Vérifie que findById() lève ResourceNotFoundException quand l'id n'existe pas.
     */
    @Test
    void findById_shouldThrowException_whenIdDoesNotExist() {
        when(curvePointRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> curvePointService.findById(99));
    }

    /**
     * Vérifie que save() délègue bien au repository et retourne l'objet sauvegardé.
     */
    @Test
    void save_shouldCallRepositoryAndReturnSavedCurvePoint() {
        when(curvePointRepository.findById(1)).thenReturn(Optional.of(curvePoint));
        when(curvePointRepository.save(any(CurvePoint.class))).thenReturn(curvePoint);

        CurvePoint result = curvePointService.save(curvePoint);

        assertNotNull(result);
        verify(curvePointRepository, times(1)).save(curvePoint);
    }

    /**
     * Vérifie que save() fixe creationDate lors d'une création (id null),
     * sans jamais appeler findById() pour aller chercher une ancienne valeur.
     */
    @Test
    void save_shouldSetCreationDate_whenCreatingNewCurvePoint() {
        CurvePoint newCurvePoint = new CurvePoint(20, 3.0, 15.0);
        // id reste null, comme lors d'une vraie création

        when(curvePointRepository.save(any(CurvePoint.class))).thenReturn(newCurvePoint);

        curvePointService.save(newCurvePoint);

        assertNotNull(newCurvePoint.getCreationDate());
        verify(curvePointRepository, never()).findById(any());
        verify(curvePointRepository, times(1)).save(newCurvePoint);
    }

    /**
     * Vérifie que deleteById() trouve l'entité puis la supprime via le repository.
     */
    @Test
    void deleteById_shouldCallRepositoryDelete_whenIdExists() {
        when(curvePointRepository.findById(1)).thenReturn(Optional.of(curvePoint));

        curvePointService.deleteById(1);

        verify(curvePointRepository, times(1)).delete(curvePoint);
    }

    /**
     * Vérifie que deleteById() lève ResourceNotFoundException si l'id n'existe pas,
     * sans jamais appeler delete().
     */
    @Test
    void deleteById_shouldThrowException_whenIdDoesNotExist() {
        when(curvePointRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> curvePointService.deleteById(99));
        verify(curvePointRepository, never()).delete(any());
    }
}
