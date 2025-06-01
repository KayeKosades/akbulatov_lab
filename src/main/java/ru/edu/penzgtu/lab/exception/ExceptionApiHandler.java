package ru.edu.penzgtu.lab.exception;

import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.edu.penzgtu.lab.base_response.BaseResponseService;
import ru.edu.penzgtu.lab.base_response.ResponseWrapper;

@Slf4j
@RestControllerAdvice // Для глобальной обработки исключений в контроллерах
@RequiredArgsConstructor // Для инъекции BaseResponseService
public class ExceptionApiHandler {

    private final BaseResponseService baseResponseService;

    @ExceptionHandler(PenzGtuException.class)
    public ResponseWrapper<?> handlePenzGtuException(PenzGtuException exception) {
        log.warn("Handling PenzGtuException: type={}, message='{}'", exception.getType(), exception.getMessage());
        return baseResponseService.wrapErrorResponse(exception);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({ConstraintViolationException.class, MethodArgumentNotValidException.class})
    public ResponseWrapper<?> handleValidationException(Exception e) {
        log.error("Got validation exception {}, message: {}", e.getClass().getSimpleName(), e.getMessage());
        return baseResponseService.wrapErrorResponse(new PenzGtuException(ErrorType.CLIENT_ERROR, "Ошибка валидации: " + e.getMessage()));
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Throwable.class)
    public ResponseWrapper<?> handleOtherException(Throwable t) {
        log.error("Got unexpected exception {}, message: {}", t.getClass().getSimpleName(), t.getMessage(), t);
        return baseResponseService.wrapErrorResponse(new PenzGtuException(ErrorType.COMMON_ERROR, "Произошла непредвиденная ошибка.", t));
    }

}
