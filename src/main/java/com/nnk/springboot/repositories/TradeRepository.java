package com.nnk.springboot.repositories;

import com.nnk.springboot.domain.Trade;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository Spring Data JPA pour l'entité {@link Trade}.
 * Fournit les opérations CRUD standard héritées de {@link JpaRepository}.
 */
public interface TradeRepository extends JpaRepository<Trade, Integer> {
}
