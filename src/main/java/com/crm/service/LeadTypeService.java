package com.crm.service;

import com.crm.model.LeadType;
import com.crm.repository.LeadTypeRepository;
import com.crm.exception.BadRequestException;
import com.crm.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;
import java.util.List;

@Service
public class LeadTypeService {

    @Autowired
    private LeadTypeRepository leadTypeRepository;

    @PostConstruct
    public void seedInitialLeadTypes() {
        if (leadTypeRepository.count() == 0) {
            leadTypeRepository.save(new LeadType("School Admission", "Leads interested in school admissions"));
            leadTypeRepository.save(new LeadType("Apartments", "Enquiries for premium apartments"));
            leadTypeRepository.save(new LeadType("Property", "Commercial and residential property leads"));
            leadTypeRepository.save(new LeadType("Laptops", "Corporate and individual laptop purchase leads"));
            leadTypeRepository.save(new LeadType("Insurance", "Life and health insurance sales leads"));
            leadTypeRepository.save(new LeadType("Cars", "New and used car purchase enquiries"));
            leadTypeRepository.save(new LeadType("Hospitals", "Hospital booking and healthcare service leads"));
        }
    }

    public List<LeadType> getAllLeadTypes() {
        return leadTypeRepository.findAll();
    }

    public LeadType getLeadTypeById(Long id) {
        return leadTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lead Type not found with id: " + id));
    }

    public LeadType createLeadType(LeadType leadType) {
        if (leadTypeRepository.existsByName(leadType.getName())) {
            throw new BadRequestException("Lead Type name already exists: " + leadType.getName());
        }
        return leadTypeRepository.save(leadType);
    }

    public LeadType updateLeadType(Long id, LeadType leadTypeDetails) {
        LeadType leadType = getLeadTypeById(id);
        if (!leadType.getName().equalsIgnoreCase(leadTypeDetails.getName()) && 
            leadTypeRepository.existsByName(leadTypeDetails.getName())) {
            throw new BadRequestException("Lead Type name already exists: " + leadTypeDetails.getName());
        }
        leadType.setName(leadTypeDetails.getName());
        leadType.setDescription(leadTypeDetails.getDescription());
        return leadTypeRepository.save(leadType);
    }

    public void deleteLeadType(Long id) {
        LeadType leadType = getLeadTypeById(id);
        leadTypeRepository.delete(leadType);
    }
}
