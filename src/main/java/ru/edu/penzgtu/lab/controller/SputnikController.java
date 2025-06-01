package ru.edu.penzgtu.lab.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.edu.penzgtu.lab.entity.Sputnik;
import ru.edu.penzgtu.lab.service.SputnikService;

import java.util.List;

@RestController
@RequestMapping("/api/sputniks") // Базовый путь для всех эндпоинтов спутников
@RequiredArgsConstructor
public class SputnikController {

    private final SputnikService sputnikService;

    // Получение списка всех спутников
    // GET http://localhost:8086/api/sputniks
    @GetMapping
    public List<Sputnik> findAllSputniks() {
        return sputnikService.findAllSputniks();
    }

    // Получение спутника по ID
    // GET http://localhost:8086/api/sputniks/{id}
    @GetMapping("/{id}")
    public Sputnik findSputnikById(@PathVariable Long id) {
        return sputnikService.findSputnikById(id);
    }

    // Создание нового спутника
    // POST http://localhost:8086/api/sputniks
    // В теле запроса (JSON): {"name": "Moon", "orbitalPeriod": 27.3, "isNatural": true, "planet": {"id": 1}}
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Sputnik createSputnik(@RequestBody Sputnik sputnik) {
        return sputnikService.saveSputnik(sputnik);
    }

    // Обновление существующего спутника
    // PUT http://localhost:8086/api/sputniks/{id}
    // В теле запроса (JSON): {"id": 1, "name": "Updated Moon", ..., "planet": {"id": 1}}
    @PutMapping("/{id}")
    public Sputnik updateSputnik(@PathVariable Long id, @RequestBody Sputnik sputnikDetails) {
        if (sputnikDetails.getId() == null) {
            sputnikDetails.setId(id);
        } else if (!sputnikDetails.getId().equals(id)) {
            throw new IllegalArgumentException("ID в пути (" + id + ") не совпадает с ID в теле запроса (" + sputnikDetails.getId() + ").");
        }
        return sputnikService.updateSputnik(sputnikDetails);
    }

    // Удаление спутника по ID
    // DELETE http://localhost:8086/api/sputniks/{id}
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSputnikById(@PathVariable Long id) {
        sputnikService.deleteSputnikById(id);
    }
}
