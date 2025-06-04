package ru.edu.penzgtu.lab.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
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
    @Size(max = 200, message = "Название спутника не должно превышать 200 символов")
    @Schema(description = "Название спутника", example = "Луна")
    private String name;

    @JsonProperty("orbital_period_days")
    @NotNull(message = "Орбитальный период не может быть пустым")
    @Positive(message = "Орбитальный период должен быть положительным числом")
    @Schema(description = "Орбитальный период в днях", example = "27.3")
    private Double orbitalPeriod;

    @JsonProperty("is_natural")
    @NotNull(message = "Информация о естественности спутника обязательна")
    @Schema(description = "Естественный или искусственный спутник", example = "true")
    private Boolean isNatural;

    @JsonProperty("planet_id")
    @NotNull(message = "ID планеты должен быть указан для спутника")
    @Schema(description = "ID планеты, к которой принадлежит спутник", example = "1")
    private Long planetId;

    @JsonProperty("mean_radius_km")
    @NotNull(message = "Средний радиус не может быть пустым")
    @Positive(message = "Средний радиус должен быть положительным числом")
    @Schema(description = "Средний радиус в км", example = "1737.4")
    private Double meanRadiusKm;

    @JsonProperty("mass_kg")
    @Positive(message = "Масса должна быть положительным числом, если указана")
    @Schema(description = "Масса в 10^20 кг", example = "734.2")
    private Double massKg;
}
