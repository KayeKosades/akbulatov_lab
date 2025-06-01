package ru.edu.penzgtu.lab.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@Schema(description = "Информация о планете")
public class PlanetDto {

    @JsonProperty("id")
    @Schema(description = "ID планеты в БД", example = "1")
    private Long id;

    @JsonProperty("name")
    @NotBlank(message = "Название планеты не может быть пустым")
    @Schema(description = "Название планеты", example = "Земля")
    private String name;

    @JsonProperty("type")
    @Schema(description = "Тип планеты", example = "Земная группа")
    private String type;

    @JsonProperty("diameter")
    @NotNull(message = "Диаметр не может быть пустым")
    @Positive(message = "Диаметр должен быть положительным числом")
    @Schema(description = "Диаметр в км", example = "12742.0")
    private Double diameter;

    @JsonProperty("hasAtmosphere")
    @Schema(description = "Наличие атмосферы", example = "true")
    private Boolean hasAtmosphere;

    @JsonProperty("starSystem")
    @NotBlank(message = "Название звездной системы не может быть пустым")
    @Schema(description = "Название звездной системы", example = "Солнечная")
    private String starSystem;

    @JsonProperty("satelliteNames")
    @Schema(description = "Названия спутников планеты (используется преимущественно для ответа)")
    private List<String> satelliteNames;
}
