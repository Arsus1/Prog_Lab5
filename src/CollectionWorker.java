import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.text.ParseException;
import java.util.*;

/**
 * Обработчик команд, для работы с коллекцией.
 *
 * @author Arsus1
 * @version 1.0
 */
public class CollectionWorker {

    /**
     * Коллекция
     */
    private VehicleCollection collection;

    /**
     * <p>Максимальное значение для параметров типа float.</p>
     */
    private final float MAX_F = 940f;

    /**
     * <p>Максимальное значение для параметров типа long.</p>
     */
    private final long MAX_L = 940L;

    /**
     * <p>Минимальное значение для параметров типа long.</p>
     */
    private final long MIN_L = 0L;

    /**
     * <p>Минимальное значение для параметров типа float.</p>
     */
    private final float MIN_F = 0f;

    /**
     * <p>Минимальное значение для параметров типа double</p>
     */
    private final double MIN_D = 0d;

    /**
     * Действия при ошибках
     */
    private final String TRY_AGAIN = "Повторите ввод!";

    private final String EMPTY_COLLECTION = "Коллекция пуста :(";
    /**
     * Список команд доступных для вызова
     */
    private final Set<String> validCommands;
    /**
     * Файл из которого загружается коллекция
     */
    File f;
    ArrayList<String> executing_scripts;

    /**
     * Сигнализирование об Exception's
     */
    CollectionWorker(File f) {
        try {
            collection = new VehicleCollection(f);
            this.f = f;
            executing_scripts = new ArrayList<>();
        } catch (IOException | ParserConfigurationException | SAXException | ParseException e) {
            System.out.println("Некорректный файл!");
            System.exit(0);
        }
        validCommands = new TreeSet<>();
        String valid = "help info show add update remove_by_id clear save execute_script exit remove_at remove_greater sort remove_all_by_capacity count_by_fuel_type filter_less_than_capacity";
        validCommands.addAll(Arrays.asList(valid.split(" ")));
    }

    /**
     * Информация о командах \help
     *
     * @return String
     */
    public String Help() {
        return "Help info: \n" +
                "help : вывести справку по доступным командам\n" +
                "info : вывести в стандартный поток вывода информацию о коллекции (тип, дата инициализации, количество элементов и т.д.)\n" +
                "show : вывести в стандартный поток вывода все элементы коллекции в строковом представлении\n" +
                "add {element} : добавить новый элемент в коллекцию\n" +
                "update id {element} : обновить значение элемента коллекции, id которого равен заданному\n" +
                "remove_by_id id : удалить элемент из коллекции по его id\n" +
                "clear : очистить коллекцию\n" +
                "save : сохранить коллекцию в файл\n" +
                "execute_script file_name : считать и исполнить скрипт из указанного файла. В скрипте содержатся команды в таком же виде, в котором их вводит пользователь в интерактивном режиме.\n" +
                "exit : завершить программу (без сохранения в файл)\n" +
                "remove_at index : удалить элемент, находящийся в заданной позиции коллекции (index)\n" +
                "remove_greater {element} : удалить из коллекции все элементы, превышающие заданный\n" +
                "sort : отсортировать коллекцию в естественном порядке\n" +
                "remove_all_by_capacity capacity : удалить из коллекции все элементы, значение поля capacity которого эквивалентно заданному\n" +
                "count_by_fuel_type fuelType : вывести количество элементов, значение поля fuelType которых равно заданному\n" +
                "filter_less_than_capacity capacity : вывести элементы, значение поля capacity которых меньше заданного";
    }

    /**
     * Завершение работы программы
     */
    public void exit() {
        System.exit(0);
    }

    /**
     * Запуск скрипта
     *
     * @param fname Название скрипта
     *              Сигнализирование о неверном пути
     */
    public void executeScript(String fname) {
        try {
            FileInputStream fileInputStream = new FileInputStream(new File(fname));
            processCommands(fileInputStream);
        } catch (FileNotFoundException e) {
            System.out.println("Неправильно указан путь! Попробуйте снова :(");
        }
    }

