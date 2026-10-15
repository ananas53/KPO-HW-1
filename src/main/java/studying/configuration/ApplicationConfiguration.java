package studying.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import studying.service.ParkService;
import studying.service.ServiceCenter;

@Configuration(proxyBeanMethods = false)
public class ApplicationConfiguration {
    @Bean
    ServiceCenter serviceCenter() {
        return new ServiceCenter();
    }

    @Bean
    ParkService parkService(final ServiceCenter serviceCenter) {
        return new ParkService(serviceCenter);
    }
}
