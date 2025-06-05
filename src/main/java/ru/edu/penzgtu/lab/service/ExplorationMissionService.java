package ru.edu.penzgtu.lab.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.edu.penzgtu.lab.dto.ExplorationMissionDto;
import ru.edu.penzgtu.lab.entity.ExplorationMission;
import ru.edu.penzgtu.lab.entity.Planet;
import ru.edu.penzgtu.lab.exception.ErrorType;
import ru.edu.penzgtu.lab.exception.PenzGtuException;
import ru.edu.penzgtu.lab.repo.ExplorationMissionRepository;
import ru.edu.penzgtu.lab.repo.PlanetRepository;
import ru.edu.penzgtu.lab.service.mapper.ExplorationMissionMapper;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExplorationMissionService {

    private final ExplorationMissionRepository missionRepository;
    private final ExplorationMissionMapper missionMapper;
    private final PlanetRepository planetRepository; // Для привязки миссии к планете

    @Transactional(readOnly = true)
    public List<ExplorationMissionDto> findAllMissions() {
        log.info("Fetching all exploration missions");
        return missionMapper.toListDto(missionRepository.findAll());
    }

    @Transactional(readOnly = true)
    public ExplorationMissionDto findMissionById(Long id) {
        log.info("Fetching exploration mission with id: {}", id);
        ExplorationMission mission = missionRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Exploration mission not found with id: {}", id);
                    return new PenzGtuException(ErrorType.NOT_FOUND, "Миссия с ID: " + id + " не найдена.");
                });
        return missionMapper.toDto(mission);
    }

    @Transactional(readOnly = true)
    public List<ExplorationMissionDto> findMissionsByPlanetId(Long planetId) {
        log.info("Fetching missions for planet id: {}", planetId);
        if (!planetRepository.existsById(planetId)) {
            log.warn("Planet not found with id {} when fetching missions.", planetId);
            throw new PenzGtuException(ErrorType.NOT_FOUND, "Планета с ID: " + planetId + " не найдена.");
        }
        return missionMapper.toListDto(missionRepository.findByTargetPlanetId(planetId));
    }

    @Transactional
    public ExplorationMissionDto createMission(ExplorationMissionDto missionDto) {
        log.info("Creating new exploration mission with name: {}", missionDto.getMissionName());
        if (missionDto.getId() != null) {
            log.warn("Attempted to create a mission with an existing ID: {}", missionDto.getId());
            throw new PenzGtuException(ErrorType.CLIENT_ERROR, "ID должен быть null при создании новой миссии.");
        }
        if (missionDto.getTargetPlanetId() == null) {
            log.warn("Target planet ID is null for new mission '{}'.", missionDto.getMissionName());
            throw new PenzGtuException(ErrorType.CLIENT_ERROR, "ID целевой планеты должен быть указан для миссии.");
        }

        Planet targetPlanet = planetRepository.findById(missionDto.getTargetPlanetId())
                .orElseThrow(() -> {
                    log.warn("Target planet not found with id: {} for mission '{}'.", missionDto.getTargetPlanetId(), missionDto.getMissionName());
                    return new PenzGtuException(ErrorType.NOT_FOUND, "Целевая планета с ID: " + missionDto.getTargetPlanetId() + " не найдена.");
                });

        missionRepository.findByMissionName(missionDto.getMissionName()).ifPresent(m -> {
            log.warn("Mission with name '{}' already exists.", missionDto.getMissionName());
            throw new PenzGtuException(ErrorType.CLIENT_ERROR, "Миссия с названием '" + missionDto.getMissionName() + "' уже существует.");
        });

        ExplorationMission missionToSave = missionMapper.toEntity(missionDto);
        missionToSave.setTargetPlanet(targetPlanet);

        ExplorationMission savedMission = missionRepository.save(missionToSave);
        log.info("Successfully created mission with id: {}", savedMission.getId());
        return missionMapper.toDto(savedMission);
    }

    @Transactional
    public ExplorationMissionDto updateMission(Long id, ExplorationMissionDto missionDto) {
        log.info("Updating exploration mission with id: {}", id);
        if (!id.equals(missionDto.getId())) {
            log.warn("Mismatch in mission ID path variable ({}) and DTO ID ({}).", id, missionDto.getId());
            throw new PenzGtuException(ErrorType.CLIENT_ERROR, "ID в пути не совпадает с ID в теле запроса.");
        }

        ExplorationMission existingMission = missionRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Mission not found for update with id: {}", id);
                    return new PenzGtuException(ErrorType.NOT_FOUND, "Миссия с ID: " + id + " не найдена для обновления.");
                });

        if (missionDto.getMissionName() != null && !missionDto.getMissionName().equals(existingMission.getMissionName())) {
            missionRepository.findByMissionName(missionDto.getMissionName()).ifPresent(m -> {
                if (!m.getId().equals(existingMission.getId())) {
                    log.warn("Attempt to update mission name to '{}', which already exists for id {}.", missionDto.getMissionName(), m.getId());
                    throw new PenzGtuException(ErrorType.CLIENT_ERROR, "Миссия с названием '" + missionDto.getMissionName() + "' уже существует.");
                }
            });
            existingMission.setMissionName(missionDto.getMissionName());
        }

        existingMission.setLaunchDate(missionDto.getLaunchDate());
        existingMission.setCompletionDate(missionDto.getCompletionDate());
        existingMission.setObjective(missionDto.getObjective());
        existingMission.setStatus(missionDto.getStatus());

        if (missionDto.getTargetPlanetId() != null &&
                (existingMission.getTargetPlanet() == null || !existingMission.getTargetPlanet().getId().equals(missionDto.getTargetPlanetId()))) {
            Planet newTargetPlanet = planetRepository.findById(missionDto.getTargetPlanetId())
                    .orElseThrow(() -> {
                        log.warn("New target planet not found with id: {} for mission update.", missionDto.getTargetPlanetId());
                        return new PenzGtuException(ErrorType.NOT_FOUND, "Новая целевая планета с ID: " + missionDto.getTargetPlanetId() + " не найдена.");
                    });
            existingMission.setTargetPlanet(newTargetPlanet);
        } else if (missionDto.getTargetPlanetId() == null) {
            log.warn("Cannot update mission {} to have no target planet.", id);
            throw new PenzGtuException(ErrorType.CLIENT_ERROR, "Миссия должна иметь целевую планету.");
        }


        ExplorationMission updatedMission = missionRepository.save(existingMission);
        log.info("Successfully updated mission with id: {}", updatedMission.getId());
        return missionMapper.toDto(updatedMission);
    }

    @Transactional
    public void deleteMission(Long id) {
        log.info("Deleting exploration mission with id: {}", id);
        if (!missionRepository.existsById(id)) {
            log.warn("Mission not found for deletion with id: {}", id);
            throw new PenzGtuException(ErrorType.NOT_FOUND, "Миссия с ID: " + id + " не найдена для удаления.");
        }
        missionRepository.deleteById(id);
        log.info("Successfully deleted mission with id: {}", id);
    }
}
