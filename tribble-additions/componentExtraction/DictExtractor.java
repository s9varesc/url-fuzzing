//package saarland.cispa.se.tribble.execution;
package saarland.cispa.se.tribble.execution.componentExtraction;

  import saarland.cispa.se.tribble.model.DTree;
  import saarland.cispa.se.tribble.model.DerivationRule;
  import saarland.cispa.se.tribble.model.Reference;
  import scala.collection.JavaConverters;
  import java.util.ArrayList;
  import java.util.List;

public class DictExtractor {
  ComponentsBuilder componentsBuilder = new FirefoxURLComponentsBuilder();
  public String extract(DTree root) {
    List<String> componentNames=componentsBuilder.getComponentNames();
    List<DTree> workList = new ArrayList<>();
    List<DTree> visitedList=new ArrayList<>();
    workList.add(root);

    while (!workList.isEmpty()) {
      DTree current = workList.remove(0);
      visitedList.add(current);
      //workList.addAll(JavaConverters.asJavaCollection(current.nodes()));
      for( DTree node:JavaConverters.asJavaCollection(current.nodes())){
        if(!visitedList.contains(node)){
          workList.add(node);
        }
      }
      DerivationRule decl = current.decl();
      if (decl instanceof Reference) {
        String name = ((Reference) decl).name();

        if (componentNames.contains(name)){
          //System.out.println(name);
          String content=current.leaves().mkString();
          componentsBuilder.addComponent(name, content);
        }

      }
    }
    return componentsBuilder.buildRepresentation();
  }

}
