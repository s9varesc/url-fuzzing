import java.net.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.io.FileOutputStream;





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
        parsingHandler.writeExceptionsFile();
    }

}
class ParsingHandler {
    private String exceptions="";

    public void parseInput(String input){
	if (input.length()>0){
	    input=input.substring(0,input.length());
	}
	
        try {
            URL url=new URL(input);
        } catch (Exception e) {
            exceptions+="\n{\"url\":\""+input+"\", \"exception\":\""+e.toString()+"\"}";
        }
    }

   public void writeExceptionsFile(){
	try ( PrintStream out = new PrintStream(new FileOutputStream("JavaExceptions.txt"))) {
    		out.print(exceptions);
	} catch(Exception e) {
            e.printStackTrace();
        }
   }
}
