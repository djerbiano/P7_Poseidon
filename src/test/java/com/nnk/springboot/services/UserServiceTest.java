package com.nnk.springboot.services;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.dto.UserDto;
import com.nnk.springboot.exceptions.ResourceNotFoundException;
import com.nnk.springboot.repositories.UserRepository;
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
 * Tests unitaires pour {@link UserService}.
 * Les dépendances (UserRepository) sont simulées avec Mockito
 * pour tester la logique du service de manière isolée, sans base de données réelle,
 * y compris le hachage du mot de passe et la logique de mise à jour conditionnelle.
 */
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User user;
    private UserDto dto;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1);
        user.setUsername("testuser");
        user.setPassword("OldHashedPassword123!");
        user.setFullname("Test User");
        user.setRole("USER");

        dto = new UserDto();
        dto.setUsername("newuser");
        dto.setFullname("New User");
        dto.setRole("ADMIN");
    }

    /**
     * Vérifie que findAll() retourne bien la liste fournie par le repository.
     */
    @Test
    void findAll_shouldReturnListOfUsers() {
        when(userRepository.findAll()).thenReturn(List.of(user));

        List<User> result = userService.findAll();

        assertEquals(1, result.size());
        verify(userRepository, times(1)).findAll();
    }

    /**
     * Vérifie que findById() retourne le bon User quand l'id existe.
     */
    @Test
    void findById_shouldReturnUser_whenIdExists() {
        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        User result = userService.findById(1);

        assertNotNull(result);
        assertEquals(1, result.getId());
    }

    /**
     * Vérifie que findById() lève ResourceNotFoundException quand l'id n'existe pas.
     */
    @Test
    void findById_shouldThrowException_whenIdDoesNotExist() {
        when(userRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.findById(99));
    }

    /**
     * Vérifie que findByIdAsDto() copie les champs de l'utilisateur
     * sans jamais copier le mot de passe haché.
     */
    @Test
    void findByIdAsDto_shouldReturnDtoWithoutPassword() {
        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        UserDto result = userService.findByIdAsDto(1);

        assertEquals(1, result.getId());
        assertEquals("testuser", result.getUsername());
        assertEquals("Test User", result.getFullname());
        assertEquals("USER", result.getRole());
        assertNull(result.getPassword());
    }

    /**
     * Vérifie que isPasswordValid() accepte un mot de passe conforme.
     */
    @Test
    void isPasswordValid_shouldReturnTrue_forStrongPassword() {
        assertTrue(userService.isPasswordValid("Str0ng@Pass"));
    }

    /**
     * Vérifie que isPasswordValid() rejette un mot de passe trop faible.
     */
    @Test
    void isPasswordValid_shouldReturnFalse_forWeakPassword() {
        assertFalse(userService.isPasswordValid("test"));
    }

    /**
     * Vérifie que create() enregistre un nouvel utilisateur avec les champs
     * du formulaire et un mot de passe haché, en ignorant tout identifiant
     * fourni par l'utilisateur.
     */
    @Test
    void create_shouldHashPasswordAndIgnoreIdFromDto() {
        dto.setId(42);
        dto.setPassword("Plain@Pass1");

        userService.create(dto);

        verify(userRepository, times(1)).save(
                argThat((User saved) ->
                        saved.getId() == null
                                && "newuser".equals(saved.getUsername())
                                && "New User".equals(saved.getFullname())
                                && "ADMIN".equals(saved.getRole())
                                && saved.getPassword() != null
                                && !"Plain@Pass1".equals(saved.getPassword())
                )
        );
    }

    /**
     * Vérifie que update() modifie les champs du formulaire et conserve
     * l'ancien mot de passe haché quand le nouveau mot de passe est vide.
     */
    @Test
    void update_shouldKeepOldPassword_whenNewPasswordIsBlank() {
        dto.setPassword("");
        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        userService.update(1, dto);

        verify(userRepository, times(1)).save(user);
        assertEquals(1, user.getId());
        assertEquals("newuser", user.getUsername());
        assertEquals("New User", user.getFullname());
        assertEquals("ADMIN", user.getRole());
        assertEquals("OldHashedPassword123!", user.getPassword());
    }

    /**
     * Vérifie que update() conserve l'ancien mot de passe haché
     * quand le nouveau mot de passe est null (pas transmis du tout).
     */
    @Test
    void update_shouldKeepOldPassword_whenNewPasswordIsNull() {
        dto.setPassword(null);
        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        userService.update(1, dto);

        verify(userRepository, times(1)).save(user);
        assertEquals("OldHashedPassword123!", user.getPassword());
    }

    /**
     * Vérifie que update() hache le nouveau mot de passe quand il est renseigné.
     */
    @Test
    void update_shouldHashNewPassword_whenPasswordIsProvided() {
        dto.setPassword("New@Pass123");
        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        userService.update(1, dto);

        verify(userRepository, times(1)).save(user);
        assertNotEquals("New@Pass123", user.getPassword());
        assertNotEquals("OldHashedPassword123!", user.getPassword());
    }

    /**
     * Vérifie que update() lève ResourceNotFoundException si l'id n'existe pas,
     * sans jamais appeler save().
     */
    @Test
    void update_shouldThrowException_whenIdDoesNotExist() {
        when(userRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.update(99, dto));
        verify(userRepository, never()).save(any());
    }

    /**
     * Vérifie que deleteById() trouve l'entité puis la supprime via le repository.
     */
    @Test
    void deleteById_shouldCallRepositoryDelete_whenIdExists() {
        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        userService.deleteById(1);

        verify(userRepository, times(1)).delete(user);
    }

    /**
     * Vérifie que deleteById() lève ResourceNotFoundException si l'id n'existe pas,
     * sans jamais appeler delete().
     */
    @Test
    void deleteById_shouldThrowException_whenIdDoesNotExist() {
        when(userRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.deleteById(99));
        verify(userRepository, never()).delete(any(User.class));
    }
}