package com.crm.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "customer_leads")
public class CustomerLead {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "customer_name", nullable = false)
    private String customerName;
    
    @Column(nullable = false)
    private String mobile;
    
    @Column(name = "alternate_number")
    private String alternateNumber;
    
    private String email;
    
    @ManyToOne(optional = false)
    @JoinColumn(name = "lead_type_id", nullable = false)
    private LeadType leadType;
    
    private String city;
    
    @Column(columnDefinition = "TEXT")
    private String address;
    
    @Column(columnDefinition = "TEXT")
    private String requirement;
    
    @Column(name = "lead_source")
    private String leadSource;
    
    @Column(name = "assigned_executive")
    private String assignedExecutive;
    
    @Column(name = "discussion_details", columnDefinition = "TEXT")
    private String discussionDetails;
    
    @Column(name = "visit_date")
    private LocalDate visitDate;
    
    @Column(name = "next_followup_date")
    private LocalDate nextFollowupDate;
    
    @Column(nullable = false)
    private String status; // New, Contacted, Interested, Follow Up, Visit Scheduled, Negotiation, Closed Won, Closed Lost, Not Interested
    
    @Column(nullable = false)
    private String priority; // Hot, Warm, Cold, Not a Customer
    
    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate;
    
    @PrePersist
    protected void onCreate() {
        if (createdDate == null) {
            createdDate = LocalDateTime.now();
        }
    }
    
    public CustomerLead() {}
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
    
    public String getMobile() { return mobile; }
    public void setMobile(String mobile) { this.mobile = mobile; }
    
    public String getAlternateNumber() { return alternateNumber; }
    public void setAlternateNumber(String alternateNumber) { this.alternateNumber = alternateNumber; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public LeadType getLeadType() { return leadType; }
    public void setLeadType(LeadType leadType) { this.leadType = leadType; }
    
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    
    public String getRequirement() { return requirement; }
    public void setRequirement(String requirement) { this.requirement = requirement; }
    
    public String getLeadSource() { return leadSource; }
    public void setLeadSource(String leadSource) { this.leadSource = leadSource; }
    
    public String getAssignedExecutive() { return assignedExecutive; }
    public void setAssignedExecutive(String assignedExecutive) { this.assignedExecutive = assignedExecutive; }
    
    public String getDiscussionDetails() { return discussionDetails; }
    public void setDiscussionDetails(String discussionDetails) { this.discussionDetails = discussionDetails; }
    
    public LocalDate getVisitDate() { return visitDate; }
    public void setVisitDate(LocalDate visitDate) { this.visitDate = visitDate; }
    
    public LocalDate getNextFollowupDate() { return nextFollowupDate; }
    public void setNextFollowupDate(LocalDate nextFollowupDate) { this.nextFollowupDate = nextFollowupDate; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }
    
    public LocalDateTime getCreatedDate() { return createdDate; }
    public void setCreatedDate(LocalDateTime createdDate) { this.createdDate = createdDate; }
}
