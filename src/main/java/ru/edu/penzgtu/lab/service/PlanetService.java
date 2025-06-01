package ru.edu.penzgtu.lab.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.edu.penzgtu.lab.entity.Planet;
import ru.edu.penzgtu.lab.repo.PlanetRepository;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class PlanetService {
    private final PlanetRepository planetRepository;

    public List<Planet> findAllPlanets() {
        return planetRepository.findAll();
    }

    public Planet findPlanetById(Long id) {
        return planetRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Не найдена планета по id: " + id));
    }

    public Planet savePlanet(Planet planet) {
        return planetRepository.save(planet);
    }

    public Planet updatePlanet(Planet planetWithUpdates) {
        Long planetId = planetWithUpdates.getId();

        if (planetId == null) {
            throw new IllegalArgumentException("Не задан id планеты для обновления.");
        }

        Planet existingPlanet = planetRepository.findById(planetId)
                .orElseThrow(() -> new NoSuchElementException("Не найдена планета для обновления по id: " + planetId));

        existingPlanet.setName(planetWithUpdates.getName());
        existingPlanet.setType(planetWithUpdates.getType());
        existingPlanet.setDiameter(planetWithUpdates.getDiameter());
        existingPlanet.setHasAtmosphere(planetWithUpdates.getHasAtmosphere());
        existingPlanet.setStarSystem(planetWithUpdates.getStarSystem());

        return planetRepository.save(existingPlanet);
    }

    public void deletePlanetById(Long id) {
        if (!planetRepository.existsById(id)) {
            throw new NoSuchElementException("Не найдена планета для удаления по id: " + id);
        }
        planetRepository.deleteById(id);
    }
}