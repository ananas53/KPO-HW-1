package studying.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import studying.service.impl.Bicycle;
import studying.service.impl.Charger;
import studying.service.impl.ElectricBicycle;
import studying.service.impl.Helmet;
import studying.service.impl.Scooter;

@DisplayName("Unit-тесты сервиса парка (ParkService)")
class ParkServiceTest {

    @Test
    @DisplayName("При успешном техосмотре транспорт принимается на баланс и учитывается энергия")
    void addNewTransport_whenInspectionPasses_addsTransportAndUpdatesEnergy() {
        // Arrange
        ServiceCenter serviceCenterMock = mock(ServiceCenter.class);
        when(serviceCenterMock.inspect(any(Transport.class))).thenReturn(true);
        ParkService parkService = new ParkService(serviceCenterMock);
        Scooter scooter = new Scooter("Xiaomi Pro", 8, 2.5);

        // Act
        boolean accepted = parkService.addNewTransport(scooter);

        // Assert
        assertTrue(accepted, "Транспорт должен быть успешно принят");
        assertEquals(1, parkService.getItemsCount(), "В парке должна быть 1 единица");
        assertEquals(2.5, parkService.getAllEnergyUsagePerDay(), 0.001, "Суточная энергия должна увеличиться на 2.5");
        verify(serviceCenterMock, times(1)).inspect(scooter);
    }

    @Test
    @DisplayName("При успешном техосмотре безмоторного транспорта энергия не увеличивается")
    void addNewTransport_whenNonElectricPasses_energyRemainsZero() {
        // Arrange
        ServiceCenter serviceCenterMock = mock(ServiceCenter.class);
        when(serviceCenterMock.inspect(any(Transport.class))).thenReturn(true);
        ParkService parkService = new ParkService(serviceCenterMock);
        Bicycle bike = new Bicycle("Forward", 9);

        // Act
        boolean accepted = parkService.addNewTransport(bike);

        // Assert
        assertTrue(accepted);
        assertEquals(1, parkService.getItemsCount());
        assertEquals(0.0, parkService.getAllEnergyUsagePerDay(), 0.001);
        verify(serviceCenterMock, times(1)).inspect(bike);
    }

    @Test
    @DisplayName("При отклонении техосмотра транспорт не попадает в активный парк")
    void addNewTransport_whenInspectionFails_rejectsTransport() {
        // Arrange
        ServiceCenter serviceCenterMock = mock(ServiceCenter.class);
        when(serviceCenterMock.inspect(any(Transport.class))).thenReturn(false);
        ParkService parkService = new ParkService(serviceCenterMock);
        Scooter scooter = new Scooter("Broken Scooter", 4, 3.0);

        // Act
        boolean accepted = parkService.addNewTransport(scooter);

        // Assert
        assertFalse(accepted, "Транспорт должен быть отклонен");
        assertEquals(0, parkService.getItemsCount(), "Парк должен остаться пустым");
        assertEquals(0.0, parkService.getAllEnergyUsagePerDay(), 0.001);
        verify(serviceCenterMock, times(1)).inspect(scooter);
    }

    @Test
    @DisplayName("Вещи принимаются на баланс без прохождения техосмотра")
    void addNewThing_addsItemWithoutInspection() {
        // Arrange
        ServiceCenter serviceCenterMock = mock(ServiceCenter.class);
        ParkService parkService = new ParkService(serviceCenterMock);
        Helmet helmet = new Helmet("Шлем защитный");
        Charger charger = new Charger("Зарядный шкаф", 10.0);

        // Act
        parkService.addNewThing(helmet);
        parkService.addNewThing(charger);

        // Assert
        assertEquals(2, parkService.getItemsCount());
        assertEquals(10.0, parkService.getAllEnergyUsagePerDay(), 0.001);
        verify(serviceCenterMock, never()).inspect(any());
    }

