package ru.edu.penzgtu.lab.base_response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "Информация об ошибке")
public class ErrorDto {

    @Schema(description = "Код ошибки (имя ErrorType)", example = "NOT_FOUND")
    private String code;

    @Schema(description = "Заголовок ошибки", example = "Не удалось найти ресурс")
    private String title;

    @Schema(description = "Текст/описание ошибки", example = "По вашему запросу ресурс не найден")
    private String text;
}
