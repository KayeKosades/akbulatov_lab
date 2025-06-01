package ru.edu.penzgtu.lab.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.edu.penzgtu.lab.dto.SputnikDto;
import ru.edu.penzgtu.lab.entity.Planet;
import ru.edu.penzgtu.lab.repo.PlanetRepository;
import ru.edu.penzgtu.lab.repo.SputnikRepository;
import ru.edu.penzgtu.lab.entity.Sputnik;
import ru.edu.penzgtu.lab.service.mapper.SputnikMapper;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class SputnikService {

    private final SputnikRepository sputnikRepository;
    private final PlanetRepository planetRepository; // Нужен для привязки спутника к планете
    private final SputnikMapper sputnikMapper;   // Инъекция маппера

    @Transactional(readOnly = true)
    public List<SputnikDto> findAllSputniks() {
        List<Sputnik> sputniks = sputnikRepository.findAll();
        return sputnikMapper.toListDto(sputniks);
    }

    @Transactional(readOnly = true)
    public SputnikDto findSputnikById(Long id) {
        Sputnik sputnik = sputnikRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Sputnik not found with id: " + id));
        return sputnikMapper.toDto(sputnik);
    }

    @Transactional(readOnly = true)
    public List<SputnikDto> findSputniksByPlanetId(Long planetId) {
        if (!planetRepository.existsById(planetId)) {
            // Можно просто вернуть пустой список или кинуть исключение, если планета не найдена
            throw new NoSuchElementException("Planet not found with id: " + planetId + " when searching for its sputniks.");
        }
        List<Sputnik> sputniks = sputnikRepository.findByPlanetId(planetId);
        return sputnikMapper.toListDto(sputniks);
    }

    @Transactional
    public SputnikDto saveSputnik(SputnikDto sputnikDto) {
        if (sputnikDto.getPlanetId() == null) {
            throw new IllegalArgumentException("Planet ID must be provided in SputnikDto to save it.");
        }
        // Загружаем родительскую сущность Planet
        Planet planet = planetRepository.findById(sputnikDto.getPlanetId())
                .orElseThrow(() -> new NoSuchElementException("Planet not found with id: " + sputnikDto.getPlanetId() + ". Cannot save Sputnik."));

        Sputnik sputnikToSave = sputnikMapper.toEntity(sputnikDto);
        sputnikToSave.setPlanet(planet); // Устанавливаем связь с существующей планетой

        if (sputnikDto.getId() != null && sputnikRepository.existsById(sputnikDto.getId())) {
            throw new IllegalArgumentException("Sputnik with ID " + sputnikDto.getId() + " already exists. Use update method.");
        }
        if (sputnikDto.getId() == null) { // Если это создание нового спутника
            sputnikToSave.setId(null); // Убедимся, что ID генерируется базой
        }

        Sputnik savedSputnik = sputnikRepository.save(sputnikToSave);
        return sputnikMapper.toDto(savedSputnik);
    }

    @Transactional
    public SputnikDto updateSputnik(SputnikDto sputnikDto) {
        Long sputnikId = sputnikDto.getId();
        if (sputnikId == null) {
            throw new IllegalArgumentException("Sputnik ID must be provided for update.");
        }
        if (sputnikDto.getPlanetId() == null) {
            throw new IllegalArgumentException("Planet ID must be provided in SputnikDto for update.");
        }

        Sputnik existingSputnik = sputnikRepository.findById(sputnikId)
                .orElseThrow(() -> new NoSuchElementException("Sputnik not found with id: " + sputnikId + " for update."));

        // Обновляем поля
        existingSputnik.setName(sputnikDto.getName());
        existingSputnik.setOrbitalPeriod(sputnikDto.getOrbitalPeriod());
        existingSputnik.setIsNatural(sputnikDto.getIsNatural());

        // Проверяем, изменилась ли планета
        if (!existingSputnik.getPlanet().getId().equals(sputnikDto.getPlanetId())) {
            Planet newPlanet = planetRepository.findById(sputnikDto.getPlanetId())
                    .orElseThrow(() -> new NoSuchElementException("New Planet not found with id: " + sputnikDto.getPlanetId()));
            existingSputnik.setPlanet(newPlanet);
        }

        Sputnik updatedSputnik = sputnikRepository.save(existingSputnik);
        return sputnikMapper.toDto(updatedSputnik);
    }

    @Transactional
    public void deleteSputnikById(Long id) {
        if (!sputnikRepository.existsById(id)) {
            throw new NoSuchElementException("Sputnik not found with id: " + id + " for deletion.");
        }
        sputnikRepository.deleteById(id);
    }
}