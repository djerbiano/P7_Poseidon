package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.dto.UserDto;
import com.nnk.springboot.security.CustomUserDetailsService;
import com.nnk.springboot.services.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Tests unitaires pour {@link UserController}.
 * Utilise @WebMvcTest pour tester la couche HTTP (routes, statuts, redirections)
 * en isolant le controller du reste de l'application, avec UserService simulé.
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
     * Vérifie que la liste des User s'affiche correctement pour un utilisateur connecté.
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
                .andExpect(view().name("user/list"))
                .andExpect(model().attributeExists("users"));
    }

    /**
     * Vérifie que le formulaire d'ajout s'affiche avec un DTO vide.
     */
    @Test
    @WithMockUser
    void addForm_shouldReturnAddView() throws Exception {
        mockMvc.perform(get("/user/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/add"))
                .andExpect(model().attributeExists("user"));
    }

    /**
     * Vérifie qu'une soumission valide (mot de passe fort) redirige vers la liste
     * et appelle create().
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

        verify(userService, times(1)).create(any(UserDto.class));
    }

    /**
     * Vérifie qu'une soumission avec un mot de passe trop faible réaffiche le formulaire.
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

        verify(userService, never()).create(any(UserDto.class));
    }

    /**
     * Vérifie qu'une soumission avec un mot de passe vide réaffiche le formulaire.
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

        verify(userService, never()).create(any(UserDto.class));
    }

    /**
     * Vérifie qu'une soumission sans le champ password réaffiche le formulaire.
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

        verify(userService, never()).create(any(UserDto.class));
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

        verify(userService, never()).create(any(UserDto.class));
    }

    /**
     * Vérifie que le formulaire de modification s'affiche avec les données
     * existantes, sans aucun mot de passe.
     */
    @Test
    @WithMockUser
    void showUpdateForm_shouldReturnUpdateView_withoutPassword() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setId(1);
        userDto.setUsername("testuser");
        userDto.setFullname("Test User");
        userDto.setRole("USER");
        when(userService.findByIdAsDto(1)).thenReturn(userDto);

        mockMvc.perform(get("/user/update/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/update"))
                .andExpect(model().attribute("user", hasProperty("id", is(1))))
                .andExpect(model().attribute("user", hasProperty("password", nullValue())));
    }

    /**
     * Vérifie qu'une soumission de mise à jour valide (mot de passe vide, non changé)
     * appelle update() avec l'identifiant de l'URL et redirige vers la liste.
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

        verify(userService, times(1)).update(eq(1), any(UserDto.class));
    }

    /**
     * Vérifie qu'une soumission de mise à jour sans le champ password du tout
     * redirige vers la liste.
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

        verify(userService, times(1)).update(eq(1), any(UserDto.class));
    }

    /**
     * Vérifie qu'une soumission de mise à jour avec un nouveau mot de passe valide
     * redirige vers la liste et appelle bien update().
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

        verify(userService, times(1)).update(eq(1), any(UserDto.class));
    }

    /**
     * Vérifie qu'une soumission de mise à jour avec un nouveau mot de passe trop faible
     * réaffiche le formulaire avec l'identifiant de l'URL, sans appeler update().
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
                .andExpect(view().name("user/update"))
                .andExpect(model().attribute("user", hasProperty("id", is(1))));

        verify(userService, never()).update(anyInt(), any(UserDto.class));
    }

    /**
     * Vérifie qu'une soumission de mise à jour invalide réaffiche le formulaire
     * avec l'identifiant de l'URL, sans appeler update().
     */
    @Test
    @WithMockUser
    void updateUser_shouldReturnUpdateView_whenDataIsInvalid() throws Exception {
        mockMvc.perform(post("/user/update/1")
                        .with(csrf())
                        .param("password", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("user/update"))
                .andExpect(model().attribute("user", hasProperty("id", is(1))));

        verify(userService, never()).update(anyInt(), any(UserDto.class));
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