    /**
     * <p>Проверка, введено ли корректное значение в поле.</p>
     *
     * @param scanner Данные из потока ввода.
     * @param ID      Берётся ID коллекции.
     * @return Vehicle, если считывание выполнено успешно или null, если ошибка.
     */
    private Vehicle readVehicle(Scanner scanner, Long ID) {
        try {
            String name = scanner.next();

            float x = readFloatBounds(scanner, "Некорректное значение поля x. " + TRY_AGAIN, MIN_F, MAX_F);

            long y = readLongBounds(scanner, "Некорректное значение поля y. " + TRY_AGAIN, MIN_L, MAX_L);

            float enginePower = readFloatBounds(scanner, "Некорректное значение поля enginePower. " + TRY_AGAIN, 0, Float.MAX_VALUE);

            float capacity = readFloatBounds(scanner, "Некорректное значение поля capacity. " + TRY_AGAIN, 0, Float.MAX_VALUE);

            double distanceTravelled = readDoubleBounds(scanner, "Некорректное значение поля distanceTravelled. " + TRY_AGAIN, 0, Double.MAX_VALUE);

            FuelType fuelType = readFuelType(scanner, "Некорректное значение поля fuelType. " + TRY_AGAIN);

            long id;

            if (ID != null) {
                id = ID;
            } else {
                id = collection.getNewId();
            }

            return new Vehicle(
                    id,
                    name,
                    new Coordinates(x, y),
                    enginePower,
                    capacity,
                    distanceTravelled,
                    fuelType
            );
        } catch (NumberFormatException | NoSuchElementException e) {
            System.out.println("Введены некорректные данные! Введите корректные данные");
        }
        return null;
    }

    /**
     * <p>Считывание до тех пор, пока он не будет введен корректно.</p>
     *
     * @param scanner Данные из потока ввода.
     * @param err     Текст ошибки.
     * @return dLong, если считывание выполнено успешно или null, если ошибка.
     */
    private Long readLong(Scanner scanner, String err) {
        long out;
        try {
            while (true) {
                try {
                    out = Long.parseLong(scanner.next());
                    return out;
                } catch (NumberFormatException e) {
                    System.out.println(err);
                }
            }
        } catch (NoSuchElementException e1) {
            System.out.println("Введены некорректные данные! Введите корректные данные");
        }
        return null;
    }

    /**
     * <p>Считывание параметра, до тех пор, пока он не будет введён корректно и определение, находится ли он в допустимом диапазоне</p>
     *
     * @param scanner Данные из потока ввода.
     * @param err     Текст ошибки.
     * @param min     Минимально допустимое значение.
     * @param max     Максимально допустимое значение.
     * @return Long, если считывание выполнено успешно или null, если ошибка.
     */
    private Long readLongBounds(Scanner scanner, String err, long min, long max) {
        long out;
        try {
            while (true) {
                try {
                    out = Long.parseLong(scanner.next());
                    if (out > max || out < min) {
                        throw new NumberFormatException();
                    }
                    return out;
                } catch (NumberFormatException e) {
                    System.out.println(err);
                }
            }
        } catch (NoSuchElementException e1) {
            System.out.println("Введены некорректные данные! Введите корректные данные");
        }
        return null;
    }

    /**
     * <p>Считывание параметра, до тех пор, пока он не будет введён корректно и определение, находится ли он в допустимом диапазоне</p>
     *
     * @param scanner  Данные из потока ввода.
     * @param err      Текст ошибки.
     * @param contains Параметр, указывающий на то, предполагается ли нахождение в коллекции.
     * @return Long, если считывание выполнено успешно или null, если ошибка.
     */
    private Long readId(Scanner scanner, String err, boolean contains) {
        long out;
        try {
            while (true) {
                try {
                    out = Long.parseLong(scanner.next());
                    if (contains && collection.numId() == 0) {
                        System.out.println(EMPTY_COLLECTION);
                        return null;
                    }
                    if ((contains && !collection.checkId(out) || (!contains && collection.checkId(out)))) {
                        throw new NumberFormatException();
                    }
                    return out;
                } catch (NumberFormatException e) {
                    System.out.println(err);
                }
            }
        } catch (NoSuchElementException e1) {
            System.out.println("Введены некорректные данные! Введите корректные данные");
        }
        return null;
    }
    /*
    предп сод, не сод
    предп не сод, сод
     */

