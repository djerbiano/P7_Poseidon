package com.nnk.springboot.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

/**
 * Représente une transaction effectivement réalisée (achat ou vente d'un
 * instrument financier), avec les quantités et prix associés, ainsi que
 * les informations de suivi (trader, book, dates de révision, etc.).
 */
@Entity
@Table(name = "trade")
@Getter
@Setter
@NoArgsConstructor
public class Trade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer tradeId;


    private String account;
    private String type;
    private Double buyQuantity;
    private Double sellQuantity;
    private Double buyPrice;
    private Double sellPrice;
    private String benchmark;
    private Timestamp tradeDate;
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
     * Construit un Trade avec ses champs essentiels, sans identifiant.
     * Utilisé par {@code TradeService} lors d'une création et par les
     * tests unitaires pour créer des instances sans passer par tous les
     * champs de l'entité.
     *
     * @param account le compte associé au Trade
     * @param type    le type de Trade
     */
    public Trade(String account, String type) {
        this.account = account;
        this.type = type;
    }
}
