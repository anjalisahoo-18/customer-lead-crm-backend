package com.crm.controller;

import com.crm.model.Note;
import com.crm.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/leads/{leadId}/notes")
public class NoteController {

    @Autowired
    private NoteService noteService;

    @GetMapping
    public ResponseEntity<List<Note>> getNotesByLead(@PathVariable Long leadId) {
        return ResponseEntity.ok(noteService.getNotesByLead(leadId));
    }

    @PostMapping
    public ResponseEntity<Note> addNote(@PathVariable Long leadId, @RequestBody Note note) {
        return ResponseEntity.ok(noteService.addNote(leadId, note));
    }

    @DeleteMapping("/{noteId}")
    public ResponseEntity<Void> deleteNote(@PathVariable Long noteId) {
        noteService.deleteNote(noteId);
        return ResponseEntity.noContent().build();
    }
}
