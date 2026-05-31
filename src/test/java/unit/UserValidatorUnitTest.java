package unit;

import com.hibernatehw.util.UserValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserValidatorUnitTest {
    @Test
    void validate_WithValidData_ShouldNotThrowException() {
        assertDoesNotThrow(() -> UserValidator.validate("Иван", "ivan@example.com", "25"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"Иван", "Петр", "Анна", "John", "Anna"})
    void validate_WithValidNames_ShouldPass(String name) {
        assertDoesNotThrow(() -> UserValidator.validate(name, "test@example.com", "30"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "123", "Иван@", "Иван123", "@#$%"})
    void validate_WithInvalidNames_ShouldThrowException(String name) {
        assertThrows(IllegalArgumentException.class,
                () -> UserValidator.validate(name, "test@example.com", "30"));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "user@example.com",
            "user.name@example.com",
            "user@example.co.uk",
            "user-name@example.org"
    })
    void validate_WithValidEmails_ShouldPass(String email) {
        assertDoesNotThrow(() -> UserValidator.validate("Иван", email, "25"));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "", "invalid", "invalid@", "@example.com", "user@.com", "user@example."
    })
    void validate_WithInvalidEmails_ShouldThrowException(String email) {
        assertThrows(IllegalArgumentException.class,
                () -> UserValidator.validate("Иван", email, "25"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"18", "25", "100", "150"})
    void validate_WithValidAges_ShouldPass(String age) {
        assertDoesNotThrow(() -> UserValidator.validate("Иван", "test@example.com", age));
    }

    @ParameterizedTest
    @ValueSource(strings = {"-1", "-5", "151", "200", "строка", "", " "})
    void validate_WithInvalidAges_ShouldThrowException(String age) {
        assertThrows(IllegalArgumentException.class,
                () -> UserValidator.validate("Иван", "test@example.com", age));
    }
}
