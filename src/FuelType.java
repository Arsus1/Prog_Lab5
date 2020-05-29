/**
 * Индикация топлива.
 * @author Arsus1
 * @version 1.0
 */
public enum FuelType {
    GASOLINE {
        public String toString() {
            return "Gasoline";
        }
    },
    NUCLEAR {
        public String toString() {
            return "Nuclear";
        }
    },
    PLASMA {
        public String toString() {
            return "Plasma";
        }
    };

    /**
     * <p>Парсинг видов топлива.</p>
     * @return FuelType
     */
    public static FuelType parseFuelType(String str) {
        if (str.equals("GASOLINE")) {
            return GASOLINE;
        }
        if (str.equals("NUCLEAR")) {
            return NUCLEAR;
        }
        if (str.equals("PLASMA")) {
            return PLASMA;
        }
        throw new NumberFormatException();
    }
}
