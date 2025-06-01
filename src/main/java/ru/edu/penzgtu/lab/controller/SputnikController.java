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
import ru.edu.penzgtu.lab.dto.SputnikDto;
import ru.edu.penzgtu.lab.exception.ErrorType;
import ru.edu.penzgtu.lab.exception.PenzGtuException;
import ru.edu.penzgtu.lab.service.SputnikService;

import java.util.List;

@Validated
@RestController
@RequestMapping("/api/sputniks")
@RequiredArgsConstructor
@Tag(name = "Спутники", description = "Операции над спутниками")
public class SputnikController {

    private final SputnikService sputnikService;
    private final BaseResponseService baseResponseService;

    @Operation(summary = "Получение всех спутников", description = "Позволяет выгрузить все спутники из БД")
    @GetMapping
    public ResponseWrapper<List<SputnikDto>> findAllSputniks() {
        return baseResponseService.wrapSuccessResponse(sputnikService.findAllSputniks());
    }

    @Operation(summary = "Получение спутника по ID", description = "Позволяет выгрузить один спутник по ID из БД")
    @GetMapping("/{id}")
    public ResponseWrapper<SputnikDto> findSputnikById(@PathVariable @Min(1) Long id) {
        return baseResponseService.wrapSuccessResponse(sputnikService.findSputnikById(id));
    }

    @Operation(summary = "Получение спутников по ID планеты", description = "Позволяет выгрузить все спутники указанной планеты")
    @GetMapping("/by-planet/{planetId}")
    public ResponseWrapper<List<SputnikDto>> findSputniksByPlanetId(@PathVariable @Min(1) Long planetId) {
        return baseResponseService.wrapSuccessResponse(sputnikService.findSputniksByPlanetId(planetId));
    }

    @Operation(summary = "Создать спутник", description = "Позволяет создать новую запись о спутнике в БД")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseWrapper<SputnikDto> createSputnik(@RequestBody @Valid SputnikDto sputnikDto) {
        SputnikDto createdSputnik = sputnikService.saveSputnik(sputnikDto);
        return baseResponseService.wrapSuccessResponse(createdSputnik);
    }

    @Operation(summary = "Обновить данные о спутнике", description = "Позволяет обновить информацию о спутнике в БД")
    @PutMapping("/{id}")
    public ResponseWrapper<SputnikDto> updateSputnik(@PathVariable @Min(1) Long id, @RequestBody @Valid SputnikDto sputnikDto) {
        if (sputnikDto.getId() == null) {
            sputnikDto.setId(id);
        } else if (!sputnikDto.getId().equals(id)) {
            throw new PenzGtuException(ErrorType.CLIENT_ERROR, "ID в пути (" + id + ") не совпадает с ID в теле запроса (" + sputnikDto.getId() + ").");
        }
        SputnikDto updatedSputnik = sputnikService.updateSputnik(sputnikDto);
        return baseResponseService.wrapSuccessResponse(updatedSputnik);
    }

    @Operation(summary = "Удалить спутник по ID", description = "Позволяет удалить спутник по ID из БД")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseWrapper<?> deleteSputnikById(@PathVariable @Min(1) Long id) {
        sputnikService.deleteSputnikById(id);
        return baseResponseService.wrapSuccessResponse(null);
    }
}