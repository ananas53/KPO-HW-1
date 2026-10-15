package studying.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import studying.service.impl.Scooter;

@DisplayName("Unit-тесты пункта техосмотра (ServiceCenter)")
class ServiceCenterTest {

    @Test
    @DisplayName("Метод inspect возвращает результат проверки без исключений")
    void inspect_executesSuccessfully() {
        // Arrange
        ServiceCenter serviceCenter = new ServiceCenter();
        Scooter scooter = new Scooter("Ninebot", 8, 2.0);

        for (int i = 0; i < 50; i++) {
            // Act
            boolean result = serviceCenter.inspect(scooter);

            // Assert
            assertNotNull(result);
        }
    }
}
