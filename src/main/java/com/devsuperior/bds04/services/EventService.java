package com.devsuperior.bds04.services;

import com.devsuperior.bds04.dto.EventDTO;
import com.devsuperior.bds04.entities.City;
import com.devsuperior.bds04.entities.Event;
import com.devsuperior.bds04.repositories.CityRepository;
import com.devsuperior.bds04.repositories.EventRepository;
import com.devsuperior.bds04.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class EventService {

    private final EventRepository eventRepository;
    private final CityRepository cityRepository;

    public EventService(EventRepository eventRepository, CityRepository cityRepository) {
        this.eventRepository = eventRepository;
        this.cityRepository = cityRepository;
    }

    @Transactional
    public EventDTO update(Long id, EventDTO dto) {
        try {
            Event event = eventRepository.getReferenceById(id);
            City city = cityRepository.getReferenceById(dto.getCityId());

            event.setName(dto.getName());
            event.setDate(dto.getDate());
            event.setUrl(dto.getUrl());
            event.setCity(city);

            return new EventDTO(eventRepository.save(event));
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("Event not found for this id: " + id);
        }
    }
}
