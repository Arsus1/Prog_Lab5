/**
 * Класс, описывающий координаты объекта..
 * @author Arsus1
 * @version 1.0
 */
public class Coordinates {
    private float x;
    private long y;
    /**Допустимые координаты
     * @param x Координата по X
     * @param y Координата по Y
     * */
    Coordinates(float x, long y) {
        this.x = x;
        this.y = y;
        assert x <= 940 : "Координата X должна быть меньше 940!";
        assert y <= 407 : "Координата Y должна быть меньше 407!";
    }

    /**Getter для поля X.
     * @return float, Возвращает значение координаты X.
     * */
    public float getX() {
        return x;
    }

    /**Установка значения .
     * @param x Установка значения X.
     * */
    public void setX(float x) {
        this.x = x;
    }

    /**Getter для поля Y.
     * @return long, Возвращает значение координаты Y.
     * */
    public long getY() {
        return y;
    }

    /**Установка значения .
     * @param y Установка значения Y.
     * */
    public void setY(long y) {
        this.y = y;
    }

    /**Преобразование класса.
     * @return String Преобразование X.
     * @return String, Преобразование Y.
     * */
    public String toString() {
        return "(X: " + x + ", Y: " + y + ")";
    }

    /**Конвертанция в XML
     * @return String, Конвертация значения X.
     * @return String, Конвертация значения Y.
     * */
    public String toXML() {
        return "<x>" + x + "</x>\n" +
                "<y>" + y + "</y>";
    }
}
