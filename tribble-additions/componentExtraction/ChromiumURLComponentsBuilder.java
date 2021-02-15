package saarland.cispa.se.tribble.execution.componentExtraction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


/***
* concrete implementation of a ComponentsBuilder for the livingstandard-url grammar and the Chromium URL format
*/
public class ChromiumURLComponentsBuilder extends URLComponentsBuilder {

  String format = "chromium";
  UniversalURLComponentsBuilder univcomp;


  public ChromiumURLComponentsBuilder(UniversalURLComponentsBuilder univcomp){
    super(univcomp);
    this.univcomp=univcomp;
  }

  /***
  * @return returns the name of the specified format that this ComponentBuilder will produce 
  */
  public String getComponentFormat(){
    return format;
  }

  /***
  *
  * @return components and their contents in the Chromium component format
  */
  public String buildRepresentation(){ //TODO use univcomp methods
    //build actual representation
    //{input, scheme, username, password, host, port, path, query, ref}
    //{"http://user:pass@foo:21/bar;par?b#c", "http", "user", "pass",    "foo",       21, "/bar;par","b",          "c"},
    //{"http:foo.com",                        "http", "",  "",      "foo.com",    -1, "",      "",        ""}

    String host=(univcomp.getComponentContents("host")!=null) ? escapeContent(univcomp.getComponentContents("host")) : "";
    
    String port=(univcomp.getComponentContents("port")!=null) ? escapeContent(univcomp.getComponentContents("port")) : "-1";
    
    String frag=(univcomp.getComponentContents("fragment")!=null) ? escapeContent(univcomp.getComponentContents("fragment")) : "";
    
    String path=(univcomp.getComponentContents("path")!=null) ? escapeContent(univcomp.getComponentContents("path")) : "";
    String query=(univcomp.getComponentContents("query")!=null) ? escapeContent(univcomp.getComponentContents("query")) : "";
    String fragment=(univcomp.getComponentContents("fragment")!=null) ? escapeContent(univcomp.getComponentContents("fragment")) : "";

    String result="{";
    result+="\""+escapeContent(univcomp.getComponentContents("input"))+"\",";
    result+="\""+escapeContent(univcomp.getComponentContents("scheme"))+"\",";
    result+="\""+"\","; //username
    result+="\""+"\","; //password
    result+="\""+host+"\",";
    result+=port+",";
    result+="\""+path+"\",";
    result+="\""+query+"\",";
    result+="\""+fragment+"\"";
    result+="}";
    return result;

  }

  /**
  * escapes all chars that could cause problems when using this representation
  * @return the string with all "dangerous" chars escaped
  */
  private String escapeContent(String input){
    if(input != null){
        String result=input;
        result=result.replaceAll("\\\\", "\\\\\\\\");
        result=result.replaceAll("\"", "\\\\\"");
        if(result.endsWith("\\")){
            result+="\\";
        }
        return result; 
    }
    return input;
  }

}