package com.crm.service;

import com.crm.model.CustomerLead;
import com.crm.model.Note;
import com.crm.repository.CustomerLeadRepository;
import com.crm.repository.NoteRepository;
import com.crm.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteService {

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private CustomerLeadRepository customerLeadRepository;

    public List<Note> getNotesByLead(Long leadId) {
        return noteRepository.findByCustomerLeadIdOrderByCreatedDateDesc(leadId);
    }

    public Note addNote(Long leadId, Note note) {
        CustomerLead lead = customerLeadRepository.findById(leadId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer Lead not found with id: " + leadId));
        
        note.setCustomerLead(lead);
        return noteRepository.save(note);
    }
    
    public void deleteNote(Long noteId) {
        noteRepository.deleteById(noteId);
    }
}
