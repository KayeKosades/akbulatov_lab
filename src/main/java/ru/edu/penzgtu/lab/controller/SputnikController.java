package ru.edu.penzgtu.lab.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.edu.penzgtu.lab.dto.SputnikDto;
import ru.edu.penzgtu.lab.service.SputnikService;

import java.util.List;

@Validated
@RestController
@RequestMapping("/api/sputniks")
@RequiredArgsConstructor
@Tag(name = "Спутники", description = "Операции над спутниками")
public class SputnikController {

    private final SputnikService sputnikService;

    @Operation(summary = "Получение всех спутников", description = "Позволяет выгрузить все спутники из БД")
    @GetMapping
    public List<SputnikDto> findAllSputniks() {
        return sputnikService.findAllSputniks();
    }

    @Operation(summary = "Получение спутника по ID", description = "Позволяет выгрузить один спутник по ID из БД")
    @GetMapping("/{id}")
    public SputnikDto findSputnikById(@PathVariable @Min(1) Long id) {
        return sputnikService.findSputnikById(id);
    }

    @Operation(summary = "Создать спутник", description = "Позволяет создать новую запись о спутнике в БД")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SputnikDto createSputnik(@RequestBody @Valid SputnikDto sputnikDto) {
        return sputnikService.saveSputnik(sputnikDto);
    }

    @Operation(summary = "Обновить данные о спутнике", description = "Позволяет обновить информацию о спутнике в БД")
    @PutMapping("/{id}")
    public SputnikDto updateSputnik(@PathVariable @Min(1) Long id, @RequestBody @Valid SputnikDto sputnikDto) {
        if (sputnikDto.getId() == null) {
            sputnikDto.setId(id);
        } else if (!sputnikDto.getId().equals(id)) {
            throw new IllegalArgumentException("ID в пути (" + id + ") не совпадает с ID в теле запроса (" + sputnikDto.getId() + ").");
        }
        return sputnikService.updateSputnik(sputnikDto);
    }

    @Operation(summary = "Удалить спутник по ID", description = "Позволяет удалить спутник по ID из БД")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSputnikById(@PathVariable @Min(1) Long id) {
        sputnikService.deleteSputnikById(id);
    }
}
