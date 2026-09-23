package com.nnk.springboot.services;

import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.dto.CurvePointDto;
import com.nnk.springboot.exceptions.ResourceNotFoundException;
import com.nnk.springboot.repositories.CurvePointRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Timestamp;
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
    private CurvePointDto dto;

    @BeforeEach
    void setUp() {
        curvePoint = new CurvePoint(10, 5.0, 20.0);
        curvePoint.setId(1);

        dto = new CurvePointDto();
        dto.setCurveId(30);
        dto.setTerm(7.0);
        dto.setValue(40.0);
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
     * Vérifie que create() enregistre un nouveau CurvePoint contenant les champs
     * du formulaire et ses deux dates, en ignorant tout identifiant fourni par
     * l'utilisateur.
     */
    @Test
    void create_shouldSaveNewEntityWithDates_andIgnoreIdFromDto() {
        dto.setId(42);

        curvePointService.create(dto);

        verify(curvePointRepository, times(1)).save(
                argThat((CurvePoint saved) ->
                        saved.getId() == null
                                && saved.getCurveId().equals(30)
                                && saved.getTerm().equals(7.0)
                                && saved.getValue().equals(40.0)
                                && saved.getCreationDate() != null
                                && saved.getAsOfDate() != null
                )
        );
    }

    /**
     * Vérifie que update() modifie les champs du formulaire, met à jour
     * asOfDate et conserve la creationDate d'origine.
     */
    @Test
    void update_shouldOverwriteFormFields_keepCreationDate_andRefreshAsOfDate() {
        Timestamp oldDate = Timestamp.valueOf("2020-01-01 00:00:00");
        curvePoint.setCreationDate(oldDate);
        curvePoint.setAsOfDate(oldDate);
        when(curvePointRepository.findById(1)).thenReturn(Optional.of(curvePoint));

        curvePointService.update(1, dto);

        verify(curvePointRepository, times(1)).save(curvePoint);
        assertEquals(1, curvePoint.getId());
        assertEquals(30, curvePoint.getCurveId());
        assertEquals(7.0, curvePoint.getTerm());
        assertEquals(40.0, curvePoint.getValue());
        assertEquals(oldDate, curvePoint.getCreationDate());
        assertNotEquals(oldDate, curvePoint.getAsOfDate());
    }

    /**
     * Vérifie que update() lève ResourceNotFoundException si l'id n'existe pas,
     * sans jamais appeler save().
     */
    @Test
    void update_shouldThrowException_whenIdDoesNotExist() {
        when(curvePointRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> curvePointService.update(99, dto));
        verify(curvePointRepository, never()).save(any());
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