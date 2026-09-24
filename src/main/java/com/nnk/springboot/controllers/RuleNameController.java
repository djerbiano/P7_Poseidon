package com.nnk.springboot.controllers;

import com.nnk.springboot.dto.RuleNameDto;
import com.nnk.springboot.services.RuleNameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.validation.Valid;

/**
 * Contrôleur MVC gérant les pages CRUD des RuleName (liste, ajout,
 * modification, suppression). Les données soumises par les formulaires
 * sont reçues sous forme de {@link RuleNameDto}. Délègue toute la logique
 * métier à {@link RuleNameService}.
 */
@Controller
public class RuleNameController {

    @Autowired
    private RuleNameService ruleNameService;

    /**
     * Affiche la liste de tous les RuleName.
     *
     * @param model le modèle Spring MVC, alimenté avec la liste des RuleName
     * @return le nom de la vue affichant la liste
     */
    @RequestMapping("/ruleName/list")
    public String home(Model model) {
        model.addAttribute("ruleNames", ruleNameService.findAll());
        return "ruleName/list";
    }

    /**
     * Affiche le formulaire d'ajout d'un nouveau RuleName.
     *
     * @param model le modèle Spring MVC, alimenté avec un DTO vide
     * @return le nom de la vue du formulaire d'ajout
     */
    @GetMapping("/ruleName/add")
    public String addRuleNameForm(Model model) {
        model.addAttribute("ruleName", new RuleNameDto());
        return "ruleName/add";
    }

    /**
     * Valide et enregistre un nouveau RuleName soumis depuis le formulaire
     * d'ajout. En cas d'erreur de validation, réaffiche le formulaire.
     *
     * @param ruleName le DTO soumis, validé par ses contraintes
     * @param result   le résultat de la validation
     * @return le nom de la vue du formulaire en cas d'erreur, sinon une
     *         redirection vers la liste
     */
    @PostMapping("/ruleName/validate")
    public String validate(@Valid @ModelAttribute("ruleName") RuleNameDto ruleName,
                           BindingResult result) {
        if (result.hasErrors()) {
            return "ruleName/add";
        }
        ruleNameService.create(ruleName);
        return "redirect:/ruleName/list";
    }

    /**
     * Affiche le formulaire de modification d'un RuleName existant.
     *
     * @param id    l'identifiant du RuleName à modifier
     * @param model le modèle Spring MVC, alimenté avec le RuleName trouvé
     * @return le nom de la vue du formulaire de modification
     */
    @GetMapping("/ruleName/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        model.addAttribute("ruleName", ruleNameService.findById(id));
        return "ruleName/update";
    }

    /**
     * Valide et enregistre les modifications apportées à un RuleName
     * existant. L'identifiant utilisé est toujours celui de l'URL. En cas
     * d'erreur de validation, réaffiche le formulaire.
     *
     * @param id       l'identifiant du RuleName à mettre à jour
     * @param ruleName le DTO soumis, validé par ses contraintes
     * @param result   le résultat de la validation
     * @return le nom de la vue du formulaire en cas d'erreur, sinon une
     *         redirection vers la liste
     */
    @PostMapping("/ruleName/update/{id}")
    public String updateRuleName(@PathVariable("id") Integer id,
                                 @Valid @ModelAttribute("ruleName") RuleNameDto ruleName,
                                 BindingResult result) {
        if (result.hasErrors()) {
            ruleName.setId(id);
            return "ruleName/update";
        }
        ruleNameService.update(id, ruleName);
        return "redirect:/ruleName/list";
    }

    /**
     * Supprime un RuleName à partir de son identifiant.
     *
     * @param id l'identifiant du RuleName à supprimer
     * @return une redirection vers la liste des RuleName
     */
    @GetMapping("/ruleName/delete/{id}")
    public String deleteRuleName(@PathVariable("id") Integer id) {
        ruleNameService.deleteById(id);
        return "redirect:/ruleName/list";
    }
}