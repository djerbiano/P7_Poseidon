package com.nnk.springboot.controllers;

import com.nnk.springboot.dto.BidListDto;
import com.nnk.springboot.services.BidListService;
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
 * Contrôleur MVC gérant les pages CRUD des BidList (liste, ajout,
 * modification, suppression). Les données soumises par les formulaires
 * sont reçues sous forme de {@link BidListDto}. Délègue toute la logique
 * métier à {@link BidListService}.
 */
@Controller
public class BidListController {

    @Autowired
    private BidListService bidListService;

    /**
     * Affiche la liste de toutes les BidList.
     *
     * @param model le modèle Spring MVC, alimenté avec la liste des BidList
     * @return le nom de la vue affichant la liste
     */
    @RequestMapping("/bidList/list")
    public String home(Model model) {
        model.addAttribute("bidLists", bidListService.findAll());
        return "bidList/list";
    }

    /**
     * Affiche le formulaire d'ajout d'une nouvelle BidList.
     *
     * @param model le modèle Spring MVC, alimenté avec un DTO vide
     * @return le nom de la vue du formulaire d'ajout
     */
    @GetMapping("/bidList/add")
    public String addBidListForm(Model model) {
        model.addAttribute("bidList", new BidListDto());
        return "bidList/add";
    }

    /**
     * Valide et enregistre une nouvelle BidList soumise depuis le formulaire
     * d'ajout. En cas d'erreur de validation, réaffiche le formulaire.
     *
     * @param bidList le DTO soumis, validé par ses contraintes
     * @param result  le résultat de la validation
     * @return le nom de la vue du formulaire en cas d'erreur, sinon une
     *         redirection vers la liste
     */
    @PostMapping("/bidList/validate")
    public String validate(@Valid @ModelAttribute("bidList") BidListDto bidList,
                           BindingResult result) {
        if (result.hasErrors()) {
            return "bidList/add";
        }
        bidListService.create(bidList);
        return "redirect:/bidList/list";
    }

    /**
     * Affiche le formulaire de modification d'une BidList existante.
     *
     * @param id    l'identifiant de la BidList à modifier
     * @param model le modèle Spring MVC, alimenté avec la BidList trouvée
     * @return le nom de la vue du formulaire de modification
     */
    @GetMapping("/bidList/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        model.addAttribute("bidList", bidListService.findById(id));
        return "bidList/update";
    }

    /**
     * Valide et enregistre les modifications apportées à une BidList
     * existante. L'identifiant utilisé est toujours celui de l'URL. En cas
     * d'erreur de validation, réaffiche le formulaire.
     *
     * @param id      l'identifiant de la BidList à mettre à jour
     * @param bidList le DTO soumis, validé par ses contraintes
     * @param result  le résultat de la validation
     * @return le nom de la vue du formulaire en cas d'erreur, sinon une
     *         redirection vers la liste
     */
    @PostMapping("/bidList/update/{id}")
    public String updateBidList(@PathVariable("id") Integer id,
                                @Valid @ModelAttribute("bidList") BidListDto bidList,
                                BindingResult result) {
        if (result.hasErrors()) {
            bidList.setBidListId(id);
            return "bidList/update";
        }
        bidListService.update(id, bidList);
        return "redirect:/bidList/list";
    }

    /**
     * Supprime une BidList à partir de son identifiant.
     *
     * @param id l'identifiant de la BidList à supprimer
     * @return une redirection vers la liste des BidList
     */
    @GetMapping("/bidList/delete/{id}")
    public String deleteBidList(@PathVariable("id") Integer id) {
        bidListService.deleteById(id);
        return "redirect:/bidList/list";
    }
}