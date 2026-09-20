package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.security.CustomUserDetailsService;
import com.nnk.springboot.services.UserService;
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
 * Tests unitaires pour {@link UserController}.
 * Utilise @WebMvcTest pour tester la couche HTTP (routes, statuts, redirections)
 * en isolant le controller du reste de l'application, avec UserService simule.
 */
@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private CustomUserDetailsService customUserDetailsService;

    /**
     * Vérifie que la liste des User s'affiche correctement pour un utilisateur connecte.
     */
    @Test
    @WithMockUser
    void list_shouldReturnUserListView() throws Exception {
        User user = new User();
        user.setId(1);
        user.setUsername("testuser");
        user.setFullname("Test User");
        user.setRole("USER");
        when(userService.findAll()).thenReturn(List.of(user));

        mockMvc.perform(get("/user/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/list"));
    }

    /**
     * Vérifie que le formulaire d'ajout s'affiche correctement.
     */
    @Test
    @WithMockUser
    void addForm_shouldReturnAddView() throws Exception {
        mockMvc.perform(get("/user/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/add"));
    }

    /**
     * Vérifie qu'une soumission valide (mot de passe fort) redirige vers la liste.
     */
    @Test
    @WithMockUser
    void validate_shouldRedirectToList_whenDataIsValid() throws Exception {
        when(userService.isPasswordValid("Str0ng@Pass")).thenReturn(true);

        mockMvc.perform(post("/user/validate")
                        .with(csrf())
                        .param("username", "testuser")
                        .param("password", "Str0ng@Pass")
                        .param("fullname", "Test User")
                        .param("role", "USER"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/list"));

        verify(userService, times(1)).createUser(any(User.class));
    }

    /**
     * Vérifie qu'une soumission avec un mot de passe trop faible reaffiche le formulaire.
     */
    @Test
    @WithMockUser
    void validate_shouldReturnAddView_whenPasswordIsWeak() throws Exception {
        when(userService.isPasswordValid("weak")).thenReturn(false);

        mockMvc.perform(post("/user/validate")
                        .with(csrf())
                        .param("username", "testuser")
                        .param("password", "weak")
                        .param("fullname", "Test User")
                        .param("role", "USER"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/add"));

        verify(userService, never()).createUser(any(User.class));
    }

    /**
     * Vérifie qu'une soumission avec un mot de passe vide reaffiche le formulaire
     */
    @Test
    @WithMockUser
    void validate_shouldReturnAddView_whenPasswordIsBlank() throws Exception {
        mockMvc.perform(post("/user/validate")
                        .with(csrf())
                        .param("username", "testuser")
                        .param("password", "")
                        .param("fullname", "Test User")
                        .param("role", "USER"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/add"));

        verify(userService, never()).createUser(any(User.class));
    }

    /**
     * Vérifie qu'une soumission sans le champ password affiche le formulaire.
     */
    @Test
    @WithMockUser
    void validate_shouldReturnAddView_whenPasswordIsMissing() throws Exception {
        mockMvc.perform(post("/user/validate")
                        .with(csrf())
                        .param("username", "testuser")
                        .param("fullname", "Test User")
                        .param("role", "USER"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/add"));

        verify(userService, never()).createUser(any(User.class));
    }

    /**
     * Vérifie qu'une soumission invalide (champ obligatoire manquant) affiche le formulaire.
     */
    @Test
    @WithMockUser
    void validate_shouldReturnAddView_whenDataIsInvalid() throws Exception {
        mockMvc.perform(post("/user/validate")
                        .with(csrf())
                        .param("password", "Str0ng@Pass"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/add"));

        verify(userService, never()).createUser(any(User.class));
    }

    /**
     * Vérifie que le formulaire de modification s'affiche avec les donnees existantes.
     */
    @Test
    @WithMockUser
    void showUpdateForm_shouldReturnUpdateView() throws Exception {
        User user = new User();
        user.setId(1);
        user.setUsername("testuser");
        user.setFullname("Test User");
        user.setRole("USER");
        when(userService.findById(1)).thenReturn(user);

        mockMvc.perform(get("/user/update/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/update"));
    }

    /**
     * Vérifie qu'une soumission de mise à jour valide (mot de passe vide, non changé) redirige vers la liste.
     */
    @Test
    @WithMockUser
    void updateUser_shouldRedirectToList_whenDataIsValid() throws Exception {
        mockMvc.perform(post("/user/update/1")
                        .with(csrf())
                        .param("username", "testuser")
                        .param("password", "")
                        .param("fullname", "Test User Update")
                        .param("role", "USER"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/list"));

        verify(userService, times(1)).updateUser(eq(1), any(User.class));
    }

    /**
     * Vérifie qu'une soumission de mise à jour sans le champ password du tout
     * conserve l'ancien mot de passe et redirige vers la liste.
     */
    @Test
    @WithMockUser
    void updateUser_shouldRedirectToList_whenPasswordIsMissing() throws Exception {
        mockMvc.perform(post("/user/update/1")
                        .with(csrf())
                        .param("username", "testuser")
                        .param("fullname", "Test User Update")
                        .param("role", "USER"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/list"));

        verify(userService, times(1)).updateUser(eq(1), any(User.class));
    }

    /**
     * Vérifie qu'une soumission de mise à jour avec un nouveau mot de passe valide
     * redirige vers la liste et appelle bien updateUser().
     */
    @Test
    @WithMockUser
    void updateUser_shouldRedirectToList_whenNewPasswordIsValid() throws Exception {
        when(userService.isPasswordValid("New@Pass123")).thenReturn(true);

        mockMvc.perform(post("/user/update/1")
                        .with(csrf())
                        .param("username", "testuser")
                        .param("password", "New@Pass123")
                        .param("fullname", "Test User Update")
                        .param("role", "USER"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/list"));

        verify(userService, times(1)).updateUser(eq(1), any(User.class));
    }

    /**
     * Vérifie qu'une soumission de mise à jour avec un nouveau mot de passe trop faible
     * reaffiche le formulaire, sans jamais appeler updateUser().
     */
    @Test
    @WithMockUser
    void updateUser_shouldReturnUpdateView_whenNewPasswordIsWeak() throws Exception {
        when(userService.isPasswordValid("weak")).thenReturn(false);

        mockMvc.perform(post("/user/update/1")
                        .with(csrf())
                        .param("username", "testuser")
                        .param("password", "weak")
                        .param("fullname", "Test User Update")
                        .param("role", "USER"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/update"));

        verify(userService, never()).updateUser(anyInt(), any(User.class));
    }

    /**
     * Vérifie qu'une soumission de mise à jour invalide affiche le formulaire.
     */
    @Test
    @WithMockUser
    void updateUser_shouldReturnUpdateView_whenDataIsInvalid() throws Exception {
        mockMvc.perform(post("/user/update/1")
                        .with(csrf())
                        .param("password", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("user/update"));

        verify(userService, never()).updateUser(anyInt(), any(User.class));
    }

    /**
     * Vérifie que la suppression redirige vers la liste et appelle deleteById().
     */
    @Test
    @WithMockUser
    void deleteUser_shouldRedirectToList() throws Exception {
        mockMvc.perform(get("/user/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/list"));

        verify(userService, times(1)).deleteById(1);
    }
}