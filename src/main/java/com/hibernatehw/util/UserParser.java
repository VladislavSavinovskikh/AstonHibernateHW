package com.hibernatehw.util;

import com.hibernatehw.model.User;
import lombok.extern.log4j.Log4j2;


@Log4j2
public class UserParser {
    public static User parse(String s) {
        log.debug("Парсинг строки: {}", s);

        String[] split = s.trim().split("\\s+");

        if (split.length < 3) {
            log.error("Неверный формат строки. Получено {} аргументов, ожидается 3", split.length);
            throw new IllegalArgumentException("Неверный формат строки. Ожидается: Имя Email Возраст");
        }

        String name = split[0];
        String email = split[1];
        String age = split[2];

        log.debug("Извлеченные данные: name={}, email={}, age={}", name, email, age);

        UserValidator.validate(name, email, age);

        User user = new User(name, email, Integer.parseInt(age));
        log.debug("Создан пользователь: {}", user);

        return user;
    }
}
