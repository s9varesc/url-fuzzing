import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class TestJava {

    public static void main(String[] args) {

        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader("../exampleURLs/plainURLs"));

            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
