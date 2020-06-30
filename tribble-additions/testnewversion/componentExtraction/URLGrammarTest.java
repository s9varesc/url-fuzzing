package saarland.cispa.se.tribble.execution.componentExtraction;


import java.util.*;

public class URLGrammarTest extends ComponentsBuilder {

  ArrayList<String> internalComponentNames=new ArrayList<String>();

  public URLGrammarTest(){
    this.internalComponentNames.add("urireference");
    this.internalComponentNames.add("scheme");
    this.internalComponentNames.add("hierpart");
    this.internalComponentNames.add("query");
    this.internalComponentNames.add("fragment");
    this.internalComponentNames.add("authority");
    this.internalComponentNames.add("host");
    this.internalComponentNames.add("userinfo");
    this.internalComponentNames.add("ipv6address");

    this.internalComponentNames.add("ls32");
    this.internalComponentNames.add("ipv4address");
    this.internalComponentNames.add("query");
    this.internalComponentNames.add("fragment");

  }
  public String getComponentFormat(){
      return "test";
    }

  @Override
  public String buildRepresentation() {
    Map<String, String> components=dict;
    String result="{";
    for(String key:components.keySet()){
      result+=key+":\""+components.get(key)+"\",\n";
    }
    result=result.subSequence(0, result.length()-1).toString();
    result+="}";
    return result;
  }


}

