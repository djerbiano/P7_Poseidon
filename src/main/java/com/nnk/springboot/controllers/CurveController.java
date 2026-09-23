package com.nnk.springboot.controllers;

import com.nnk.springboot.dto.CurvePointDto;
import com.nnk.springboot.services.CurvePointService;
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
 * Contrôleur MVC gérant les pages CRUD des CurvePoint (liste, ajout,
 * modification, suppression). Les données soumises par les formulaires
 * sont reçues sous forme de {@link CurvePointDto}. Délègue toute la
 * logique métier à {@link CurvePointService}.
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
     * @param model le modèle Spring MVC, alimenté avec un DTO vide
     * @return le nom de la vue du formulaire d'ajout
     */
    @GetMapping("/curvePoint/add")
    public String addCurvePointForm(Model model) {
        model.addAttribute("curvePoint", new CurvePointDto());
        return "curvePoint/add";
    }

    /**
     * Valide et enregistre un nouveau CurvePoint soumis depuis le formulaire
     * d'ajout. En cas d'erreur de validation, réaffiche le formulaire.
     *
     * @param curvePoint le DTO soumis, validé par ses contraintes
     * @param result     le résultat de la validation
     * @return le nom de la vue du formulaire en cas d'erreur, sinon une
     *         redirection vers la liste
     */
    @PostMapping("/curvePoint/validate")
    public String validate(@Valid @ModelAttribute("curvePoint") CurvePointDto curvePoint,
                           BindingResult result) {
        if (result.hasErrors()) {
            return "curvePoint/add";
        }
        curvePointService.create(curvePoint);
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
        model.addAttribute("curvePoint", curvePointService.findById(id));
        return "curvePoint/update";
    }

    /**
     * Valide et enregistre les modifications apportées à un CurvePoint
     * existant. L'identifiant utilisé est toujours celui de l'URL. En cas
     * d'erreur de validation, réaffiche le formulaire.
     *
     * @param id         l'identifiant du CurvePoint à mettre à jour
     * @param curvePoint le DTO soumis, validé par ses contraintes
     * @param result     le résultat de la validation
     * @return le nom de la vue du formulaire en cas d'erreur, sinon une
     *         redirection vers la liste
     */
    @PostMapping("/curvePoint/update/{id}")
    public String updateCurvePoint(@PathVariable("id") Integer id,
                                   @Valid @ModelAttribute("curvePoint") CurvePointDto curvePoint,
                                   BindingResult result) {
        if (result.hasErrors()) {
            curvePoint.setId(id);
            return "curvePoint/update";
        }
        curvePointService.update(id, curvePoint);
        return "redirect:/curvePoint/list";
    }

    /**
     * Supprime un CurvePoint à partir de son identifiant.
     *
     * @param id l'identifiant du CurvePoint à supprimer
     * @return une redirection vers la liste des CurvePoint
     */
    @GetMapping("/curvePoint/delete/{id}")
    public String deleteCurvePoint(@PathVariable("id") Integer id) {
        curvePointService.deleteById(id);
        return "redirect:/curvePoint/list";
    }
}