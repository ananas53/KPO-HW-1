package studying.service.impl;

import java.util.UUID;
import lombok.Getter;
import lombok.ToString;
import studying.exception.ApplicationErrorCode;
import studying.exception.ApplicationException;
import studying.service.EnergyConsumer;
import studying.service.Thing;

@ToString
@Getter
public class Charger implements Thing, EnergyConsumer {
    private final String id;
    private final String name;
    private final double energyUsagePerDay;

    public Charger(String name, double energyUsagePerDay) {
        if (name == null || name.isBlank()) {
            throw new ApplicationException(ApplicationErrorCode.VALIDATION_ERROR, "Название не может быть пустым");
        }
        if (energyUsagePerDay < 0) {
            throw new ApplicationException(ApplicationErrorCode.VALIDATION_ERROR,
                    "Расход энергии не может быть отрицательным");
        }
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.energyUsagePerDay = energyUsagePerDay;
    }
}
