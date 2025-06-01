package ru.edu.penzgtu.lab.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "Информация о спутнике")
public class SputnikDto {

    @JsonProperty("id")
    @Schema(description = "ID спутника в БД", example = "101")
    private Long id;

    @JsonProperty("name")
    @NotBlank(message = "Название спутника не может быть пустым")
    @Schema(description = "Название спутника", example = "Луна")
    private String name;

    @JsonProperty("orbitalPeriod")
    @NotNull(message = "Орбитальный период не может быть пустым")
    @Positive(message = "Орбитальный период должен быть положительным числом")
    @Schema(description = "Орбитальный период в днях", example = "27.3")
    private Double orbitalPeriod;

    @JsonProperty("isNatural")
    @Schema(description = "Естественный или искусственный спутник", example = "true")
    private Boolean isNatural;

    @JsonProperty("planetId")
    @NotNull(message = "ID планеты должен быть указан для спутника")
    @Schema(description = "ID планеты, к которой принадлежит спутник", example = "1")
    private Long planetId;
}
