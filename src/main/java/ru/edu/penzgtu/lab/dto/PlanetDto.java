package ru.edu.penzgtu.lab.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
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
    @NotNull(message = "Название планеты не может быть null")
    @Schema(description = "Название планеты", example = "Земля")
    private String name;

    @JsonProperty("type")
    @Size(max = 100, message = "Тип планеты не должен превышать 100 символов")
    @Schema(description = "Тип планеты", example = "Земная группа")
    private String type;

    @JsonProperty("diameter_km")
    @NotNull(message = "Диаметр не может быть null")
    @Positive(message = "Диаметр должен быть положительным числом")
    @Schema(description = "Диаметр в км", example = "12742.0")
    private Double diameter;

    @JsonProperty("has_atmosphere")
    @NotNull(message = "Поле Наличие атмфосфера не может быть пустым")
    @Schema(description = "Наличие атмосферы", example = "true")
    private Boolean hasAtmosphere;

    @JsonProperty("star_system")
    @NotBlank(message = "Название звездной системы не может быть пустым")
    @Size(max = 150, message = "Название звездной системы не должно превышать 150 символов")
    @Schema(description = "Название звездной системы", example = "Солнечная")
    private String starSystem;

    @JsonProperty("discovery_date")
    @PastOrPresent(message = "Дата открытия не может быть в будущем")
    @Schema(description = "Дата открытия", example = "1781-03-13")
    private LocalDate discoveryDate;

    @JsonProperty("surface_gravity_g")
    @NotNull(message = "Поле Поверхостная гравитация не может быть пустым")
    @Positive(message = "Поверхностная гравитация должна быть положительной")
    @Schema(description = "Поверхностная гравитация в g", example = "20.2")
    private Double surfaceGravity;

    @JsonProperty("number_of_moons_confirmed")
    @NotNull(message = "Количество подтвержденных спутников обязательно")
    @PositiveOrZero(message = "Количество подтвержденных спутников не может быть отрицательным")
    @Schema(description = "Количество подтвержденных спутников", example = "1")
    private Integer numberOfMoonsConfirmed;

    @JsonProperty("satellite_names")
    @Schema(description = "Названия спутников планеты (используется преимущественно для ответа)")
    private List<String> satelliteNames;
}
