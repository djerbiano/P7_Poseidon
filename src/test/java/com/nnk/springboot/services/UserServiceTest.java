package com.nnk.springboot.services;

import com.nnk.springboot.domain.User;
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
 * pour tester la logique du service de manière isolée, sans base de donnees réelle,
 * y compris le hachage du mot de passe et la logique de mise à jour conditionnelle.
 */
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1);
        user.setUsername("testuser");
        user.setPassword("OldHashedPassword123!");
        user.setFullname("Test User");
        user.setRole("USER");
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
     * Vérifie que createUser() hash le mot de passe avant de sauvegarder.
     */
    @Test
    void createUser_shouldHashPasswordBeforeSaving() {
        User candidate = new User();
        candidate.setUsername("candidate");
        candidate.setPassword("Plain@Pass1");
        candidate.setFullname("Candidate User");
        candidate.setRole("USER");

        userService.createUser(candidate);

        assertNotEquals("Plain@Pass1", candidate.getPassword());
        verify(userRepository, times(1)).save(candidate);
    }

    /**
     * Vérifie que updateUser() conserve l'ancien mot de passe hashe
     * quand le nouveau mot de passe est vide (l'utilisateur ne veut pas le changer).
     */
    @Test
    void updateUser_shouldKeepOldPassword_whenNewPasswordIsBlank() {
        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        User candidate = new User();
        candidate.setUsername("testuser");
        candidate.setPassword("");
        candidate.setFullname("Test User Update");
        candidate.setRole("USER");

        userService.updateUser(1, candidate);

        assertEquals("OldHashedPassword123!", candidate.getPassword());
        verify(userRepository, times(1)).save(candidate);
    }

    /**
     * Vérifie que updateUser() conserve l'ancien mot de passe hashé
     * quand le nouveau mot de passe est null (pas transmis du tout).
     */
    @Test
    void updateUser_shouldKeepOldPassword_whenNewPasswordIsNull() {
        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        User candidate = new User();
        candidate.setUsername("testuser");
        candidate.setPassword(null);
        candidate.setFullname("Test User Update");
        candidate.setRole("USER");

        userService.updateUser(1, candidate);

        assertEquals("OldHashedPassword123!", candidate.getPassword());
        verify(userRepository, times(1)).save(candidate);
    }
    
    /**
     * Vérifie que updateUser() hash le nouveau mot de passe quand il est renseigné.
     */
    @Test
    void updateUser_shouldHashNewPassword_whenPasswordIsProvided() {
        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        User candidate = new User();
        candidate.setUsername("testuser");
        candidate.setPassword("New@Pass123");
        candidate.setFullname("Test User Update");
        candidate.setRole("USER");

        userService.updateUser(1, candidate);

        assertNotEquals("New@Pass123", candidate.getPassword());
        verify(userRepository, times(1)).save(candidate);
    }

    /**
     * Vérifie que updateUser() lève ResourceNotFoundException si l'id n'existe pas.
     */
    @Test
    void updateUser_shouldThrowException_whenIdDoesNotExist() {
        when(userRepository.findById(99)).thenReturn(Optional.empty());

        User candidate = new User();
        candidate.setPassword("");

        assertThrows(ResourceNotFoundException.class, () -> userService.updateUser(99, candidate));
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
