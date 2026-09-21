package com.nnk.springboot.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Représente un utilisateur de l'application, avec ses identifiants de
 * connexion et son rôle (ADMIN ou USER).
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Username is mandatory")
    private String username;

    /**
     * Mot de passe hashé de l'utilisateur. Volontairement sans annotation
     * de validation sur l'entité : la robustesse du mot de passe et son
     * caractère obligatoire sont vérifiés manuellement dans le service et
     * le contrôleur, pour permettre une mise à jour sans changer le mot
     * de passe existant.
     */
    private String password;

    @NotBlank(message = "FullName is mandatory")
    private String fullname;

    @NotBlank(message = "Role is mandatory")
    @Pattern(regexp = "ADMIN|USER", message = "Role must be ADMIN or USER")
    private String role;

}