    /**
     * <p>Считывание параметра, до тех пор, пока он не будет введён корректно.</p>
     *
     * @param scanner Данные из потока ввода.
     * @param err     Текст ошибки.
     * @return Float, если считывание выполнено успешно или null, если ошибка.
     */
    private Float readFloat(Scanner scanner, String err) {
        float out;
        try {
            while (true) {
                try {
                    out = Float.parseFloat(scanner.next());
                    return out;
                } catch (NumberFormatException e) {
                    System.out.println(err);
                }
            }
        } catch (NoSuchElementException e1) {
            System.out.println("Введены некорректные данные! Введите корректные данные");
        }
        return null;
    }

    /**
     * <p>Считывание параметра, до тех пор, пока он не будет введён корректно, проверка допустимости значения.</p>
     *
     * @param scanner Данные из потока ввода.
     * @param err     Текст ошибки.
     * @param min     Минимально возможное значение.
     * @param max     Максимально возможное значение.
     * @return Float, если считывание выполнено успешно или null, если ошибка.
     */
    private Float readFloatBounds(Scanner scanner, String err, float min, float max) {
        float out;
        try {
            while (true) {
                try {
                    out = Float.parseFloat(scanner.next());
                    if (out > max || out < min) {
                        throw new NumberFormatException();
                    }
                    return out;
                } catch (NumberFormatException e) {
                    System.out.println(err);
                }
            }
        } catch (NoSuchElementException e1) {
            System.out.println("Введены некорректные данные! Введите корректные данные");
        }
        return null;
    }

    /**
     * <p>Считывание параметра, до тех пор, пока он не будет введён корректно.</p>
     *
     * @param scanner Данные из потока ввода.
     * @param err     Текст ошибки.
     * @return Integer, если считывание выполнено успешно или null, если ошибка.
     */
    private Integer readInteger(Scanner scanner, String err) {
        int out;
        try {
            while (true) {
                try {
                    out = Integer.parseInt(scanner.next());
                    return out;
                } catch (NumberFormatException e) {
                    System.out.println(err);
                }
            }
        } catch (NoSuchElementException e1) {
            System.out.println("Введены некорректные данные! Введите корректные данные");
        }
        return null;
    }

    /**
     * <p>Считывание параметра, до тех пор, пока он не будет введён корректно.</p>
     *
     * @param scanner Данные из потока ввода.
     * @param err     Текст ошибки.
     * @return FuelType, если считывание выполнено успешно или null, если ошибка.
     */
    private FuelType readFuelType(Scanner scanner, String err) {
        FuelType out;
        try {
            while (true) {
                try {
                    out = FuelType.parseFuelType(scanner.next());
                    return out;
                } catch (NumberFormatException e) {
                    System.out.println(err);
                }
            }
        } catch (NoSuchElementException e1) {
            System.out.println("Введены некорректные данные! Введите корректные данные");
        }
        return null;
    }

    /**
     * <p>Считывание параметра, до тех пор, пока он не будет введён корректно.</p>
     *
     * @param scanner Данные из потока ввода.
     * @param err     Текст ошибки.
     * @return Double, если считывание выполнено успешно или null, если ошибка.
     */
    private Double readDouble(Scanner scanner, String err) {
        double out;
        try {
            while (true) {
                try {
                    out = Double.parseDouble(scanner.next());
                    return out;
                } catch (NumberFormatException e) {
                    System.out.println(err);
                }
            }
        } catch (NoSuchElementException e1) {
            System.out.println("Введены некорректные данные! Введите корректные данные");
        }
        return null;
    }

    /**
     * Считывание параметра, до тех пор, пока он не будет введён корректно, проверка допустимости значения.
     *
     * @param scanner Данные из потока ввода.
     * @param err     Текст ошибки.
     * @param min     Минимально возможное значение.
     * @param max     Максимально возможное значение.
     * @return Double, если считывание выполнено успешно или null, если ошибка.
     */
    private Double readDoubleBounds(Scanner scanner, String err, double min, double max) {
        double out;
        try {
            while (true) {
                try {
                    out = Double.parseDouble(scanner.next());
                    if (out > max || max < min) {
                        throw new NumberFormatException();
                    }
                    return out;
                } catch (NumberFormatException e) {
                    System.out.println(err);
                }
            }
        } catch (NoSuchElementException e1) {
            System.out.println("Введены некорректные данные! Введите корректные данные");
        }
        return null;
    }

