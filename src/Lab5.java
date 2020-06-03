import java.io.File;
import java.io.FileNotFoundException;
import java.util.Map;

/**
 * Основной класс Лабораторной работы №5.
 * @author Arsus1
 * @version 1.0
 */
public class Lab5 {
    public static void main(String[] args) {
        String fname = System.getenv("INPUT");
        try {
            File f = new File(fname);
            CollectionWorker worker = new CollectionWorker(f);
            worker.processCommands(System.in);
        } catch (NullPointerException e) {
            System.out.println("Файл не найден!");
        }

        // Обработка команд, передающихся из потока ввода
    }
}