package com.nnk.springboot.services;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.dto.UserDto;
import com.nnk.springboot.exceptions.ResourceNotFoundException;
import com.nnk.springboot.repositories.UserRepository;
import com.nnk.springboot.security.PasswordValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service métier gérant les utilisateurs de l'application.
 * Il reçoit les données des formulaires sous forme de {@link UserDto},
 * assure le hachage des mots de passe avec BCrypt (facteur de coût 14)
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
     * Récupère un utilisateur sous forme de DTO, pour pré-remplir le
     * formulaire de modification. Le mot de passe n'est volontairement
     * pas copié : le hash stocké en base n'est jamais envoyé au navigateur.
     *
     * @param id l'identifiant de l'utilisateur recherché
     * @return le DTO de l'utilisateur, sans mot de passe
     * @throws ResourceNotFoundException si aucun utilisateur n'existe pour cet identifiant
     */
    public UserDto findByIdAsDto(Integer id) {
        User user = findById(id);
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setFullname(user.getFullname());
        dto.setRole(user.getRole());
        return dto;
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
     * Crée un utilisateur à partir des champs saisis dans le formulaire, en
     * hachant son mot de passe avec BCrypt avant l'enregistrement. La
     * robustesse du mot de passe doit être vérifiée par l'appelant via
     * {@link #isPasswordValid(String)}. L'identifiant éventuellement présent
     * dans le DTO est ignoré : il est généré par la base de données.
     *
     * @param dto les données soumises, avec le mot de passe en clair
     */
    public void create(UserDto dto) {
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(encoder.encode(dto.getPassword()));
        user.setFullname(dto.getFullname());
        user.setRole(dto.getRole());
        userRepository.save(user);
    }

    /**
     * Met à jour un utilisateur existant. Si aucun nouveau mot de passe n'est
     * fourni, le mot de passe haché actuel est conservé ; sinon le nouveau
     * mot de passe est haché avec BCrypt.
     *
     * @param id  l'identifiant de l'utilisateur à mettre à jour
     * @param dto les nouvelles valeurs saisies dans le formulaire de modification
     * @throws ResourceNotFoundException si aucun utilisateur n'existe pour cet identifiant
     */
    public void update(Integer id, UserDto dto) {
        User user = findById(id);
        user.setUsername(dto.getUsername());
        user.setFullname(dto.getFullname());
        user.setRole(dto.getRole());
        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            user.setPassword(encoder.encode(dto.getPassword()));
        }
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