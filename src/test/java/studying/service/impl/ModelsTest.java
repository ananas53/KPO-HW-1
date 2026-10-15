package studying.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import studying.exception.ApplicationErrorCode;
import studying.exception.ApplicationException;
import studying.service.EnergyConsumer;
import studying.service.InventoryItem;
import studying.service.Thing;
import studying.service.Transport;

@DisplayName("Unit-тесты моделей транспорта и вещей")
class ModelsTest {

    @Test
    @DisplayName("Проверка модели Scooter")
    void testScooter() {
        // Arrange & Act
        Scooter scooter = new Scooter("Ninebot KickScooter", 7, 2.5);

        // Assert
        assertEquals("Ninebot KickScooter", scooter.getName());
        assertNotNull(scooter.getId());
        assertEquals(7, scooter.getSimplicity());
        assertEquals(2.5, scooter.getEnergyUsagePerDay(), 0.001);

        assertInstanceOf(Transport.class, scooter);
        assertInstanceOf(EnergyConsumer.class, scooter);
        assertInstanceOf(InventoryItem.class, scooter);
        assertNotNull(scooter.toString());
        assertTrue(scooter.toString().contains("Ninebot"));
    }

    @Test
    @DisplayName("Проверка модели Bicycle")
    void testBicycle() {
        // Arrange & Act
        Bicycle bike = new Bicycle("Forward Arsenal", 9);

        // Assert
        assertEquals("Forward Arsenal", bike.getName());
        assertNotNull(bike.getId());
        assertEquals(9, bike.getSimplicity());

        assertInstanceOf(Transport.class, bike);
        assertInstanceOf(InventoryItem.class, bike);
        assertNotNull(bike.toString());
    }

    @Test
    @DisplayName("Проверка модели ElectricBicycle")
    void testElectricBicycle() {
        // Arrange & Act
        ElectricBicycle ebike = new ElectricBicycle("Kugoo E-Bike", 6, 3.2);

        // Assert
        assertEquals("Kugoo E-Bike", ebike.getName());
        assertNotNull(ebike.getId());
        assertEquals(6, ebike.getSimplicity());
        assertEquals(3.2, ebike.getEnergyUsagePerDay(), 0.001);

        assertInstanceOf(Transport.class, ebike);
        assertInstanceOf(EnergyConsumer.class, ebike);
        assertNotNull(ebike.toString());
    }

    @Test
    @DisplayName("Проверка модели Helmet")
    void testHelmet() {
        // Arrange & Act
        Helmet helmet = new Helmet("Шлем защитный Yellow");

        // Assert
        assertEquals("Шлем защитный Yellow", helmet.getName());
        assertNotNull(helmet.getId());

        assertInstanceOf(Thing.class, helmet);
        assertInstanceOf(InventoryItem.class, helmet);
        assertNotNull(helmet.toString());
    }

    @Test
    @DisplayName("Проверка модели DockStation")
    void testDockStation() {
        // Arrange & Act
        DockStation station = new DockStation("Док-станция Шаболовка", 5.5);

        // Assert
        assertEquals("Док-станция Шаболовка", station.getName());
        assertNotNull(station.getId());
        assertEquals(5.5, station.getEnergyUsagePerDay(), 0.001);

        assertInstanceOf(Thing.class, station);
        assertInstanceOf(EnergyConsumer.class, station);
        assertNotNull(station.toString());
    }

    @Test
    @DisplayName("Проверка модели Charger")
    void testCharger() {
        // Arrange & Act
        Charger charger = new Charger("Зарядный шкаф Покровка", 12.0);

        // Assert
        assertEquals("Зарядный шкаф Покровка", charger.getName());
        assertNotNull(charger.getId());
        assertEquals(12.0, charger.getEnergyUsagePerDay(), 0.001);

        assertInstanceOf(Thing.class, charger);
        assertInstanceOf(EnergyConsumer.class, charger);
        assertNotNull(charger.toString());
    }

    @Test
    @DisplayName("Конструкторы выбрасывают ApplicationException при некорректных аргументах")
    void constructors_throwApplicationExceptionOnInvalidArgs() {
        // Некорректное имя (null или пустое)
        ApplicationException ex1 = org.junit.jupiter.api.Assertions.assertThrows(
                ApplicationException.class, () -> new Bicycle(null, 5));
        assertEquals(ApplicationErrorCode.VALIDATION_ERROR, ex1.getCode());

        ApplicationException ex2 = org.junit.jupiter.api.Assertions.assertThrows(
                ApplicationException.class, () -> new Helmet("   "));
        assertEquals(ApplicationErrorCode.VALIDATION_ERROR, ex2.getCode());

        // Некорректная простота (< 1 или > 10)
        ApplicationException ex3 = org.junit.jupiter.api.Assertions.assertThrows(
                ApplicationException.class, () -> new Bicycle("Bike", 0));
        assertEquals(ApplicationErrorCode.VALIDATION_ERROR, ex3.getCode());

        ApplicationException ex4 = org.junit.jupiter.api.Assertions.assertThrows(
                ApplicationException.class, () -> new Scooter("Scooter", 11, 2.0));
        assertEquals(ApplicationErrorCode.VALIDATION_ERROR, ex4.getCode());

        ApplicationException ex5 = org.junit.jupiter.api.Assertions.assertThrows(
                ApplicationException.class, () -> new ElectricBicycle("EBike", -2, 2.0));
        assertEquals(ApplicationErrorCode.VALIDATION_ERROR, ex5.getCode());

        // Отрицательный расход энергии (< 0)
        ApplicationException ex6 = org.junit.jupiter.api.Assertions.assertThrows(
                ApplicationException.class, () -> new Scooter("Scooter", 5, -1.0));
        assertEquals(ApplicationErrorCode.VALIDATION_ERROR, ex6.getCode());

        ApplicationException ex7 = org.junit.jupiter.api.Assertions.assertThrows(
                ApplicationException.class, () -> new ElectricBicycle("EBike", 5, -0.1));
        assertEquals(ApplicationErrorCode.VALIDATION_ERROR, ex7.getCode());

        ApplicationException ex8 = org.junit.jupiter.api.Assertions.assertThrows(
                ApplicationException.class, () -> new Charger("Charger", -5.0));
        assertEquals(ApplicationErrorCode.VALIDATION_ERROR, ex8.getCode());

        ApplicationException ex9 = org.junit.jupiter.api.Assertions.assertThrows(
                ApplicationException.class, () -> new DockStation("Dock", -10.0));
        assertEquals(ApplicationErrorCode.VALIDATION_ERROR, ex9.getCode());
    }
}
