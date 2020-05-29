import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Основная коллекция транпорта.
 * @author Arsus1
 * @version 1.0
 */
public class VehicleCollection {
    private Vector<Vehicle> vehicles;
    private final java.time.LocalDate creationDate;
    private DateTimeFormatter dateFormatter;
    private Set<Long> vehicleIds;

    private long maxId;

    VehicleCollection(File f) throws IOException, ParserConfigurationException, SAXException, ParseException {
        dateFormatter = DateTimeFormatter.ofPattern("uuuu-MM-dd");
        vehicles = new Vector<>();
        creationDate = LocalDate.now();
        vehicleIds = new TreeSet<>();

        maxId = 0;

        DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document document = documentBuilder.parse(f.toString());

        NodeList xmlVehicles = document.getElementsByTagName("vehicle");

        for (int i = 0; i < xmlVehicles.getLength(); i++) {
            Element xmlVehicleInfo = (Element) xmlVehicles.item(i);

            long id = Long.parseLong(xmlVehicleInfo.getElementsByTagName("id")
                    .item(0).getTextContent());

            String name = xmlVehicleInfo.getElementsByTagName("name").item(0).getTextContent();

            Element coordinates = (Element) xmlVehicleInfo.getElementsByTagName("coordinates").item(0);

            float x = Float.parseFloat(coordinates.getElementsByTagName("x")
                    .item(0).getTextContent());

            long y = Long.parseLong(coordinates.getElementsByTagName("y")
                    .item(0).getTextContent());

            String date = xmlVehicleInfo.getElementsByTagName("creationDate").item(0).getTextContent();

            float enginePower = Float.parseFloat(xmlVehicleInfo.getElementsByTagName("enginePower")
                    .item(0).getTextContent());

            float capacity = Float.parseFloat(xmlVehicleInfo.getElementsByTagName("capacity")
                    .item(0).getTextContent());

            double distanceTravelled = Double.parseDouble(xmlVehicleInfo.getElementsByTagName("distanceTravelled")
                    .item(0).getTextContent());

            FuelType fuelType = FuelType.parseFuelType(xmlVehicleInfo.getElementsByTagName("fuelType")
                    .item(0).getTextContent());

            Vehicle vehicle = new Vehicle(
                    id,
                    name,
                    new Coordinates(x, y),
                    enginePower,
                    capacity,
                    distanceTravelled,
                    fuelType
            );

            vehicle.setCreationDate(LocalDate.parse(date, dateFormatter));

            vehicles.add(vehicle);

            maxId = Math.max(maxId, id);
            vehicleIds.add(id);
        }
    }

    /**Получение нового id
     * @return long
     * */
    public long getNewId() {
        return ++maxId;
    }

    /**Проверка id
     * @return boolean
     * */
    public boolean checkId(long id) {
        return vehicleIds.contains(id);
    }

    /**Порядковый номер
     * @return long
     * */
    public long numId() {
        return vehicleIds.size();
    }

    /**Информация
     * @return String
     * */
    public String info() {
        return "Vehicle collection: \n" +
                "Creation date: " + creationDate.format(dateFormatter) + "\n" +
                "Number of elements: " + vehicles.size();
    }

    /**XML
     * return String
     * */
    public String toXML() {
        StringBuilder build = new StringBuilder();
        build.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n");
        build.append("<vehicles>\n");
        for (Vehicle vehicle : vehicles) {
            build.append(vehicle.toXML()).append("\n");
        }
        build.append("</vehicles>\n");
        return build.toString();
    }

    /**Show
     * @return String
     * */
    public String show() {
        StringBuilder build = new StringBuilder();
        build.append("Collection: \n");
        for (Vehicle v : vehicles) {
            build.append(v.toString()).append("\n");
        }

        if (vehicles.size() == 0) {
            build.append("empty :(");
        }
        return build.toString();
    }

    /**Добавление
     * */
    public void add(Vehicle vehicle) {
        vehicles.add(vehicle);
        vehicleIds.add(vehicle.getId());
    }

    /**Очищение
     * */
    public void clear() {
        vehicles.clear();
        vehicleIds.clear();
        maxId = 0;
    }

    /**Обновление Id
     * @param id индекс
     * @param vehicle транспортное средство
     * */
    public void updateId(long id, Vehicle vehicle) {
        for (int i = 0; i < vehicles.size(); i++) {
            if (vehicles.get(i).getId() == id) {
                vehicles.remove(i);
                vehicles.add(i, vehicle);
                return;
            }
        }

        // NOT FOUND ID
    }

    /**Удаление по индексу
     * @param id индекс
     * @throws NoSuchElementException исключение элемента
     * */
    public void removeById(long id) {
        for (int i = 0; i < vehicles.size(); i++) {
            if (vehicles.get(i).getId() == id) {
                vehicles.remove(i);
                vehicleIds.remove(id);
                return;
            }
        }
        throw new NoSuchElementException();
        // NOT FOUND ID
    }

    /**Сохранение
     * @throws FileNotFoundException файл не найден
     * */
    public void save(File f) throws FileNotFoundException {
        PrintWriter writer = new PrintWriter(f);
        writer.write(toXML());
        writer.close();
    }

    /**Удаление индекса
     * @param index индекс
     * */
    public void removeAtIndex(int index) {
        if (index < vehicles.size() && index >= 0) {
            vehicleIds.remove(vehicles.get(index).getId());
            vehicles.remove(index);
        }
        throw new IndexOutOfBoundsException();
    }

    /**Объём двигателя
     * @param vehicle Объём двигателя
     * */
    public void removeGreaterCapacity(Vehicle vehicle) {
        List<Vehicle> vehicles1 = vehicles.stream().sorted().collect(Collectors.toList());
        vehicles1.forEach(x -> {
            if (x.compareTo(vehicle) > 0) {
                vehicles.remove(x);
                vehicleIds.remove(x.getId());
            }
        });
    }

    /**Сортировка по мощности двигателя
     * */
    public void sortByEnginePower() {
        vehicles.sort((o1, o2) -> (int) (o1.getEnginePower() - o2.getEnginePower()));
    }

    /**Работа с коллекцией
     * @param capacity Объём двигателя
     * */
    public void removeAllByCapacity(float capacity) {
        List<Vehicle> vehicles1 = vehicles.stream().sorted().collect(Collectors.toList());
        vehicles1.forEach(x -> {
            if (x.getCapacity() == capacity) {
                vehicles.remove(x);
                vehicleIds.remove(x.getId());
            }
        });
    }

    /**Тип топлива
     * @return long Тип топлива
     * */
    public long countByFuelType(FuelType type) {
        return vehicles.stream().filter(x -> x.getFuelType() == type).count();
    }

    /**Объём двигателя
     * @param capacity Объём двигателя
     * */
    public String filterLessThanCapacity(float capacity) {
        StringBuilder builder = new StringBuilder();
        List<Vehicle> vehicles1 = vehicles.stream().filter(x -> x.getCapacity() < capacity).collect(Collectors.toList());
        for (Vehicle vehicle : vehicles1) {
            builder.append(vehicle.toString()).append("\n");
        }
        return builder.toString();
    }

}

// info OK
// show OK
// add OK?
// upd_id OK?
// remove_by_id OK?
// save OK
// remove_at_index OK?
// remove_greater OK
// sort OK
// remove_all_by_capacity OK
// count_by_fuel_type OK
// filter_less_than_capacity OK