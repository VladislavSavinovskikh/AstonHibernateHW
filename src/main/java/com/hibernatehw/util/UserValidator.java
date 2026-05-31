package com.hibernatehw.util;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class UserValidator {

    private static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
    private static final String NAME_REGEX = "^[a-zA-Zа-яА-Я]+$";
    private static final String AGE_REGEX = "^\\d+$";

    public static void validate(String name, String email, String age) {
        log.debug("Валидация данных: name={}, email={}, age={}", name, email, age);

        if (name == null || !name.matches(NAME_REGEX)) {
            log.warn("Ошибка валидации имени: {}", name);
            throw new IllegalArgumentException("Имя должно содержать только буквы");
        }

        if (email == null || !email.matches(EMAIL_REGEX)) {
            log.warn("Ошибка валидации email: {}", email);
            throw new IllegalArgumentException("Некорректный формат email");
        }

        if (age == null || !age.matches(AGE_REGEX)) {
            log.warn("Ошибка валидации возраста: {}", age);
            throw new IllegalArgumentException("Возраст может содержать только цифры");
        }

        int ageInt = Integer.parseInt(age);
        if (ageInt < 0 || ageInt > 150) {
            log.warn("Некорректный возраст: {}", ageInt);
            throw new IllegalArgumentException("Возраст должен быть в диапазоне 0-150 лет");
        }

        log.debug("Валидация успешно пройдена");
    }
}
