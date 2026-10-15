package studying;

import java.util.List;
import java.util.Scanner;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import studying.configuration.ApplicationConfiguration;
import studying.exception.ApplicationException;
import studying.service.ParkService;
import studying.service.Transport;
import studying.service.impl.Bicycle;
import studying.service.impl.Charger;
import studying.service.impl.DockStation;
import studying.service.impl.ElectricBicycle;
import studying.service.impl.Helmet;
import studying.service.impl.Scooter;

public class Main {
    public static void main(String[] args) {
        var context = new AnnotationConfigApplicationContext(ApplicationConfiguration.class);
        var parkService = context.getBean(ParkService.class);

        printBanner();

        if (args.length > 0 && args[0].equalsIgnoreCase("--demo")) {
            runAutomatedDemo(parkService);
            return;
        }

        runInteractiveCli(parkService);
    }

    private static void printBanner() {
        System.out.println("==========================================================");
        System.out.println("        Добро пожаловать в сервис кикшеринга «ВышКат»      ");
        System.out.println("                   «Доедем до дедлайна»                   ");
        System.out.println("==========================================================");
        printHelp();
    }

    private static void printHelp() {
        System.out.println("\nДоступные команды:");
        System.out.println("  1. add-scooter <название> <простота 1-10> <кВт*ч>  — Добавить электросамокат");
        System.out.println("  2. add-bike <название> <простота 1-10>             — Добавить обычный велосипед");
        System.out.println("  3. add-ebike <название> <простота 1-10> <кВт*ч>    — Добавить электровелосипед");
        System.out.println("  4. add-helmet <название>                           — Поставить на баланс шлем");
        System.out.println("  5. add-station <название> <кВт*ч>                  — Добавить док-станцию");
        System.out.println("  6. add-charger <название> <кВт*ч>                  — Добавить зарядный шкаф");
        System.out.println("  7. list                                            — Вывести все объекты парка");
        System.out.println("  8. beginners                                       — Список техники для новичков (>=6)");
        System.out.println("  9. energy                                          — Суммарное потребление энергии");
        System.out.println("  10. count                                          — Общее число единиц на балансе");
        System.out.println("  11. demo                                           — Запустить готовый демо-сценарий");
        System.out.println("  12. help                                           — Показать это меню");
        System.out.println("  0. exit                                            — Выход из программы\n");
    }

    private static void runInteractiveCli(ParkService parkService) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("vyshkat> ");
            if (!scanner.hasNextLine()) {
                break;
            }
            String line = scanner.nextLine().trim();
            if (line.isEmpty()) {
                continue;
            }

            String[] tokens = line.split("\\s+");
            String command = tokens[0].toLowerCase();

