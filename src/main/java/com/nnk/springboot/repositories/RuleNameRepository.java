package com.nnk.springboot.repositories;

import com.nnk.springboot.domain.RuleName;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository Spring Data JPA pour l'entité {@link RuleName}.
 * Fournit les opérations CRUD standard héritées de {@link JpaRepository}.
 */
public interface RuleNameRepository extends JpaRepository<RuleName, Integer> {
}
