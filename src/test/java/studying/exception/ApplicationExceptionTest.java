package studying.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Unit-тесты пользовательского исключения ApplicationException")
class ApplicationExceptionTest {

    @Test
    @DisplayName("Создание исключения с кодом и сообщением")
    void constructorWithMessageAndCode() {
        // Arrange & Act
        ApplicationException exception = new ApplicationException(
                ApplicationErrorCode.VALIDATION_ERROR, "Ошибка валидации");

        // Assert
        assertEquals(ApplicationErrorCode.VALIDATION_ERROR, exception.getCode());
        assertEquals("Ошибка валидации", exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    @DisplayName("Создание исключения с кодом, сообщением и причиной")
    void constructorWithMessageCodeAndCause() {
        // Arrange
        Throwable cause = new RuntimeException("Корневая причина");

        // Act
        ApplicationException exception = new ApplicationException(
                ApplicationErrorCode.VALIDATION_ERROR, "Ошибка с причиной", cause);

        // Assert
        assertEquals(ApplicationErrorCode.VALIDATION_ERROR, exception.getCode());
        assertEquals("Ошибка с причиной", exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    @Test
    @DisplayName("Перечисление ApplicationErrorCode содержит ожидаемые значения")
    void enumValues() {
        assertNotNull(ApplicationErrorCode.valueOf("VALIDATION_ERROR"));
    }
}
