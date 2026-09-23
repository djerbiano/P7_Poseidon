package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.services.CurvePointService;
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
 * Contrôleur MVC gérant les pages CRUD des CurvePoint (liste, ajout,
 * modification, suppression). Délègue toute la logique métier à
 * {@link CurvePointService}.
 */
@Controller
public class CurveController {

    @Autowired
    private CurvePointService curvePointService;

    /**
     * Affiche la liste de tous les CurvePoint.
     *
     * @param model le modèle Spring MVC, alimenté avec la liste des CurvePoint
     * @return le nom de la vue affichant la liste
     */
    @RequestMapping("/curvePoint/list")
    public String home(Model model) {
        model.addAttribute("curvePoints", curvePointService.findAll());
        return "curvePoint/list";
    }

    /**
     * Affiche le formulaire d'ajout d'un nouveau CurvePoint.
     *
     * @param bid un CurvePoint vide lié au formulaire
     * @return le nom de la vue du formulaire d'ajout
     */
    @GetMapping("/curvePoint/add")
    public String addBidForm(CurvePoint bid) {
        return "curvePoint/add";
    }

    /**
     * Valide et enregistre un nouveau CurvePoint soumis depuis le formulaire
     * d'ajout. En cas d'erreur de validation, réaffiche le formulaire.
     *
     * @param curvePoint le CurvePoint soumis, validé par les contraintes de l'entité
     * @param result     le résultat de la validation
     * @return le nom de la vue du formulaire en cas d'erreur, sinon une
     *         redirection vers la liste
     */
    @PostMapping("/curvePoint/validate")
    public String validate(@Valid CurvePoint curvePoint, BindingResult result) {
        if (result.hasErrors()) {
            return "curvePoint/add";
        }
        curvePointService.save(curvePoint);
        return "redirect:/curvePoint/list";
    }

    /**
     * Affiche le formulaire de modification d'un CurvePoint existant.
     *
     * @param id    l'identifiant du CurvePoint à modifier
     * @param model le modèle Spring MVC, alimenté avec le CurvePoint trouvé
     * @return le nom de la vue du formulaire de modification
     */
    @GetMapping("/curvePoint/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        CurvePoint curvePoint = curvePointService.findById(id);
        model.addAttribute("curvePoint", curvePoint);
        return "curvePoint/update";
    }

    /**
     * Valide et enregistre les modifications apportées à un CurvePoint
     * existant. En cas d'erreur de validation, réaffiche le formulaire.
     *
     * @param id         l'identifiant du CurvePoint à mettre à jour
     * @param curvePoint le CurvePoint soumis, validé par les contraintes de l'entité
     * @param result     le résultat de la validation
     * @return le nom de la vue du formulaire en cas d'erreur, sinon une
     *         redirection vers la liste
     */
    @PostMapping("/curvePoint/update/{id}")
    public String updateBid(@PathVariable("id") Integer id, @Valid CurvePoint curvePoint,
                            BindingResult result) {
        if (result.hasErrors()) {
            return "curvePoint/update";
        }
        curvePoint.setId(id);
        curvePointService.save(curvePoint);
        return "redirect:/curvePoint/list";
    }

    /**
     * Supprime un CurvePoint à partir de son identifiant.
     *
     * @param id l'identifiant du CurvePoint à supprimer
     * @return une redirection vers la liste des CurvePoint
     */
    @GetMapping("/curvePoint/delete/{id}")
    public String deleteBid(@PathVariable("id") Integer id) {
        curvePointService.deleteById(id);
        return "redirect:/curvePoint/list";
    }
}
