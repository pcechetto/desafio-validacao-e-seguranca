package com.devsuperior.bds04.services;

import com.devsuperior.bds04.dto.CityDTO;
import com.devsuperior.bds04.entities.City;
import com.devsuperior.bds04.repositories.CityRepository;
import com.devsuperior.bds04.services.exceptions.ResourceNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CityService {

    private final CityRepository cityRepository;

    public CityService(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    @Transactional(readOnly = true)
    public Page<CityDTO> findAll(Pageable pageable) {
        Pageable pageableSortedByName = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("name"));
        return cityRepository.findAll(pageableSortedByName).map(CityDTO::new);
    }

    @Transactional
    public CityDTO insert(CityDTO dto) {
        City entity = new City();
        copyDtoToEntity(dto, entity);
        return new CityDTO(cityRepository.save(entity));
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public void delete(Long id) {
        if (!cityRepository.existsById(id)) {
            throw new ResourceNotFoundException("City not found for this id: " + id);
        }
        try {
            cityRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException("City cannot be deleted because it has dependent events");
        }
    }

    private void copyDtoToEntity(CityDTO dto, City entity) {
        entity.setId(dto.getId());
        entity.setName(dto.getName());
    }
}
