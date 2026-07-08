package com.crm.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "follow_ups")
public class FollowUp {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(optional = false)
    @JoinColumn(name = "customer_lead_id", nullable = false)
    private CustomerLead customerLead;
    
    @Column(name = "followup_date", nullable = false)
    private LocalDateTime followupDate;
    
    @Column(name = "discussion_details", columnDefinition = "TEXT")
    private String discussionDetails;
    
    @Column(name = "next_followup_date")
    private LocalDate nextFollowupDate;
    
    private String status;
    private String priority;
    private String executive;
    
    @PrePersist
    protected void onCreate() {
        if (followupDate == null) {
            followupDate = LocalDateTime.now();
        }
    }
    
    public FollowUp() {}
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public CustomerLead getCustomerLead() { return customerLead; }
    public void setCustomerLead(CustomerLead customerLead) { this.customerLead = customerLead; }
    
    public LocalDateTime getFollowupDate() { return followupDate; }
    public void setFollowupDate(LocalDateTime followupDate) { this.followupDate = followupDate; }
    
    public String getDiscussionDetails() { return discussionDetails; }
    public void setDiscussionDetails(String discussionDetails) { this.discussionDetails = discussionDetails; }
    
    public LocalDate getNextFollowupDate() { return nextFollowupDate; }
    public void setNextFollowupDate(LocalDate nextFollowupDate) { this.nextFollowupDate = nextFollowupDate; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }
    
    public String getExecutive() { return executive; }
    public void setExecutive(String executive) { this.executive = executive; }
}
