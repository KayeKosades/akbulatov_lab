package ru.edu.penzgtu.lab.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.edu.penzgtu.lab.dto.PlanetDto;
import ru.edu.penzgtu.lab.entity.Planet;
import ru.edu.penzgtu.lab.entity.StarSystem;
import ru.edu.penzgtu.lab.exception.ErrorType;
import ru.edu.penzgtu.lab.exception.PenzGtuException;
import ru.edu.penzgtu.lab.repo.PlanetRepository;
import ru.edu.penzgtu.lab.repo.StarSystemRepository;
import ru.edu.penzgtu.lab.service.mapper.PlanetMapper;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlanetService {

    private final PlanetRepository planetRepository;
    private final PlanetMapper planetMapper;
    private final StarSystemRepository starSystemRepository;

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
    public void deletePlanetById(Long id) {
        if (!planetRepository.existsById(id)) {
            throw new PenzGtuException(ErrorType.NOT_FOUND, "Планета с ID: " + id + " не найдена для удаления.");
        }
        planetRepository.deleteById(id);
    }

    @Transactional
    public PlanetDto savePlanet(PlanetDto planetDto) {
        log.info("Creating new planet with name: {}", planetDto.getName());
        if (planetDto.getId() != null) {
            log.warn("Attempted to create a planet with an existing ID: {}", planetDto.getId());
            throw new PenzGtuException(ErrorType.CLIENT_ERROR, "ID должен быть null при создании новой планеты.");
        }
        // Проверка на уникальность имени
        planetRepository.findByName(planetDto.getName()).ifPresent(p -> {
            log.warn("Planet with name '{}' already exists.", planetDto.getName());
            throw new PenzGtuException(ErrorType.CLIENT_ERROR, "Планета с названием '" + planetDto.getName() + "' уже существует.");
        });


        Planet planetToSave = planetMapper.toEntity(planetDto);

        // Установка связи со звездной системой, если starSystemId предоставлен в DTO
        if (planetDto.getStarSystemId() != null) {
            StarSystem starSystem = starSystemRepository.findById(planetDto.getStarSystemId())
                    .orElseThrow(() -> {
                        log.warn("Star system not found with id: {} when creating planet '{}'.", planetDto.getStarSystemId(), planetDto.getName());
                        return new PenzGtuException(ErrorType.NOT_FOUND, "Звездная система с ID: " + planetDto.getStarSystemId() + " не найдена.");
                    });
            planetToSave.setStarSystemEntity(starSystem);
            // Если используете вспомогательные методы: starSystem.addPlanet(planetToSave);
        }

        Planet savedPlanet = planetRepository.save(planetToSave);
        log.info("Successfully created planet with id: {}", savedPlanet.getId());
        return planetMapper.toDto(savedPlanet);
    }

    @Transactional
    public PlanetDto updatePlanet(Long id, PlanetDto planetDto) {
        log.info("Updating planet with id: {}", id);
        if (!id.equals(planetDto.getId())) {
            log.warn("Mismatch in planet ID path variable ({}) and DTO ID ({}).", id, planetDto.getId());
            throw new PenzGtuException(ErrorType.CLIENT_ERROR, "ID в пути не совпадает с ID в теле запроса.");
        }

        Planet existingPlanet = planetRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Planet not found for update with id: {}", id);
                    return new PenzGtuException(ErrorType.NOT_FOUND, "Планета с ID: " + id + " не найдена для обновления.");
                });

        // Обновление основных полей
        if (planetDto.getName() != null && !planetDto.getName().equals(existingPlanet.getName())) {
            planetRepository.findByName(planetDto.getName()).ifPresent(p -> {
                if (!p.getId().equals(existingPlanet.getId())) {
                    log.warn("Attempt to update planet name to '{}', which already exists for id {}.", planetDto.getName(), p.getId());
                    throw new PenzGtuException(ErrorType.CLIENT_ERROR, "Планета с названием '" + planetDto.getName() + "' уже существует.");
                }
            });
            existingPlanet.setName(planetDto.getName());
        }
        existingPlanet.setType(planetDto.getType()); // Предполагаем, что эти поля могут быть null в DTO, если не меняются
        existingPlanet.setDiameter(planetDto.getDiameter());
        existingPlanet.setHasAtmosphere(planetDto.getHasAtmosphere());
        existingPlanet.setDiscoveryDate(planetDto.getDiscoveryDate());
        existingPlanet.setSurfaceGravity(planetDto.getSurfaceGravity());
        existingPlanet.setNumberOfMoons(planetDto.getNumberOfMoons());

        // Обновление связи со звездной системой
        if (planetDto.getStarSystemId() != null) {
            if (existingPlanet.getStarSystemEntity() == null || !existingPlanet.getStarSystemEntity().getId().equals(planetDto.getStarSystemId())) {
                StarSystem newStarSystem = starSystemRepository.findById(planetDto.getStarSystemId())
                        .orElseThrow(() -> {
                            log.warn("New star system not found with id: {} for planet update.", planetDto.getStarSystemId());
                            return new PenzGtuException(ErrorType.NOT_FOUND, "Новая звездная система с ID: " + planetDto.getStarSystemId() + " не найдена.");
                        });
                existingPlanet.setStarSystemEntity(newStarSystem);
            }
        } else {
            existingPlanet.setStarSystemEntity(null);
        }

        Planet updatedPlanet = planetRepository.save(existingPlanet);
        log.info("Successfully updated planet with id: {}", updatedPlanet.getId());
        return planetMapper.toDto(updatedPlanet);
    }

    @Transactional
    public PlanetDto assignPlanetToStarSystem(Long planetId, Long starSystemId) {
        log.info("Assigning planet id {} to star system id {}", planetId, starSystemId);
        Planet planet = planetRepository.findById(planetId)
                .orElseThrow(() -> new PenzGtuException(ErrorType.NOT_FOUND, "Планета с ID: " + planetId + " не найдена."));
        StarSystem starSystem = starSystemRepository.findById(starSystemId)
                .orElseThrow(() -> new PenzGtuException(ErrorType.NOT_FOUND, "Звездная система с ID: " + starSystemId + " не найдена."));

        planet.setStarSystemEntity(starSystem); // Прямая установка

        Planet updatedPlanet = planetRepository.save(planet);
        log.info("Planet id {} successfully assigned to star system id {}", planetId, starSystemId);
        return planetMapper.toDto(updatedPlanet);
    }

    @Transactional
    public PlanetDto removePlanetFromStarSystem(Long planetId) {
        log.info("Removing planet id {} from its star system", planetId);
        Planet planet = planetRepository.findById(planetId)
                .orElseThrow(() -> new PenzGtuException(ErrorType.NOT_FOUND, "Планета с ID: " + planetId + " не найдена."));

        if (planet.getStarSystemEntity() != null) {
            log.info("Planet id {} was in star system id {}. Removing.", planetId, planet.getStarSystemEntity().getId());
            // Если использовали вспомогательные методы:
            // planet.getStarSystemEntity().removePlanet(planet);
            planet.setStarSystemEntity(null); // Прямая установка
            Planet updatedPlanet = planetRepository.save(planet);
            return planetMapper.toDto(updatedPlanet);
        } else {
            log.info("Planet id {} is already not in any star system.", planetId);
            return planetMapper.toDto(planet); // Возвращаем как есть, изменений не было
        }
    }

}