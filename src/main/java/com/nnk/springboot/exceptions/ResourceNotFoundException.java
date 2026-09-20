package com.nnk.springboot.exceptions;

/**
 * Exception levée lorsqu'une ressource (entité) recherchée par son
 * identifiant n'existe pas en base de données. Interceptée par
 * {@link GlobalExceptionHandler} pour afficher un message d'erreur
 * clair à l'utilisateur, plutôt qu'une erreur technique brute.
 */
public class ResourceNotFoundException extends RuntimeException {

    /**
     * Construit l'exception avec un message décrivant la ressource introuvable.
     *
     * @param message le message d'erreur, typiquement l'identifiant recherché
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
