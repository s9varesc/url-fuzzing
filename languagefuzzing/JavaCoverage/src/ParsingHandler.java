import java.net.MalformedURLException;
import java.net.URL;

public class ParsingHandler {

    public void parseInput(String input){
        try {
            URL url=new URL(input);
        } catch (MalformedURLException e) {
            System.out.println("caught exception on parse of: "+input);
            System.out.println(e.getMessage());
        }
    }
}
