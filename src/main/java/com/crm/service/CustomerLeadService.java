package com.crm.service;

import com.crm.model.CustomerLead;
import com.crm.model.LeadType;
import com.crm.repository.CustomerLeadRepository;
import com.crm.repository.LeadTypeRepository;
import com.crm.exception.ResourceNotFoundException;
import com.crm.repository.FollowUpRepository;
import com.crm.repository.NoteRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class CustomerLeadService {

    @Autowired
    private CustomerLeadRepository leadRepository;

    @Autowired
    private LeadTypeRepository leadTypeRepository;

    @Autowired
    private FollowUpRepository followupRepository;

    @Autowired
    private NoteRepository noteRepository;

    @jakarta.annotation.PostConstruct
    public void seedInitialLeads() {
        if (leadRepository.count() == 0) {
            List<LeadType> types = leadTypeRepository.findAll();
            if (types.isEmpty()) return;

            LeadType schoolType = types.stream().filter(t -> t.getName().contains("School")).findFirst().orElse(types.get(0));
            LeadType aptsType = types.stream().filter(t -> t.getName().contains("Apartment")).findFirst().orElse(types.get(0));
            LeadType carType = types.stream().filter(t -> t.getName().contains("Car")).findFirst().orElse(types.get(0));
            LeadType laptopType = types.stream().filter(t -> t.getName().contains("Laptop")).findFirst().orElse(types.get(0));
            LeadType insType = types.stream().filter(t -> t.getName().contains("Insurance")).findFirst().orElse(types.get(0));

            CustomerLead l1 = new CustomerLead();
            l1.setCustomerName("Rahul Kumar");
            l1.setMobile("9876543210");
            l1.setAlternateNumber("9876543211");
            l1.setEmail("rahul.k@example.com");
            l1.setLeadType(laptopType);
            l1.setCity("Delhi");
            l1.setAddress("A-12, Lajpat Nagar, Delhi");
            l1.setRequirement("Requires 5 Dell Inspiron laptops for office use.");
            l1.setLeadSource("Web");
            l1.setAssignedExecutive("executive");
            l1.setDiscussionDetails("Shared catalog and quoted pricing.");
            l1.setNextFollowupDate(LocalDate.now().plusDays(1));
            l1.setStatus("New");
            l1.setPriority("Warm");
            l1.setCreatedDate(LocalDateTime.now().minusDays(3));
            leadRepository.save(l1);

            CustomerLead l2 = new CustomerLead();
            l2.setCustomerName("Priya Sharma");
            l2.setMobile("9123456789");
            l2.setEmail("priya.s@example.com");
            l2.setLeadType(aptsType);
            l2.setCity("Mumbai");
            l2.setAddress("Flats 402, Sea Breeze, Bandra, Mumbai");
            l2.setRequirement("Interested in a 3 BHK premium apartment.");
            l2.setLeadSource("Campaign");
            l2.setAssignedExecutive("admin");
            l2.setDiscussionDetails("Customer requested site visit details.");
            l2.setNextFollowupDate(LocalDate.now()); // Today!
            l2.setStatus("Interested");
            l2.setPriority("Hot");
            l2.setCreatedDate(LocalDateTime.now().minusDays(5));
            leadRepository.save(l2);

            CustomerLead l3 = new CustomerLead();
            l3.setCustomerName("Amit Patel");
            l3.setMobile("9988776655");
            l3.setLeadType(carType);
            l3.setCity("Ahmedabad");
            l3.setAddress("45, Shanti Kunj, Ahmedabad");
            l3.setRequirement("Enquiry for new Honda City CVT.");
            l3.setLeadSource("Referral");
            l3.setAssignedExecutive("executive");
            l3.setDiscussionDetails("Completed payment and closed deal!");
            l3.setStatus("Closed Won");
            l3.setPriority("Hot");
            l3.setCreatedDate(LocalDateTime.now().minusDays(10));
            leadRepository.save(l3);

            CustomerLead l4 = new CustomerLead();
            l4.setCustomerName("Vikram Malhotra");
            l4.setMobile("9543210987");
            l4.setEmail("vikram.m@example.com");
            l4.setLeadType(insType);
            l4.setCity("Bangalore");
            l4.setAddress("102, Green Glen Layout, Bangalore");
            l4.setRequirement("Requires family health cover for 4 members.");
            l4.setLeadSource("Cold Call");
            l4.setAssignedExecutive("executive");
            l4.setDiscussionDetails("Discussed premium options, customer asked to call back yesterday.");
            l4.setNextFollowupDate(LocalDate.now().minusDays(1)); // Overdue!
            l4.setStatus("Follow Up");
            l4.setPriority("Cold");
            l4.setCreatedDate(LocalDateTime.now().minusDays(4));
            leadRepository.save(l4);
        }
    }

    private Specification<CustomerLead> createSpecification(
            Long leadTypeId, String status, String priority, String city,
            LocalDate startDate, LocalDate endDate, String customerName, String mobile,
            String search, String assignedExecutive) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (leadTypeId != null) {
                predicates.add(cb.equal(root.get("leadType").get("id"), leadTypeId));
            }
            if (status != null && !status.isEmpty()) {
                predicates.add(cb.equal(cb.lower(root.get("status")), status.toLowerCase()));
            }
            if (priority != null && !priority.isEmpty()) {
                predicates.add(cb.equal(cb.lower(root.get("priority")), priority.toLowerCase()));
            }
            if (city != null && !city.isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("city")), "%" + city.toLowerCase() + "%"));
            }
            if (startDate != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("createdDate"), startDate.atStartOfDay()));
            }
            if (endDate != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("createdDate"), endDate.atTime(LocalTime.MAX)));
            }
            if (customerName != null && !customerName.isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("customerName")), "%" + customerName.toLowerCase() + "%"));
            }
            if (mobile != null && !mobile.isEmpty()) {
                predicates.add(cb.like(root.get("mobile"), "%" + mobile + "%"));
            }
            if (assignedExecutive != null && !assignedExecutive.isEmpty()) {
                predicates.add(cb.equal(root.get("assignedExecutive"), assignedExecutive));
            }
            if (search != null && !search.isEmpty()) {
                String searchPattern = "%" + search.toLowerCase() + "%";
                predicates.add(cb.or(
                    cb.like(cb.lower(root.get("customerName")), searchPattern),
                    cb.like(root.get("mobile"), searchPattern),
                    cb.like(cb.lower(root.get("email")), searchPattern),
                    cb.like(cb.lower(root.get("city")), searchPattern)
                ));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    public Page<CustomerLead> getFilteredLeads(
            Long leadTypeId, String status, String priority, String city,
            LocalDate startDate, LocalDate endDate, String customerName, String mobile,
            String search, String assignedExecutive, Pageable pageable) {
        
        Specification<CustomerLead> spec = createSpecification(
                leadTypeId, status, priority, city, startDate, endDate, customerName, mobile, search, assignedExecutive
        );
        return leadRepository.findAll(spec, pageable);
    }

    public List<CustomerLead> getFilteredLeadsList(
            Long leadTypeId, String status, String priority, String city,
            LocalDate startDate, LocalDate endDate, String customerName, String mobile,
            String search, String assignedExecutive) {
        
        Specification<CustomerLead> spec = createSpecification(
                leadTypeId, status, priority, city, startDate, endDate, customerName, mobile, search, assignedExecutive
        );
        return leadRepository.findAll(spec);
    }

    public List<CustomerLead> getAllLeads() {
        return leadRepository.findAll();
    }

    public CustomerLead getLeadById(Long id) {
        return leadRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer Lead not found with id: " + id));
    }

    public CustomerLead createLead(CustomerLead lead) {
        if (lead.getLeadType() != null && lead.getLeadType().getId() != null) {
            LeadType leadType = leadTypeRepository.findById(lead.getLeadType().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Lead Type not found"));
            lead.setLeadType(leadType);
        }
        if (lead.getStatus() == null) {
            lead.setStatus("New");
        }
        if (lead.getPriority() == null) {
            lead.setPriority("Warm");
        }
        return leadRepository.save(lead);
    }

    public CustomerLead updateLead(Long id, CustomerLead leadDetails) {
        CustomerLead lead = getLeadById(id);
        
        lead.setCustomerName(leadDetails.getCustomerName());
        lead.setMobile(leadDetails.getMobile());
        lead.setAlternateNumber(leadDetails.getAlternateNumber());
        lead.setEmail(leadDetails.getEmail());
        lead.setCity(leadDetails.getCity());
        lead.setAddress(leadDetails.getAddress());
        lead.setRequirement(leadDetails.getRequirement());
        lead.setLeadSource(leadDetails.getLeadSource());
        lead.setAssignedExecutive(leadDetails.getAssignedExecutive());
        lead.setDiscussionDetails(leadDetails.getDiscussionDetails());
        lead.setVisitDate(leadDetails.getVisitDate());
        lead.setNextFollowupDate(leadDetails.getNextFollowupDate());
        lead.setStatus(leadDetails.getStatus());
        lead.setPriority(leadDetails.getPriority());

        if (leadDetails.getLeadType() != null && leadDetails.getLeadType().getId() != null) {
            LeadType leadType = leadTypeRepository.findById(leadDetails.getLeadType().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Lead Type not found"));
            lead.setLeadType(leadType);
        }

        return leadRepository.save(lead);
    }

    @Transactional
    public void deleteLead(Long id) {
        CustomerLead lead = getLeadById(id);
        followupRepository.deleteByCustomerLeadId(id);
        noteRepository.deleteByCustomerLeadId(id);
        leadRepository.delete(lead);
    }

    public List<CustomerLead> getReminders(LocalDate date) {
        return leadRepository.findReminders(date);
    }

    public void saveAll(List<CustomerLead> leads) {
        leadRepository.saveAll(leads);
    }
}
