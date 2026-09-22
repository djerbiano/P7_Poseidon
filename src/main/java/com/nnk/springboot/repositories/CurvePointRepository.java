package com.nnk.springboot.repositories;

import com.nnk.springboot.domain.CurvePoint;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository Spring Data JPA pour l'entité {@link CurvePoint}.
 * Fournit les opérations CRUD standard héritées de {@link JpaRepository}.
 */
public interface CurvePointRepository extends JpaRepository<CurvePoint, Integer> {

}
