package ru.edu.penzgtu.lab.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor // Для генерации конструктора для final полей
public enum ErrorType {

    COMMON_ERROR("Ошибка бизнес-логики", "Повторите запрос позже"),
    NOT_FOUND("Не удалось найти ресурс", "По вашему запросу ресурс не найден"),
    CLIENT_ERROR("Ошибка в запросе", "Проверьте параметры и повторите запрос");

    private final String title;
    private final String text;

}