    @Test
    @DisplayName("Фильтрация техники для новичков (простота >= 6)")
    void getTransportForBeginners_filtersBySimplicityThreshold() {
        // Arrange
        ServiceCenter serviceCenterMock = mock(ServiceCenter.class);
        when(serviceCenterMock.inspect(any(Transport.class))).thenReturn(true);
        ParkService parkService = new ParkService(serviceCenterMock);

        Scooter hardScooter = new Scooter("Fast & Furious", 3, 4.0);
        Bicycle mediumBike = new Bicycle("City Bike", 6);
        ElectricBicycle easyEbike = new ElectricBicycle("Easy Rider", 9, 2.0);
        Helmet helmet = new Helmet("Шлем M");

        parkService.addNewTransport(hardScooter);
        parkService.addNewTransport(mediumBike);
        parkService.addNewTransport(easyEbike);
        parkService.addNewThing(helmet);

        // Act
        List<Transport> beginners = parkService.getTransportForBeginners();

        // Assert
        assertEquals(2, beginners.size(), "Для новичков должно подойти ровно 2 транспортных средства");
        assertTrue(beginners.contains(mediumBike));
        assertTrue(beginners.contains(easyEbike));
        assertFalse(beginners.contains(hardScooter));
    }

    @Test
    @DisplayName("Очистка парка обнуляет счетчик энергии и список объектов")
    void clearPark_resetsItemsAndEnergy() {
        // Arrange
        ServiceCenter serviceCenterMock = mock(ServiceCenter.class);
        when(serviceCenterMock.inspect(any(Transport.class))).thenReturn(true);
        ParkService parkService = new ParkService(serviceCenterMock);

        parkService.addNewTransport(new Scooter("Ninebot", 7, 2.0));
        parkService.addNewThing(new Charger("Станция", 5.0));

        // Act
        parkService.clearPark();

        // Assert
        assertEquals(0, parkService.getItemsCount());
        assertEquals(0.0, parkService.getAllEnergyUsagePerDay(), 0.001);
    }

    @Test
    @DisplayName("Печать элементов отрабатывает корректно для пустого и заполненного парка")
    void printItems_executesWithoutError() {
        // Arrange
        ServiceCenter serviceCenterMock = mock(ServiceCenter.class);
        ParkService parkService = new ParkService(serviceCenterMock);

        // Act & Assert (empty park)
        parkService.printItems();
        assertEquals(0, parkService.getItemsCount());

        // Arrange (populated park)
        when(serviceCenterMock.inspect(any(Transport.class))).thenReturn(true);
        parkService.addNewTransport(new Bicycle("Author", 8));
        parkService.addNewThing(new Helmet("Шлем L"));

        // Act & Assert (populated park)
        parkService.printItems();
        assertEquals(2, parkService.getItemsCount());
    }

    @Test
    @DisplayName("При попытке добавить null-транспорт выбрасывается ApplicationException")
    void addNewTransport_whenNull_throwsApplicationException() {
        // Arrange
        ServiceCenter serviceCenterMock = mock(ServiceCenter.class);
        ParkService parkService = new ParkService(serviceCenterMock);

        // Act & Assert
        studying.exception.ApplicationException ex = org.junit.jupiter.api.Assertions.assertThrows(
                studying.exception.ApplicationException.class,
                () -> parkService.addNewTransport(null),
                "Ожидается исключение при передаче null"
        );
        assertEquals(studying.exception.ApplicationErrorCode.VALIDATION_ERROR, ex.getCode());
    }

    @Test
    @DisplayName("При попытке добавить null-вещь выбрасывается ApplicationException")
    void addNewThing_whenNull_throwsApplicationException() {
        // Arrange
        ServiceCenter serviceCenterMock = mock(ServiceCenter.class);
        ParkService parkService = new ParkService(serviceCenterMock);

        // Act & Assert
        studying.exception.ApplicationException ex = org.junit.jupiter.api.Assertions.assertThrows(
                studying.exception.ApplicationException.class,
                () -> parkService.addNewThing(null),
                "Ожидается исключение при передаче null"
        );
        assertEquals(studying.exception.ApplicationErrorCode.VALIDATION_ERROR, ex.getCode());
    }
}
