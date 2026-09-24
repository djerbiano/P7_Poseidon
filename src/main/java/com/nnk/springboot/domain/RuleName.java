package com.nnk.springboot.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Représente une règle métier, définissant une logique (SQL, template,
 * JSON) qui peut être appliquée dynamiquement par l'application.
 */
@Entity
@Table(name = "rulename")
@Getter
@Setter
@NoArgsConstructor
public class RuleName {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;
    private String description;
    private String json;
    private String template;
    private String sqlStr;
    private String sqlPart;

    /**
     * Construit un RuleName avec l'ensemble de ses champs métier, sans
     * identifiant. Utilisé par {@code RuleNameService} lors d'une création
     * et par les tests unitaires.
     *
     * @param name        le nom de la règle
     * @param description la description de la règle
     * @param json        la définition JSON de la règle
     * @param template    le template associé à la règle
     * @param sqlStr      la requête SQL de la règle
     * @param sqlPart     la partie SQL de la règle
     */
    public RuleName(String name, String description, String json, String template, String sqlStr, String sqlPart) {
        this.name = name;
        this.description = description;
        this.json = json;
        this.template = template;
        this.sqlStr = sqlStr;
        this.sqlPart = sqlPart;
    }
}