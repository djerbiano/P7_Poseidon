package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.dto.TradeDto;
import com.nnk.springboot.security.CustomUserDetailsService;
import com.nnk.springboot.services.TradeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Tests unitaires pour {@link TradeController}.
 * Utilise @WebMvcTest pour tester la couche HTTP (routes, statuts, redirections)
 * en isolant le controller du reste de l'application, avec TradeService simulé.
 */
@WebMvcTest(TradeController.class)
public class TradeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TradeService tradeService;

    @MockitoBean
    private CustomUserDetailsService customUserDetailsService;

    /**
     * Vérifie que la liste des Trade s'affiche correctement pour un utilisateur connecté.
     */
    @Test
    @WithMockUser
    void list_shouldReturnTradeListView() throws Exception {
        when(tradeService.findAll()).thenReturn(List.of(new Trade("Trade Account", "Type")));

        mockMvc.perform(get("/trade/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("trade/list"))
                .andExpect(model().attributeExists("trades"));
    }

    /**
     * Vérifie que le formulaire d'ajout s'affiche avec un DTO vide.
     */
    @Test
    @WithMockUser
    void addForm_shouldReturnAddView() throws Exception {
        mockMvc.perform(get("/trade/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("trade/add"))
                .andExpect(model().attributeExists("trade"));
    }

    /**
     * Vérifie qu'une soumission valide redirige vers la liste et appelle create().
     */
    @Test
    @WithMockUser
    void validate_shouldRedirectToList_whenDataIsValid() throws Exception {
        mockMvc.perform(post("/trade/validate")
                        .with(csrf())
                        .param("account", "Trade Account")
                        .param("type", "Type")
                        .param("buyQuantity", "10"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/trade/list"));

        verify(tradeService, times(1)).create(any(TradeDto.class));
    }

    /**
     * Vérifie qu'une soumission invalide (champ obligatoire manquant) affiche le formulaire.
     */
    @Test
    @WithMockUser
    void validate_shouldReturnAddView_whenDataIsInvalid() throws Exception {
        mockMvc.perform(post("/trade/validate")
                        .with(csrf())
                        .param("buyQuantity", "10"))
                .andExpect(status().isOk())
                .andExpect(view().name("trade/add"));

        verify(tradeService, never()).create(any(TradeDto.class));
    }

    /**
     * Vérifie que le formulaire de modification s'affiche avec les données existantes.
     */
    @Test
    @WithMockUser
    void showUpdateForm_shouldReturnUpdateView() throws Exception {
        Trade trade = new Trade("Trade Account", "Type");
        trade.setTradeId(1);
        when(tradeService.findById(1)).thenReturn(trade);

        mockMvc.perform(get("/trade/update/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("trade/update"))
                .andExpect(model().attribute("trade", hasProperty("tradeId", is(1))));
    }

    /**
     * Vérifie qu'une soumission de mise à jour valide appelle update() avec
     * l'identifiant de l'URL, puis redirige vers la liste.
     */
    @Test
    @WithMockUser
    void updateTrade_shouldRedirectToList_whenDataIsValid() throws Exception {
        mockMvc.perform(post("/trade/update/1")
                        .with(csrf())
                        .param("account", "Trade Account")
                        .param("type", "Type")
                        .param("buyQuantity", "10"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/trade/list"));

        verify(tradeService, times(1)).update(eq(1), any(TradeDto.class));
    }

    /**
     * Vérifie qu'une soumission de mise à jour invalide réaffiche le formulaire
     * avec l'identifiant de l'URL remis dans le DTO, sans appeler update().
     */
    @Test
    @WithMockUser
    void updateTrade_shouldReturnUpdateView_whenDataIsInvalid() throws Exception {
        mockMvc.perform(post("/trade/update/1")
                        .with(csrf())
                        .param("buyQuantity", "10"))
                .andExpect(status().isOk())
                .andExpect(view().name("trade/update"))
                .andExpect(model().attribute("trade", hasProperty("tradeId", is(1))));

        verify(tradeService, never()).update(anyInt(), any(TradeDto.class));
    }

    /**
     * Vérifie que la suppression redirige vers la liste et appelle deleteById().
     */
    @Test
    @WithMockUser
    void deleteTrade_shouldRedirectToList() throws Exception {
        mockMvc.perform(get("/trade/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/trade/list"));

        verify(tradeService, times(1)).deleteById(1);
    }
}