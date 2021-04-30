
package de.cispa.se.tribble.componentExtraction;

  //import de.cispa.se.tribble.model.DerivationRule;
  import de.cispa.se.tribble;
  //import de.cispa.se.tribble.*;
  
  //import de.cispa.se.tribble.model.Reference;
  
  import scala.collection.JavaConverters;
  import java.util.ArrayList;
  import java.util.List;

/***
* extracts possible components and their contents from the tree representations and passes them to ComponentsBuilder classes for further processing
*/
public class DictExtractor {
  private List<ComponentsBuilder> componentBuilders;
  private UniversalComponentsBuilder univcompBuilder;

  public DictExtractor(UniversalComponentsBuilder univcomp, List<ComponentsBuilder> builders){
    componentBuilders=builders;
    univcompBuilder=univcomp;
  }


  /***
  * extracts all specified components from the tree representation and passes them to the specified component builders
  * @return a list containing the formats and component representations e.g [["firefox", "{spec:...}"], ["chromium", "{input:..."], ["plain", "http://..."]]
  */
  public List<List<String>> extract(DTree root) {
    
    //extract components
    List<DTree> workList = new ArrayList<>();
    List<DTree> visitedList=new ArrayList<>();
    workList.add(root);

    while (!workList.isEmpty()) {
      DTree current = workList.remove(0);
      visitedList.add(current);
      for( DTree node:JavaConverters.asJavaCollection(current.nodes())){
        if(!visitedList.contains(node)){
          workList.add(node);
        }
      }
      DerivationRule decl = current.decl();
      if (decl instanceof Reference) {
        String name = ((Reference) decl).name();
        String content=current.leaves().mkString();
        univcompBuilder.addComponent(name, content);
      }
    }

    // pre-format component contents
    univcompBuilder.prepareComponents();
    //use specified component builders to format components
    List<List<String>> representations = new ArrayList<List<String>>();
    for (ComponentsBuilder cb : componentBuilders){
      List<String> rep = new ArrayList<String>();
      rep.add(0, cb.getComponentFormat()); //e.g. "firefox"
      rep.add(1, cb.buildRepresentation()); //e.g. "{spec: ...}"
      representations.add(rep);
    }
    return representations;
  }

}
