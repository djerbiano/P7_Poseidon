package com.nnk.springboot.services;

import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.dto.RuleNameDto;
import com.nnk.springboot.exceptions.ResourceNotFoundException;
import com.nnk.springboot.repositories.RuleNameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service métier gérant les opérations CRUD sur l'entité {@link RuleName}.
 * Il s'appuie sur {@link RuleNameRepository} pour l'accès aux données et
 * reçoit les données des formulaires sous forme de {@link RuleNameDto}.
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
     * Crée un nouveau RuleName à partir des champs saisis dans le formulaire.
     * L'identifiant éventuellement présent dans le DTO est ignoré : il est
     * généré par la base de données.
     *
     * @param dto les données soumises depuis le formulaire d'ajout
     */
    public void create(RuleNameDto dto) {
        RuleName ruleName = new RuleName(dto.getName(), dto.getDescription(), dto.getJson(),
                dto.getTemplate(), dto.getSqlStr(), dto.getSqlPart());
        ruleNameRepository.save(ruleName);
    }

    /**
     * Met à jour un RuleName existant avec les champs saisis dans le
     * formulaire.
     *
     * @param id  l'identifiant du RuleName à modifier
     * @param dto les nouvelles valeurs saisies dans le formulaire de modification
     * @throws ResourceNotFoundException si aucun RuleName n'existe pour cet identifiant
     */
    public void update(Integer id, RuleNameDto dto) {
        RuleName ruleName = findById(id);
        ruleName.setName(dto.getName());
        ruleName.setDescription(dto.getDescription());
        ruleName.setJson(dto.getJson());
        ruleName.setTemplate(dto.getTemplate());
        ruleName.setSqlStr(dto.getSqlStr());
        ruleName.setSqlPart(dto.getSqlPart());
        ruleNameRepository.save(ruleName);
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