package saarland.cispa.se.tribble.execution.componentExtraction;

import java.util.*;

public class FirefoxURLComponentsBuilder extends ComponentsBuilder {

  ArrayList<String> InternalComponentNames=new ArrayList<String>();
  HashMap<String, String> translation=new HashMap<>();

  public FirefoxURLComponentsBuilder(){
    this.InternalComponentNames.add("spec");
    this.InternalComponentNames.add("specIgnoringRef");
    this.InternalComponentNames.add("scheme");
    this.InternalComponentNames.add("hostPort");
    this.InternalComponentNames.add("host");
    this.InternalComponentNames.add("port");
    this.InternalComponentNames.add("userPass");
    this.InternalComponentNames.add("username");
    this.InternalComponentNames.add("password");
    this.InternalComponentNames.add("pathQueryRef");
    this.InternalComponentNames.add("prePath");
    this.InternalComponentNames.add("hasRef");
    this.InternalComponentNames.add("ref");

    //translation.put("spec", "url");//there is never a node named url -> build from xxaddress node
    //translation.put("specIgnoringRef", "TODO");//doesn't exist in grammar
    //translation.put("scheme", "");//doesn't exist in grammar ->build from full specification
    translation.put("hostPort", "hostport");
    translation.put("host", "host");
    translation.put("port", "port");
    //translation.put("userPass","" );//doesn't exist in grammar ->build afterwards if username + password exist
    translation.put("username", "user");
    translation.put("password", "password");
    //translation.put("pathQueryRef", "");//doesn't exist in grammar -> build afterwards
    //translation.put("prePath", "TODO");//doesn'T exist in grammar
    //translation.put("hasRef", "TODO");//doesn't exist in grammar
    //translation.put("ref", "TODO");//doesn't exist in grammar
    //TODO what about ;AB parts
  }

  @Override
  public String buildRepresentation() {
    Map<String, String> components=buildMapping();
    String result="{";
    for(String key:components.keySet()){
      result+=key+":\""+components.get(key)+"\",";
    }
    result=result.subSequence(0, result.length()-1).toString();
    result+="}";
    return result;
  }

  private Map<String, String> buildMapping(){
    Map<String, String> components=new HashMap<>();
    for(String name:translation.keySet()){
      String content=dict.get(translation.get(name));
      if (content!=null){
        components.put(name, content);
      }
    }
    //building the scheme
    String fullurl=dict.get("url");
    for(String s:Arrays.asList("http","ftp", "news", "nntp", "mailto", "wais", "prospero", "telnet", "gopher")){
      String content=dict.get(s+"address");
      if (content!=null){
        fullurl=content;
        components.put("spec", content);
      }
    }
    ArrayList<String> schemes = new ArrayList<String>(Arrays.asList("http://","ftp://", "news:", "nntp:", "mailto:", "wais://", "prospero://", "telnet://", "gopher://"));
    for(String scheme:schemes){
      if(fullurl.startsWith(scheme)){
        components.put("scheme", scheme);
      }
    }
    //building userPass
    String user=components.get("username");
    if(user != null){
      String password=components.get("password");
      if(password!=null){
        components.put("userPass", user+":"+password);
      }
      else{
        components.put("userPass", user);
      }
    }
    //building PathQueryRef
    String path=dict.get("path");
    String query=dict.get("search");
    String pathquery="";
    if(query != null){
      if(path != null){
        components.put("pathQueryRef", "/"+path+"?"+query);
      }
      else{
        components.put("pathQueryRef", "?"+query);
      }
    }
    else{
      if(path != null){
        components.put("pathQueryRef", "/"+path);
      }
    }

    return components;
  }
}

