package com.crm.repository;

import com.crm.model.FollowUp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface FollowUpRepository extends JpaRepository<FollowUp, Long> {
    List<FollowUp> findByCustomerLeadIdOrderByFollowupDateDesc(Long customerLeadId);
    void deleteByCustomerLeadId(Long customerLeadId);
}