    /**
     * Сигнализирование и вывод сообщений об ошибках.
     *
     * @param stream Входной поток команд
     */
    public void processCommands(InputStream stream) {
        Scanner scanner = new Scanner(stream);
        while (scanner.hasNext()) {
            String command = scanner.next();

            if (validCommands.contains(command)) {

                if (command.equals("help")) {
                    System.out.println(Help());
                }
                if (command.equals("exit")) {
                    exit();
                }

                if (command.equals("info")) {
                    System.out.println(collection.info());
                }

                if (command.equals("execute_script")) {
                    //Expecting 1 param
                    try {
                        String fname = scanner.next();
                        if (!executing_scripts.contains(fname)) {
                            executing_scripts.add(fname);
                            executeScript(fname);
                        } else {
                            System.out.println("Обнаружена рекурсия! Невозможно запустить скрипт!");
                            executing_scripts.clear();
                        }
                    } catch (NoSuchElementException e) {
                        System.out.println("Введены некорректные данные! Введите корректные данные");
                    }
                    executing_scripts.clear();
                }

                if (command.equals("show")) {
                    System.out.println(collection.show());
                }

                if (command.equals("add")) {
                    // Expecting 7 params
                    Vehicle vehicle = readVehicle(scanner, null);
                    if (vehicle != null) {
                        collection.add(vehicle);
                    }
                }

                if (command.equals("update")) {
                    Long id = readId(scanner, "Введен некорректный id! " + TRY_AGAIN, true);
                    if (id != null) {
                        collection.updateId(id, readVehicle(scanner, id));
                    } else {
                        System.out.println("Invalid arguments! Try again :(");
                    }
                }

                if (command.equals("remove_by_id")) {
                    Long id = readId(scanner, "Введен некорректный id! " + TRY_AGAIN, true);
                    if (id != null) {
                        collection.removeById(id);
                    } else {
                        System.out.println("Введены некорректные данные! Введите корректные данные");
                    }
                }

                if (command.equals("clear")) {
                    collection.clear();
                }

                if (command.equals("save")) {
                    try {
                        collection.save(f);
                    } catch (FileNotFoundException e1) {
                        System.out.println("Ошибка: Невозможно сохранить коллекцию!");
                    }
                }

                if (command.equals("remove_at")) {
                    Integer index = readInteger(scanner, "Некорректный индекс! " + TRY_AGAIN);
                    if (index != null) {
                        collection.removeAtIndex(index);
                    } else {
                        System.out.println("Введены некорректные данные! Введите корректные данные");
                    }
                }

                if (command.equals("remove_greatest")) {
                    try {
                        Vehicle vehicle = readVehicle(scanner, -1L);
                        if (vehicle != null) {
                            collection.removeGreaterCapacity(vehicle);
                        }
                    } catch (NumberFormatException | IndexOutOfBoundsException e) {
                        System.out.println("Введены некорректные данные! Введите корректные данные");
                    }
                }

                if (command.equals("sort")) {
                    collection.sortByEnginePower();
                }

                if (command.equals("remove_all_by_capacity")) {
                    Float capacity = readFloatBounds(scanner, "Некорректное значение поля capacity! " + TRY_AGAIN, 0, Float.MAX_VALUE);
                    if (capacity != null) {
                        collection.removeAllByCapacity(capacity);
                    } else {
                        System.out.println("Введены некорректные данные! Введите корректные данные");
                    }
                }

                if (command.equals("count_by_fuel_type")) {
                    FuelType fuelType = readFuelType(scanner, "Некорректное значение fuelType " + TRY_AGAIN);
                    if (fuelType != null) {
                        long num = collection.countByFuelType(fuelType);
                        System.out.println("Количество элементов с типом топлива: " + fuelType + "= " + num);
                    } else {
                        System.out.println("Введены некорректные данные! Введите корректные данные");
                    }
                }

                if (command.equals("filter_less_than_capacity")) {
                    Float capacity = readFloatBounds(scanner, "Некорректное значение поля capacity! " + TRY_AGAIN, 0, Float.MAX_VALUE);
                    if (capacity != null) {
                        System.out.println(collection.filterLessThanCapacity(capacity));
                    } else {
                        System.out.println("Введены некорректные данные! Введите корректные данные");
                    }
                }

            } else {
                System.out.println("Введена не существующая команда, введите help для просмотра доступных команд.");
            }
        }
    }
}

// help
// execute_script
// exit
