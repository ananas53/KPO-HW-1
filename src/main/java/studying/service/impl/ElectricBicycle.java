package studying.service.impl;

import java.util.UUID;
import lombok.Getter;
import lombok.ToString;
import studying.exception.ApplicationErrorCode;
import studying.exception.ApplicationException;
import studying.service.EnergyConsumer;
import studying.service.Transport;

@ToString
@Getter
public class ElectricBicycle implements Transport, EnergyConsumer {
    private final String id;
    private final String name;
    private final int simplicity;
    private final double energyUsagePerDay;

    public ElectricBicycle(String name, int simplicity, double energyUsagePerDay) {
        if (name == null || name.isBlank()) {
            throw new ApplicationException(ApplicationErrorCode.VALIDATION_ERROR, "Название не может быть пустым");
        }
        if (simplicity < 1 || simplicity > 10) {
            throw new ApplicationException(ApplicationErrorCode.VALIDATION_ERROR,
                    "Простота для новичка должна быть в диапазоне от 1 до 10");
        }
        if (energyUsagePerDay < 0) {
            throw new ApplicationException(ApplicationErrorCode.VALIDATION_ERROR,
                    "Расход энергии не может быть отрицательным");
        }
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.simplicity = simplicity;
        this.energyUsagePerDay = energyUsagePerDay;
    }
}
