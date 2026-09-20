package com.nnk.springboot.security;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitaires pour {@link PasswordValidator}.
 * Vérifie chaque règle de validation (longueur, majuscule, chiffre, symbole)
 * indépendamment, ainsi que les cas limites (null, chaîne vide).
 */
public class PasswordValidatorTest {

    /**
     * Vérifie qu'un mot de passe respectant toutes les règles est accepté.
     */
    @Test
    void isValid_shouldReturnTrue_forStrongPassword() {
        assertTrue(PasswordValidator.isValid("Str0ng@Pass"));
    }

    /**
     * Vérifie qu'un mot de passe null est rejeté.
     */
    @Test
    void isValid_shouldReturnFalse_whenPasswordIsNull() {
        assertFalse(PasswordValidator.isValid(null));
    }

    /**
     * Vérifie qu'un mot de passe trop court (moins de 8 caractères) est rejeté,
     * même s'il contient tous les autres types de caractères requis.
     */
    @Test
    void isValid_shouldReturnFalse_whenPasswordTooShort() {
        assertFalse(PasswordValidator.isValid("Sh0rt@"));
    }

    /**
     * Vérifie qu'un mot de passe sans majuscule est rejeté.
     */
    @Test
    void isValid_shouldReturnFalse_whenNoUppercase() {
        assertFalse(PasswordValidator.isValid("str0ng@pass"));
    }

    /**
     * Vérifie qu'un mot de passe sans chiffre est rejeté.
     */
    @Test
    void isValid_shouldReturnFalse_whenNoDigit() {
        assertFalse(PasswordValidator.isValid("Strong@Pass"));
    }

    /**
     * Vérifie qu'un mot de passe sans symbole est rejeté.
     */
    @Test
    void isValid_shouldReturnFalse_whenNoSymbol() {
        assertFalse(PasswordValidator.isValid("Str0ngPass"));
    }

    /**
     * Vérifie qu'une chaîne vide est rejetée.
     */
    @Test
    void isValid_shouldReturnFalse_whenPasswordIsEmpty() {
        assertFalse(PasswordValidator.isValid(""));
    }
}
