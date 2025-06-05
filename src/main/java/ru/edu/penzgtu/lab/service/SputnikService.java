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
        planet.setNumberOfMoons(planet.getNumberOfMoons() + 1);
        planetRepository.save(planet);

        return sputnikMapper.toDto(savedSputnik);
    }

    @Transactional
    public SputnikDto updateSputnik(SputnikDto sputnikDto) {
        Long sputnikId = sputnikDto.getId();
        // ... (валидации sputnikDto.getId(), sputnikDto.getPlanetId() и т.д.) ...
        if (sputnikId == null) {
            throw new PenzGtuException(ErrorType.CLIENT_ERROR, "ID спутника должен быть предоставлен для обновления.");
        }
        if (sputnikDto.getPlanetId() == null) {
            throw new PenzGtuException(ErrorType.CLIENT_ERROR, "ID планеты должен быть предоставлен для обновления спутника.");
        }


        Sputnik existingSputnik = sputnikRepository.findById(sputnikId)
                .orElseThrow(() -> new PenzGtuException(ErrorType.NOT_FOUND, "Спутник с ID: " + sputnikId + " не найден для обновления."));

        Planet oldPlanet = existingSputnik.getPlanet(); // Запоминаем старую планету

        existingSputnik.setName(sputnikDto.getName());
        existingSputnik.setOrbitalPeriod(sputnikDto.getOrbitalPeriod());
        existingSputnik.setIsNatural(sputnikDto.getIsNatural());
        existingSputnik.setMeanRadiusKm(sputnikDto.getMeanRadiusKm());
        existingSputnik.setMassKg(sputnikDto.getMassKg());


        // Проверяем, изменилась ли планета-родитель
        if (!oldPlanet.getId().equals(sputnikDto.getPlanetId())) {
            Planet newPlanet = planetRepository.findById(sputnikDto.getPlanetId())
                    .orElseThrow(() -> new PenzGtuException(ErrorType.NOT_FOUND, "Новая планета с ID: " + sputnikDto.getPlanetId() + " не найдена."));
            existingSputnik.setPlanet(newPlanet);

            // Декремент у старой планеты
            oldPlanet.setNumberOfMoons(Math.max(0, oldPlanet.getNumberOfMoons() - 1));
            planetRepository.save(oldPlanet);

            // Инкремент у новой планеты
            newPlanet.setNumberOfMoons(newPlanet.getNumberOfMoons() + 1);
            planetRepository.save(newPlanet);
        }

        Sputnik updatedSputnik = sputnikRepository.save(existingSputnik);
        return sputnikMapper.toDto(updatedSputnik);
    }

    @Transactional
    public void deleteSputnikById(Long id) {
        Sputnik sputnikToDelete = sputnikRepository.findById(id)
                .orElseThrow(() -> new PenzGtuException(ErrorType.NOT_FOUND, "Спутник с ID: " + id + " не найден для удаления."));

        Planet planet = sputnikToDelete.getPlanet();
        sputnikRepository.delete(sputnikToDelete); // Удаляем сам спутник

        //Обновление счетчика спутников у планеты
        if (planet != null) {
            planet.setNumberOfMoons(Math.max(0, planet.getNumberOfMoons() - 1));
            planetRepository.save(planet); // Сохраняем обновленную планету
        }}
}