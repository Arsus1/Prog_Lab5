import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Основной класс транспорта.
 * @author Arsus1
 * @version 1.0
 */
public class Vehicle implements Comparable {

    /**ID транспортного средства.
     * */
    private Long id;

    /**Имя транспортного средства.
     * */
    private String name;

    /**Координаты транспортного средства.
     * */
    private Coordinates coordinates;

    /**Дата создания.
     * */
    private LocalDate creationDate;

    /**Мощность двигателя.
     * */
    private float enginePower;

    /**Объём двигателя.
     * */
    private Float capacity;

    /**Пробег
     * */
    private Double distanceTravelled;

    /**Тип используемого топлива.
     * */
    private final FuelType fuelType;

    /**Дата создания в коллекции.
     * */
    private DateTimeFormatter dateFormatter;

    /**
     * <p>Параметры транспортного средства</p>
     * @param id ID транспортного средства.
     * @param coordinates Координаты.
     * @param enginePower Мощность двигателя.
     * @param capacity Объём двигателя.
     * @param distanceTravelled Пробег.
     * @param fuelType Тип топлива.
     */
    Vehicle(Long id, String name, Coordinates coordinates, float enginePower, Float capacity
            , Double distanceTravelled, FuelType fuelType) throws NumberFormatException {
        this.name = name;
        this.coordinates = coordinates;
        this.enginePower = enginePower;
        this.capacity = capacity;
        this.distanceTravelled = distanceTravelled;
        this.fuelType = fuelType;

        this.id = id;

        creationDate = LocalDate.now();

        dateFormatter = DateTimeFormatter.ofPattern("uuuu-MM-dd");

        if (!(coordinates.getX() <= 940 && coordinates.getY() <= 407 && id != null && name != null && name.length() > 0 && enginePower > 0 && capacity > 0 && distanceTravelled > 0)) {
            throw new NumberFormatException();
        }
    }

    /**Getter ID
     * @return Long, Возвращает ID
     * */
    public Long getId() {
        return id;
    }

    /**ID
     * */
    public void setId(Long id) {
        this.id = id;
    }

    /**Getter Имени
     * @return String, Возвращает имя
     * */
    public String getName() {
        return name;
    }

    /**Getter Наименование
     * @param name Наименование
     * */
    public void setName(String name) {
        this.name = name;
    }

    /**Getter координат
     * @return координаты
     * */
    public Coordinates getCoordinates() {
        return coordinates;
    }

    /**Getter координат
     *@param coordinates  координаты
     * */
    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    /**Getter даты создания
     * @return LocalDate, Возвращает координаты
     * */
    public LocalDate getCreationDate() {
        return creationDate;
    }

    /**Дата создания
     * @param creationDate даты создания
     * */
    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    /**Getter мощности двигателя
     * @return float, мощьность двигателя
     * */
    public float getEnginePower() {
        return enginePower;
    }

    /**Мощность двигателя
     * @param enginePower Мощность двигателя
     * */
    public void setEnginePower(float enginePower) {
        this.enginePower = enginePower;
    }

    /**Getter объёма двигателя
     * @return Float, объём
     * */
    public Float getCapacity() {
        return capacity;
    }

    /**Объём двигателя
     * @param capacity объём двигателя
     * */
    public void setCapacity(Float capacity) {
        this.capacity = capacity;
    }

    /**Getter пробега
     * @return Double пробег
     * */
    public Double getDistanceTravelled() {
        return distanceTravelled;
    }

    /**Пробег
     * @param distanceTravelled пробег
     * */
    public void setDistanceTravelled(Double distanceTravelled) {
        this.distanceTravelled = distanceTravelled;
    }

    /**Getter Типа топлива
     * @return FuelType, тип топлива
     * */
    public FuelType getFuelType() {
        return fuelType;
    }

    /**Переопределение метода
     * */
    public String toString() {
        return "Id: " + id + "\n" +
                "Name: " + name + "\n" +
                "Coordinate: " + coordinates.toString() + "\n" +
                "Creation date: " + creationDate.toString() + "\n" +
                "Engine power: " + enginePower + "\n" +
                "Capacity: " + capacity + "\n" +
                "Distance travelled: " + distanceTravelled + "\n" +
                "Fuel type: " + fuelType.toString();
    }

    /**Пассер XML
     * */
    public String toXML() {
        return "<vehicle>\n" +
                "<id>" + id + "</id>\n" +
                "<name>" + name + "</name>\n" +
                "<coordinates>\n" + coordinates.toXML() + "\n</coordinates>\n" +
                "<creationDate>" + dateFormatter.format(creationDate) + "</creationDate>" +
                "<enginePower>" + enginePower + "</enginePower>\n" +
                "<capacity>" + capacity + "</capacity>\n" +
                "<distanceTravelled>" + distanceTravelled + "</distanceTravelled>\n" +
                "<fuelType>" + fuelType.name() + "</fuelType>\n" +
                "</vehicle>";
    }

    @Override
    public int compareTo(Object o) {
        return (int) (capacity - ((Vehicle)o).capacity);
    }
}