            switch (command) {
                case "0":
                case "exit":
                    System.out.println("До свидания! Сервис «ВышКат» завершает работу.");
                    return;
                case "12":
                case "help":
                    printHelp();
                    break;
                case "7":
                case "list":
                    System.out.println("\n--- Реестр объектов на балансе парка ---");
                    parkService.printItems();
                    break;
                case "8":
                case "beginners":
                    System.out.println("\n--- Техника для новичков (простота >= 6) ---");
                    List<Transport> beginners = parkService.getTransportForBeginners();
                    if (beginners.isEmpty()) {
                        System.out.println("Нет подходящей техники для новичков.");
                    } else {
                        beginners.forEach(t -> System.out.println("• " + t));
                    }
                    break;
                case "9":
                case "energy":
                    System.out.printf("Суммарный суточный расход энергии: %.2f кВт·ч\n",
                            parkService.getAllEnergyUsagePerDay());
                    break;
                case "10":
                case "count":
                    System.out.println("Всего единиц на балансе: " + parkService.getItemsCount());
                    break;
                case "11":
                case "demo":
                    runAutomatedDemo(parkService);
                    break;
                case "1":
                case "add-scooter":
                    handleAddScooter(tokens, parkService);
                    break;
                case "2":
                case "add-bike":
                    handleAddBike(tokens, parkService);
                    break;
                case "3":
                case "add-ebike":
                    handleAddElectricBike(tokens, parkService);
                    break;
                case "4":
                case "add-helmet":
                    handleAddHelmet(tokens, parkService);
                    break;
                case "5":
                case "add-station":
                    handleAddStation(tokens, parkService);
                    break;
                case "6":
                case "add-charger":
                    handleAddCharger(tokens, parkService);
                    break;
                default:
                    System.out.println("Неизвестная команда. Введите 'help' для списка доступных команд.");
            }
        }
    }

    private static void handleAddScooter(String[] tokens, ParkService parkService) {
        if (tokens.length < 4) {
            System.out.println("Использование: add-scooter <название> <простота 1-10> <кВт*ч>");
            return;
        }
        try {
            String name = tokens[1];
            int simplicity = Integer.parseInt(tokens[2]);
            double energy = Double.parseDouble(tokens[3]);
            Scooter scooter = new Scooter(name, simplicity, energy);
            tryAddTransport(parkService, scooter);
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: простота и расход энергии должны быть числами.");
        } catch (ApplicationException e) {
            System.out.println("Ошибка [" + e.getCode() + "]: " + e.getMessage());
        }
    }

    private static void handleAddBike(String[] tokens, ParkService parkService) {
        if (tokens.length < 3) {
            System.out.println("Использование: add-bike <название> <простота 1-10>");
            return;
        }
        try {
            String name = tokens[1];
            int simplicity = Integer.parseInt(tokens[2]);
            Bicycle bike = new Bicycle(name, simplicity);
            tryAddTransport(parkService, bike);
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: простота должна быть числом.");
        } catch (ApplicationException e) {
            System.out.println("Ошибка [" + e.getCode() + "]: " + e.getMessage());
        }
    }

    private static void handleAddElectricBike(String[] tokens, ParkService parkService) {
        if (tokens.length < 4) {
            System.out.println("Использование: add-ebike <название> <простота 1-10> <кВт*ч>");
            return;
        }
        try {
            String name = tokens[1];
            int simplicity = Integer.parseInt(tokens[2]);
            double energy = Double.parseDouble(tokens[3]);
            ElectricBicycle ebike = new ElectricBicycle(name, simplicity, energy);
            tryAddTransport(parkService, ebike);
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: параметры должны быть числами.");
        } catch (ApplicationException e) {
            System.out.println("Ошибка [" + e.getCode() + "]: " + e.getMessage());
        }
    }

    private static void handleAddHelmet(String[] tokens, ParkService parkService) {
        if (tokens.length < 2) {
            System.out.println("Использование: add-helmet <название>");
            return;
        }
        try {
            String name = tokens[1];
            Helmet helmet = new Helmet(name);
            parkService.addNewThing(helmet);
            System.out.println("Вещь поставлена на баланс: " + helmet);
        } catch (ApplicationException e) {
            System.out.println("Ошибка [" + e.getCode() + "]: " + e.getMessage());
        }
    }

    private static void handleAddStation(String[] tokens, ParkService parkService) {
        if (tokens.length < 3) {
            System.out.println("Использование: add-station <название> <кВт*ч>");
            return;
        }
        try {
            String name = tokens[1];
            double energy = Double.parseDouble(tokens[2]);
            DockStation station = new DockStation(name, energy);
            parkService.addNewThing(station);
            System.out.println("Док-станция поставлена на баланс: " + station);
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: расход энергии должен быть числом.");
        } catch (ApplicationException e) {
            System.out.println("Ошибка [" + e.getCode() + "]: " + e.getMessage());
        }
    }

    private static void handleAddCharger(String[] tokens, ParkService parkService) {
        if (tokens.length < 3) {
            System.out.println("Использование: add-charger <название> <кВт*ч>");
            return;
        }
        try {
            String name = tokens[1];
            double energy = Double.parseDouble(tokens[2]);
            Charger charger = new Charger(name, energy);
            parkService.addNewThing(charger);
            System.out.println("Зарядный шкаф поставлен на баланс: " + charger);
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: расход энергии должен быть числом.");
        } catch (ApplicationException e) {
            System.out.println("Ошибка [" + e.getCode() + "]: " + e.getMessage());
        }
    }

    private static void tryAddTransport(ParkService parkService, Transport transport) {
        System.out.println("Отправляем на техосмотр: " + transport.getName() + "...");
        boolean accepted = parkService.addNewTransport(transport);
        if (accepted) {
            System.out.println("✅ Техосмотр ПРОЙДЕН! Транспорт принят на баланс.");
        } else {
            System.out.println("❌ Техосмотр ОТКЛОНЁН! Транспорт не допущен в активный парк.");
        }
    }

    private static void runAutomatedDemo(ParkService parkService) {
        System.out.println("\n================ ДЕМОНСТРАЦИОННЫЙ СЦЕНАРИЙ ================");
        System.out.println("1. Приём вещей на баланс (шлемы, зарядные шкафы, док-станции):");
        parkService.addNewThing(new Helmet("Шлем_HSE_Yellow"));
        parkService.addNewThing(new Charger("Шкаф_Зарядный_Корпус_Покровка", 15.0));
        parkService.addNewThing(new DockStation("ДокСтанция_Шаболовка", 5.5));
        System.out.println("   -> Вещи успешно добавлены на баланс.");

        System.out.println("\n2. Приём транспорта через техосмотр:");
        Scooter scooter1 = new Scooter("Ninebot_Pro", 8, 2.5);
        Scooter scooter2 = new Scooter("SpeedDemon_Extreme", 3, 4.0);
        Bicycle bike = new Bicycle("Forward_Classic", 9);
        ElectricBicycle ebike = new ElectricBicycle("ElectroBike_City", 6, 3.2);

        tryAddTransport(parkService, scooter1);
        tryAddTransport(parkService, scooter2);
        tryAddTransport(parkService, bike);
        tryAddTransport(parkService, ebike);

        System.out.println("\n3. Полный реестр имущества на балансе:");
        parkService.printItems();

        System.out.println("\n4. Техника, доступная для новичков (простота >= 6):");
        List<Transport> forBeginners = parkService.getTransportForBeginners();
        forBeginners.forEach(t -> System.out.println("   • " + t));

        System.out.printf("\n5. Суммарный расход энергии активного парка: %.2f кВт·ч\n",
                parkService.getAllEnergyUsagePerDay());
        System.out.println("   (Учитываются только электросамокаты, электровелосипеды и зарядные станции)");

        System.out.println("================ КОНЕЦ ДЕМОНСТРАЦИИ ================");
        parkService.clearPark();
        System.out.println("🧹 Парк успешно очищен после демонстрации.\n");
    }
}
