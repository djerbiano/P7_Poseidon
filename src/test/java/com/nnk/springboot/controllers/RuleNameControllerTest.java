package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.security.CustomUserDetailsService;
import com.nnk.springboot.services.RuleNameService;
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
 * Tests unitaires pour {@link RuleNameController}.
 * Utilise @WebMvcTest pour tester la couche HTTP (routes, statuts, redirections)
 * en isolant le controller du reste de l'application, avec RuleNameService simule.
 */
@WebMvcTest(RuleNameController.class)
public class RuleNameControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RuleNameService ruleNameService;

    @MockitoBean
    private CustomUserDetailsService customUserDetailsService;

    /**
     * Vérifie que la liste des RuleName s'affiche correctement pour un utilisateur connecte.
     */
    @Test
    @WithMockUser
    void list_shouldReturnRuleNameListView() throws Exception {
        when(ruleNameService.findAll()).thenReturn(List.of(
                new RuleName("Rule Name", "Description", "Json", "Template", "SQL", "SQL Part")));

        mockMvc.perform(get("/ruleName/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("ruleName/list"));
    }

    /**
     * Vérifie que le formulaire d'ajout s'affiche correctement.
     */
    @Test
    @WithMockUser
    void addForm_shouldReturnAddView() throws Exception {
        mockMvc.perform(get("/ruleName/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("ruleName/add"));
    }

    /**
     * Vérifie qu'une soumission valide redirige vers la liste et appelle save().
     */
    @Test
    @WithMockUser
    void validate_shouldRedirectToList_whenDataIsValid() throws Exception {
        mockMvc.perform(post("/ruleName/validate")
                        .with(csrf())
                        .param("name", "Rule Name")
                        .param("description", "Description")
                        .param("json", "Json")
                        .param("template", "Template")
                        .param("sqlStr", "SQL")
                        .param("sqlPart", "SQL Part"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/ruleName/list"));

        verify(ruleNameService, times(1)).save(any(RuleName.class));
    }

    /**
     * Vérifie qu'une soumission invalide (name manquant) affiche le formulaire.
     */
    @Test
    @WithMockUser
    void validate_shouldReturnAddView_whenDataIsInvalid() throws Exception {
        mockMvc.perform(post("/ruleName/validate")
                        .with(csrf())
                        .param("description", "Description"))
                .andExpect(status().isOk())
                .andExpect(view().name("ruleName/add"));

        verify(ruleNameService, never()).save(any(RuleName.class));
    }

    /**
     * Vérifie que le formulaire de modification s'affiche avec les donnees existantes.
     */
    @Test
    @WithMockUser
    void showUpdateForm_shouldReturnUpdateView() throws Exception {
        RuleName ruleName = new RuleName("Rule Name", "Description", "Json", "Template", "SQL", "SQL Part");
        ruleName.setId(1);
        when(ruleNameService.findById(1)).thenReturn(ruleName);

        mockMvc.perform(get("/ruleName/update/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("ruleName/update"));
    }

    /**
     * Vérifie qu'une soumission de mise à jour valide redirige vers la liste.
     */
    @Test
    @WithMockUser
    void updateRuleName_shouldRedirectToList_whenDataIsValid() throws Exception {
        mockMvc.perform(post("/ruleName/update/1")
                        .with(csrf())
                        .param("name", "Rule Name")
                        .param("description", "Description")
                        .param("json", "Json")
                        .param("template", "Template")
                        .param("sqlStr", "SQL")
                        .param("sqlPart", "SQL Part"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/ruleName/list"));

        verify(ruleNameService, times(1)).save(any(RuleName.class));
    }

    /**
     * Vérifie qu'une soumission de mise à jour invalide (name manquant) affiche le formulaire.
     */
    @Test
    @WithMockUser
    void updateRuleName_shouldReturnUpdateView_whenDataIsInvalid() throws Exception {
        mockMvc.perform(post("/ruleName/update/1")
                        .with(csrf())
                        .param("description", "Description"))
                .andExpect(status().isOk())
                .andExpect(view().name("ruleName/update"));

        verify(ruleNameService, never()).save(any(RuleName.class));
    }

    /**
     * Vérifie que la suppression redirige vers la liste et appelle deleteById().
     */
    @Test
    @WithMockUser
    void deleteRuleName_shouldRedirectToList() throws Exception {
        mockMvc.perform(get("/ruleName/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/ruleName/list"));

        verify(ruleNameService, times(1)).deleteById(1);
    }
}
