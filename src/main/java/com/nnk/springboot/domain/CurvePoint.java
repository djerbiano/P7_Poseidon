package com.nnk.springboot.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;
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

    private Integer curveId;

    /**
     * Date à laquelle ce point de courbe est valide. Renseignée
     * automatiquement par le service lors de chaque création ou mise à
     * jour, jamais saisie par l'utilisateur.
     */
    private Timestamp asOfDate;

    private Double term;
    private Double value;

    /**
     * Date de création de l'enregistrement. Renseignée automatiquement
     * par le service lors de la création, et jamais modifiée par la suite.
     */
    private Timestamp creationDate;

    /**
     * Construit un CurvePoint avec ses champs essentiels, sans identifiant
     * ni dates de création/mise à jour. Utilisé par {@code CurvePointService}
     * lors d'une création et par les tests unitaires pour créer des
     * instances sans passer par tous les champs de l'entité.
     *
     * @param curveId l'identifiant de la courbe associée
     * @param term    le terme du point de courbe
     * @param value   la valeur du point de courbe
     */
    public CurvePoint(Integer curveId, Double term, Double value) {
        this.curveId = curveId;
        this.term = term;
        this.value = value;
    }
}