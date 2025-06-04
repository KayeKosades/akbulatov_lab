package ru.edu.penzgtu.lab.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.edu.penzgtu.lab.dto.SputnikDto;
import ru.edu.penzgtu.lab.entity.Planet;
import ru.edu.penzgtu.lab.exception.ErrorType;
import ru.edu.penzgtu.lab.exception.PenzGtuException;
import ru.edu.penzgtu.lab.repo.PlanetRepository;
import ru.edu.penzgtu.lab.repo.SputnikRepository;
import ru.edu.penzgtu.lab.entity.Sputnik;
import ru.edu.penzgtu.lab.service.mapper.SputnikMapper;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SputnikService {

    private final SputnikRepository sputnikRepository;
    private final PlanetRepository planetRepository;
    private final SputnikMapper sputnikMapper;

    @Transactional(readOnly = true)
    public List<SputnikDto> findAllSputniks() {
        List<Sputnik> sputniks = sputnikRepository.findAll();
        return sputnikMapper.toListDto(sputniks);
    }

    @Transactional(readOnly = true)
    public SputnikDto findSputnikById(Long id) {
        Sputnik sputnik = sputnikRepository.findById(id)
                .orElseThrow(() -> new PenzGtuException(ErrorType.NOT_FOUND, "Спутник с ID: " + id + " не найден."));
        return sputnikMapper.toDto(sputnik);
    }

    @Transactional(readOnly = true)
    public List<SputnikDto> findSputniksByPlanetId(Long planetId) {
        if (!planetRepository.existsById(planetId)) {
            throw new PenzGtuException(ErrorType.NOT_FOUND, "Планета с ID: " + planetId + " не найдена при поиске ее спутников.");
        }
        List<Sputnik> sputniks = sputnikRepository.findByPlanetId(planetId);
        return sputnikMapper.toListDto(sputniks);
    }

    @Transactional
    public SputnikDto saveSputnik(SputnikDto sputnikDto) {
        if (sputnikDto.getPlanetId() == null) {
            throw new PenzGtuException(ErrorType.CLIENT_ERROR, "ID планеты должен быть предоставлен для сохранения спутника.");
        }
        if (sputnikDto.getName() == null || sputnikDto.getName().isBlank()) {
            throw new PenzGtuException(ErrorType.CLIENT_ERROR, "Название спутника не может быть пустым.");
        }

        Planet planet = planetRepository.findById(sputnikDto.getPlanetId())
                .orElseThrow(() -> new PenzGtuException(ErrorType.NOT_FOUND, "Планета с ID: " + sputnikDto.getPlanetId() + " не найдена. Невозможно сохранить спутник."));

        Sputnik sputnikToSave = sputnikMapper.toEntity(sputnikDto);
        sputnikToSave.setPlanet(planet);

        if (sputnikDto.getId() != null && sputnikRepository.existsById(sputnikDto.getId())) {
            throw new PenzGtuException(ErrorType.CLIENT_ERROR, "Спутник с ID " + sputnikDto.getId() + " уже существует. Используйте метод обновления.");
        }
        if (sputnikDto.getId() == null) {
            sputnikToSave.setId(null);
        }

        Sputnik savedSputnik = sputnikRepository.save(sputnikToSave);

        //обновление количества спутников у связанной планеты
        planet.setNumberOfMoonsConfirmed(planet.getNumberOfMoonsConfirmed() + 1);
        planetRepository.save(planet);

        return sputnikMapper.toDto(savedSputnik);
    }

    @Transactional
    public SputnikDto updateSputnik(SputnikDto sputnikDto) {
        Long sputnikId = sputnikDto.getId();
        if (sputnikId == null) {
            throw new PenzGtuException(ErrorType.CLIENT_ERROR, "ID спутника должен быть предоставлен для обновления.");
        }
        if (sputnikDto.getPlanetId() == null) {
            throw new PenzGtuException(ErrorType.CLIENT_ERROR, "ID планеты должен быть предоставлен для обновления спутника.");
        }
        if (sputnikDto.getName() == null || sputnikDto.getName().isBlank()) {
            throw new PenzGtuException(ErrorType.CLIENT_ERROR, "Название спутника не может быть пустым при обновлении.");
        }

        Sputnik existingSputnik = sputnikRepository.findById(sputnikId)
                .orElseThrow(() -> new PenzGtuException(ErrorType.NOT_FOUND, "Спутник с ID: " + sputnikId + " не найден для обновления."));

        existingSputnik.setName(sputnikDto.getName());
        existingSputnik.setOrbitalPeriod(sputnikDto.getOrbitalPeriod());
        existingSputnik.setIsNatural(sputnikDto.getIsNatural());
        existingSputnik.setMeanRadiusKm(sputnikDto.getMeanRadiusKm());
        existingSputnik.setMassKg(sputnikDto.getMassKg());

        if (!existingSputnik.getPlanet().getId().equals(sputnikDto.getPlanetId())) {
            Planet newPlanet = planetRepository.findById(sputnikDto.getPlanetId())
                    .orElseThrow(() -> new PenzGtuException(ErrorType.NOT_FOUND, "Новая планета с ID: " + sputnikDto.getPlanetId() + " не найдена."));
            existingSputnik.setPlanet(newPlanet);
        }

        Sputnik updatedSputnik = sputnikRepository.save(existingSputnik);
        return sputnikMapper.toDto(updatedSputnik);
    }

    @Transactional
    public void deleteSputnikById(Long id) {
        if (!sputnikRepository.existsById(id)) {
            throw new PenzGtuException(ErrorType.NOT_FOUND, "Спутник с ID: " + id + " не найден для удаления.");
        }
        sputnikRepository.deleteById(id);
    }
}