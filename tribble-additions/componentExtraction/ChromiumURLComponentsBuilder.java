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
    
    /*this.InternalComponentNames.add("input");
    this.InternalComponentNames.add("scheme");
    this.InternalComponentNames.add("username");
    this.InternalComponentNames.add("password");
    this.InternalComponentNames.add("host");
    this.InternalComponentNames.add("port");
    this.InternalComponentNames.add("path");
    this.InternalComponentNames.add("query");
    this.InternalComponentNames.add("ref");

//components which need no further processing
    translation.put("port", "URLport"); 
    translation.put("ref", "URLfragment");
    translation.put("input", "absoluteURLwithFragment");*/
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

    Map<String, String> components=buildMapping(); 
    String result="{";
    result+= "\""+fixEscaping(""+components.get("input"))+"\",";
    result+= "\""+fixEscaping(""+components.get("scheme"))+"\",";
    result+= "\""+fixEscaping(""+components.get("username"))+"\",";
    result+= "\""+fixEscaping(""+components.get("password"))+"\",";
    String tmp=components.get("host");
    if (tmp != null){
      if (tmp.startsWith("[") && tmp.endsWith("]")){ //ipv6: remove leading zeros and convert ipv4 pieces
        tmp=tmp.subSequence(1, tmp.length()-1).toString(); 
        tmp="["+util.formatIPv6(tmp)+"]";
      }
    }
    result+= "\""+fixEscaping(""+tmp)+"\",";
    result+= fixEscaping(""+components.get("port"))+",";
    String p=components.get("path");
    if(p != null && p != ""){
      result+= "\""+fixEscaping(""+p)+"\",";
    }
    else{
      result+= "\""+fixEscaping("/")+"\","; //empty path
    }

    result+= "\""+fixEscaping(""+components.get("query"))+"\",";
    result+= "\""+fixEscaping(""+components.get("ref"))+"\"";
    result +="}";

    String res=result.replaceAll("\"null\"", "\"\"");
    result=res;
    return result;

  }

}