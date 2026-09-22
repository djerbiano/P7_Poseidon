package com.nnk.springboot.repositories;

import com.nnk.springboot.domain.Rating;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository Spring Data JPA pour l'entité {@link Rating}.
 * Fournit les opérations CRUD standard héritées de {@link JpaRepository}.
 */
public interface RatingRepository extends JpaRepository<Rating, Integer> {

}
