package ru.edu.penzgtu.lab.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@Schema(description = "Информация о звездной системе")
public class StarSystemDto {

    @JsonProperty("id")
    @Schema(description = "ID звездной системы в БД", example = "1")
    private Long id;

    @JsonProperty("name")
    @NotBlank(message = "Название звездной системы не может быть пустым")
    @Schema(description = "Название звездной системы", example = "Солнечная система")
    private String name;

    @JsonProperty("galaxyName")
    @Schema(description = "Название галактики", example = "Млечный Путь")
    private String galaxyName;

    @JsonProperty("discoveryDate")
    @PastOrPresent(message = "Дата открытия не может быть в будущем")
    @Schema(description = "Дата открытия", example = "1610-01-07")
    private LocalDate discoveryDate;

    @JsonProperty("planetIds")
    @Schema(description = "Список ID планет в этой системе (для ответа)")
    private List<Long> planetIds;

}
