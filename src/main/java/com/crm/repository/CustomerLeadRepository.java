package com.crm.repository;

import com.crm.model.CustomerLead;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CustomerLeadRepository extends JpaRepository<CustomerLead, Long>, JpaSpecificationExecutor<CustomerLead> {
    
    @Query("SELECT c FROM CustomerLead c WHERE " +
           "(:leadTypeId IS NULL OR c.leadType.id = :leadTypeId) AND " +
           "(:status IS NULL OR LOWER(c.status) = LOWER(:status)) AND " +
           "(:priority IS NULL OR LOWER(c.priority) = LOWER(:priority)) AND " +
           "(:city IS NULL OR LOWER(c.city) LIKE LOWER(CONCAT('%', :city, '%'))) AND " +
           "(:startDate IS NULL OR c.createdDate >= :startDate) AND " +
           "(:endDate IS NULL OR c.createdDate <= :endDate) AND " +
           "(:customerName IS NULL OR LOWER(c.customerName) LIKE LOWER(CONCAT('%', :customerName, '%'))) AND " +
           "(:mobile IS NULL OR c.mobile LIKE CONCAT('%', :mobile, '%')) AND " +
           "(:search IS NULL OR LOWER(c.customerName) LIKE LOWER(CONCAT('%', :search, '%')) " +
           "OR c.mobile LIKE CONCAT('%', :search, '%') " +
           "OR LOWER(c.email) LIKE LOWER(CONCAT('%', :search, '%')) " +
           "OR LOWER(c.city) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<CustomerLead> filterLeads(
            @Param("leadTypeId") Long leadTypeId,
            @Param("status") String status,
            @Param("priority") String priority,
            @Param("city") String city,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            @Param("customerName") String customerName,
            @Param("mobile") String mobile,
            @Param("search") String search,
            Pageable pageable
    );

    @Query("SELECT c FROM CustomerLead c WHERE " +
           "(:leadTypeId IS NULL OR c.leadType.id = :leadTypeId) AND " +
           "(:status IS NULL OR LOWER(c.status) = LOWER(:status)) AND " +
           "(:priority IS NULL OR LOWER(c.priority) = LOWER(:priority)) AND " +
           "(:city IS NULL OR LOWER(c.city) LIKE LOWER(CONCAT('%', :city, '%'))) AND " +
           "(:startDate IS NULL OR c.createdDate >= :startDate) AND " +
           "(:endDate IS NULL OR c.createdDate <= :endDate) AND " +
           "(:customerName IS NULL OR LOWER(c.customerName) LIKE LOWER(CONCAT('%', :customerName, '%'))) AND " +
           "(:mobile IS NULL OR c.mobile LIKE CONCAT('%', :mobile, '%')) AND " +
           "(:search IS NULL OR LOWER(c.customerName) LIKE LOWER(CONCAT('%', :search, '%')) " +
           "OR c.mobile LIKE CONCAT('%', :search, '%') " +
           "OR LOWER(c.email) LIKE LOWER(CONCAT('%', :search, '%')) " +
           "OR LOWER(c.city) LIKE LOWER(CONCAT('%', :search, '%')))")
    List<CustomerLead> filterLeadsList(
            @Param("leadTypeId") Long leadTypeId,
            @Param("status") String status,
            @Param("priority") String priority,
            @Param("city") String city,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            @Param("customerName") String customerName,
            @Param("mobile") String mobile,
            @Param("search") String search
    );

    long countByStatus(String status);
    
    long countByPriority(String priority);

    @Query("SELECT COUNT(c) FROM CustomerLead c WHERE c.nextFollowupDate = :date")
    long countFollowupsForDate(@Param("date") LocalDate date);

    @Query("SELECT COUNT(c) FROM CustomerLead c WHERE c.nextFollowupDate < :date AND c.status NOT IN ('Closed Won', 'Closed Lost', 'Not Interested')")
    long countOverdueFollowups(@Param("date") LocalDate date);

    @Query("SELECT c FROM CustomerLead c WHERE c.nextFollowupDate = :date OR (c.nextFollowupDate < :date AND c.status NOT IN ('Closed Won', 'Closed Lost', 'Not Interested')) ORDER BY c.nextFollowupDate ASC")
    List<CustomerLead> findReminders(@Param("date") LocalDate date);

    @Query("SELECT c.status, COUNT(c) FROM CustomerLead c GROUP BY c.status")
    List<Object[]> getLeadsCountByStatus();

    @Query("SELECT c.priority, COUNT(c) FROM CustomerLead c GROUP BY c.priority")
    List<Object[]> getLeadsCountByPriority();

    @Query("SELECT c.leadType.name, COUNT(c) FROM CustomerLead c GROUP BY c.leadType.name")
    List<Object[]> getLeadsCountByLeadType();
}
