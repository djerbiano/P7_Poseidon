package com.nnk.springboot.services;

import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.exceptions.ResourceNotFoundException;
import com.nnk.springboot.repositories.RatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service métier gérant les opérations CRUD sur l'entité {@link Rating}.
 * Il porte également la règle métier exigeant qu'au moins une note d'agence
 * soit renseignée.
 */
@Service
public class RatingService {
    @Autowired
    private RatingRepository ratingRepository;

    /**
     * Récupère l'ensemble des Rating enregistrés.
     *
     * @return la liste de tous les Rating
     */
    public List<Rating> findAll() {
        return ratingRepository.findAll();
    }

    /**
     * Récupère un Rating à partir de son identifiant.
     *
     * @param id l'identifiant du Rating recherché
     * @return le Rating correspondant
     * @throws ResourceNotFoundException si aucun Rating n'existe pour cet identifiant
     */
    public Rating findById(Integer id) {
        return ratingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invalid rating Id:" + id));
    }

    /**
     * Vérifie qu'au moins une des trois notes d'agence (Moody's, S&amp;P, Fitch)
     * est renseignée, c'est-à-dire non nulle et non vide.
     *
     * @param rating le Rating à contrôler
     * @return true si au moins une note est renseignée, false si les trois sont vides ou nulles
     */
    public boolean hasAnyRating(Rating rating) {
        return !isBlank(rating.getMoodysRating())
                || !isBlank(rating.getSandPRating())
                || !isBlank(rating.getFitchRating());
    }

    /**
     * Indique si une chaîne est nulle ou ne contient que des espaces.
     *
     * @param value la chaîne à tester
     * @return true si la chaîne est nulle ou vide, false sinon
     */
    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }

    /**
     * Enregistre un Rating : création s'il n'a pas d'identifiant,
     * mise à jour sinon. La règle {@link #hasAnyRating(Rating)} doit être
     * vérifiée par l'appelant avant l'enregistrement.
     *
     * @param rating le Rating à enregistrer
     * @return le Rating enregistré, avec son identifiant généré le cas échéant
     */
    public Rating save(Rating rating) {
        return ratingRepository.save(rating);
    }

    /**
     * Supprime un Rating à partir de son identifiant.
     *
     * @param id l'identifiant du Rating à supprimer
     * @throws ResourceNotFoundException si aucun Rating n'existe pour cet identifiant
     */
    public void deleteById(Integer id) {
        Rating rating = findById(id);
        ratingRepository.delete(rating);
    }
}
