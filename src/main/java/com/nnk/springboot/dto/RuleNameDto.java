package com.nnk.springboot.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Objet de transfert représentant les champs d'un RuleName saisis dans les
 * formulaires d'ajout et de modification. Seuls ces champs peuvent être
 * liés depuis une requête HTTP.
 * <p>
 * L'identifiant sert uniquement à reconstruire l'URL du formulaire de
 * modification en cas d'erreur de validation : il n'est jamais lu par
 * le service.
 */
@Getter
@Setter
@NoArgsConstructor
public class RuleNameDto {

    private Integer id;

    @NotBlank(message = "Name is mandatory")
    private String name;

    @NotBlank(message = "Description is mandatory")
    private String description;

    @NotBlank(message = "Json is mandatory")
    private String json;

    @NotBlank(message = "Template is mandatory")
    private String template;

    @NotBlank(message = "SqlStr is mandatory")
    private String sqlStr;

    @NotBlank(message = "SqlPart is mandatory")
    private String sqlPart;
}