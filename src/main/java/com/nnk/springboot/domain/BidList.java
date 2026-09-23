package com.nnk.springboot.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;
import java.sql.Timestamp;

/**
 * Représente une offre d'achat (bid) soumise par un compte pour un
 * instrument financier donné, avec la quantité proposée et les
 * informations de suivi associées (trader, book, dates de révision, etc.).
 */
@Entity
@Table(name = "bidlist")
@Getter
@Setter
@NoArgsConstructor
public class BidList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer bidListId;

    private String account;
    private String type;
    private Double bidQuantity;
    private Double askQuantity;
    private Double bid;
    private Double ask;
    private String benchmark;
    private Timestamp bidListDate;
    private String commentary;
    private String security;
    private String status;
    private String trader;
    private String book;
    private String creationName;
    private Timestamp creationDate;
    private String revisionName;
    private Timestamp revisionDate;
    private String dealName;
    private String dealType;
    private String sourceListId;
    private String side;

    /**
     * Construit une BidList avec ses champs essentiels, sans identifiant
     * ni date d'ajout. Utilisé par {@code BidListService} lors d'une
     * création et par les tests unitaires pour créer des instances sans
     * passer par tous les champs de l'entité.
     *
     * @param account     le compte associé à la BidList
     * @param type        le type de BidList
     * @param bidQuantity la quantité proposée
     */
    public BidList(String account, String type, Double bidQuantity) {
        this.account = account;
        this.type = type;
        this.bidQuantity = bidQuantity;
    }
}