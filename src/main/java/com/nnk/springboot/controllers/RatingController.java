package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.services.RatingService;
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
 * Contrôleur MVC gérant les pages CRUD des Rating (liste, ajout,
 * modification, suppression). Délègue toute la logique métier à
 * {@link RatingService}, y compris la règle exigeant qu'au moins une
 * note d'agence soit renseignée.
 */
@Controller
public class RatingController {
    @Autowired
    private RatingService ratingService;

    /**
     * Affiche la liste de tous les Rating.
     *
     * @param model le modèle Spring MVC, alimenté avec la liste des Rating
     * @return le nom de la vue affichant la liste
     */
    @RequestMapping("/rating/list")
    public String home(Model model) {
        model.addAttribute("ratings", ratingService.findAll());
        return "rating/list";
    }

    /**
     * Affiche le formulaire d'ajout d'un nouveau Rating.
     *
     * @param rating un Rating vide lié au formulaire
     * @return le nom de la vue du formulaire d'ajout
     */
    @GetMapping("/rating/add")
    public String addRatingForm(Rating rating) {
        return "rating/add";
    }

    /**
     * Valide et enregistre un nouveau Rating soumis depuis le formulaire
     * d'ajout. En cas d'erreur de validation, ou si aucune note d'agence
     * n'est renseignée, réaffiche le formulaire.
     *
     * @param rating le Rating soumis, validé par les contraintes de l'entité
     * @param result le résultat de la validation
     * @return le nom de la vue du formulaire en cas d'erreur, sinon une
     * redirection vers la liste
     */
    @PostMapping("/rating/validate")
    public String validate(@Valid Rating rating, BindingResult result) {
        if (result.hasErrors()) {
            return "rating/add";
        }
        if (!ratingService.hasAnyRating(rating)) {
            result.reject("rating.empty", "At least one rating agency (Moody's, sandP, Fitch) must be provided");
            return "rating/add";
        }
        ratingService.save(rating);
        return "redirect:/rating/list";
    }

    /**
     * Affiche le formulaire de modification d'un Rating existant.
     *
     * @param id    l'identifiant du Rating à modifier
     * @param model le modèle Spring MVC, alimenté avec le Rating trouvé
     * @return le nom de la vue du formulaire de modification
     */
    @GetMapping("/rating/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        Rating rating = ratingService.findById(id);
        model.addAttribute("rating", rating);
        return "rating/update";
    }

    /**
     * Valide et enregistre les modifications apportées à un Rating
     * existant. En cas d'erreur de validation, ou si aucune note d'agence
     * n'est renseignée, réaffiche le formulaire.
     *
     * @param id     l'identifiant du Rating à mettre à jour
     * @param rating le Rating soumis, validé par les contraintes de l'entité
     * @param result le résultat de la validation
     * @return le nom de la vue du formulaire en cas d'erreur, sinon une
     * redirection vers la liste
     */
    @PostMapping("/rating/update/{id}")
    public String updateRating(@PathVariable("id") Integer id, @Valid Rating rating,
                               BindingResult result) {
        if (result.hasErrors()) {
            return "rating/update";
        }
        if (!ratingService.hasAnyRating(rating)) {
            result.reject("rating.empty", "At least one rating agency (Moody's, sandP, Fitch) must be provided");
            return "rating/update";
        }
        rating.setId(id);
        ratingService.save(rating);
        return "redirect:/rating/list";
    }

    /**
     * Supprime un Rating à partir de son identifiant.
     *
     * @param id l'identifiant du Rating à supprimer
     * @return une redirection vers la liste des Rating
     */
    @GetMapping("/rating/delete/{id}")
    public String deleteRating(@PathVariable("id") Integer id) {
        ratingService.deleteById(id);
        return "redirect:/rating/list";
    }
}
