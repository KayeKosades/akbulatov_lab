package ru.edu.penzgtu.lab.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.edu.penzgtu.lab.entity.Planet;
import ru.edu.penzgtu.lab.repo.PlanetRepository;
import ru.edu.penzgtu.lab.repo.SputnikRepository;
import ru.edu.penzgtu.lab.entity.Sputnik;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class SputnikService {
    private final SputnikRepository sputnikRepository;
    private final PlanetRepository planetRepository;

    public List<Sputnik> findAllSputniks() {
        return sputnikRepository.findAll();
    }

    public Sputnik findSputnikById(Long id) {
        return sputnikRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Не найден спутник по id: " + id));
    }

    public Sputnik saveSputnik(Sputnik sputnik) {
        if (sputnik.getPlanet() == null || sputnik.getPlanet().getId() == null) {
            throw new IllegalArgumentException("Объект спутника должен содержать Planet ID");
        }

        Planet planet = planetRepository.findById(sputnik.getPlanet().getId())
                .orElseThrow(() -> new NoSuchElementException("Планета не найдена в таблице планет по id " + sputnik.getPlanet().getId() + ". Невозможно сохранить спутник"));

        sputnik.setPlanet(planet);
        return sputnikRepository.save(sputnik);
    }

    public Sputnik updateSputnik(Sputnik sputnikWithUpdates) {
        Long sputnikId = sputnikWithUpdates.getId();
        if (sputnikId == null) {
            throw new IllegalArgumentException("Не найден id спутника для обновления.");
        }

        Sputnik existingSputnik = sputnikRepository.findById(sputnikId)
                .orElseThrow(() -> new NoSuchElementException("Спутник для обновления не найден в таблице спутников по id " + sputnikId));

        existingSputnik.setName(sputnikWithUpdates.getName());
        existingSputnik.setOrbitalPeriod(sputnikWithUpdates.getOrbitalPeriod());
        existingSputnik.setIsNatural(sputnikWithUpdates.getIsNatural());

        // Обработка изменения связи с планетой
        if (sputnikWithUpdates.getPlanet() != null && sputnikWithUpdates.getPlanet().getId() != null) {
            Long newPlanetId = sputnikWithUpdates.getPlanet().getId();
            if (existingSputnik.getPlanet() == null || !existingSputnik.getPlanet().getId().equals(newPlanetId)) {
                Planet newPlanet = planetRepository.findById(newPlanetId)
                        .orElseThrow(() -> new NoSuchElementException("New Planet not found with id: " + newPlanetId));

                existingSputnik.setPlanet(newPlanet);
            }
        } else {
            throw new IllegalArgumentException("Спутник должен иметь связанную с ним планету. ID планеты не может быть null");
        }

        return sputnikRepository.save(existingSputnik);
    }

    public void deleteSputnikById(Long id) {
        Sputnik sputnikToDelete = sputnikRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Спутник для удаления не найден по id: " + id));

        sputnikRepository.deleteById(sputnikToDelete.getId());
    }

}
