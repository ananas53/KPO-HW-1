package studying.service;

import java.util.concurrent.ThreadLocalRandom;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ServiceCenter {
    private static final double PASS_PROBABILITY = 0.8;

    public boolean inspect(Transport transport) {
        return ThreadLocalRandom.current().nextDouble() < PASS_PROBABILITY;
    }
}
