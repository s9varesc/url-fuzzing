package saarland.cispa.se.tribble.execution.componentExtraction;

import java.util.*;

public abstract class ComponentsBuilder {
  Map<String, String> dict = new HashMap<String, String>();
  List<String> componentNames=new ArrayList<String>();
  public ComponentsBuilder(){
    //this.componentNames.add("url"); there is never a node named url
    //TODO scheme is complicated
    this.componentNames.add("httpaddress");
    this.componentNames.add("ftpaddress");
    this.componentNames.add("newsaddress");
    this.componentNames.add("nntpaddress");
    this.componentNames.add("prosperoaddress");
    this.componentNames.add("telnetaddress");
    this.componentNames.add("gopheraddress");
    this.componentNames.add("waisaddress");
    this.componentNames.add("mailtoaddress");

    this.componentNames.add("login");
    this.componentNames.add("user");
    this.componentNames.add("password");
    this.componentNames.add("hostport");
    this.componentNames.add("host");
    this.componentNames.add("port");
    this.componentNames.add("path");
    this.componentNames.add("search");
    //TODO nothing about references

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
