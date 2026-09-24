package com.nnk.springboot.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Objet de transfert représentant les champs d'un Rating saisis dans les
 * formulaires d'ajout et de modification. Seuls ces champs peuvent être
 * liés depuis une requête HTTP.
 * <p>
 * Aucune des trois notes d'agence n'est individuellement obligatoire :
 * le service vérifie qu'au moins l'une d'entre elles est renseignée.
 * <p>
 * L'identifiant sert uniquement à reconstruire l'URL du formulaire de
 * modification en cas d'erreur de validation : il n'est jamais lu par
 * le service.
 */
@Getter
@Setter
@NoArgsConstructor
public class RatingDto {

    private Integer id;

    private String moodysRating;

    private String sandPRating;

    private String fitchRating;

    @NotNull(message = "OrderNumber is mandatory")
    @Positive(message = "OrderNumber must be positive")
    private Integer orderNumber;
}