package com.devsuperior.bds04.controllers;

import com.devsuperior.bds04.dto.EventDTO;
import com.devsuperior.bds04.services.EventService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/events")
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping
    public ResponseEntity<Page<EventDTO>> findAll(Pageable pageable) {
        Page<EventDTO> events = eventService.findAll(pageable);
        return ResponseEntity.ok().body(events);
    }

    @PreAuthorize("hasAnyRole('ROLE_CLIENT', 'ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<EventDTO> insert(@Valid @RequestBody EventDTO dto) {
        dto = eventService.insert(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(dto.getId()).toUri();
        return ResponseEntity.created(uri).body(dto);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PutMapping(value = "{id}")
    public ResponseEntity<EventDTO> update(@PathVariable Long id, @Valid @RequestBody EventDTO dto) {
        dto = eventService.update(id, dto);
        return ResponseEntity.ok().body(dto);
    }
}
