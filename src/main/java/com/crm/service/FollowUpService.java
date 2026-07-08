package com.crm.service;

import com.crm.model.CustomerLead;
import com.crm.model.FollowUp;
import com.crm.repository.CustomerLeadRepository;
import com.crm.repository.FollowUpRepository;
import com.crm.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FollowUpService {

    @Autowired
    private FollowUpRepository followUpRepository;

    @Autowired
    private CustomerLeadRepository customerLeadRepository;

    public List<FollowUp> getFollowUpsByLead(Long leadId) {
        return followUpRepository.findByCustomerLeadIdOrderByFollowupDateDesc(leadId);
    }

    @Transactional
    public FollowUp addFollowUp(Long leadId, FollowUp followUp) {
        CustomerLead lead = customerLeadRepository.findById(leadId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer Lead not found with id: " + leadId));
        
        followUp.setCustomerLead(lead);
        FollowUp savedFollowUp = followUpRepository.save(followUp);

        // Update the main lead with the latest follow-up information
        if (followUp.getStatus() != null) {
            lead.setStatus(followUp.getStatus());
        }
        if (followUp.getPriority() != null) {
            lead.setPriority(followUp.getPriority());
        }
        lead.setNextFollowupDate(followUp.getNextFollowupDate());
        if (followUp.getDiscussionDetails() != null) {
            lead.setDiscussionDetails(followUp.getDiscussionDetails());
        }
        
        customerLeadRepository.save(lead);
        return savedFollowUp;
    }
}
