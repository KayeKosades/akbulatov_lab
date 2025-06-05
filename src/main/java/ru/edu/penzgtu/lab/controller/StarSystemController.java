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
import ru.edu.penzgtu.lab.dto.StarSystemDto;
import ru.edu.penzgtu.lab.service.StarSystemService;

import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping("/api/starsystems")
@RequiredArgsConstructor
@Tag(name = "Звездные Системы", description = "Операции над звездными системами")
public class StarSystemController {

    private final StarSystemService starSystemService;
    private final BaseResponseService baseResponseService;

    @Operation(summary = "Получить все звездные системы")
    @GetMapping
    public ResponseWrapper<List<StarSystemDto>> getAllStarSystems() {
        log.info("API GET /api/starsystems called");
        return baseResponseService.wrapSuccessResponse(starSystemService.findAllStarSystems());
    }

    @Operation(summary = "Получить звездную систему по ID")
    @GetMapping("/{id}")
    public ResponseWrapper<StarSystemDto> getStarSystemById(@PathVariable @Min(1) Long id) {
        log.info("API GET /api/starsystems/{} called", id);
        return baseResponseService.wrapSuccessResponse(starSystemService.findStarSystemById(id));
    }

    @Operation(summary = "Создать новую звездную систему")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseWrapper<StarSystemDto> createStarSystem(@RequestBody @Valid StarSystemDto starSystemDto) {
        log.info("API POST /api/starsystems called with DTO: {}", starSystemDto);
        StarSystemDto createdSystem = starSystemService.createStarSystem(starSystemDto);
        return baseResponseService.wrapSuccessResponse(createdSystem);
    }

    @Operation(summary = "Обновить звездную систему")
    @PutMapping("/{id}")
    public ResponseWrapper<StarSystemDto> updateStarSystem(@PathVariable @Min(1) Long id,
                                                           @RequestBody @Valid StarSystemDto starSystemDto) {
        log.info("API PUT /api/starsystems/{} called with DTO: {}", id, starSystemDto);
        StarSystemDto updatedSystem = starSystemService.updateStarSystem(id, starSystemDto);
        return baseResponseService.wrapSuccessResponse(updatedSystem);
    }

    @Operation(summary = "Удалить звездную систему (и все ее планеты каскадно)")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseWrapper<?> deleteStarSystem(@PathVariable @Min(1) Long id) {
        log.info("API DELETE /api/starsystems/{} called", id);
        starSystemService.deleteStarSystem(id);
        return baseResponseService.wrapSuccessResponse(null);
    }
}