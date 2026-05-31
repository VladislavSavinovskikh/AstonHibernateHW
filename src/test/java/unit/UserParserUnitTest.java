package unit;

import com.hibernatehw.model.User;
import com.hibernatehw.util.UserParser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

public class UserParserUnitTest {

    @ParameterizedTest
    @CsvSource({
            "Анна, anna@test.ru, 20",
            "Мария, maria@test.com, 35",
            "Alexei, alexei@test.org, 45"
    })
    void parse_WithValidInput_ShouldReturnUser(String name, String email, String age) {
        String input = name + " " + email + " " + age;
        User user = UserParser.parse(input);

        assertEquals(name, user.getName());
        assertEquals(email, user.getEmail());
        assertEquals(Integer.parseInt(age), user.getAge());
    }

    @Test
    void parse_WithTrimAndMultipleSpaces_ShouldHandleCorrectly() {
        String input = "  Петр    petr@example.ru    30  ";

        User user = UserParser.parse(input);

        assertEquals("Петр", user.getName());
        assertEquals("petr@example.ru", user.getEmail());
        assertEquals(30, user.getAge());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "", "  ", "Иван", "Иван email", "Иван email age",
            "Иван email 25"
    })
    void parse_WithInvalidInput_ShouldThrowException(String input) {
        assertThrows(IllegalArgumentException.class, () -> UserParser.parse(input));
    }
}
