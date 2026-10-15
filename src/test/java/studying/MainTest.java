package studying;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Интеграционный тест запуска приложения (Main)")
class MainTest {

    @Test
    @DisplayName("Запуск приложения с флагом --demo выполняется без ошибок")
    void main_runsDemoModeSuccessfully() {
        // Arrange
        String[] args = new String[]{"--demo"};

        // Act & Assert
        assertDoesNotThrow(() -> Main.main(args));
    }

    @Test
    @DisplayName("Базовые команды консоли выполняются успешно")
    void main_runsBasicCliCommands() {
        // Arrange
        String commands = String.join("\n",
                "help",
                "add-scooter Ninebot 8 2.5",
                "add-bike Forward 9",
                "list",
                "beginners",
                "energy",
                "count",
                "exit"
        ) + "\n";

        InputStream originalIn = System.in;
        try {
            System.setIn(new ByteArrayInputStream(commands.getBytes(StandardCharsets.UTF_8)));

            // Act & Assert
            assertDoesNotThrow(() -> Main.main(new String[]{}));
        } finally {
            System.setIn(originalIn);
        }
    }
}
