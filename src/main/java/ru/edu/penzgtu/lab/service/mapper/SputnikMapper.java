package ru.edu.penzgtu.lab.service.mapper;

import org.springframework.stereotype.Service;
import ru.edu.penzgtu.lab.dto.SputnikDto;
import ru.edu.penzgtu.lab.entity.Sputnik;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SputnikMapper {

    public List<SputnikDto> toListDto(List<Sputnik> sputniks) {
        if (sputniks == null) {
            return Collections.emptyList();
        }
        return sputniks.stream().map(this::toDto).collect(Collectors.toList());
    }

    public SputnikDto toDto(Sputnik sputnik) {
        if (sputnik == null) {
            return null;
        }
        Long planetId = null;
        if (sputnik.getPlanet() != null) {
            planetId = sputnik.getPlanet().getId();
        }

        return SputnikDto.builder()
                .id(sputnik.getId())
                .name(sputnik.getName())
                .orbitalPeriod(sputnik.getOrbitalPeriod())
                .isNatural(sputnik.getIsNatural())
                .planetId(planetId)
                .build();
    }

    public Sputnik toEntity(SputnikDto sputnikDto) {
        if (sputnikDto == null) {
            return null;
        }
        Sputnik sputnik = new Sputnik();
        sputnik.setId(sputnikDto.getId()); // Осторожно при создании новых
        sputnik.setName(sputnikDto.getName());
        sputnik.setOrbitalPeriod(sputnikDto.getOrbitalPeriod());
        sputnik.setIsNatural(sputnikDto.getIsNatural());
        return sputnik;
    }
}
