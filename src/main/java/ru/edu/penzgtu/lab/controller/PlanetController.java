package ru.edu.penzgtu.lab.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.edu.penzgtu.lab.base_response.BaseResponseService;
import ru.edu.penzgtu.lab.base_response.ResponseWrapper;
import ru.edu.penzgtu.lab.dto.PlanetDto;
import ru.edu.penzgtu.lab.service.PlanetService;

import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping("/api/planets")
@RequiredArgsConstructor
@Tag(name = "Планеты", description = "Операции над планетами и их связями")
public class PlanetController {

    private final PlanetService planetService;
    private final BaseResponseService baseResponseService;

    @Operation(summary = "Получение всех планет")
    @GetMapping
    public ResponseWrapper<List<PlanetDto>> findAllPlanets() {
        log.info("API GET /api/planets called");
        return baseResponseService.wrapSuccessResponse(planetService.findAllPlanets());
    }

    @Operation(summary = "Получение планеты по ID")
    @GetMapping("/{id}")
    public ResponseWrapper<PlanetDto> findPlanetById(@PathVariable @Min(1) Long id) {
        log.info("API GET /api/planets/{} called", id);
        return baseResponseService.wrapSuccessResponse(planetService.findPlanetById(id));
    }

    @Operation(summary = "Создать планету")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseWrapper<PlanetDto> createPlanet(@RequestBody @Valid PlanetDto planetDto) {
        log.info("API POST /api/planets called with DTO: {}", planetDto);
        PlanetDto createdPlanet = planetService.savePlanet(planetDto); // Используем обновленный createPlanet
        return baseResponseService.wrapSuccessResponse(createdPlanet);
    }

    @Operation(summary = "Обновить данные о планете")
    @PutMapping("/{id}")
    public ResponseWrapper<PlanetDto> updatePlanet(@PathVariable @Min(1) Long id, @RequestBody @Valid PlanetDto planetDto) {
        log.info("API PUT /api/planets/{} called with DTO: {}", id, planetDto);
        PlanetDto updatedPlanet = planetService.updatePlanet(id, planetDto); // Используем обновленный updatePlanet
        return baseResponseService.wrapSuccessResponse(updatedPlanet);
    }

    @Operation(summary = "Удалить планету (и все ее спутники/миссии каскадно)")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseWrapper<?> deletePlanetById(@PathVariable @Min(1) Long id) {
        log.info("API DELETE /api/planets/{} called", id);
        planetService.deletePlanetById(id);
        return baseResponseService.wrapSuccessResponse(null);
    }

    @Operation(summary = "Назначить планету звездной системе",
            description = "Устанавливает или изменяет звездную систему для указанной планеты.")
    @PostMapping("/{planetId}/assign-starsystem/{starSystemId}")
    public ResponseWrapper<PlanetDto> assignPlanetToStarSystem(
            @Parameter(description = "ID планеты") @PathVariable @Min(1) Long planetId,
            @Parameter(description = "ID звездной системы") @PathVariable @Min(1) Long starSystemId) {
        log.info("API POST /api/planets/{}/assign-starsystem/{} called", planetId, starSystemId);
        PlanetDto updatedPlanet = planetService.assignPlanetToStarSystem(planetId, starSystemId);
        return baseResponseService.wrapSuccessResponse(updatedPlanet);
    }

    @Operation(summary = "Убрать планету из звездной системы",
            description = "Делает планету 'блуждающей', если это разрешено логикой (устанавливает star_system_id = null).")
    @PostMapping("/{planetId}/remove-starsystem")
    public ResponseWrapper<PlanetDto> removePlanetFromStarSystem(
            @Parameter(description = "ID планеты") @PathVariable @Min(1) Long planetId) {
        log.info("API POST /api/planets/{}/remove-starsystem called", planetId);
        PlanetDto updatedPlanet = planetService.removePlanetFromStarSystem(planetId);
        return baseResponseService.wrapSuccessResponse(updatedPlanet);
    }

}