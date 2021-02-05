package saarland.cispa.se.tribble.execution.componentExtraction;

import java.util.*;

/***
* abstract class for building component representations, should be impleneted by classes creating component representations 
* for concrete grammars and component formats
*/
public abstract class ComponentsBuilder {
  
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
