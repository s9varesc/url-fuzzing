package saarland.cispa.se.tribble.execution.componentExtraction;

import java.util.*;

public abstract class ComponentsBuilder {
  Map<String, String> dict = new HashMap<String, String>();
  List<String> componentNames=new ArrayList<String>();
  public ComponentsBuilder(){
    this.componentNames.add("uri");
    this.componentNames.add("relativeref");
    this.componentNames.add("scheme");
    this.componentNames.add("hierpart");
    this.componentNames.add("query");
    this.componentNames.add("fragment");
    this.componentNames.add("authority");
    this.componentNames.add("host");
    this.componentNames.add("port");
    this.componentNames.add("userinfo");
    this.componentNames.add("pathabempty");
    this.componentNames.add("pathabsolute");
    this.componentNames.add("pathempty");
    this.componentNames.add("pathrootless");
    this.componentNames.add("pathnoscheme");
    this.componentNames.add("query");
    this.componentNames.add("fragment");

    this.componentNames.add("relativepart");
    this.componentNames.add("ipliteral");
    this.componentNames.add("zoneID");
    this.componentNames.add("ipv6addrz");
    this.componentNames.add("ipv6address");
    this.componentNames.add("ipvFuture");
    this.componentNames.add("ipv4address");


  }
  /***
  Adds a name, content pair to the dictionary
   ***/
  public void addComponent(String name, String content){
    if (content != ""){
      dict.put(name, content);
    }

  }

  /***
   *
   * @return returns a list of names of components, that can be used to filter the parse tree for the specified components
   */
  public List<String> getComponentNames(){
    return componentNames;
  }

  /***
   * Builds the final representation of the components dictionary. Should be similar to the structure used in component tests.
   * @return a string containing the final representation to be written into a file
   */
  public abstract String buildRepresentation();
}
