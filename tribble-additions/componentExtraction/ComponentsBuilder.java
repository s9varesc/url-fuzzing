package saarland.cispa.se.tribble.execution.componentExtraction;

import java.util.*;

/***
* abstract class for building component representations, should be extended/impleneted by classes creating component representations 
* for concrete grammars and component formats
*/
public abstract class ComponentsBuilder {
  Map<String, String> dict = new HashMap<String, String>();
  List<String> componentNames=new ArrayList<String>();
  String format;
  public ComponentsBuilder(){
    this.componentNames.add("");


  }
  /***
  * Adds a name, content pair to the dictionary
   */
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

  /***
  * 
  * @return a string containing an identification of the component format used, will be used to name the output folder
  */
  public abstract String getComponentFormat();
}
