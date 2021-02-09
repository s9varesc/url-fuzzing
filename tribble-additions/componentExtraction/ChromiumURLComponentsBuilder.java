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

    String result="{";
    result+="\""+univcomp.getComponentContent("input")+"\",";
    result+="\""+univcomp.getComponentContent("scheme")+"\",";
    result+="\""+"\","; //username
    result+="\""+"\","; //password
    result+="\""+univcomp.getComponentContent("host")+"\",";
    String p=univcomp.getComponentContent("port");
    if(p==""){
      p="-1";
    }
    result+=p+",";
    result+="\""+univcomp.getComponentContent("path")+"\",";
    result+="\""+univcomp.getComponentContent("query")+"\",";
    result+="\""+univcomp.getComponentContent("fragment")+"\"";
    result+="}";
    return result;

  }

}