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

    String host=(univcomp.getComponentContents("host")!=null) ? univcomp.getComponentContents("host") : "";
    
    String port=(univcomp.getComponentContents("port")!=null) ? univcomp.getComponentContents("port") : "-1";
    
    String frag=(univcomp.getComponentContents("fragment")!=null) ? univcomp.getComponentContents("fragment") : "";
    
    String path=(univcomp.getComponentContents("path")!=null) ? univcomp.getComponentContents("path") : "/";
    String query=(univcomp.getComponentContents("query")!=null) ? univcomp.getComponentContents("query") : "";
    String frag=(univcomp.getComponentContents("fragment")!=null) ? univcomp.getComponentContents("fragment") : "";

    String result="{";
    result+="\""+univcomp.getComponentContents("input")+"\",";
    result+="\""+univcomp.getComponentContents("scheme")+"\",";
    result+="\""+"\","; //username
    result+="\""+"\","; //password
    result+="\""+host+"\",";
    result+=port+",";
    result+="\""+path+"\",";
    result+="\""+query+"\",";
    result+="\""+frag+"\"";
    result+="}";
    return result;

  }

}