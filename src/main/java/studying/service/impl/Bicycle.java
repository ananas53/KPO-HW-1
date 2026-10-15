package studying.service.impl;

import java.util.UUID;
import lombok.Getter;
import lombok.ToString;
import studying.exception.ApplicationErrorCode;
import studying.exception.ApplicationException;
import studying.service.Transport;

@ToString
@Getter
public class Bicycle implements Transport {
    private final String id;
    private final String name;
    private final int simplicity;

    public Bicycle(String name, int simplicity) {
        if (name == null || name.isBlank()) {
            throw new ApplicationException(ApplicationErrorCode.VALIDATION_ERROR, "Название не может быть пустым");
        }
        if (simplicity < 1 || simplicity > 10) {
            throw new ApplicationException(ApplicationErrorCode.VALIDATION_ERROR,
                    "Простота для новичка должна быть в диапазоне от 1 до 10");
        }
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.simplicity = simplicity;
    }
}
