package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.security.CustomUserDetailsService;
import com.nnk.springboot.services.CurvePointService;
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
 * Tests unitaires pour {@link CurveController}.
 * Utilise @WebMvcTest pour tester la couche HTTP (routes, statuts, redirections)
 * en isolant le controller du reste de l'application, avec CurvePointService simule.
 */
@WebMvcTest(CurveController.class)
public class CurveControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CurvePointService curvePointService;

    @MockitoBean
    private CustomUserDetailsService customUserDetailsService;

    /**
     * Vérifie que la liste des CurvePoint s'affiche correctement pour un utilisateur connecte.
     */
    @Test
    @WithMockUser
    void list_shouldReturnCurvePointListView() throws Exception {
        when(curvePointService.findAll()).thenReturn(List.of(new CurvePoint(10, 5.0, 20.0)));

        mockMvc.perform(get("/curvePoint/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("curvePoint/list"));
    }

    /**
     * Vérifie que le formulaire d'ajout s'affiche correctement.
     */
    @Test
    @WithMockUser
    void addForm_shouldReturnAddView() throws Exception {
        mockMvc.perform(get("/curvePoint/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("curvePoint/add"));
    }

    /**
     * Vérifie qu'une soumission valide redirige vers la liste et appelle save().
     */
    @Test
    @WithMockUser
    void validate_shouldRedirectToList_whenDataIsValid() throws Exception {
        mockMvc.perform(post("/curvePoint/validate")
                        .with(csrf())
                        .param("curveId", "10")
                        .param("term", "5.0")
                        .param("value", "20.0"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/curvePoint/list"));

        verify(curvePointService, times(1)).save(any(CurvePoint.class));
    }

    /**
     * Vérifie qu'une soumission invalide (champ obligatoire manquant) affiche le formulaire.
     */
    @Test
    @WithMockUser
    void validate_shouldReturnAddView_whenDataIsInvalid() throws Exception {
        mockMvc.perform(post("/curvePoint/validate")
                        .with(csrf())
                        .param("term", "5.0")
                        .param("value", "20.0"))
                .andExpect(status().isOk())
                .andExpect(view().name("curvePoint/add"));

        verify(curvePointService, never()).save(any(CurvePoint.class));
    }

    /**
     * Vérifie que le formulaire de modification s'affiche avec les donnees existantes.
     */
    @Test
    @WithMockUser
    void showUpdateForm_shouldReturnUpdateView() throws Exception {
        CurvePoint curvePoint = new CurvePoint(10, 5.0, 20.0);
        curvePoint.setId(1);
        when(curvePointService.findById(1)).thenReturn(curvePoint);

        mockMvc.perform(get("/curvePoint/update/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("curvePoint/update"));
    }

    /**
     * Vérifie qu'une soumission de mise à jour valide redirige vers la liste et appelle save().
     */
    @Test
    @WithMockUser
    void updateBid_shouldRedirectToList_whenDataIsValid() throws Exception {
        mockMvc.perform(post("/curvePoint/update/1")
                        .with(csrf())
                        .param("curveId", "10")
                        .param("term", "5.0")
                        .param("value", "20.0"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/curvePoint/list"));

        verify(curvePointService, times(1)).save(any(CurvePoint.class));
    }

    /**
     * Vérifie qu'une soumission de mise à jour invalide affiche le formulaire.
     */
    @Test
    @WithMockUser
    void updateBid_shouldReturnUpdateView_whenDataIsInvalid() throws Exception {
        mockMvc.perform(post("/curvePoint/update/1")
                        .with(csrf())
                        .param("term", "5.0")
                        .param("value", "20.0"))
                .andExpect(status().isOk())
                .andExpect(view().name("curvePoint/update"));

        verify(curvePointService, never()).save(any(CurvePoint.class));
    }

    /**
     * Vérifie que la suppression redirige vers la liste et appelle deleteById().
     */
    @Test
    @WithMockUser
    void deleteBid_shouldRedirectToList() throws Exception {
        mockMvc.perform(get("/curvePoint/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/curvePoint/list"));

        verify(curvePointService, times(1)).deleteById(1);
    }
}