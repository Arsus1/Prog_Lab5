import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

/**
 * Основной класс Лабораторной работы №5.
 * @author Arsus1
 * @version 1.0
 */
public class Lab5 {
    public static void main(String[] args) {
        //System.setProperty("INPUT", "test.xml");
        //String fname = System.getenv("INPUT");
        String fname = "test.xml";
        try {
            File f = new File(fname);
            CollectionWorker worker = new CollectionWorker(f);
            worker.processCommands(new InputStreamReader(System.in));
        } catch (NullPointerException | IOException e) {
            System.out.println("Файл не найден!");
        }

        // Обработка команд, передающихся из потока ввода
    }
}