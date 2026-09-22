package com.nnk.springboot.repositories;

import com.nnk.springboot.domain.BidList;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository Spring Data JPA pour l'entité {@link BidList}.
 * Fournit les opérations CRUD standard héritées de {@link JpaRepository}.
 */
public interface BidListRepository extends JpaRepository<BidList, Integer> {

}
