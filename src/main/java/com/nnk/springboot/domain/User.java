package com.nnk.springboot.domain;

import jakarta.persistence.*;
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

    private String username;

    /**
     * Mot de passe hashé avec BCrypt. N'est jamais exposé dans les
     * formulaires : il est géré exclusivement par {@code UserService}.
     */
    private String password;

    private String fullname;
    private String role;
}