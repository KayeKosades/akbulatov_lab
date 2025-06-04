package ru.edu.penzgtu.lab.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.edu.penzgtu.lab.dto.PlanetDto;
import ru.edu.penzgtu.lab.entity.Planet;
import ru.edu.penzgtu.lab.exception.ErrorType;
import ru.edu.penzgtu.lab.exception.PenzGtuException;
import ru.edu.penzgtu.lab.repo.PlanetRepository;
import ru.edu.penzgtu.lab.service.mapper.PlanetMapper;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlanetService {

    private final PlanetRepository planetRepository;
    private final PlanetMapper planetMapper;

    @Transactional(readOnly = true)
    public List<PlanetDto> findAllPlanets() {
        List<Planet> planets = planetRepository.findAll();
        return planetMapper.toListDto(planets);
    }

    @Transactional(readOnly = true)
    public PlanetDto findPlanetById(Long id) {
        Planet planet = planetRepository.findById(id)
                .orElseThrow(() -> new PenzGtuException(ErrorType.NOT_FOUND, "Планета с ID: " + id + " не найдена."));
        return planetMapper.toDto(planet);
    }

    @Transactional
    public PlanetDto savePlanet(PlanetDto planetDto) {
        if (planetDto.getName() == null || planetDto.getName().isBlank()) {
            throw new PenzGtuException(ErrorType.CLIENT_ERROR, "Название планеты не может быть пустым.");
        }

        Planet planetToSave = planetMapper.toEntity(planetDto);

        if (planetDto.getId() != null && planetRepository.existsById(planetDto.getId())) {
            throw new PenzGtuException(ErrorType.CLIENT_ERROR, "Планета с ID " + planetDto.getId() + " уже существует. Используйте метод обновления.");
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
            throw new PenzGtuException(ErrorType.CLIENT_ERROR, "ID планеты должен быть предоставлен для обновления.");
        }

        Planet existingPlanet = planetRepository.findById(planetId)
                .orElseThrow(() -> new PenzGtuException(ErrorType.NOT_FOUND, "Планета с ID: " + planetId + " не найдена для обновления."));

        if (planetDto.getName() == null || planetDto.getName().isBlank()) {
            throw new PenzGtuException(ErrorType.CLIENT_ERROR, "Название планеты не может быть пустым при обновлении.");
        }

        existingPlanet.setName(planetDto.getName());
        existingPlanet.setType(planetDto.getType());
        existingPlanet.setDiameter(planetDto.getDiameter());
        existingPlanet.setHasAtmosphere(planetDto.getHasAtmosphere());
        existingPlanet.setStarSystem(planetDto.getStarSystem());
        existingPlanet.setDiscoveryDate(planetDto.getDiscoveryDate());
        existingPlanet.setSurfaceGravity(planetDto.getSurfaceGravity());
        existingPlanet.setNumberOfMoonsConfirmed(planetDto.getNumberOfMoonsConfirmed());

        Planet updatedPlanet = planetRepository.save(existingPlanet);
        return planetMapper.toDto(updatedPlanet);
    }

    @Transactional
    public void deletePlanetById(Long id) {
        if (!planetRepository.existsById(id)) {
            throw new PenzGtuException(ErrorType.NOT_FOUND, "Планета с ID: " + id + " не найдена для удаления.");
        }
        planetRepository.deleteById(id);
    }
}