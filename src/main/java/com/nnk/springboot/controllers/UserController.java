package com.nnk.springboot.controllers;

import com.nnk.springboot.dto.UserDto;
import com.nnk.springboot.services.UserService;
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
 * Contrôleur MVC gérant les pages CRUD des User (liste, ajout,
 * modification, suppression). Les données soumises par les formulaires
 * sont reçues sous forme de {@link UserDto}. Délègue toute la logique
 * métier à {@link UserService}, y compris le hachage et la validation de
 * robustesse des mots de passe.
 */
@Controller
public class UserController {

    private static final String WEAK_PASSWORD_MESSAGE =
            "Password must contain at least 8 characters, one uppercase letter, one digit and one symbol";

    @Autowired
    private UserService userService;

    /**
     * Affiche la liste de tous les User.
     *
     * @param model le modèle Spring MVC, alimenté avec la liste des User
     * @return le nom de la vue affichant la liste
     */
    @RequestMapping("/user/list")
    public String home(Model model) {
        model.addAttribute("users", userService.findAll());
        return "user/list";
    }

    /**
     * Affiche le formulaire d'ajout d'un nouvel User.
     *
     * @param model le modèle Spring MVC, alimenté avec un DTO vide
     * @return le nom de la vue du formulaire d'ajout
     */
    @GetMapping("/user/add")
    public String addUserForm(Model model) {
        model.addAttribute("user", new UserDto());
        return "user/add";
    }

    /**
     * Valide et crée un nouvel User soumis depuis le formulaire d'ajout.
     * En cas d'erreur de validation, de mot de passe manquant, ou de mot de
     * passe ne respectant pas les règles de robustesse, réaffiche le formulaire.
     *
     * @param user   le DTO soumis, validé par ses contraintes
     * @param result le résultat de la validation
     * @return le nom de la vue du formulaire en cas d'erreur, sinon une
     *         redirection vers la liste
     */
    @PostMapping("/user/validate")
    public String validate(@Valid @ModelAttribute("user") UserDto user,
                           BindingResult result) {
        if (result.hasErrors()) {
            return "user/add";
        }
        if (user.getPassword() == null || user.getPassword().isBlank()) {
            result.rejectValue("password", "error.user", "Password is mandatory");
            return "user/add";
        }
        if (!userService.isPasswordValid(user.getPassword())) {
            result.rejectValue("password", "error.user", WEAK_PASSWORD_MESSAGE);
            return "user/add";
        }
        userService.create(user);
        return "redirect:/user/list";
    }

    /**
     * Affiche le formulaire de modification d'un User existant. Le DTO
     * transmis à la vue ne contient jamais le mot de passe haché.
     *
     * @param id    l'identifiant de l'User à modifier
     * @param model le modèle Spring MVC, alimenté avec le DTO de l'User
     * @return le nom de la vue du formulaire de modification
     */
    @GetMapping("/user/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        model.addAttribute("user", userService.findByIdAsDto(id));
        return "user/update";
    }

    /**
     * Valide et enregistre les modifications apportées à un User existant.
     * L'identifiant utilisé est toujours celui de l'URL. Si le mot de passe
     * soumis est vide, l'ancien mot de passe est conservé ; s'il est
     * renseigné, sa robustesse est vérifiée avant l'enregistrement.
     *
     * @param id     l'identifiant de l'User à mettre à jour
     * @param user   le DTO soumis, validé par ses contraintes
     * @param result le résultat de la validation
     * @return le nom de la vue du formulaire en cas d'erreur, sinon une
     *         redirection vers la liste
     */
    @PostMapping("/user/update/{id}")
    public String updateUser(@PathVariable("id") Integer id,
                             @Valid @ModelAttribute("user") UserDto user,
                             BindingResult result) {
        if (result.hasErrors()) {
            user.setId(id);
            return "user/update";
        }
        if (user.getPassword() != null && !user.getPassword().isBlank()
                && !userService.isPasswordValid(user.getPassword())) {
            result.rejectValue("password", "error.user", WEAK_PASSWORD_MESSAGE);
            user.setId(id);
            return "user/update";
        }
        userService.update(id, user);
        return "redirect:/user/list";
    }

    /**
     * Supprime un User à partir de son identifiant.
     *
     * @param id l'identifiant de l'User à supprimer
     * @return une redirection vers la liste des User
     */
    @GetMapping("/user/delete/{id}")
    public String deleteUser(@PathVariable("id") Integer id) {
        userService.deleteById(id);
        return "redirect:/user/list";
    }
}