package ru.edu.penzgtu.lab.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.edu.penzgtu.lab.dto.PlanetDto;
import ru.edu.penzgtu.lab.entity.Planet;
import ru.edu.penzgtu.lab.repo.PlanetRepository;
import ru.edu.penzgtu.lab.service.mapper.PlanetMapper;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class PlanetService {

    private final PlanetRepository planetRepository;
    private final PlanetMapper planetMapper; // Инъекция маппера

    @Transactional(readOnly = true)
    public List<PlanetDto> findAllPlanets() {
        List<Planet> planets = planetRepository.findAll();
        return planetMapper.toListDto(planets);
    }

    @Transactional(readOnly = true)
    public PlanetDto findPlanetById(Long id) {
        Planet planet = planetRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Planet not found with id: " + id));
        return planetMapper.toDto(planet);
    }

    @Transactional
    public PlanetDto savePlanet(PlanetDto planetDto) {
        Planet planetToSave = planetMapper.toEntity(planetDto);
        if (planetDto.getId() != null && planetRepository.existsById(planetDto.getId())) {
            throw new IllegalArgumentException("Planet with ID " + planetDto.getId() + " already exists. Use update method.");
        }
        if (planetDto.getId() == null) {
            planetToSave.setId(null);
        }

        Planet savedPlanet = planetRepository.save(planetToSave);
        return planetMapper.toDto(savedPlanet);
    }

    @Transactional
    public PlanetDto updatePlanet(PlanetDto planetDto) {
        Long planetId = planetDto.getId();
        if (planetId == null) {
            throw new IllegalArgumentException("Planet ID must be provided for update.");
        }

        Planet existingPlanet = planetRepository.findById(planetId)
                .orElseThrow(() -> new NoSuchElementException("Planet not found with id: " + planetId + " for update."));

        existingPlanet.setName(planetDto.getName());
        existingPlanet.setType(planetDto.getType());
        existingPlanet.setDiameter(planetDto.getDiameter());
        existingPlanet.setHasAtmosphere(planetDto.getHasAtmosphere());
        existingPlanet.setStarSystem(planetDto.getStarSystem());

        Planet updatedPlanet = planetRepository.save(existingPlanet);
        return planetMapper.toDto(updatedPlanet);
    }

    @Transactional
    public void deletePlanetById(Long id) {
        if (!planetRepository.existsById(id)) {
            throw new NoSuchElementException("Planet not found with id: " + id + " for deletion.");
        }
        planetRepository.deleteById(id);
    }
}