package com.hibernatehw.ui;

import com.hibernatehw.model.User;
import com.hibernatehw.service.UserService;
import lombok.extern.log4j.Log4j2;

import java.util.Optional;
import java.util.Scanner;

@Log4j2
public class UI {
    Scanner sc;
    UserService userService;

    public UI(UserService userService) {
        this.sc = new Scanner(System.in);
        this.userService = userService;
    }

    public void run() {
        log.info("Запуск пользовательского интерфейса");
        boolean running = true;
        while (running) {
            clearConsole();
            printMainMenu();
            int choice = readInt();
            log.debug("Пользователь выбрал пункт меню: {}", choice);
            running = handleMainMenu(choice);
        }
        log.info("Завершение работы приложения");
    }

    private boolean handleMainMenu(int choice) {
        switch (choice) {
            case 0:
                log.info("Пользователь выбрал выход");
                return false;
            case 1:
                log.debug("Переход к показу списка пользователей");
                showUsersList();
                break;
            case 2:
                log.debug("Переход к добавлению пользователя");
                showAdd();
                break;
            case 3:
                log.debug("Переход к удалению пользователя");
                showDelete();
                break;
            case 4:
                log.debug("Переход к обновлению пользователя");
                showUpdate();
                break;
            default:
                log.warn("Пользователь ввел некорректный пункт меню: {}", choice);
                System.out.println("Некорректный ввод! Выберите пункт от 0 до 4");
                pause();
        }
        return true;
    }

    private void showUsersList() {
        this.clearConsole();
        this.userService.showUsers();
        this.pause();
    }

    private void showAdd() {
        while (true) {
            System.out.println("Введите данные в формате: Имя E-mail Возраст");
            System.out.println("0. Выйти в меню");

            String input = this.sc.nextLine().trim();
            if (input.equals("0")) return;

            if (!input.isEmpty()) {
                if (this.userService.addUser(input)) {
                    System.out.println("Пользователь успешно добавлен");
                    return;
                }
            } else {
                System.out.println("Пустой ввод!");
            }
        }
    }

    private Optional<User> getUserFromInput(String actionName) {
        while (true) {
            System.out.println("Введите id пользователя которого хотите " + actionName);
            System.out.println("0. Выйти в меню");

            String input = sc.nextLine().trim();

            if (input.isEmpty()) {
                System.out.println("Ввод не может быть пустым!");
                continue;
            }

            try {
                long userId = Long.parseLong(input);

                if (userId == 0) return Optional.empty();
                if (userId < 0) {
                    System.out.println("Id пользователя должно быть положительным!");
                    continue;
                }

                Optional<User> userOptional = userService.getUserById(userId);
                if (userOptional.isEmpty()) {
                    System.out.println("Пользователь с id " + userId + " не найден");
                    continue;
                }

                return userOptional;

            } catch (NumberFormatException e) {
                System.out.println("Ошибка: введите целое число!");
            }
        }
    }

    private void showDelete() {
        Optional<User> userOptional = getUserFromInput("удалить");
        if (userOptional.isEmpty()) return;

        User user = userOptional.get();

        System.out.println("Данные выбранного пользователя:");
        System.out.println(user);
        System.out.println("Удалить пользователя?\n1. Да\n2. Нет");

        String choice = sc.nextLine().trim();
        if (choice.equals("1")) {
            userService.deleteUser(user.getId());
            System.out.println("Пользователь успешно удален");
        } else if (choice.equals("2")) {
            System.out.println("Удаление отменено");
        } else {
            System.out.println("Некорректный выбор");
        }
    }


    private void showUpdate() {
        Optional<User> userOptional = getUserFromInput("изменить");
        if (userOptional.isEmpty()) {
            System.out.println("Операция отменена");
            return;
        }

        User user = userOptional.get();
        System.out.println("Данные выбранного пользователя:");
        System.out.println(user);

        while (true) {
            System.out.println("Введите обновленные данные в формате: Имя E-mail Возраст ");
            System.out.println("0. Выйти в меню");

            String input = sc.nextLine().trim();

            if (input.equals("0")) {
                System.out.println("Обновление отменено");
                return;
            }

            if (input.isEmpty()) {
                System.out.println("Пустой ввод!");
            } else if (userService.updateUser(user, input)) {
                System.out.println("Пользователь успешно обновлен");
                return;
            }
        }
    }

    private void printMainMenu() {
        System.out.println("1. Показать список");
        System.out.println("2. Добавить");
        System.out.println("3. Удалить");
        System.out.println("4. Редактировать");
        System.out.println("0. Выход");
        System.out.print("\nВыберите действие: ");
    }

    private int readInt() {
        while (!this.sc.hasNextInt()) {
            System.out.println("Введите число: ");
            this.sc.next();
        }

        int value = this.sc.nextInt();
        this.sc.nextLine();
        return value;
    }

    private void clearConsole() {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (Exception e) {
            for (int i = 0; i < 50; i++) System.out.println();
        }
    }

    private void flushBuffer() {
        if (sc.hasNextLine()) {
            sc.nextLine();
        }
    }

    private void pause() {
        System.out.println("\nНажмите Enter...");
        flushBuffer();
        sc.nextLine();
    }
}
