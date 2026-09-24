package com.nnk.springboot.services;

import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.dto.RatingDto;
import com.nnk.springboot.exceptions.ResourceNotFoundException;
import com.nnk.springboot.repositories.RatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service métier gérant les opérations CRUD sur l'entité {@link Rating}.
 * Il reçoit les données des formulaires sous forme de {@link RatingDto}
 * et porte également la règle métier exigeant qu'au moins une note
 * d'agence soit renseignée.
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
     * @param dto les données du formulaire à contrôler
     * @return true si au moins une note est renseignée, false si les trois sont vides ou nulles
     */
    public boolean hasAnyRating(RatingDto dto) {
        return !isBlank(dto.getMoodysRating())
                || !isBlank(dto.getSandPRating())
                || !isBlank(dto.getFitchRating());
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
     * Crée un nouveau Rating à partir des champs saisis dans le formulaire.
     * La règle {@link #hasAnyRating(RatingDto)} doit être vérifiée par
     * l'appelant avant la création. L'identifiant éventuellement présent
     * dans le DTO est ignoré : il est généré par la base de données.
     *
     * @param dto les données soumises depuis le formulaire d'ajout
     */
    public void create(RatingDto dto) {
        Rating rating = new Rating(dto.getMoodysRating(), dto.getSandPRating(),
                dto.getFitchRating(), dto.getOrderNumber());
        ratingRepository.save(rating);
    }

    /**
     * Met à jour un Rating existant avec les champs saisis dans le
     * formulaire. La règle {@link #hasAnyRating(RatingDto)} doit être
     * vérifiée par l'appelant avant la mise à jour.
     *
     * @param id  l'identifiant du Rating à modifier
     * @param dto les nouvelles valeurs saisies dans le formulaire de modification
     * @throws ResourceNotFoundException si aucun Rating n'existe pour cet identifiant
     */
    public void update(Integer id, RatingDto dto) {
        Rating rating = findById(id);
        rating.setMoodysRating(dto.getMoodysRating());
        rating.setSandPRating(dto.getSandPRating());
        rating.setFitchRating(dto.getFitchRating());
        rating.setOrderNumber(dto.getOrderNumber());
        ratingRepository.save(rating);
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