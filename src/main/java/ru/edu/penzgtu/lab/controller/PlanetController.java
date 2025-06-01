package ru.edu.penzgtu.lab.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.edu.penzgtu.lab.base_response.BaseResponseService;
import ru.edu.penzgtu.lab.base_response.ResponseWrapper;
import ru.edu.penzgtu.lab.dto.PlanetDto;
import ru.edu.penzgtu.lab.exception.ErrorType;
import ru.edu.penzgtu.lab.exception.PenzGtuException;
import ru.edu.penzgtu.lab.service.PlanetService;

import java.util.List;

@Validated
@RestController
@RequestMapping("/api/planets")
@RequiredArgsConstructor
@Tag(name = "Планеты", description = "Операции над планетами")
public class PlanetController {

    private final PlanetService planetService;
    private final BaseResponseService baseResponseService;

    @Operation(summary = "Получение всех планет", description = "Позволяет выгрузить все планеты из БД")
    @GetMapping
    public ResponseWrapper<List<PlanetDto>> findAllPlanets() {
        return baseResponseService.wrapSuccessResponse(planetService.findAllPlanets());
    }

    @Operation(summary = "Получение планеты по ID", description = "Позволяет выгрузить одну планету по ID из БД")
    @GetMapping("/{id}")
    public ResponseWrapper<PlanetDto> findPlanetById(@PathVariable @Min(1) Long id) {
        return baseResponseService.wrapSuccessResponse(planetService.findPlanetById(id));
    }

    @Operation(summary = "Создать планету", description = "Позволяет создать новую запись о планете в БД")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseWrapper<PlanetDto> createPlanet(@RequestBody @Valid PlanetDto planetDto) {
        PlanetDto createdPlanet = planetService.savePlanet(planetDto);
        return baseResponseService.wrapSuccessResponse(createdPlanet);
    }

    @Operation(summary = "Обновить данные о планете", description = "Позволяет обновить информацию о планете в БД")
    @PutMapping("/{id}")
    public ResponseWrapper<PlanetDto> updatePlanet(@PathVariable @Min(1) Long id, @RequestBody @Valid PlanetDto planetDto) { // <--- Изменен тип возврата
        if (planetDto.getId() == null) {
            planetDto.setId(id);
        } else if (!planetDto.getId().equals(id)) {
            throw new PenzGtuException(ErrorType.CLIENT_ERROR,"ID в пути (" + id + ") не совпадает с ID в теле запроса (" + planetDto.getId() + ").");
        }
        PlanetDto updatedPlanet = planetService.updatePlanet(planetDto);
        return baseResponseService.wrapSuccessResponse(updatedPlanet);
    }

    @Operation(summary = "Удалить планету по ID", description = "Позволяет удалить планету по ID из БД")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseWrapper<?> deletePlanetById(@PathVariable @Min(1) Long id) {
        planetService.deletePlanetById(id);
        return baseResponseService.wrapSuccessResponse(null);
    }
}