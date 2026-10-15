package studying.configuration;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import studying.service.ParkService;
import studying.service.ServiceCenter;

@DisplayName("Тесты Spring конфигурации (ApplicationConfiguration)")
class ApplicationConfigurationTest {

    @Test
    @DisplayName("Spring контейнер корректно поднимается и разрешает бины ServiceCenter и ParkService")
    void springContainer_resolvesBeansSuccessfully() {
        // Arrange
        var context = new AnnotationConfigApplicationContext(ApplicationConfiguration.class);

        // Act
        ServiceCenter serviceCenter = context.getBean(ServiceCenter.class);
        ParkService parkService = context.getBean(ParkService.class);

        // Assert
        assertNotNull(serviceCenter, "Бин ServiceCenter должен быть зарегистрирован");
        assertNotNull(parkService, "Бин ParkService должен быть зарегистрирован");

        context.close();
    }
}
