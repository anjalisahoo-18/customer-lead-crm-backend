package com.crm.repository;

import com.crm.model.LeadType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface LeadTypeRepository extends JpaRepository<LeadType, Long> {
    Optional<LeadType> findByName(String name);
    boolean existsByName(String name);
}
