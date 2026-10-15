package studying.service.impl;

import java.util.UUID;
import lombok.Getter;
import lombok.ToString;
import studying.exception.ApplicationErrorCode;
import studying.exception.ApplicationException;
import studying.service.Thing;

@ToString
@Getter
public class Helmet implements Thing {
    private final String id;
    private final String name;

    public Helmet(String name) {
        if (name == null || name.isBlank()) {
            throw new ApplicationException(ApplicationErrorCode.VALIDATION_ERROR, "Название не может быть пустым");
        }
        this.id = UUID.randomUUID().toString();
        this.name = name;
    }
}
