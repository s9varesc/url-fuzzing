package saarland.cispa.se.tribble.execution.componentExtraction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.nio.file.Path;
import java.nio.file.Paths;

/***
* abstract class for universal component builders, ensures that components are processed before the specialized
* builders access them
*/
public abstract class UniversalComponentsBuilder{

	Map<String, String> dict = new HashMap<String, String>();
	ArrayList<String> componentNames=new ArrayList<String>();
	Map<String, String> components = new HashMap<String, String>();
	
	public UniversalComponentsBuilder(){
	  this.componentNames.add("");


	}
	/***
	* Adds a name, content pair to the dictionary
	* @param name name of the grammar rule
	* @param content the string corresponding to the instantiation of the specified rule
	 */
	public abstract void addComponent(String name, String content){
		dict.put(name, content);
		return;
	}

	/***
	* creates a internal representation of the specified components by processing the dictionary of 
	* grammar rule name and component contents 
	*/
	public abstract void prepareComponents();

	/***
	* method to access the basic components
	* @param component the name of the component whose content is requested
	* @return a string containing the contents of the specified component,  null = unknown component
	*/
	public String getComponentContents(String component){
		if(components.containsKey(component)){
			return components.get(component);
		}
		return null;

	}

	/***
	* method to access component contents not contained in the basic components
	* note: no transformations are applied to the returned string
	* @param grammarrule the name of the rule whose instantiation is requested
	* @return a string containing the instantiation of the specified rule, null = unknown rule
	*/
	public String getSpecialComponentContent(String grammarrule){
		if(dict.containsKey(grammarrule)){
			return dict.get(grammarrule);
		}
		return null;
	}
}