package com.crm.controller;

import com.crm.model.FollowUp;
import com.crm.service.FollowUpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/leads/{leadId}/followups")
public class FollowUpController {

    @Autowired
    private FollowUpService followUpService;

    @GetMapping
    public ResponseEntity<List<FollowUp>> getFollowUpsByLead(@PathVariable Long leadId) {
        return ResponseEntity.ok(followUpService.getFollowUpsByLead(leadId));
    }

    @PostMapping
    public ResponseEntity<FollowUp> addFollowUp(@PathVariable Long leadId, @RequestBody FollowUp followUp) {
        return ResponseEntity.ok(followUpService.addFollowUp(leadId, followUp));
    }
}
