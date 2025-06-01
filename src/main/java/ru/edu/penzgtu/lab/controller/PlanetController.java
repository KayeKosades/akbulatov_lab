package ru.edu.penzgtu.lab.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.edu.penzgtu.lab.dto.PlanetDto;
import ru.edu.penzgtu.lab.service.PlanetService;

import java.util.List;

@Validated
@RestController
@RequestMapping("/api/planets")
@RequiredArgsConstructor
@Tag(name = "Планеты", description = "Операции над планетами")
public class PlanetController {

    private final PlanetService planetService;

    @Operation(summary = "Получение всех планет", description = "Позволяет выгрузить все планеты из БД")
    @GetMapping
    public List<PlanetDto> findAllPlanets() {
        return planetService.findAllPlanets();
    }

    @Operation(summary = "Получение планеты по ID", description = "Позволяет выгрузить одну планету по ID из БД")
    @GetMapping("/{id}")
    public PlanetDto findPlanetById(@PathVariable @Min(1) Long id) {
        return planetService.findPlanetById(id);
    }

    @Operation(summary = "Создать планету", description = "Позволяет создать новую запись о планете в БД")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PlanetDto createPlanet(@RequestBody @Valid PlanetDto planetDto) {
        return planetService.savePlanet(planetDto);
    }

    @Operation(summary = "Обновить данные о планете", description = "Позволяет обновить информацию о планете в БД")
    @PutMapping("/{id}")
    public PlanetDto updatePlanet(@PathVariable @Min(1) Long id, @RequestBody @Valid PlanetDto planetDto) {
        if (planetDto.getId() == null) {
            planetDto.setId(id);
        } else if (!planetDto.getId().equals(id)) {
            throw new IllegalArgumentException("ID в пути (" + id + ") не совпадает с ID в теле запроса (" + planetDto.getId() + ").");
        }
        return planetService.updatePlanet(planetDto);
    }

    @Operation(summary = "Удалить планету по ID", description = "Позволяет удалить планету по ID из БД")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePlanetById(@PathVariable @Min(1) Long id) {
        planetService.deletePlanetById(id);
    }

}
