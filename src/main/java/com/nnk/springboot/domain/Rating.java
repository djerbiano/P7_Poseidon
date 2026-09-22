package com.nnk.springboot.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Représente la notation de crédit d'un titre financier, telle qu'attribuée
 * par les trois principales agences de notation (Moody's, Standard &amp; Poor's,
 * Fitch).
 */
@Entity
@Table(name = "rating")
@Getter
@Setter
@NoArgsConstructor
public class Rating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * Notation attribuée par l'agence Moody's. Aucun des trois champs de
     * notation n'est individuellement obligatoire : le service vérifie
     * qu'au moins l'un d'entre eux est renseigné.
     */
    private String moodysRating;

    /**
     * Notation attribuée par l'agence Standard &amp; Poor's. Voir
     * {@link #moodysRating} pour la règle de validation appliquée.
     */
    private String sandPRating;

    /**
     * Notation attribuée par l'agence Fitch. Voir
     * {@link #moodysRating} pour la règle de validation appliquée.
     */
    private String fitchRating;

    @NotNull(message = "OrderNumber is mandatory")
    @Positive(message = "OrderNumber must be positive")
    private Integer orderNumber;

    /**
     * Construit un Rating avec ses champs essentiels, sans identifiant.
     * Utilisé notamment par les tests unitaires pour créer des instances
     * sans passer par tous les champs de l'entité.
     *
     * @param moodysRating la note attribuée par Moody's
     * @param sandPRating  la note attribuée par Standard &amp; Poor's
     * @param fitchRating  la note attribuée par Fitch
     * @param orderNumber  le numéro d'ordre du Rating
     */
    public Rating(String moodysRating, String sandPRating, String fitchRating, Integer orderNumber) {
        this.moodysRating = moodysRating;
        this.sandPRating = sandPRating;
        this.fitchRating = fitchRating;
        this.orderNumber = orderNumber;
    }
}
