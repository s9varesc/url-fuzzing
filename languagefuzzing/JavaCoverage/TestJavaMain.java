import java.net.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;





public class TestJavaMain {

    public static void main(String[] args) {
        InputReader reader=new InputReader();
        reader.readInput("../urls/plainURLs");
    }
}
class InputReader {
    ParsingHandler parsingHandler=new ParsingHandler();

    public void readInput(String path){
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(path));

            String line;
            while ((line = br.readLine()) != null) {
                //System.out.println(line);
                parsingHandler.parseInput(line);
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
class ParsingHandler {

    public void parseInput(String input){
        try {
            URL url=new URL(input);
        } catch (Exception e) {
            //System.out.println("caught exception on parse of: "+input);
            //System.out.println(e.getMessage());
        }
    }
}
