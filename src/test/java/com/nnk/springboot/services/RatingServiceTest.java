package com.nnk.springboot.services;

import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.exceptions.ResourceNotFoundException;
import com.nnk.springboot.repositories.RatingRepository;
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
 * Tests unitaires pour {@link RatingService}.
 * Les dépendances (RatingRepository) sont simulées avec Mockito
 * pour tester la logique du service de manière isolée, sans base de données réelle.
 */
@ExtendWith(MockitoExtension.class)
public class RatingServiceTest {

    @Mock
    private RatingRepository ratingRepository;

    @InjectMocks
    private RatingService ratingService;

    private Rating rating;

    @BeforeEach
    void setUp() {
        rating = new Rating("AAA", "AA+", "AA", 1);
        rating.setId(1);
    }

    /**
     * Vérifie que findAll() retourne bien la liste fournie par le repository.
     */
    @Test
    void findAll_shouldReturnListOfRatings() {
        when(ratingRepository.findAll()).thenReturn(List.of(rating));

        List<Rating> result = ratingService.findAll();

        assertEquals(1, result.size());
        verify(ratingRepository, times(1)).findAll();
    }

    /**
     * Vérifie que findById() retourne le bon Rating quand l'id existe.
     */
    @Test
    void findById_shouldReturnRating_whenIdExists() {
        when(ratingRepository.findById(1)).thenReturn(Optional.of(rating));

        Rating result = ratingService.findById(1);

        assertNotNull(result);
        assertEquals(1, result.getId());
    }

    /**
     * Vérifie que findById() lève ResourceNotFoundException quand l'id n'existe pas.
     */
    @Test
    void findById_shouldThrowException_whenIdDoesNotExist() {
        when(ratingRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> ratingService.findById(99));
    }

    /**
     * Vérifie que hasAnyRating() retourne true si au moins une agence est renseignée.
     */
    @Test
    void hasAnyRating_shouldReturnTrue_whenAtLeastOneFieldIsFilled() {
        Rating r = new Rating("AAA", null, null, 1);

        assertTrue(ratingService.hasAnyRating(r));
    }

    /**
     * Vérifie que hasAnyRating() retourne true quand seul sandPRating est renseigné.
     */
    @Test
    void hasAnyRating_shouldReturnTrue_whenOnlySandPRatingIsFilled() {
        Rating r = new Rating(null, "AA+", null, 1);

        assertTrue(ratingService.hasAnyRating(r));
    }

    /**
     * Vérifie que hasAnyRating() retourne true quand seul fitchRating est renseigné.
     */
    @Test
    void hasAnyRating_shouldReturnTrue_whenOnlyFitchRatingIsFilled() {
        Rating r = new Rating(null, null, "AA", 1);

        assertTrue(ratingService.hasAnyRating(r));
    }
    
    /**
     * Vérifie que hasAnyRating() retourne false si les trois agences sont vides ou blanches.
     */
    @Test
    void hasAnyRating_shouldReturnFalse_whenAllFieldsAreEmpty() {
        Rating r = new Rating("", null, "   ", 1);

        assertFalse(ratingService.hasAnyRating(r));
    }

    /**
     * Vérifie que save() délègue bien au repository et retourne l'objet sauvegardé.
     */
    @Test
    void save_shouldCallRepositoryAndReturnSavedRating() {
        when(ratingRepository.save(any(Rating.class))).thenReturn(rating);

        Rating result = ratingService.save(rating);

        assertNotNull(result);
        verify(ratingRepository, times(1)).save(rating);
    }

    /**
     * Vérifie que deleteById() trouve l'entité puis la supprime via le repository.
     */
    @Test
    void deleteById_shouldCallRepositoryDelete_whenIdExists() {
        when(ratingRepository.findById(1)).thenReturn(Optional.of(rating));

        ratingService.deleteById(1);

        verify(ratingRepository, times(1)).delete(rating);
    }

    /**
     * Vérifie que deleteById() lève ResourceNotFoundException si l'id n'existe pas,
     * sans jamais appeler delete().
     */
    @Test
    void deleteById_shouldThrowException_whenIdDoesNotExist() {
        when(ratingRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> ratingService.deleteById(99));
        verify(ratingRepository, never()).delete(any());
    }
}
