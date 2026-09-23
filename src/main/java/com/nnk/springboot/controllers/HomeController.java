package com.nnk.springboot.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Contrôleur MVC gérant la page d'accueil de l'application.
 */
@Controller
public class HomeController {

    /**
     * Affiche la page d'accueil.
     *
     * @return le nom de la vue de la page d'accueil
     */
    @RequestMapping("/")
    public String home() {
        return "home";
    }


}
