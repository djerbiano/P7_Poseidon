package com.nnk.springboot.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

/**
 * Gestionnaire d'exceptions global pour l'application, appliqué à tous les
 * controllers grâce à {@link ControllerAdvice}. Centralise la gestion de
 * certaines erreurs pour éviter d'afficher la page d'erreur générique de
 * Spring Boot, proposer un message clair à l'utilisateur et renvoyer le
 * code HTTP correspondant à la situation.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Intercepte les erreurs de conversion de type sur un paramètre d'URL
     * (par exemple, un identifiant textuel là où un nombre est attendu).
     * La requête est mal formée : le code HTTP renvoyé est 400.
     *
     * @param ex    l'exception levée par Spring lors de la conversion
     * @param model le modèle utilisé pour transmettre le message d'erreur à la vue
     * @return le nom de la vue affichant l'erreur ("error/400")
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleTypeMismatch(MethodArgumentTypeMismatchException ex, Model model) {
        String fieldName = ex.getName();
        model.addAttribute("errorMsg",
                "Invalid value for field '" + fieldName + "': a number is expected.");
        return "error/400";
    }

    /**
     * Intercepte les erreurs "ressource introuvable", levées lorsqu'un
     * identifiant fourni ne correspond à aucune entité en base de données.
     * La ressource demandée n'existe pas : le code HTTP renvoyé est 404.
     *
     * @param ex    l'exception personnalisée levée par les services
     * @param model le modèle utilisé pour transmettre le message d'erreur à la vue
     * @return le nom de la vue affichant l'erreur ("error/404")
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleResourceNotFound(ResourceNotFoundException ex, Model model) {
        model.addAttribute("errorMsg", ex.getMessage());
        return "error/404";
    }
}