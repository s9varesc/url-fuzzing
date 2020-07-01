//package saarland.cispa.se.tribble.execution;
package saarland.cispa.se.tribble.execution.componentExtraction;

  import saarland.cispa.se.tribble.model.DTree;
  import saarland.cispa.se.tribble.model.DerivationRule;
  import saarland.cispa.se.tribble.model.Reference;
  import scala.collection.JavaConverters;
  import java.util.ArrayList;
  import java.util.List;

public class DictExtractor {
  private List<ComponentsBuilder> componentBuilders;


  public DictExtractor(){
    componentBuilders=new ArrayList<ComponentsBuilder>();
  }

  public void addComponentsBuilder(ComponentsBuilder builder){
    componentBuilders.add(builder);
  }

  public List<List<String>> extract(DTree root) {
    List<String> componentNames = new ArrayList<String>();
    for (ComponentsBuilder cb : componentBuilders){
      for(String name: cb.getComponentNames()){
        if (!componentNames.contains(name)){
          componentNames.add(name);
        }
      }
    }

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

        //if (componentNames.contains(name)){
          //System.out.println(name);
          String content=current.leaves().mkString();
          for (ComponentsBuilder cb : componentBuilders){
            cb.addComponent(name, content);
          }
        //}

      }
    }
    List<List<String>> representations = new ArrayList<List<String>>();
    for (ComponentsBuilder cb : componentBuilders){
      List<String> rep = new ArrayList<String>();
      rep.add(0, cb.getComponentFormat());
      rep.add(1, cb.buildRepresentation());
      representations.add(rep);
    }
    return representations;
  }

}
