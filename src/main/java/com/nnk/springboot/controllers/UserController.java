package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.services.UserService;
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
 * Contrôleur MVC gérant les pages CRUD des User (liste, ajout,
 * modification, suppression). Délègue toute la logique métier à
 * {@link UserService}, y compris le hachage et la validation de robustesse
 * des mots de passe.
 */
@Controller
public class UserController {

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
     * @param bid un User vide lié au formulaire
     * @return le nom de la vue du formulaire d'ajout
     */
    @GetMapping("/user/add")
    public String addUser(User bid) {
        return "user/add";
    }

    /**
     * Valide et crée un nouvel User soumis depuis le formulaire d'ajout.
     * En cas d'erreur de validation, de mot de passe manquant, ou de mot de
     * passe ne respectant pas les règles de robustesse, réaffiche le formulaire.
     *
     * @param user   l'User soumis, validé par les contraintes de l'entité
     * @param result le résultat de la validation
     * @return le nom de la vue du formulaire en cas d'erreur, sinon une
     * redirection vers la liste
     */
    @PostMapping("/user/validate")
    public String validate(@Valid User user, BindingResult result) {
        if (result.hasErrors()) {
            return "user/add";
        }
        if (user.getPassword() == null || user.getPassword().isBlank()) {
            result.rejectValue("password", "error.user", "Password is mandatory");
            return "user/add";
        }
        if (!userService.isPasswordValid(user.getPassword())) {
            result.rejectValue("password", "error.user",
                    "Password must contain at least 8 characters, one uppercase letter, one digit and one symbol");
            return "user/add";
        }
        userService.createUser(user);
        return "redirect:/user/list";
    }

    /**
     * Affiche le formulaire de modification d'un User existant. Le mot de
     * passe haché n'est jamais renvoyé au formulaire : il est vidé pour
     * éviter de l'exposer côté client.
     *
     * @param id    l'identifiant de l'User à modifier
     * @param model le modèle Spring MVC, alimenté avec l'User trouvé
     * @return le nom de la vue du formulaire de modification
     */
    @GetMapping("/user/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        User user = userService.findById(id);
        user.setPassword("");
        model.addAttribute("user", user);
        return "user/update";
    }

    /**
     * Valide et enregistre les modifications apportées à un User existant.
     * Si le mot de passe soumis est vide, l'ancien mot de passe est conservé
     * (voir {@link UserService#updateUser(Integer, User)}) ; s'il est renseigné,
     * sa robustesse est vérifiée avant l'enregistrement.
     *
     * @param id     l'identifiant de l'User à mettre à jour
     * @param user   l'User soumis, validé par les contraintes de l'entité
     * @param result le résultat de la validation
     * @return le nom de la vue du formulaire en cas d'erreur, sinon une
     * redirection vers la liste
     */
    @PostMapping("/user/update/{id}")
    public String updateUser(@PathVariable("id") Integer id, @Valid User user,
                             BindingResult result) {
        if (result.hasErrors()) {
            return "user/update";
        }
        if (user.getPassword() != null && !user.getPassword().isBlank()
                && !userService.isPasswordValid(user.getPassword())) {
            result.rejectValue("password", "error.user",
                    "Password must contain at least 8 characters, one uppercase letter, one digit and one symbol");
            return "user/update";
        }
        userService.updateUser(id, user);
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
