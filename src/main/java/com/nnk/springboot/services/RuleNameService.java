package com.nnk.springboot.services;

import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.exceptions.ResourceNotFoundException;
import com.nnk.springboot.repositories.RuleNameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service métier gérant les opérations CRUD sur l'entité {@link RuleName}.
 * Il s'appuie sur {@link RuleNameRepository} pour l'accès aux données.
 */
@Service
public class RuleNameService {

    @Autowired
    private RuleNameRepository ruleNameRepository;

    /**
     * Récupère l'ensemble des RuleName enregistrés.
     *
     * @return la liste de tous les RuleName
     */
    public List<RuleName> findAll() {
        return ruleNameRepository.findAll();
    }

    /**
     * Récupère un RuleName à partir de son identifiant.
     *
     * @param id l'identifiant du RuleName recherché
     * @return le RuleName correspondant
     * @throws ResourceNotFoundException si aucun RuleName n'existe pour cet identifiant
     */
    public RuleName findById(Integer id) {
        return ruleNameRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invalid ruleName Id:" + id));
    }

    /**
     * Enregistre un RuleName : création s'il n'a pas d'identifiant,
     * mise à jour sinon.
     *
     * @param ruleName le RuleName à enregistrer
     * @return le RuleName enregistré, avec son identifiant généré le cas échéant
     */
    public RuleName save(RuleName ruleName) {
        return ruleNameRepository.save(ruleName);
    }

    /**
     * Supprime un RuleName à partir de son identifiant.
     *
     * @param id l'identifiant du RuleName à supprimer
     * @throws ResourceNotFoundException si aucun RuleName n'existe pour cet identifiant
     */
    public void deleteById(Integer id) {
        RuleName ruleName = findById(id);
        ruleNameRepository.delete(ruleName);
    }
}
