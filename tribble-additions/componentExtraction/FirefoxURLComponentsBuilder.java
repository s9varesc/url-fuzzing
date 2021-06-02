package de.cispa.se.tribble.componentExtraction;

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
        String spec=univcomp.getComponentContents("base");
        if(spec != null){
            result+="spec:\""+escapeContent(spec)+"\",\n";
            result+="relativeURI:\""+escapeContent(univcomp.getComponentContents("input"))+"\",\n";
        }
        else{
            result+="spec:\""+escapeContent(univcomp.getComponentContents("input"))+"\",\n";
        }
       
        result+="scheme:\""+escapeContent(univcomp.getComponentContents("scheme"))+"\",\n";
        String host=(univcomp.getComponentContents("host")!=null) ? escapeContent(univcomp.getComponentContents("host")) : "";
        String fullhost=host; //needed for prePath
        if(univcomp.getSpecialComponentContent("ipv6address")!=null){ 
            host=host.replaceAll("\\[", "");
            host=host.replaceAll("\\]", "");
        }
        result+="host:\""+host+"\",\n";

        String port=(univcomp.getComponentContents("port")!=null) ? escapeContent(univcomp.getComponentContents("port")) : "";
        result+="port:\""+port+"\",\n";
        String ref=(univcomp.getComponentContents("fragment")!=null) ? escapeContent(univcomp.getComponentContents("fragment")) : "";
        result+="ref:\""+ref+"\",\n"; 
        
        String pqr="";
        pqr+=(univcomp.getComponentContents("path")!=null ) ? escapeContent(univcomp.getComponentContents("path")) : "/";
        String query=(univcomp.getComponentContents("query")!=null) ? "?"+escapeContent(univcomp.getComponentContents("query")) : "";
        pqr+=(!query.equals("?") ? query : "");//avoid adding a ? if the query is empty
        String frag=(univcomp.getComponentContents("fragment")!=null) ? "#"+escapeContent(univcomp.getComponentContents("fragment")) : "";
        pqr+=(!frag.equals("#") ? frag :""); //avoid adding a # if the fragment is empty

        result+="pathQueryRef:\""+pqr+"\",\n";
        String prp="";
        prp+=escapeContent(univcomp.getComponentContents("scheme"));
        String input=escapeContent(univcomp.getComponentContents("input")); 
        String username=(univcomp.getComponentContents("username")!=null) ? escapeContent(univcomp.getComponentContents("username")): "";
        String password=(univcomp.getComponentContents("password")!=null) ? escapeContent(univcomp.getComponentContents("password")): "";
        
        spec=(spec!=null)?spec:"";
        if(input.startsWith(prp+"://")||spec.startsWith(prp+"://")){
            prp+="://";
        }
        else{
            prp+=":"; 
        }
        if(username != ""){
            prp+=username;
            if(password!=""){
                prp+=":"+password;
            }
            prp+="@"
        }
        prp+=fullhost;
        prp+=(port!="") ? ":"+port.replaceFirst("^0+(?!$)", "") : "";
        

        result+="prePath:\""+prp+"\",\n";
        
        result+="username:\"\""+username+",\n"; 
        result+="password:\"\""+password+",\n";
  
        result+="}\n";

        
        return result;
    }

    /**
    * escapes all chars that could cause problems when using this representation 
    * @return the string with all problematic chars escaped
    */
    private String escapeContent(String input){
        if(input != null){
            String result=input;
            result=result.replaceAll("\\\\", "\\\\\\\\");
            result=result.replaceAll("\"", "\\\\\"");
            return result; 
        }
        return "";
    }

}
