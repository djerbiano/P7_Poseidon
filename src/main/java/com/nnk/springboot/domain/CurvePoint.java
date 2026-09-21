package com.nnk.springboot.domain;

import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.sql.Timestamp;

/**
 * Représente un point sur une courbe de taux d'intérêt : pour une courbe
 * donnée ({@code curveId}), associe une échéance ({@code term}) à un taux
 * ({@code value}) à une date donnée.
 */
@Entity
@Table(name = "curvepoint")
@Getter
@Setter
@NoArgsConstructor
public class CurvePoint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull(message = "CurveId is mandatory")
    @Positive(message = "CurveId must be positive")
    private Integer curveId;

    /**
     * Date à laquelle ce point de courbe est valide. Renseignée
     * automatiquement par le service lors de chaque sauvegarde,
     * jamais saisie par l'utilisateur.
     */
    private Timestamp asOfDate;

    @NotNull(message = "Term is mandatory")
    @Positive(message = "Term must be positive")
    private Double term;

    @NotNull(message = "Value is mandatory")
    private Double value;

    /**
     * Date de création de l'enregistrement. Renseignée automatiquement
     * par le service lors de la première sauvegarde, et jamais modifiée
     * par la suite.
     */
    private Timestamp creationDate;

    public CurvePoint(Integer curveId, Double term, Double value) {
        this.curveId = curveId;
        this.term = term;
        this.value = value;
    }
}
