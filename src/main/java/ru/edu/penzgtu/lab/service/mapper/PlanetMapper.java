package ru.edu.penzgtu.lab.service.mapper;

import org.springframework.stereotype.Service;
import ru.edu.penzgtu.lab.dto.PlanetDto;
import ru.edu.penzgtu.lab.entity.Planet;
import ru.edu.penzgtu.lab.entity.Sputnik;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PlanetMapper {

    public List<PlanetDto> toListDto(List<Planet> planets) {
        if (planets == null) {
            return Collections.emptyList();
        }
        return planets.stream().map(this::toDto).collect(Collectors.toList());
    }

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

        return PlanetDto.builder()
                .id(planet.getId())
                .name(planet.getName())
                .type(planet.getType())
                .diameter(planet.getDiameter())
                .hasAtmosphere(planet.getHasAtmosphere())
                .starSystem(planet.getStarSystem())
                .satelliteNames(satelliteNames) // Список имен спутников
                .build();
    }

    public Planet toEntity(PlanetDto planetDto) {
        if (planetDto == null) {
            return null;
        }
        Planet planet = new Planet();
        planet.setId(planetDto.getId()); // Будь осторожен с этим при создании новых сущностей
        planet.setName(planetDto.getName());
        planet.setType(planetDto.getType());
        planet.setDiameter(planetDto.getDiameter());
        planet.setHasAtmosphere(planetDto.getHasAtmosphere());
        planet.setStarSystem(planetDto.getStarSystem());
        return planet;
    }
}
