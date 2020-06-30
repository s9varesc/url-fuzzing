package saarland.cispa.se.tribble.execution.componentExtraction;

import java.util.*;

public class FirefoxURLComponentsBuilderOLD extends ComponentsBuilder {

  ArrayList<String> InternalComponentNames=new ArrayList<String>();
  HashMap<String, String> translation=new HashMap<>();

  public FirefoxURLComponentsBuilderOLD(){
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



    translation.put("scheme", "scheme");
    translation.put("host", "host");
    translation.put("port", "port");
    translation.put("userPass","userinfo" );
    translation.put("ref", "fragment");

  }

  @Override
  public String buildRepresentation() {
    Map<String, String> components=buildMapping();
    String result="{";
    for(String key:components.keySet()){
      if(key != "hasRef") {
        result += key + ":\"" + components.get(key) + "\",\n";
      }
      else{
        result += key + ":" + components.get(key) + ",\n";
      }
    }
    result=result.subSequence(0, result.length()-2).toString();
    result+="}\n";
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
    //build spec
    String uri=dict.get("uri");
    if (uri != null){
      components.put("spec", uri);
    }
    else {
      String uriref=dict.get("relativeref");
      if(uriref != null){
        components.put("spec", uriref);
      }
    }
    //build path

    String pr=dict.get("pathrootless");
    String pn=dict.get("pathnoscheme");
    String pae=dict.get("pathabempty");
    String pa=dict.get("pathabsolute");
    String pe=dict.get("pathempty");
    for (String content: Arrays.asList(pae, pa, pe, pr, pn)){
      if(content !=null){
        components.put("path", content);
      }
    }
    //build hostport
    String host=components.get("host");
    String port=components.get("port");
    String hostport=host;
    if(port !=null){
      hostport+=":"+port;
    }
    components.put("hostPort", hostport);

    //build pathQueryRef
    String path=components.get("path");
    String query=dict.get("query");
    String ref=components.get("ref");
    String pqr=path;
    if (query !=null){
      pqr+="?"+query;
    }
    if(ref !=null){
      pqr+="#"+ref;
      //build hasRef
      components.put("hasRef", "true");
    }
    else{
      components.put("hasRef", "false");
    }
    components.put("pathQueryRef", pqr);

    //build prePath
    String scheme=components.get("scheme");
    String authority=dict.get("authority");
    if(scheme != null){
      components.put("prePath", scheme +"://"+authority);
    }
    else{
      components.put("prePath", "//"+authority);
    }




    return components;
  }
}

