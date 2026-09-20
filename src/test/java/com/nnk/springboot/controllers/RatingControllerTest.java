package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.security.CustomUserDetailsService;
import com.nnk.springboot.services.RatingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Tests unitaires pour {@link RatingController}.
 * Utilise @WebMvcTest pour tester la couche HTTP (routes, statuts, redirections)
 * en isolant le controller du reste de l'application, avec RatingService simule.
 */
@WebMvcTest(RatingController.class)
public class RatingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RatingService ratingService;

    @MockitoBean
    private CustomUserDetailsService customUserDetailsService;

    /**
     * Vérifie que la liste des Rating s'affiche correctement pour un utilisateur connecte.
     */
    @Test
    @WithMockUser
    void list_shouldReturnRatingListView() throws Exception {
        when(ratingService.findAll()).thenReturn(List.of(new Rating("AAA", "AA+", "AA", 1)));

        mockMvc.perform(get("/rating/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("rating/list"));
    }

    /**
     * Vérifie que le formulaire d'ajout s'affiche correctement.
     */
    @Test
    @WithMockUser
    void addForm_shouldReturnAddView() throws Exception {
        mockMvc.perform(get("/rating/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("rating/add"));
    }

    /**
     * Vérifie qu'une soumission valide (au moins une agence remplie) redirige vers la liste.
     */
    @Test
    @WithMockUser
    void validate_shouldRedirectToList_whenDataIsValid() throws Exception {
        when(ratingService.hasAnyRating(any(Rating.class))).thenReturn(true);

        mockMvc.perform(post("/rating/validate")
                        .with(csrf())
                        .param("moodysRating", "AAA")
                        .param("orderNumber", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/rating/list"));

        verify(ratingService, times(1)).save(any(Rating.class));
    }

    /**
     * Vérifie qu'une soumission sans aucune agence renseignée affiche le formulaire
     * avec une erreur globale, sans jamais sauvegarder.
     */
    @Test
    @WithMockUser
    void validate_shouldReturnAddView_whenNoRatingAgencyProvided() throws Exception {
        when(ratingService.hasAnyRating(any(Rating.class))).thenReturn(false);

        mockMvc.perform(post("/rating/validate")
                        .with(csrf())
                        .param("orderNumber", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("rating/add"));

        verify(ratingService, never()).save(any(Rating.class));
    }

    /**
     * Vérifie qu'une soumission invalide (champ obligatoire manquant) affiche le formulaire.
     */
    @Test
    @WithMockUser
    void validate_shouldReturnAddView_whenDataIsInvalid() throws Exception {
        mockMvc.perform(post("/rating/validate")
                        .with(csrf())
                        .param("moodysRating", "AAA"))
                .andExpect(status().isOk())
                .andExpect(view().name("rating/add"));

        verify(ratingService, never()).save(any(Rating.class));
    }

    /**
     * Vérifie que le formulaire de modification s'affiche avec les donnees existantes.
     */
    @Test
    @WithMockUser
    void showUpdateForm_shouldReturnUpdateView() throws Exception {
        Rating rating = new Rating("AAA", "AA+", "AA", 1);
        rating.setId(1);
        when(ratingService.findById(1)).thenReturn(rating);

        mockMvc.perform(get("/rating/update/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("rating/update"));
    }

    /**
     * Vérifie qu'une soumission de mise à jour valide redirige vers la liste.
     */
    @Test
    @WithMockUser
    void updateRating_shouldRedirectToList_whenDataIsValid() throws Exception {
        when(ratingService.hasAnyRating(any(Rating.class))).thenReturn(true);

        mockMvc.perform(post("/rating/update/1")
                        .with(csrf())
                        .param("moodysRating", "AAA")
                        .param("orderNumber", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/rating/list"));

        verify(ratingService, times(1)).save(any(Rating.class));
    }

    /**
     * Vérifie qu'une soumission de mise à jour invalide (champ obligatoire manquant) affiche le formulaire.
     */
    @Test
    @WithMockUser
    void updateRating_shouldReturnUpdateView_whenDataIsInvalid() throws Exception {
        mockMvc.perform(post("/rating/update/1")
                        .with(csrf())
                        .param("moodysRating", "AAA"))
                .andExpect(status().isOk())
                .andExpect(view().name("rating/update"));

        verify(ratingService, never()).save(any(Rating.class));
    }

    /**
     * Vérifie qu'une soumission de mise à jour sans aucune agence renseignée
     * affiche le formulaire avec une erreur globale, sans sauvegarder.
     */
    @Test
    @WithMockUser
    void updateRating_shouldReturnUpdateView_whenNoRatingAgencyProvided() throws Exception {
        when(ratingService.hasAnyRating(any(Rating.class))).thenReturn(false);

        mockMvc.perform(post("/rating/update/1")
                        .with(csrf())
                        .param("orderNumber", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("rating/update"));

        verify(ratingService, never()).save(any(Rating.class));
    }


    /**
     * Vérifie que la suppression redirige vers la liste et appelle deleteById().
     */
    @Test
    @WithMockUser
    void deleteRating_shouldRedirectToList() throws Exception {
        mockMvc.perform(get("/rating/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/rating/list"));

        verify(ratingService, times(1)).deleteById(1);
    }
}
