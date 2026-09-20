package com.nnk.springboot.security;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Tests unitaires pour {@link CustomUserDetailsService}.
 * Vérifie que le chargement d'un utilisateur pour Spring Security
 * fonctionne correctement, et qu'un utilisateur inconnu est rejeté
 * sans révéler s'il s'agit d'un problème de username ou de password.
 */
@ExtendWith(MockitoExtension.class)
public class CustomUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1);
        user.setUsername("testuser");
        user.setPassword("HashedPassword123!");
        user.setFullname("Test User");
        user.setRole("USER");
    }

    /**
     * Vérifie que loadUserByUsername() retourne un UserDetails correctement
     * rempli à partir de l'utilisateur trouvé en base.
     */
    @Test
    void loadUserByUsername_shouldReturnUserDetails_whenUserExists() {
        when(userRepository.findByUsername("testuser")).thenReturn(user);

        UserDetails result = customUserDetailsService.loadUserByUsername("testuser");

        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
        assertEquals("HashedPassword123!", result.getPassword());
    }

    /**
     * Vérifie que loadUserByUsername() lève UsernameNotFoundException
     * quand l'utilisateur n'existe pas, avec un message générique
     * (ne révélant pas si le username existe ou non).
     */
    @Test
    void loadUserByUsername_shouldThrowException_whenUserDoesNotExist() {
        when(userRepository.findByUsername("unknown")).thenReturn(null);

        UsernameNotFoundException exception = assertThrows(
                UsernameNotFoundException.class,
                () -> customUserDetailsService.loadUserByUsername("unknown")
        );
        assertEquals("Invalid username or password", exception.getMessage());
    }
}
