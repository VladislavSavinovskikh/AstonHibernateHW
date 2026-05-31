package fixtures;

import com.hibernatehw.model.User;

public class TestDataFactory {
    public static User createValidUser() {
        return new User("Иван", "ivan@test.ru", 25);
    }

    public static User createUserWithName(String name) {
        return new User(name, "test@test.ru", 30);
    }

    public static String getValidUserInput() {
        return "Тестовый test@example.ru 30";
    }

    public static String getInvalidUserInput() {
        return "invalid-input";
    }
}
