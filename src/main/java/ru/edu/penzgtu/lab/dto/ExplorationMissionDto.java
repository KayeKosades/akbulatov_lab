package ru.edu.penzgtu.lab.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Builder;
import lombok.Data;
import ru.edu.penzgtu.lab.entity.enums.MissionStatus;

import java.time.LocalDate;

@Data
@Builder
@Schema(description = "Информация об исследовательской миссии")
public class ExplorationMissionDto {

    @JsonProperty("id")
    @Schema(description = "ID миссии в БД", example = "1")
    private Long id;

    @JsonProperty("missionName")
    @NotBlank(message = "Название миссии не может быть пустым")
    @Schema(description = "Название миссии", example = "Марс-Экспресс")
    private String missionName;

    @JsonProperty("launchDate")
    @NotNull(message = "Дата запуска миссии не может быть пустой")
    @PastOrPresent(message = "Дата запуска миссии не может быть в будущем")
    @Schema(description = "Дата запуска", example = "2003-06-02")
    private LocalDate launchDate;

    @JsonProperty("completionDate")
    @FutureOrPresent(message = "Дата завершения миссии не может быть в прошлом (если указана)")
    @Schema(description = "Дата завершения (null, если активна)", example = "2025-12-31")
    private LocalDate completionDate;

    @JsonProperty("objective")
    @Schema(description = "Цель миссии", example = "Исследование атмосферы и поверхности Марса")
    private String objective;

    @JsonProperty("status")
    @NotNull(message = "Статус миссии должен быть указан")
    @Schema(description = "Статус миссии", example = "ACTIVE")
    private MissionStatus status;

    @JsonProperty("targetPlanetId")
    @NotNull(message = "ID целевой планеты должен быть указан")
    @Schema(description = "ID целевой планеты", example = "4")
    private Long targetPlanetId;
}
