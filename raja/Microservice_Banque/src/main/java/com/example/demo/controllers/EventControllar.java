package com.example.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.model.CenterEntity;
import com.example.demo.model.EventEntity;
import com.example.demo.Repository.CenterRepository;
import com.example.demo.Repository.EventRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/events")
@CrossOrigin(origins = "*")
public class EventControllar {

    @Autowired
    private EventRepository eventRepository;

    // 🔹 Get all events
    @GetMapping
    public ResponseEntity<List<EventEntity>> getAllEvents() {
        List<EventEntity> events = eventRepository.findAll();
        if (events.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // Retourne 204 si aucune ressource n'est trouvée
        }
        return new ResponseEntity<>(events, HttpStatus.OK); // Retourne 200 si des événements sont trouvés
    }
    @Autowired
    private CenterRepository CenterRepository;

    // 🔹 Create new event
    @PostMapping
    public ResponseEntity<EventEntity> createEvent(@RequestBody EventEntity event) {
        if (event == null || event.getCenter() == null || event.getCenter().getId() == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // Retourne 400 si l'événement ou le centre est invalide
        }

        // Récupérer le centre existant par son ID
        Optional<CenterEntity> centerOptional = CenterRepository.findById(event.getCenter().getId());
        if (centerOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // Retourne 400 si le centre n'existe pas
        }
        event.setCenter(centerOptional.get()); // Assigner le centre à l'événement

        // Sauvegarder l'événement
        EventEntity createdEvent = eventRepository.save(event);
        return new ResponseEntity<>(createdEvent, HttpStatus.CREATED); // Retourne 201 pour la création réussie
    }

    // 🔹 Get event by ID
    @GetMapping("/{id}")
    public ResponseEntity<EventEntity> getEventById(@PathVariable Long id) {
        Optional<EventEntity> event = eventRepository.findById(id);
        if (event.isPresent()) {
            return new ResponseEntity<>(event.get(), HttpStatus.OK); // Retourne 200 si l'événement est trouvé
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Retourne 404 si l'événement n'est pas trouvé
        }
    }

    // 🔹 Update event
    @PutMapping("/{id}")
    public ResponseEntity<EventEntity> updateEvent(@PathVariable Long id, @RequestBody EventEntity eventDetails) {
        Optional<EventEntity> optionalEvent = eventRepository.findById(id);
        if (optionalEvent.isPresent()) {
            EventEntity event = optionalEvent.get();
            event.setDate(eventDetails.getDate());
            event.setCenter(eventDetails.getCenter());
            EventEntity updatedEvent = eventRepository.save(event);
            return new ResponseEntity<>(updatedEvent, HttpStatus.OK); // Retourne 200 après mise à jour
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Retourne 404 si l'événement n'est pas trouvé
        }
    }

    // 🔹 Delete event
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        if (eventRepository.existsById(id)) {
            eventRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // Retourne 204 si suppression réussie
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Retourne 404 si l'événement n'est pas trouvé
        }
    }
}
