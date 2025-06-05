package ru.edu.penzgtu.lab.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.edu.penzgtu.lab.dto.StarSystemDto;
import ru.edu.penzgtu.lab.entity.StarSystem;
import ru.edu.penzgtu.lab.exception.ErrorType;
import ru.edu.penzgtu.lab.exception.PenzGtuException;
import ru.edu.penzgtu.lab.repo.StarSystemRepository;
import ru.edu.penzgtu.lab.service.mapper.StarSystemMapper;

import java.util.List;

@Slf4j // Аннотация для SLF4J логгера
@Service
@RequiredArgsConstructor
public class StarSystemService {

    private final StarSystemRepository starSystemRepository;
    private final StarSystemMapper starSystemMapper;

    @Transactional(readOnly = true)
    public List<StarSystemDto> findAllStarSystems() {
        log.info("Fetching all star systems");
        List<StarSystem> starSystems = starSystemRepository.findAll();
        return starSystemMapper.toListDto(starSystems);
    }

    @Transactional(readOnly = true)
    public StarSystemDto findStarSystemById(Long id) {
        log.info("Fetching star system with id: {}", id);
        StarSystem starSystem = starSystemRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Star system not found with id: {}", id);
                    return new PenzGtuException(ErrorType.NOT_FOUND, "Звездная система с ID: " + id + " не найдена.");
                });
        return starSystemMapper.toDto(starSystem);
    }

    @Transactional
    public StarSystemDto createStarSystem(StarSystemDto starSystemDto) {
        log.info("Creating new star system with name: {}", starSystemDto.getName());
        if (starSystemDto.getId() != null) {
            log.warn("Attempted to create a star system with an existing ID: {}", starSystemDto.getId());
            throw new PenzGtuException(ErrorType.CLIENT_ERROR, "ID должен быть null при создании новой звездной системы.");
        }
        starSystemRepository.findByName(starSystemDto.getName()).ifPresent(ss -> {
            log.warn("Star system with name '{}' already exists.", starSystemDto.getName());
            throw new PenzGtuException(ErrorType.CLIENT_ERROR, "Звездная система с названием '" + starSystemDto.getName() + "' уже существует.");
        });

        StarSystem starSystemToSave = starSystemMapper.toEntity(starSystemDto);
        StarSystem savedStarSystem = starSystemRepository.save(starSystemToSave);
        log.info("Successfully created star system with id: {}", savedStarSystem.getId());
        return starSystemMapper.toDto(savedStarSystem);
    }

    @Transactional
    public StarSystemDto updateStarSystem(Long id, StarSystemDto starSystemDto) {
        log.info("Updating star system with id: {}", id);
        if (!id.equals(starSystemDto.getId())) {
            log.warn("Mismatch in star system ID path variable ({}) and DTO ID ({}).", id, starSystemDto.getId());
            throw new PenzGtuException(ErrorType.CLIENT_ERROR, "ID в пути не совпадает с ID в теле запроса.");
        }

        StarSystem existingStarSystem = starSystemRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Star system not found for update with id: {}", id);
                    return new PenzGtuException(ErrorType.NOT_FOUND, "Звездная система с ID: " + id + " не найдена для обновления.");
                });

        // Проверка на уникальность имени, если имя меняется
        if (starSystemDto.getName() != null && !starSystemDto.getName().equals(existingStarSystem.getName())) {
            starSystemRepository.findByName(starSystemDto.getName()).ifPresent(ss -> {
                if (!ss.getId().equals(existingStarSystem.getId())) { // Убедимся, что это не та же самая система
                    log.warn("Attempt to update star system name to '{}', which already exists for id {}.", starSystemDto.getName(), ss.getId());
                    throw new PenzGtuException(ErrorType.CLIENT_ERROR, "Звездная система с названием '" + starSystemDto.getName() + "' уже существует.");
                }
            });
            existingStarSystem.setName(starSystemDto.getName());
        }

        existingStarSystem.setGalaxyName(starSystemDto.getGalaxyName());
        existingStarSystem.setDiscoveryDate(starSystemDto.getDiscoveryDate());

        StarSystem updatedStarSystem = starSystemRepository.save(existingStarSystem);
        log.info("Successfully updated star system with id: {}", updatedStarSystem.getId());
        return starSystemMapper.toDto(updatedStarSystem);
    }

    @Transactional
    public void deleteStarSystem(Long id) {
        log.info("Deleting star system with id: {}", id);
        StarSystem starSystem = starSystemRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Star system not found for deletion with id: {}", id);
                    return new PenzGtuException(ErrorType.NOT_FOUND, "Звездная система с ID: " + id + " не найдена для удаления.");
                });
        starSystemRepository.delete(starSystem);
        log.info("Successfully deleted star system with id: {}", id);
    }

}
