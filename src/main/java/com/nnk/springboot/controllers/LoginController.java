package com.nnk.springboot.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Contrôleur MVC gérant les pages liées à l'authentification :
 * le formulaire de connexion et la page d'accès refusé.
 */
@Controller
public class LoginController {

    /**
     * Affiche le formulaire de connexion.
     *
     * @return la vue du formulaire de connexion
     */
    @GetMapping("/login")
    public ModelAndView login() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("login");
        return mav;
    }

    /**
     * Affiche la page d'erreur 403 lorsqu'un utilisateur tente d'accéder à
     * une ressource pour laquelle il n'a pas les droits nécessaires.
     *
     * @return la vue 403, avec un message d'erreur explicatif
     */
    @GetMapping("/access-denied")
    public ModelAndView accessDenied() {
        ModelAndView mav = new ModelAndView();
        String errorMessage = "You are not authorized for the requested data.";
        mav.addObject("errorMsg", errorMessage);
        mav.setViewName("403");
        return mav;
    }
}
