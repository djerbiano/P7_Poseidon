package com.nnk.springboot.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Objet de transfert représentant les champs d'un User saisis dans les
 * formulaires d'ajout et de modification.
 * <p>
 * Le mot de passe est toujours en clair dans ce DTO : il n'est jamais
 * rempli avec le hash stocké en base, et il est haché par le service avant
 * tout enregistrement. Il ne porte volontairement aucune annotation de
 * validation : il est obligatoire à la création mais facultatif à la
 * modification (vide = mot de passe inchangé), ce qui est vérifié dans le
 * contrôleur.
 * <p>
 * L'identifiant sert uniquement à reconstruire l'URL du formulaire de
 * modification : il n'est jamais lu par le service lors d'une création
 * ou d'une mise à jour.
 */
@Getter
@Setter
@NoArgsConstructor
public class UserDto {

    private Integer id;

    @NotBlank(message = "Username is mandatory")
    private String username;

    private String password;

    @NotBlank(message = "FullName is mandatory")
    private String fullname;

    @NotBlank(message = "Role is mandatory")
    @Pattern(regexp = "ADMIN|USER", message = "Role must be ADMIN or USER")
    private String role;
}