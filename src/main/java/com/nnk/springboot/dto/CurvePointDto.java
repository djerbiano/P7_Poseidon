package com.nnk.springboot.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Objet de transfert représentant les champs d'un CurvePoint saisis dans
 * les formulaires d'ajout et de modification. Seuls ces champs peuvent
 * être liés depuis une requête HTTP : les dates ({@code creationDate},
 * {@code asOfDate}) restent gérées exclusivement par le service.
 * <p>
 * L'identifiant sert uniquement à reconstruire l'URL du formulaire de
 * modification en cas d'erreur de validation : il n'est jamais lu par
 * le service.
 */
@Getter
@Setter
@NoArgsConstructor
public class CurvePointDto {

    private Integer id;

    @NotNull(message = "CurveId is mandatory")
    @Positive(message = "CurveId must be positive")
    private Integer curveId;

    @NotNull(message = "Term is mandatory")
    @Positive(message = "Term must be positive")
    private Double term;

    @NotNull(message = "Value is mandatory")
    private Double value;
}