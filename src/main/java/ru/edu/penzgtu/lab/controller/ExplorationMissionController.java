package ru.edu.penzgtu.lab.controller;

import io.swagger.v3.oas.annotations.Operation;
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
import ru.edu.penzgtu.lab.dto.ExplorationMissionDto;
import ru.edu.penzgtu.lab.service.ExplorationMissionService;

import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping("/api/missions")
@RequiredArgsConstructor
@Tag(name = "Исследовательские Миссии", description = "Операции над исследовательскими миссиями")
public class ExplorationMissionController {

    private final ExplorationMissionService missionService;
    private final BaseResponseService baseResponseService;

    @Operation(summary = "Получить все миссии")
    @GetMapping
    public ResponseWrapper<List<ExplorationMissionDto>> getAllMissions() {
        log.info("API GET /api/missions called");
        return baseResponseService.wrapSuccessResponse(missionService.findAllMissions());
    }

    @Operation(summary = "Получить миссию по ID")
    @GetMapping("/{id}")
    public ResponseWrapper<ExplorationMissionDto> getMissionById(@PathVariable @Min(1) Long id) {
        log.info("API GET /api/missions/{} called", id);
        return baseResponseService.wrapSuccessResponse(missionService.findMissionById(id));
    }

    @Operation(summary = "Получить все миссии для указанной планеты")
    @GetMapping("/planet/{planetId}")
    public ResponseWrapper<List<ExplorationMissionDto>> getMissionsByPlanetId(@PathVariable @Min(1) Long planetId) {
        log.info("API GET /api/missions/planet/{} called", planetId);
        return baseResponseService.wrapSuccessResponse(missionService.findMissionsByPlanetId(planetId));
    }

    @Operation(summary = "Создать новую миссию")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseWrapper<ExplorationMissionDto> createMission(@RequestBody @Valid ExplorationMissionDto missionDto) {
        log.info("API POST /api/missions called with DTO: {}", missionDto);
        ExplorationMissionDto createdMission = missionService.createMission(missionDto);
        return baseResponseService.wrapSuccessResponse(createdMission);
    }

    @Operation(summary = "Обновить миссию")
    @PutMapping("/{id}")
    public ResponseWrapper<ExplorationMissionDto> updateMission(@PathVariable @Min(1) Long id,
                                                                @RequestBody @Valid ExplorationMissionDto missionDto) {
        log.info("API PUT /api/missions/{} called with DTO: {}", id, missionDto);
        ExplorationMissionDto updatedMission = missionService.updateMission(id, missionDto);
        return baseResponseService.wrapSuccessResponse(updatedMission);
    }

    @Operation(summary = "Удалить миссию")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseWrapper<?> deleteMission(@PathVariable @Min(1) Long id) {
        log.info("API DELETE /api/missions/{} called", id);
        missionService.deleteMission(id);
        return baseResponseService.wrapSuccessResponse(null);
    }
}
