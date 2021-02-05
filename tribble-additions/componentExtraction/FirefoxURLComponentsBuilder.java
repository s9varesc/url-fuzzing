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
    UniversalURLComponentsBuilder univcomp;

    public FirefoxURLComponentsBuilder(UniversalURLComponentsBuilder univcomp){
       /* this.InternalComponentNames.add("spec");
        this.InternalComponentNames.add("specIgnoringRef");
        this.InternalComponentNames.add("scheme");
//this.InternalComponentNames.add("hostPort");
        this.InternalComponentNames.add("host");
        this.InternalComponentNames.add("port");
        this.InternalComponentNames.add("userPass");
        this.InternalComponentNames.add("username");
        this.InternalComponentNames.add("password");
        this.InternalComponentNames.add("pathQueryRef");
        this.InternalComponentNames.add("prePath");
        this.InternalComponentNames.add("hasRef");
        this.InternalComponentNames.add("ref");


//components which need no further processing
        translation.put("port", "URLport");
        translation.put("userPass","userinfo" );
        translation.put("ref", "URLfragment");
        translation.put("spec", "absoluteURLwithFragment");*/

    }

    /***
    * @return returns the name of the specified format that this ComponentBuilder will produce 
    */
    public String getComponentFormat(){
        return format;
    }

    @Override
    public String buildRepresentation() { //TODO use univcomp methods
        //TODO escape quotation marks in content
        Map<String, String> components=buildMapping();
        String result="{";
        for(String key:components.keySet()){
            if(key != "hasRef") {
                String content = components.get(key);
                String tmp=content.replaceAll("\\\\", "\\\\\\\\");
                tmp=tmp.replaceAll("\\\"", "\\\\\\\"");
                if (key=="host"){
                    if (tmp.startsWith("[") && tmp.endsWith("]")){ //ipv6: need to remove leading zeros and convert ipv4 pieces
                        tmp=tmp.subSequence(1, tmp.length()-1).toString(); 
                        tmp=util.formatIPv6(tmp);
                    }
                }
                result += key + ":\"" + tmp + "\",\n";
            }
            else{
                result += key + ":" + components.get(key) + ",\n";
            }
        }
        result=result.subSequence(0, result.length()-2).toString();
        result+="}\n";
        return result;
    }

}
