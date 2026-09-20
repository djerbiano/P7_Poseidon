package com.nnk.springboot.security;

/**
 * Classe utilitaire pour valider la robustesse d'un mot de passe.
 * Un mot de passe valide doit contenir au moins 8 caractères, une lettre
 * majuscule, un chiffre et un symbole (tout caractère qui n'est ni une
 * lettre ni un chiffre).
 */
public class PasswordValidator {

    /**
     * Vérifie si le mot de passe fourni respecte les règles de robustesse
     * exigées par l'application (longueur minimale, majuscule, chiffre, symbole).
     *
     * @param password le mot de passe à valider, peut être null
     * @return true si le mot de passe respecte toutes les règles, false sinon
     */
    public static boolean isValid(String password) {
        if (password == null || password.length() < 8) {
            return false;
        }

        boolean hasUppercase = false;
        boolean hasDigit = false;
        boolean hasSymbol = false;

        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) {
                hasUppercase = true;
            } else if (Character.isDigit(c)) {
                hasDigit = true;
            } else if (!Character.isLetterOrDigit(c)) {
                hasSymbol = true;
            }
        }

        return hasUppercase && hasDigit && hasSymbol;
    }
}