package ru.edu.penzgtu.lab.service.mapper;

import org.springframework.stereotype.Service;
import ru.edu.penzgtu.lab.dto.ExplorationMissionDto;
import ru.edu.penzgtu.lab.entity.ExplorationMission;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExplorationMissionMapper {

    public ExplorationMissionDto toDto(ExplorationMission mission) {
        if (mission == null) {
            return null;
        }
        Long targetPlanetId = null;
        if (mission.getTargetPlanet() != null) {
            targetPlanetId = mission.getTargetPlanet().getId();
        }

        return ExplorationMissionDto.builder()
                .id(mission.getId())
                .missionName(mission.getMissionName())
                .launchDate(mission.getLaunchDate())
                .completionDate(mission.getCompletionDate())
                .objective(mission.getObjective())
                .status(mission.getStatus()) // Enum просто присваивается
                .targetPlanetId(targetPlanetId)
                .build();
    }

    public List<ExplorationMissionDto> toListDto(List<ExplorationMission> missions) {
        if (missions == null) {
            return Collections.emptyList();
        }
        return missions.stream().map(this::toDto).collect(Collectors.toList());
    }

    public ExplorationMission toEntity(ExplorationMissionDto dto) {
        if (dto == null) {
            return null;
        }
        ExplorationMission mission = new ExplorationMission();
        mission.setId(dto.getId());
        mission.setMissionName(dto.getMissionName());
        mission.setLaunchDate(dto.getLaunchDate());
        mission.setCompletionDate(dto.getCompletionDate());
        mission.setObjective(dto.getObjective());
        mission.setStatus(dto.getStatus());
        return mission;
    }
}
