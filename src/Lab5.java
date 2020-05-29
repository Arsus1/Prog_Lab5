import java.io.File;

/**
 * Основной класс Лабораторной работы №5.
 * @author Arsus1
 * @version 1.0
 */
public class Lab5 {
    public static void main(String[] args) {
        String fname = System.getenv("input");
        File f = new File(fname);
        CollectionWorker worker = new CollectionWorker(f);
        worker.processCommands(System.in);
        // Обработка команд, передающихся из потока ввода
    }
}