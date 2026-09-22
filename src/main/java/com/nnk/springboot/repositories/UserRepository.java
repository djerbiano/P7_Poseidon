package com.nnk.springboot.repositories;

import com.nnk.springboot.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Repository Spring Data JPA pour l'entité {@link User}.
 * Fournit les opérations CRUD standard héritées de {@link JpaRepository},
 * ainsi que la possibilité de construire des requêtes dynamiques via
 * {@link JpaSpecificationExecutor}.
 */
public interface UserRepository extends JpaRepository<User, Integer>, JpaSpecificationExecutor<User> {

    /**
     * Recherche un utilisateur à partir de son nom d'utilisateur.
     * Utilisée notamment lors de l'authentification par Spring Security.
     *
     * @param username le nom d'utilisateur recherché
     * @return l'utilisateur correspondant, ou null si aucun utilisateur ne porte ce nom
     */
    User findByUsername(String username);
}
