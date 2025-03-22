import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class App {
    public static void main(String[] args) throws Exception {
        List<Double> l = new ArrayList<>();
        PseudoRandom pr = new PseudoRandom(2879);
        
        for (int i = 0; i < 1000; i++) {
            l.add(pr.getNext());
        }

        saveNumbersToFile(l, "random_numbers.txt");
    }

    public static void saveNumbersToFile(List<Double> numbers, String filename) {
        try (FileWriter writer = new FileWriter(filename)) {
            for (Double num : numbers) {
                writer.write(num + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
