package com.crm.controller;

import com.crm.model.LeadType;
import com.crm.service.LeadTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lead-types")
public class LeadTypeController {

    @Autowired
    private LeadTypeService leadTypeService;

    @GetMapping
    public ResponseEntity<List<LeadType>> getAllLeadTypes() {
        return ResponseEntity.ok(leadTypeService.getAllLeadTypes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LeadType> getLeadTypeById(@PathVariable Long id) {
        return ResponseEntity.ok(leadTypeService.getLeadTypeById(id));
    }

    @PostMapping
    public ResponseEntity<LeadType> createLeadType(@RequestBody LeadType leadType) {
        return ResponseEntity.ok(leadTypeService.createLeadType(leadType));
    }

    @PutMapping("/{id}")
    public ResponseEntity<LeadType> updateLeadType(@PathVariable Long id, @RequestBody LeadType leadType) {
        return ResponseEntity.ok(leadTypeService.updateLeadType(id, leadType));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLeadType(@PathVariable Long id) {
        leadTypeService.deleteLeadType(id);
        return ResponseEntity.noContent().build();
    }
}
