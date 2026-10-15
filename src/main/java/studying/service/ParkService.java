package studying.service;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import studying.exception.ApplicationErrorCode;
import studying.exception.ApplicationException;

public class ParkService {
    private List<InventoryItem> items = new ArrayList<>();

    @Getter
    private double allEnergyUsagePerDay;
    private ServiceCenter serviceCenter;

    public ParkService(ServiceCenter serviceCenter) {
        this.serviceCenter = serviceCenter;
    }

    public boolean addNewTransport(Transport transport) {
        if (transport == null) {
            throw new ApplicationException(ApplicationErrorCode.VALIDATION_ERROR, "Транспорт не может быть null");
        }
        if (serviceCenter.inspect(transport)) {
            items.add(transport);
            if (transport instanceof EnergyConsumer) {
                allEnergyUsagePerDay += ((EnergyConsumer) transport).getEnergyUsagePerDay();
            }
            return true;
        }

        return false;
    }

    public void addNewThing(Thing thing) {
        if (thing == null) {
            throw new ApplicationException(ApplicationErrorCode.VALIDATION_ERROR, "Вещь не может быть null");
        }
        items.add(thing);
        if (thing instanceof EnergyConsumer) {
            allEnergyUsagePerDay += ((EnergyConsumer) thing).getEnergyUsagePerDay();
        }
    }

    public void printItems() {
        if (items.isEmpty()) {
            System.out.println("Парк пуст.");
            return;
        }
        items.forEach(item -> System.out.println("• [" + item.getId() + "] " + item.getName() + " -> " + item));
    }

    public int getItemsCount() {
        return items.size();
    }

    public List<Transport> getTransportForBeginners() {
        List<Transport> beginnersTransport = new ArrayList<>();
        for (InventoryItem item : items) {
            if (item instanceof Transport transport && transport.getSimplicity() >= 6) {
                beginnersTransport.add(transport);
            }
        }
        return beginnersTransport;
    }

    public void clearPark() {
        items.clear();
        allEnergyUsagePerDay = 0.0;
    }
}
