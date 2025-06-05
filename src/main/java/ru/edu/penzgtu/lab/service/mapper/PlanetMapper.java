package ru.edu.penzgtu.lab.service.mapper;

import org.springframework.stereotype.Service;
import ru.edu.penzgtu.lab.dto.PlanetDto;
import ru.edu.penzgtu.lab.entity.ExplorationMission;
import ru.edu.penzgtu.lab.entity.Planet;
import ru.edu.penzgtu.lab.entity.Sputnik;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PlanetMapper {

    public PlanetDto toDto(Planet planet) {
        if (planet == null) {
            return null;
        }

        List<String> satelliteNames = Collections.emptyList();
        if (planet.getSatellites() != null && !planet.getSatellites().isEmpty()) {
            satelliteNames = planet.getSatellites().stream()
                    .map(Sputnik::getName)
                    .collect(Collectors.toList());
        }

        Long starSystemId = null;
        if (planet.getStarSystemEntity() != null) {
            starSystemId = planet.getStarSystemEntity().getId();
        }

        List<Long> missionIds = Collections.emptyList();
        if (planet.getExplorationMissions() != null && !planet.getExplorationMissions().isEmpty()) {
            missionIds = planet.getExplorationMissions().stream()
                    .map(ExplorationMission::getId)
                    .collect(Collectors.toList());
        }

        return PlanetDto.builder()
                .id(planet.getId())
                .name(planet.getName())
                .type(planet.getType())
                .diameter(planet.getDiameter())
                .hasAtmosphere(planet.getHasAtmosphere())
                .starSystemId(starSystemId) // <--- Обновлено
                .discoveryDate(planet.getDiscoveryDate())
                .surfaceGravity(planet.getSurfaceGravity())
                .numberOfMoons(planet.getNumberOfMoons())
                .satelliteNames(satelliteNames)
                .missionIds(missionIds) // <--- Добавлено
                .build();
    }

    public List<PlanetDto> toListDto(List<Planet> planets) {
        if (planets == null) {
            return Collections.emptyList();
        }
        return planets.stream().map(this::toDto).collect(Collectors.toList());
    }

    public Planet toEntity(PlanetDto dto) {
        if (dto == null) {
            return null;
        }
        Planet planet = new Planet();
        planet.setId(dto.getId());
        planet.setName(dto.getName());
        planet.setType(dto.getType());
        planet.setDiameter(dto.getDiameter());
        planet.setHasAtmosphere(dto.getHasAtmosphere());
        planet.setDiscoveryDate(dto.getDiscoveryDate());
        planet.setSurfaceGravity(dto.getSurfaceGravity());
        planet.setNumberOfMoons(dto.getNumberOfMoons());
        return planet;
    }
}