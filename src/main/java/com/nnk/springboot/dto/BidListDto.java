package com.nnk.springboot.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Objet de transfert représentant les champs d'une BidList saisis dans
 * les formulaires d'ajout et de modification. Seuls ces champs peuvent
 * être liés depuis une requête HTTP, ce qui protège les autres colonnes
 * de l'entité contre le Mass Assignment.
 * <p>
 * L'identifiant sert uniquement à reconstruire l'URL du formulaire de
 * modification en cas d'erreur de validation : il n'est jamais lu par
 * le service.
 */
@Getter
@Setter
@NoArgsConstructor
public class BidListDto {

    private Integer bidListId;

    @NotBlank(message = "Account is mandatory")
    private String account;

    @NotBlank(message = "Type is mandatory")
    private String type;

    @NotNull(message = "BidQuantity is mandatory")
    @Positive(message = "BidQuantity must be positive")
    private Double bidQuantity;
}