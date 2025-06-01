package ru.edu.penzgtu.lab.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.edu.penzgtu.lab.entity.Planet;
import ru.edu.penzgtu.lab.service.PlanetService;

import java.util.List;

@RestController
@RequestMapping("/api/planets") // Базовый путь для всех эндпоинтов планет
@RequiredArgsConstructor
public class PlanetController {

    private final PlanetService planetService;

    // Получение списка всех планет
    // GET http://localhost:8086/api/planets
    @GetMapping
    public List<Planet> findAllPlanets() {
        return planetService.findAllPlanets();
    }

    // Получение планеты по ID
    // GET http://localhost:8086/api/planets/{id}
    @GetMapping("/{id}")
    public Planet findPlanetById(@PathVariable Long id) {
        return planetService.findPlanetById(id);
    }

    // Создание новой планеты
    // POST http://localhost:8086/api/planets
    // В теле запроса (JSON): {"name": "Mars", "type": "Terrestrial", ...}
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED) // Возвращаем статус 201 Created
    public Planet createPlanet(@RequestBody Planet planet) {
        return planetService.savePlanet(planet);
    }

    // Обновление существующей планеты
    // PUT http://localhost:8086/api/planets/{id}
    // В теле запроса (JSON): {"id": 1, "name": "Updated Mars", "type": "Rocky Planet", ...}
    @PutMapping("/{id}")
    public Planet updatePlanet(@PathVariable Long id, @RequestBody Planet planetDetails) {
        if (planetDetails.getId() == null) {
            planetDetails.setId(id);
        } else if (!planetDetails.getId().equals(id)) {
            throw new IllegalArgumentException("ID в пути (" + id + ") не совпадает с ID в теле запроса (" + planetDetails.getId() + ").");
        }
        return planetService.updatePlanet(planetDetails);
    }

    // Удаление планеты по ID
    // DELETE http://localhost:8086/api/planets/{id}
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT) // Возвращаем статус 204 No Content
    public void deletePlanetById(@PathVariable Long id) {
        planetService.deletePlanetById(id);
    }

}
