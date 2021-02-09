package saarland.cispa.se.tribble.execution.componentExtraction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.nio.file.Path;
import java.nio.file.Paths;

/***
* concrete implementation of a ComponentsBuilder for the livingstandard-url grammar and the Firefox URL format
*/
public class FirefoxURLComponentsBuilder extends URLComponentsBuilder {

    String format = "firefox";
    

    public FirefoxURLComponentsBuilder(UniversalURLComponentsBuilder univcomp){
        super(univcomp);
    }

    /***
    * @return returns the name of the specified format that this ComponentBuilder will produce 
    */
    public String getComponentFormat(){
        return format;
    }

    @Override
    public String buildRepresentation() { 

        String result="{";
        result+="spec:\""+univcomp.getComponentContents("input")+"\",\n";
        result+="scheme:\""+univcomp.getComponentContents("scheme")+"\",\n";
        result+="host:\""+univcomp.getComponentContents("host")+"\",\n";
        result+="port\""+univcomp.getComponentContents("port")+"\",\n";
        result+="ref:\""+univcomp.getComponentContents("fragment")+"\",\n"; //TODO check if hasRef is necessary
        String pqr="";
        pqr+=univcomp.getComponentContents("path");
        pqr+="?"+univcomp.getComponentContents("query");
        pqr+="#"+univcomp.getComponentContents("fragment");

        result+="pathQueryRef:\""+pqr+"\",\n";
        String prp="";
        prp+=univcomp.getComponentContents("scheme"); //TODO check :// etc
        prp+=univcomp.getComponentContents("host");
        String tmp=univcomp.getComponentContents("port");
        if(tmp!=""){
            prp+=":"+tmp;
        }

        result+="prePath:\""+prp+"\",\n";
        result+="}\n";
        return result;
    }

}
