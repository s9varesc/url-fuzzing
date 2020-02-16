import java.io.BufferedReader;
import java.io.FileReader;
import java.net.MalformedURLException;
import java.net.URL;

public class InputReader {
    ParsingHandler parsingHandler=new ParsingHandler();

    public void readInput(String path){
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(path));

            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
                parsingHandler.parseInput(line);
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
