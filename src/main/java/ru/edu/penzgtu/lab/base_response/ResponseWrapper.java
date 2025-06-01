package ru.edu.penzgtu.lab.base_response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "Обёртка для ответа")
public class ResponseWrapper<T> {

    @Schema(description = "Флаг успешности выполнения запроса")
    private boolean success;

    @Schema(description = "Тело ответа")
    private T body;

    @Schema(description = "Описание ошибки")
    private ErrorDto error;
}
