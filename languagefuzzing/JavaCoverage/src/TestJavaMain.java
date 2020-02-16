import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class TestJavaMain {

    public static void main(String[] args) {
        InputReader reader=new InputReader();
    }

    private static void parseInput(String input){
        try {
            URL url=new URL(input);
        } catch (MalformedURLException e) {
            System.out.println("caught exception on parse of: "+input);
            System.out.println(e.getMessage());
        }
    }


}
