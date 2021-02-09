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
    public String buildRepresentation() { //TODO escape any chars that could cause problems when using this representation

        String result="{";
        result+="spec:\""+univcomp.getComponentContents("input")+"\",\n";
        result+="scheme:\""+univcomp.getComponentContents("scheme")+"\",\n";
        String host=(univcomp.getComponentContents("host")!=null) ? univcomp.getComponentContents("host") : "";
        result+="host:\""+host+"\",\n";
        String port=(univcomp.getComponentContents("port")!=null) ? univcomp.getComponentContents("port") : "";
        result+="port:\""+port+"\",\n";
        String ref=(univcomp.getComponentContents("fragment")!=null) ? univcomp.getComponentContents("fragment") : "";
        result+="ref:\""+ref+"\",\n"; //TODO check if hasRef is necessary
        
        String pqr="";
        pqr+=(univcomp.getComponentContents("path")!=null ) ? univcomp.getComponentContents("path") : "";
        String query=(univcomp.getComponentContents("query")!=null) ? "?"+univcomp.getComponentContents("query") : "";
        pqr+=query;
        String frag=(univcomp.getComponentContents("fragment")!=null) ? "#"+univcomp.getComponentContents("fragment") : "";
        pqr+=frag;

        result+="pathQueryRef:\""+pqr+"\",\n";
        String prp="";
        prp+=univcomp.getComponentContents("scheme")+"://"; //TODO this is not always correct, maybe check with whole spec
        prp+=host;
        prp+=(port!="") ? ":"+port : "";
        

        result+="prePath:\""+prp+"\",\n";
        result+="}\n";
        return result;
    }

}
