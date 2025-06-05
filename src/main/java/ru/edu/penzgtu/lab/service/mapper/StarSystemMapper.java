package ru.edu.penzgtu.lab.service.mapper;


import org.springframework.stereotype.Service;
import ru.edu.penzgtu.lab.dto.StarSystemDto;
import ru.edu.penzgtu.lab.entity.Planet;
import ru.edu.penzgtu.lab.entity.StarSystem;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StarSystemMapper {

    public StarSystemDto toDto(StarSystem starSystem) {
        if (starSystem == null) {
            return null;
        }

        List<Long> planetIds = Collections.emptyList();
        if (starSystem.getPlanets() != null && !starSystem.getPlanets().isEmpty()) {
            planetIds = starSystem.getPlanets().stream()
                    .map(Planet::getId)
                    .collect(Collectors.toList());
        }

        return StarSystemDto.builder()
                .id(starSystem.getId())
                .name(starSystem.getName())
                .galaxyName(starSystem.getGalaxyName())
                .discoveryDate(starSystem.getDiscoveryDate())
                .planetIds(planetIds)
                .build();
    }

    public List<StarSystemDto> toListDto(List<StarSystem> starSystems) {
        if (starSystems == null) {
            return Collections.emptyList();
        }
        return starSystems.stream().map(this::toDto).collect(Collectors.toList());
    }

    public StarSystem toEntity(StarSystemDto dto) {
        if (dto == null) {
            return null;
        }
        StarSystem starSystem = new StarSystem();
        starSystem.setId(dto.getId()); // Осторожно при создании новых сущностей
        starSystem.setName(dto.getName());
        starSystem.setGalaxyName(dto.getGalaxyName());
        starSystem.setDiscoveryDate(dto.getDiscoveryDate());
        return starSystem;
    }
}
