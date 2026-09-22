package com.nnk.springboot.services;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.exceptions.ResourceNotFoundException;
import com.nnk.springboot.repositories.UserRepository;
import com.nnk.springboot.security.PasswordValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service métier gérant les utilisateurs de l'application.
 * Il assure le hachage des mots de passe avec BCrypt (facteur de coût 14)
 * avant toute persistance, et délègue la validation de leur robustesse
 * à {@link PasswordValidator}.
 */
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(14);

    /**
     * Récupère l'ensemble des utilisateurs enregistrés.
     *
     * @return la liste de tous les utilisateurs
     */
    public List<User> findAll() {
        return userRepository.findAll();
    }

    /**
     * Récupère un utilisateur à partir de son identifiant.
     *
     * @param id l'identifiant de l'utilisateur recherché
     * @return l'utilisateur correspondant
     * @throws ResourceNotFoundException si aucun utilisateur n'existe pour cet identifiant
     */
    public User findById(Integer id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invalid user Id:" + id));
    }

    /**
     * Vérifie qu'un mot de passe respecte les règles de robustesse de l'application
     * (8 caractères minimum, une majuscule, un chiffre et un symbole).
     *
     * @param password le mot de passe en clair à valider
     * @return true si le mot de passe est valide, false sinon
     * @see PasswordValidator#isValid(String)
     */
    public boolean isPasswordValid(String password) {
        return PasswordValidator.isValid(password);
    }

    /**
     * Crée un utilisateur en hachant son mot de passe avec BCrypt avant
     * l'enregistrement. La robustesse du mot de passe doit être vérifiée par
     * l'appelant via {@link #isPasswordValid(String)}.
     *
     * @param user l'utilisateur à créer, avec son mot de passe en clair
     */
    public void createUser(User user) {
        user.setPassword(encoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    /**
     * Met à jour un utilisateur existant. Si aucun nouveau mot de passe n'est
     * fourni, le mot de passe haché actuel est conservé ;
     * sinon le nouveau mot de passe est haché avec BCrypt.
     *
     * @param id   l'identifiant de l'utilisateur à mettre à jour
     * @param user les nouvelles données de l'utilisateur
     * @throws ResourceNotFoundException si aucun utilisateur n'existe pour cet identifiant
     */
    public void updateUser(Integer id, User user) {
        User existing = findById(id);
        if (user.getPassword() == null || user.getPassword().isBlank()) {
            user.setPassword(existing.getPassword());
        } else {
            user.setPassword(encoder.encode(user.getPassword()));
        }
        user.setId(id);
        userRepository.save(user);
    }

    /**
     * Supprime un utilisateur à partir de son identifiant.
     *
     * @param id l'identifiant de l'utilisateur à supprimer
     * @throws ResourceNotFoundException si aucun utilisateur n'existe pour cet identifiant
     */
    public void deleteById(Integer id) {
        User user = findById(id);
        userRepository.delete(user);
    }
}
