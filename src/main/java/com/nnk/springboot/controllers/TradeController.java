package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.services.TradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.validation.Valid;

/**
 * Contrôleur MVC gérant les pages CRUD des Trade (liste, ajout,
 * modification, suppression). Délègue toute la logique métier à
 * {@link TradeService}.
 */
@Controller
public class TradeController {

    @Autowired
    private TradeService tradeService;

    /**
     * Affiche la liste de tous les Trade.
     *
     * @param model le modèle Spring MVC, alimenté avec la liste des Trade
     * @return le nom de la vue affichant la liste
     */
    @RequestMapping("/trade/list")
    public String home(Model model) {
        model.addAttribute("trades", tradeService.findAll());
        return "trade/list";
    }

    /**
     * Affiche le formulaire d'ajout d'un nouveau Trade.
     *
     * @param bid un Trade vide lié au formulaire
     * @return le nom de la vue du formulaire d'ajout
     */
    @GetMapping("/trade/add")
    public String addTradeForm(Trade bid) {
        return "trade/add";
    }

    /**
     * Valide et enregistre un nouveau Trade soumis depuis le formulaire
     * d'ajout. En cas d'erreur de validation, réaffiche le formulaire.
     *
     * @param trade  le Trade soumis, validé par les contraintes de l'entité
     * @param result le résultat de la validation
     * @return le nom de la vue du formulaire en cas d'erreur, sinon une
     * redirection vers la liste
     */
    @PostMapping("/trade/validate")
    public String validate(@Valid Trade trade, BindingResult result) {
        if (result.hasErrors()) {
            return "trade/add";
        }
        tradeService.save(trade);
        return "redirect:/trade/list";
    }

    /**
     * Affiche le formulaire de modification d'un Trade existant.
     *
     * @param id    l'identifiant du Trade à modifier
     * @param model le modèle Spring MVC, alimenté avec le Trade trouvé
     * @return le nom de la vue du formulaire de modification
     */
    @GetMapping("/trade/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        Trade trade = tradeService.findById(id);
        model.addAttribute("trade", trade);
        return "trade/update";
    }

    /**
     * Valide et enregistre les modifications apportées à un Trade
     * existant. En cas d'erreur de validation, réaffiche le formulaire.
     *
     * @param id     l'identifiant du Trade à mettre à jour
     * @param trade  le Trade soumis, validé par les contraintes de l'entité
     * @param result le résultat de la validation
     * @return le nom de la vue du formulaire en cas d'erreur, sinon une
     * redirection vers la liste
     */
    @PostMapping("/trade/update/{id}")
    public String updateTrade(@PathVariable("id") Integer id, @Valid Trade trade,
                              BindingResult result) {
        if (result.hasErrors()) {
            return "trade/update";
        }
        trade.setTradeId(id);
        tradeService.save(trade);
        return "redirect:/trade/list";
    }

    /**
     * Supprime un Trade à partir de son identifiant.
     *
     * @param id l'identifiant du Trade à supprimer
     * @return une redirection vers la liste des Trade
     */
    @GetMapping("/trade/delete/{id}")
    public String deleteTrade(@PathVariable("id") Integer id) {
        tradeService.deleteById(id);
        return "redirect:/trade/list";
    }
}
